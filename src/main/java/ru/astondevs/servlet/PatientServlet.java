package ru.astondevs.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.config.Configuration;
import ru.astondevs.exception.AppointmentNotFoundException;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Patient;
import ru.astondevs.service.PatientService;
import ru.astondevs.servlet.dto.incomingDto.IncomingAppointmentDto;
import ru.astondevs.servlet.dto.incomingDto.IncomingPatientDto;
import ru.astondevs.servlet.dto.outGoingDto.OutAppointmentDto;
import ru.astondevs.servlet.dto.outGoingDto.OutPatientDto;
import ru.astondevs.servlet.mapper.AppointmentMapper;
import ru.astondevs.servlet.mapper.PatientMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(urlPatterns = {"/user", "/user/*"})
public class PatientServlet extends HttpServlet {

    private static final String REGEX_USER = "user/?";
    private static final String REGEX_USER_BY_ID = "user/\\d+/?";
    private static final String REGEX_USER_ID_APPOINTMENT = "user/\\d+/appointment/?";
    private static final String REGEX_USER_ID_APPOINTMENT_ID = "user/\\d+/appointment/\\d+";
    private PatientService patientService;
    private ObjectMapper mapper;


    @Override
    public void init() throws ServletException {
        Configuration config = (Configuration) getServletContext().getAttribute("config");
        patientService = config.getPatientService();
        mapper = new ObjectMapper();

    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI().substring(1);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        String[] separatedUriAddress = requestURI.split("/");
        response.setStatus(HttpServletResponse.SC_OK);
        try {
            if (requestURI.matches(REGEX_USER)) {
                mapper.writeValue(writer, patientListToPatientDtoList(patientService.findAll()));
            } else if (requestURI.matches(REGEX_USER_BY_ID)) {
                Patient byId = patientService.findById(Long.valueOf(separatedUriAddress[1]));
                mapper.writeValue(writer, patientToPatientDto(byId));
            } else if (requestURI.matches(REGEX_USER_ID_APPOINTMENT)) {
                List<Appointment> appointments = patientService.getAppointments(Long.valueOf(separatedUriAddress[1]));
                //mapper.writeValue(writer, new Appointment(LocalDate.now(), LocalTime.now(),new Doctor(), new Patient()));
                //mapper.writeValue(writer, new Appointment(LocalDate.now(), LocalTime.now(), new Doctor(), new Patient()));
                //mapper.writeValue(writer, new Appointment(null, null, new Doctor(), new Patient()));
                //mapper.writeValue(writer, appointments);
                mapper.writeValue(writer, appointmentsListToAppointmentDtoList(appointments));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Страница не найдена");
            }
        } catch (PatientNotFoundException | DoctorNotFoundException | ScheduleNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String uri = request.getRequestURI().substring(1);
        String[] separatedUriAddress = uri.split("/");
        try (BufferedReader reader = request.getReader()) {
            if (uri.matches(REGEX_USER_BY_ID)) {
                updatingPatient(response, reader, Long.valueOf(separatedUriAddress[1]));
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String uri = request.getRequestURI().substring(1);
        String[] separatedUriAddress = uri.split("/");
        try (BufferedReader reader = request.getReader()) {
            if (uri.matches(REGEX_USER)) {
                patientSavingProcess(response, reader);
            } else if (uri.matches(REGEX_USER_ID_APPOINTMENT)) {
                long patientId = Long.parseLong(separatedUriAddress[1]);
                appointmentSavingProcess(response, patientId, reader);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void appointmentSavingProcess(HttpServletResponse response, long patientId, BufferedReader reader) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            String json = readRequest(reader);
            IncomingAppointmentDto incomingAppointmentDto = mapper.readValue(json, IncomingAppointmentDto.class);
            try {
                Appointment savedAppointment
                        = patientService.addAppointment(patientId, appointmentDtoToAppointment(incomingAppointmentDto));
                if (savedAppointment == null) {
                    writer.println("Ошибка сохранения");
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST);
                } else {
                    response.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (PatientNotFoundException | DoctorNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        }
    }

    private void patientSavingProcess(HttpServletResponse response, BufferedReader reader) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            String json = readRequest(reader);
            IncomingPatientDto incomingPatientDto = mapper.readValue(json.toString(), IncomingPatientDto.class);
            Patient savedPatient = patientService.save(incomingPatientDtoToPatient(incomingPatientDto));
            if (savedPatient == null) {
                writer.println("Ошибка сохранения");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                mapper.writeValue(writer, patientToPatientDto(savedPatient));
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
    }

    private void updatingPatient(HttpServletResponse response, BufferedReader reader, long patientId) throws IOException {
        String json = readRequest(reader);
        IncomingPatientDto incomingPatientDto = mapper.readValue(json.toString(), IncomingPatientDto.class);

        try {
            boolean isUpdate = patientService.update(incomingPatientDtoToPatient(incomingPatientDto), patientId);
            if (isUpdate) {
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        } catch (PatientNotFoundException | DoctorNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String requestURI = request.getRequestURI().substring(1);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        String[] separatedUriAddress = requestURI.split("/");
        try {
            if (requestURI.matches(REGEX_USER_BY_ID)) {
                patientService.deleteById(Long.valueOf(separatedUriAddress[1]));
                response.setStatus(HttpServletResponse.SC_OK);
            } else if (requestURI.matches(REGEX_USER_ID_APPOINTMENT_ID)) {
                long userId = Long.valueOf(separatedUriAddress[1]);
                long appointmentId = Long.valueOf(separatedUriAddress[3]);
                patientService.deleteAppointment(userId, appointmentId);
            } else {
                writer.println("По вашему запросу ничего не найдено");
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "По вашему запросу ничего не найдено");
            }
        } catch (DoctorNotFoundException | PatientNotFoundException | AppointmentNotFoundException e) {
            writer.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }


    private List<OutPatientDto> patientListToPatientDtoList(List<Patient> patients) {
        return patients.stream().map(patient -> PatientMapper.INSTANCE.map(patient)).collect(Collectors.toList());
    }

    private OutPatientDto patientToPatientDto(Patient patient) {
        return PatientMapper.INSTANCE.map(patient);
    }

    private List<OutAppointmentDto> appointmentsListToAppointmentDtoList(List<Appointment> appointments) {
        return appointments.stream().map(AppointmentMapper.INSTANCE::map).collect(Collectors.toList());

    }

    private Appointment appointmentDtoToAppointment(IncomingAppointmentDto appointmentDto) {
        return AppointmentMapper.INSTANCE.map(appointmentDto);
    }


    private Patient incomingPatientDtoToPatient(IncomingPatientDto incomingPatientDto) {
        return PatientMapper.INSTANCE.map(incomingPatientDto);
    }

    private String readRequest(BufferedReader reader) throws IOException {
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        return json.toString();
    }
}
