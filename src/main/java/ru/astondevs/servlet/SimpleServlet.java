package ru.astondevs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.db.ConnectionManager;
import ru.astondevs.db.DatabaseConnector;
import ru.astondevs.model.Doctor;
import ru.astondevs.repository.Repository;
import ru.astondevs.repository.impl.DoctorRepositoryImpl;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(value = "/hello")
public class SimpleServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ConnectionManager connectionManager = (DatabaseConnector) getServletContext().getAttribute("connection");


        Repository repository = new DoctorRepositoryImpl(connectionManager);
        try {
            System.out.println(connectionManager.getConnection() == null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        Doctor doctor = new Doctor("Петя", Doctor.Speciality.THERAPIST);
        PrintWriter writer = resp.getWriter();
        //Doctor save = (Doctor)repository.save(doctor);
        writer.println("<html>");
        writer.println("<p>" + doctor + " </p>");
        writer.println("<p>" + "counter  </p>");
        writer.println("</html>");
        writer.close();
    }
}
