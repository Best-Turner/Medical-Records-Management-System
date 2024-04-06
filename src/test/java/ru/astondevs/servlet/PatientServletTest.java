package ru.astondevs.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.astondevs.exception.AppointmentNotFoundException;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Patient;
import ru.astondevs.service.PatientService;

import java.io.*;
import java.util.Collections;

import static org.mockito.Mockito.*;

class PatientServletTest {
    @Mock
    private HttpServletResponse response;
    @Mock
    private HttpServletRequest request;
    @Mock
    private PatientService service;
    @Mock
    private ObjectMapper mapper;

    @InjectMocks
    private PatientServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetListPatients() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/user");
        when(service.findAll()).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).findAll();

    }

    @Test
    void whenGetPatientByIdThenMustBeCalledMethodFinById() throws ServletException, IOException, ScheduleNotFoundException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1");
        when(service.findById(1L)).thenReturn(new Patient());
        servlet.doGet(request, response);
        verify(service, times(1)).findById(1L);
    }

    @Test
    void whenGetPatientAppointments() throws ServletException, IOException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1/appointment");
        when(service.getAppointments(1)).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).getAppointments(1);
        verify(response).setStatus(200);
    }

    @Test
    void whenDeletePatientByIdThenMustBeCallMethodDeleteById() throws ServletException, IOException, PatientNotFoundException, DoctorNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1");
        servlet.doDelete(request, response);
        verify(service, times(1)).deleteById(1L);
        verify(response).setStatus(200);
    }

    @Test
    void whenDeleteAppointmentByIdThenMustBeCallMethodDeleteAppointmentById() throws ServletException, IOException, PatientNotFoundException, AppointmentNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1/appointment/1");
        servlet.doDelete(request, response);
        verify(service, times(1)).deleteAppointment(1, 1);
        verify(response).setStatus(200);
    }

    @Test
    void whenDeleteAppointmentWithNotExistsIdThenReturnErrorAndStatus400() throws ServletException, IOException, PatientNotFoundException, AppointmentNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1/appointment/1");
        when(service.deleteAppointment(1, 1)).thenThrow(new AppointmentNotFoundException("Message"));
        servlet.doDelete(request, response);
        verify(service, times(1)).deleteAppointment(1, 1);
        verify(response).sendError(400, "Message");
    }

    @Test
    void whenInvalidURIThenReturnError() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/");
        servlet.doDelete(request, response);
        verify(response, times(1)).sendError(404, "По вашему запросу ничего не найдено");
        verify(response, never()).setStatus(200);
    }

    @Test
    void whenUpdatePatientThenMustBeCallMethodUpdate() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/user/123");
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("{\"name\": \"John Doe\"}")));
        servlet.doPut(request, response);
        verify(response).setContentType("application/json");
        verify(response, times(1)).setStatus(HttpServletResponse.SC_CREATED);
    }

    @Test
    void methodPostWhenAddNewPatientThenMustBeCallMethodSaveAndStatus201() throws IOException {
        when(request.getRequestURI()).thenReturn("/user");

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(service.save(null)).thenReturn(new Patient());
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("someText")));
        servlet.doPost(request, response);
        verify(response).setContentType("application/json");
        verify(service, times(1)).save(null);
        verify(response).setStatus(201);
    }

    @Test
    void methodPostWhenNotSavingNewPatientThenReturnStatus400() throws IOException {
        when(request.getRequestURI()).thenReturn("/user");

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(service.save(null)).thenReturn(null);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("someText")));
        servlet.doPost(request, response);
        verify(response).setContentType("application/json");
        verify(service, times(1)).save(null);
        verify(response).sendError(400);
    }

    @Test
    void methodPostWhenAddNewAppointmentThenMustBeCallMethodSaveAppointmentAnrReturnStatus201() throws IOException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1/appointment");

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("someText")));
        when(service.addAppointment(1L, null)).thenReturn(new Appointment());
        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(service, times(1)).addAppointment(1L, null);
        verify(response).setStatus(201);
    }

    @Test
    void methodPostWhenNotSavingAppointment() throws IOException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1/appointment");

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("someText")));
        when(service.addAppointment(1L, null)).thenReturn(null);
        servlet.doPost(request, response);
        verify(response).setContentType("application/json");
        verify(service, times(1)).addAppointment(1L, null);
        verify(response).sendError(400);
    }

}