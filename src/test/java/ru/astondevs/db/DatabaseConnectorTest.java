package ru.astondevs.db;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.astondevs.util.ReaderFile;

import java.io.ByteArrayInputStream;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@Testcontainers
class DatabaseConnectorTest {

    private static final String DB_NAME = "testDB";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD_DB = "testPass";

    private StringBuilder properties;
    private String url = "url=";
    @Mock
    private ReaderFile readerFile;

    @InjectMocks
    DatabaseConnector databaseConnector;
    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest"))
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD_DB)
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));


    @BeforeEach
    void setUp() {
        postgresqlContainer.start();
        MockitoAnnotations.openMocks(this);
        properties = new StringBuilder();
        url = url.concat(postgresqlContainer.getJdbcUrl());
        properties.append(url+"\n").append("name=" + USERNAME+"\n").append("password=" + PASSWORD_DB);
    }

    @Test
    void getConnectionTest() throws SQLException {
        when(readerFile.read(anyString())).thenReturn(new ByteArrayInputStream(properties.toString().getBytes()));
        assertNotNull(databaseConnector.getConnection());
    }
}