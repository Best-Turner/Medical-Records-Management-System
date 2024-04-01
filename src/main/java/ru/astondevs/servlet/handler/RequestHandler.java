package ru.astondevs.servlet.handler;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.service.Service;

import java.io.IOException;
import java.io.Writer;

public interface RequestHandler {

    HttpServletResponse methodGet(HttpServletRequest request, HttpServletResponse response) throws IOException;
    HttpServletResponse methodPost(HttpServletRequest request, HttpServletResponse response) throws IOException;
    HttpServletResponse methodPut(HttpServletRequest request, HttpServletResponse response);
    HttpServletResponse methodDelete(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
