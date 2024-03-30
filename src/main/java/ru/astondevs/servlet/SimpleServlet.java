package ru.astondevs.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.db.DatabaseConnector;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.SQLException;

@WebServlet(value = "/hello")
public class SimpleServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        DatabaseConnector connection = (DatabaseConnector) getServletContext().getAttribute("connection");

        PrintWriter writer = resp.getWriter();
        writer.println("<html>");
        writer.println("<p>" + "Hello,   </p>");
        writer.println("<p>" + "counter  </p>");
        writer.println("</html>");
        writer.close();
    }
}
