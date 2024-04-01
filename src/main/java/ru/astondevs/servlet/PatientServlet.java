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
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Patient;
import ru.astondevs.service.PatientService;
import ru.astondevs.servlet.handler.PatientRequestHandlerImpl;
import ru.astondevs.servlet.handler.RequestHandler;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.util.List;

@WebServlet(urlPatterns = {"/user", "/user/*"})
public class PatientServlet extends HttpServlet {

//    private static final String REGEX_USER = "user/?";
//    private static final String REGEX_USER_BY_ID = "user/\\d+/?";
//    private static final String REGEX_USER_ID_APPOINTMENT = "user/\\d+/appointment/?";
//    private static final String REGEX_USER_ID_APPOINTMENT_ID = "user/\\d+/appointment/\\d+";
    private PatientService patientService;
    RequestHandler handler;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        Configuration config = (Configuration) getServletContext().getAttribute("config");
        patientService = config.getPatientService();
        objectMapper = new ObjectMapper();
        handler = new PatientRequestHandlerImpl(patientService);

    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        handler.methodGet(request, response);
    }


//    private void requestHandler(String requestURI, HttpServletResponse response) throws IOException, ScheduleNotFoundException, DoctorNotFoundException, PatientNotFoundException {
//        String[] separatedUriAddress = requestURI.split("/");
//        response.setContentType("application/json");
//        PrintWriter writer = response.getWriter();
//        if (requestURI.matches(REGEX_USER)) {
//            writer.println(patientService.findAll());
//        } else if (requestURI.matches(REGEX_USER_BY_ID)) {
//            Patient byId = patientService.findById(Long.valueOf(separatedUriAddress[1]));
//            writer.println(byId);
//        } else if (requestURI.matches(REGEX_USER_ID_APPOINTMENT)) {
//            List<Appointment> appointments = patientService.getAppointments(Long.valueOf(separatedUriAddress[1]));
//            writer.println(appointments);
//        } else if (requestURI.matches(REGEX_USER_ID_APPOINTMENT_ID)) {
//            patientService.getAppointments(Long.parseLong(separatedUriAddress[3]));
//        } else {
//            throw new MalformedURLException("Неверный адрес запроса");
//        }
//
//    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.methodPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        handler.methodDelete(req, resp);
    }
}
