package ru.astondevs.config;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.astondevs.db.ConnectionManager;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class ConfigurationTest {

    @Mock
    private ConnectionManager connectionManager;

    @InjectMocks
    private Configuration configuration;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        configuration = new Configuration(connectionManager);
    }

    @Test
    void whenGetDoctorServiceThenReturnDoctorService() throws SQLException {
        assertNotNull(configuration.getDoctorService());
    }

    @Test
    void whenGetPatientServiceThenReturnPatientService() throws SQLException {
        assertNotNull(configuration.getPatientService());
    }
}