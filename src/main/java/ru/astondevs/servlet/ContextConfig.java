package ru.astondevs.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import ru.astondevs.db.ConnectionManager;
import ru.astondevs.db.DatabaseConnector;
import ru.astondevs.db.DatabaseInitialize;
import ru.astondevs.util.ReadResources;
import ru.astondevs.util.ReaderFile;

@WebListener
public class ContextConfig implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        ServletContext servletContext = servletContextEvent.getServletContext();
        ReaderFile readerFile = new ReadResources();
        ConnectionManager connectionManager = new DatabaseConnector(readerFile);
        new DatabaseInitialize(connectionManager, readerFile).initDataBase();
        servletContext.setAttribute("connection", connectionManager);
    }
}



