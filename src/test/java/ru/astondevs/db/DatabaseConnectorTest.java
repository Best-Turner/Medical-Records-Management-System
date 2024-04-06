package ru.astondevs.db;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.astondevs.util.ReaderFile;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class DatabaseConnectorTest {

    private static final String DB_NAME = "testDB";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD_DB = "testPass";
    private static final String PROPERTIES =
            "url=jdbc:postgresql://localhost:5432/MedicalRecordsManagementSystem\n" +
                    "name=postgres\n" +
                    "password=postgres";
    @InjectMocks
    DatabaseConnector databaseConnector;
    @Mock
    private ReaderFile readerFile;
    @Mock
    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void getConnectionTest() throws SQLException {
        when(readerFile.read(anyString())).thenReturn(new ByteArrayInputStream(PROPERTIES.getBytes()));
        assertNotNull(databaseConnector.getConnection());
    }
}