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
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.service.DoctorService;
import ru.astondevs.servlet.mapper.DoctorScheduleMapper;

import java.io.*;
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

    @Mock
    private DoctorScheduleMapper doctorScheduleMapper;
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
    void whenGetDoctorPatients() throws ServletException, IOException, DoctorNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1/patient");
        when(service.getPatients(1)).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).getPatients(1);
    }

    @Test
    void whenGetDoctorSchedules() throws ServletException, IOException, DoctorNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1/schedule");
        when(service.getSchedules(1)).thenReturn(Collections.emptyList());
        servlet.doGet(request, response);
        verify(service, times(1)).getSchedules(1);
    }

    @Test
    void methodDeleteWhenDoctorByIdThenMustBeCallMethodDeleteByIdAndReturnStatus200() throws IOException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1");
        servlet.doDelete(request, response);
        verify(service, times(1)).deleteById(1);
        verify(response).setStatus(200);
    }

    @Test
    void methodDeleteWhenNotExistsScheduleId() throws IOException, ScheduleNotFoundException, DoctorNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1/schedule/1");
        when(service.deleteSchedule(1, 1)).thenThrow(new ScheduleNotFoundException("Message"));
        servlet.doDelete(request, response);
        verify(service, times(1)).deleteSchedule(1, 1L);
        verify(response).sendError(400, "Message");
    }

    @Test
    void methodDeleteWhenDeleteScheduleByIdThenMustBeCallMethodDeleteScheduleByIdAndReturnStatus200() throws ServletException, IOException, ScheduleNotFoundException, DoctorNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1/schedule/1");
        servlet.doDelete(request, response);
        verify(service, times(1)).deleteSchedule(1, 1L);
        verify(response).setStatus(200);
    }

    @Test
    void methodDeleteWhenInvalidDataThenReturnErrorAndStatus404() throws ServletException, IOException, ScheduleNotFoundException, DoctorNotFoundException {
        when(request.getRequestURI()).thenReturn("/");
        servlet.doDelete(request, response);
        verify(response).setContentType("application/json");
        verify(response).sendError(404, "По вашему запросу ничего не найдено");
    }

    @Test
    void methodDeleteWhenInvalidDoctorIdThenReturnErrorAndStatus400() throws IOException, DoctorNotFoundException, PatientNotFoundException {
        when(request.getRequestURI()).thenReturn("/doctor/1");
        when(service.deleteById(1)).thenThrow(new DoctorNotFoundException("Message"));
        servlet.doDelete(request, response);
        verify(response).setContentType("application/json");
        verify(response).sendError(400, "Message");
    }

    @Test
    void methodPostWhenAddNewDoctorThenMustBeCallMethodSaveAndReturnStatus201() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/doctor");

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(service.save(null)).thenReturn(new Doctor());
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("someText")));
        servlet.doPost(request, response);
        verify(response).setContentType("application/json");
        verify(service, times(1)).save(null);
        verify(response).setStatus(201);
    }

    @Test
    void methodPostWhenNotSavingNewDoctorThenReturnStatus400() throws IOException, ServletException {
        when(request.getRequestURI()).thenReturn("/doctor");

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(service.save(null)).thenReturn(null);
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("someText")));
        servlet.doPost(request, response);
        verify(response).setContentType("application/json");
        verify(service, times(1)).save(null);
        verify(response).sendError(400);
    }

    @Test
    void methodPostWhenAddNewScheduleThenMustBeCallMethodSaveScheduleAnrReturnStatus201() throws IOException, DoctorNotFoundException, ScheduleNotFoundException, ServletException {
        when(request.getRequestURI()).thenReturn("/doctor/1/schedule");

        when(response.getWriter()).thenReturn(new PrintWriter(new StringWriter()));
        when(request.getReader()).thenReturn(new BufferedReader(new StringReader("someText")));
        when(service.addSchedule(null, 1)).thenReturn(new DoctorSchedule());
        servlet.doPost(request, response);
        verify(response).setContentType("application/json");
        verify(service, times(1)).addSchedule(null, 1);
        verify(response).setStatus(201);
    }

}