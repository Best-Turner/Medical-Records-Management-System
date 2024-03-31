package ru.astondevs.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.config.Configuration;
import ru.astondevs.model.Patient;
import ru.astondevs.service.PatientService;
import ru.astondevs.servlet.dto.outGoingDto.OutPatientDto;
import ru.astondevs.servlet.mapper.PatientMapper;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(name = "UserServlet", urlPatterns = "/user/*")// /product/%7Bid%7D
public class PatientServlet extends HttpServlet {
    private PatientService patientService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        Configuration config = (Configuration) getServletContext().getAttribute("config");
        patientService = config.getPatientService();
        objectMapper = new ObjectMapper();
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //response.setContentType("application/json");
        PrintWriter writer = response.getWriter();
        String pathInfo = request.getPathInfo().substring(1);
        String[] split = pathInfo.split("/");
        if (split[0].matches("\\d+")) {
            response.setContentType("text/html");
            writer.println("Все ок");
            response.setStatus(HttpServletResponse.SC_OK);
            return;
        } else  {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
//        if (split.length > 3) {
//            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
//            return;
//        }
//        response.setStatus(HttpServletResponse.SC_OK);
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doDelete(req, resp);
    }


    private List<OutPatientDto> getPatients() {
        List<Patient> patients = patientService.findAll();
        return patients.stream()
                .map(el -> PatientMapper.INSTANCE.map(el)).collect(Collectors.toList());
    }
}
