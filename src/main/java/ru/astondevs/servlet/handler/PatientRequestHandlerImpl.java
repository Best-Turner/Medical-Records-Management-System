package ru.astondevs.servlet.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.exception.AppointmentNotFoundException;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Patient;
import ru.astondevs.service.PatientService;
import ru.astondevs.servlet.dto.incomingDto.IncomingPatientDto;
import ru.astondevs.servlet.dto.outGoingDto.OutPatientDto;
import ru.astondevs.servlet.mapper.AppointmentMapper;
import ru.astondevs.servlet.mapper.PatientMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

public class PatientRequestHandlerImpl implements RequestHandler {

    private static final String REGEX_USER = "user/?";
    private static final String REGEX_USER_BY_ID = "user/\\d+/?";
    private static final String REGEX_USER_ID_APPOINTMENT = "user/\\d+/appointment/?";
    private static final String REGEX_USER_ID_APPOINTMENT_ID = "user/\\d+/appointment/\\d+";
    private final PatientService patientService;
    private ObjectMapper mapper;


    public PatientRequestHandlerImpl(PatientService patientService) {
        mapper = new ObjectMapper();
        this.patientService = patientService;
    }


    @Override
    public HttpServletResponse methodGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                mapper.writeValue(writer, appointmentsListToAppointmentDtoList(appointments));
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (PatientNotFoundException | DoctorNotFoundException | ScheduleNotFoundException e) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
        return response;
    }


    @Override
    public HttpServletResponse methodPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        BufferedReader reader = request.getReader();
        StringBuilder json = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            json.append(line);
        }
        IncomingPatientDto incomingPatientDto = mapper.readValue(json.toString(), IncomingPatientDto.class);
        Patient savedPatient = patientService.save(incomingPatientDtoToPatient(incomingPatientDto));
        if (savedPatient == null) {
            writer.println("Ошибка сохранения");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } else {
            mapper.writeValue(writer, patientToPatientDto(savedPatient));
            response.setStatus(HttpServletResponse.SC_OK);
        }
        return response;
    }


    @Override
    public HttpServletResponse methodPut(HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public HttpServletResponse methodDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
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
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
            }
        } catch (DoctorNotFoundException | PatientNotFoundException | AppointmentNotFoundException e) {
            writer.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
        response.setStatus(HttpServletResponse.SC_OK);
        return response;
    }


    private List<OutPatientDto> patientListToPatientDtoList(List<Patient> patients) {
        return patients.stream().map(patient -> PatientMapper.INSTANCE.map(patient)).collect(Collectors.toList());
    }

    private OutPatientDto patientToPatientDto(Patient patient) {
        return PatientMapper.INSTANCE.map(patient);
    }

    private Object appointmentsListToAppointmentDtoList(List<Appointment> appointments) {
        return appointments.stream().map(el -> AppointmentMapper.INSTANCE.map(el)).collect(Collectors.toList());
    }

    private Patient incomingPatientDtoToPatient(IncomingPatientDto incomingPatientDto) {
        return PatientMapper.INSTANCE.map(incomingPatientDto);
    }
}


