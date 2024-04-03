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
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Patient;
import ru.astondevs.service.PatientService;

import java.io.IOException;
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
    void whenGetPatientAppointments() throws ServletException, IOException, ScheduleNotFoundException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/user/1/appointment");
        when(service.getAppointments(1)).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).getAppointments(1);
    }

}