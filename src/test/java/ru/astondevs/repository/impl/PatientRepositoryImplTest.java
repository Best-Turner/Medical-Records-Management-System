package ru.astondevs.repository.impl;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.astondevs.db.ConnectionManager;

import static org.postgresql.shaded.com.ongres.scram.common.ScramAttributes.USERNAME;

@Testcontainers
class PatientRepositoryImplTest {
    private static final String DB_NAME = "testDB";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD_DB = "testPass";
    @Mock
    private ConnectionManager connectionManager;
    @InjectMocks
    private PatientRepositoryImpl repository;


    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest"))
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD_DB)
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
}