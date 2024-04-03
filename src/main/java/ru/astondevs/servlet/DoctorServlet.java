package ru.astondevs.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.config.Configuration;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.model.Patient;
import ru.astondevs.service.DoctorService;
import ru.astondevs.servlet.dto.incomingDto.IncomingDoctorDto;
import ru.astondevs.servlet.dto.incomingDto.IncomingScheduleDto;
import ru.astondevs.servlet.dto.outGoingDto.OutDoctorDto;
import ru.astondevs.servlet.dto.outGoingDto.OutDoctorScheduleDto;
import ru.astondevs.servlet.dto.outGoingDto.OutPatientDto;
import ru.astondevs.servlet.mapper.DoctorMapper;
import ru.astondevs.servlet.mapper.DoctorScheduleMapper;
import ru.astondevs.servlet.mapper.PatientMapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "DoctorServlet", urlPatterns = {"/doctor", "/doctor/*"})
public class DoctorServlet extends HttpServlet {

    private static final String REGEX_DOCTOR = "doctor/?";
    private static final String REGEX_DOCTOR_BY_ID = "doctor/\\d+/?";
    private static final String REGEX_DOCTOR_ID_SCHEDULE = "doctor/\\d+/schedule/?";
    private static final String REGEX_DOCTOR_ID_SCHEDULE_ID = "doctor/\\d+/schedule/\\d+";
    private static final String REGEX_DOCTOR_ID_PATIENT = "doctor/\\d+/patient/?";
    private DoctorService doctorService;
    private ObjectMapper mapper;

    @Override
    public void init() throws ServletException {
        Configuration config = (Configuration) getServletContext().getAttribute("config");
        doctorService = config.getDoctorService();
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

            if (requestURI.matches(REGEX_DOCTOR)) {
                mapper.writeValue(writer, getDoctorDtoList(doctorService.findAll()));
            } else if (requestURI.matches(REGEX_DOCTOR_BY_ID)) {
                Doctor byId = doctorService.findById(Integer.valueOf(separatedUriAddress[1]));
                mapper.writeValue(writer, doctorToDoctorDto(byId));
            } else if (requestURI.matches(REGEX_DOCTOR_ID_PATIENT)) {
                List<Patient> patients = doctorService.getPatients(Integer.parseInt(separatedUriAddress[1]));
                mapper.writeValue(writer, getPatienDtotList(patients));
            } else if (requestURI.matches(REGEX_DOCTOR_ID_SCHEDULE)) {
                List<DoctorSchedule> schedules = doctorService.getSchedules(Integer.parseInt(separatedUriAddress[1]));
                mapper.writeValue(writer, getScheduleDtoList(schedules));
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Страница не найдена");
            }
        } catch (PatientNotFoundException | DoctorNotFoundException | ScheduleNotFoundException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        String uri = request.getRequestURI().substring(1);
        String[] separatedUriAddress = uri.split("/");
        try (BufferedReader reader = request.getReader()) {
            if (uri.matches(REGEX_DOCTOR)) {
                doctorSavingProcess(response, reader);
            } else if (uri.matches(REGEX_DOCTOR_ID_SCHEDULE)) {
                scheduleSavingProcess(response, Integer.parseInt(separatedUriAddress[1]), reader);
            } else {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            }
        }
    }

    private void scheduleSavingProcess(HttpServletResponse response, int doctorId, BufferedReader reader) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            String json = readRequest(reader);
            IncomingScheduleDto incomingScheduleDto = mapper.readValue(json, IncomingScheduleDto.class);
            try {
                DoctorSchedule schedule = doctorScheduleDtoToDoctorSchedule(incomingScheduleDto);
                DoctorSchedule savedSchedule = doctorService.addSchedule(schedule, doctorId);
                if (savedSchedule == null) {
                    response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Запись не сохранилась");
                }
                mapper.writeValue(writer, doctorScheduleToDoctorScheduleDto(schedule));
                response.setStatus(HttpServletResponse.SC_CREATED);

            } catch (DoctorNotFoundException | ScheduleNotFoundException e) {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, e.getMessage());
            }
        }
    }

    private void doctorSavingProcess(HttpServletResponse response, BufferedReader reader) throws IOException {
        try (PrintWriter writer = response.getWriter()) {
            String json = readRequest(reader);
            IncomingDoctorDto incomingDoctorDto = mapper.readValue(json.toString(), IncomingDoctorDto.class);
            Doctor savedDoctor = doctorService.save(incomingDoctortDtoToDoctor(incomingDoctorDto));
            if (savedDoctor == null) {
                writer.println("Ошибка сохранения");
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            } else {
                mapper.writeValue(writer, doctorToDoctorDto(savedDoctor));
                response.setStatus(HttpServletResponse.SC_CREATED);
            }
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String requestURI = request.getRequestURI().substring(1);
        response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        String[] separatedUriAddress = requestURI.split("/");
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            if (requestURI.matches(REGEX_DOCTOR_BY_ID)) {
                doctorService.deleteById(Integer.valueOf(separatedUriAddress[1]));
            } else if (requestURI.matches(REGEX_DOCTOR_ID_SCHEDULE_ID)) {
                int doctorId = Integer.parseInt(separatedUriAddress[1]);
                long scheduleId = Long.parseLong(separatedUriAddress[3]);
                doctorService.deleteSchedule(doctorId, scheduleId);
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "По вашему запросу ничего не найдено");
            }
        } catch (DoctorNotFoundException | PatientNotFoundException | ScheduleNotFoundException e) {
            writer.println(e.getMessage());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        }
    }


    private List<OutPatientDto> getPatienDtotList(List<Patient> patients) {
        return patients.stream().map(patient -> PatientMapper.INSTANCE.map(patient)).collect(Collectors.toList());
    }

    private OutDoctorDto doctorToDoctorDto(Doctor doctor) {
        return DoctorMapper.INSTANCE.map(doctor);
    }

    private List<OutDoctorDto> getDoctorDtoList(List<Doctor> doctors) {
        return doctors.stream().map(DoctorMapper.INSTANCE::map).collect(Collectors.toList());

    }

    private List<OutDoctorScheduleDto> getScheduleDtoList(List<DoctorSchedule> schedules) {
        return schedules.stream().map(DoctorScheduleMapper.INSTANCE::map).collect(Collectors.toList());

    }

    private DoctorSchedule doctorScheduleDtoToDoctorSchedule(IncomingScheduleDto scheduleDto) {
        return DoctorScheduleMapper.INSTANCE.map(scheduleDto);
    }

    private OutDoctorScheduleDto doctorScheduleToDoctorScheduleDto(DoctorSchedule doctorSchedule) {
        return DoctorScheduleMapper.INSTANCE.map(doctorSchedule);
    }


    private Doctor incomingDoctortDtoToDoctor(IncomingDoctorDto doctorDto) {
        return DoctorMapper.INSTANCE.map(doctorDto);
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
