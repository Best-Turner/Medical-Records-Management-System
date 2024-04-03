package ru.astondevs.servlet;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class DoctorServletTest {

    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;

    private DoctorServlet servlet;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        servlet = new DoctorServlet();
    }

    @Test
    void firstTestServlet() {

    }
}