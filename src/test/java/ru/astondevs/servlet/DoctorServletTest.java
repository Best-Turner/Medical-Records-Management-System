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
import ru.astondevs.model.Doctor;
import ru.astondevs.service.DoctorService;

import java.io.IOException;
import java.util.Collections;

import static org.mockito.Mockito.*;

class DoctorServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private DoctorService service;
    @Mock
    private ObjectMapper mapper;
    @InjectMocks
    private DoctorServlet servlet;


    @BeforeEach
    void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenGetListDoctors() throws ServletException, IOException {
        when(request.getRequestURI()).thenReturn("/doctor");
        when(service.findAll()).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).findAll();

    }

    @Test
    void whenGetDoctorByIdThenMustBeCalledMethodFinById() throws ServletException, IOException, ScheduleNotFoundException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1");
        when(service.findById(1)).thenReturn(new Doctor());
        servlet.doGet(request, response);
        verify(service, times(1)).findById(1);
    }

    @Test
    void whenGetDoctorPatients() throws ServletException, IOException, ScheduleNotFoundException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1/patient");
        when(service.getPatients(1)).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).getPatients(1);
    }

    @Test
    void whenGetDoctorSchedules() throws ServletException, IOException, ScheduleNotFoundException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1/schedule");
        when(service.getSchedules(1)).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).getSchedules(1);
    }

}