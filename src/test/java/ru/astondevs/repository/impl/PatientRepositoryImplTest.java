package ru.astondevs.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;
import ru.astondevs.db.ConnectionManager;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.PatientRepository;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class PatientRepositoryImplTest {
    private final static long PATIENT_ID = 1L;
    private final static String PATIENT_NAME = "SomeName";
    private final static String PATIENT_MEDICAL_NUMBER = "000-000";
    private final static int PATIENT_AGE = 20;
    private static final String DB_NAME = "testDB";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD_DB = "testPass";
    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest"))
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD_DB)
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));
    private ConnectionManager connectionManager;
    private PatientRepository repository;
    private Patient patient;

    @BeforeEach
    void setUp() {
        postgresqlContainer.start();
        connectionManager = new TestConnection();
        patient = new Patient(PATIENT_NAME, PATIENT_AGE, PATIENT_MEDICAL_NUMBER);
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(true);
            repository = new PatientRepositoryImpl(connectionManager);
            String createTableSQL =
                    "CREATE TABLE patients\n" +
                            "(\n" +
                            "    id            serial primary key not null,\n" +
                            "    name          varchar(100)       not null,\n" +
                            "    age           int                not null,\n" +
                            "    policy_number varchar(50) unique not null\n" +
                            ");";
            try (PreparedStatement createTable = connection.prepareStatement(createTableSQL)) {
                createTable.executeUpdate();
                populateDb(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    void tearDown() {
        String dropTableSQL = "DROP table public.patients";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement dropTableStatement = connection.prepareStatement(dropTableSQL)) {
            dropTableStatement.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void whenSavePatientWhenReturnThisPatient() {
        Patient actual = repository.save(patient).get();
        assertEquals(patient.getName(), actual.getName());
        assertEquals(patient.getPolicyNumber(), actual.getPolicyNumber());
        assertEquals(patient.getAge(), actual.getAge());
    }


    @Test
    public void findByIdTest() {
        Patient fromDb = repository.save(patient).get();
        Patient actual = repository.findById(fromDb.getId()).get();
        assertEquals(fromDb, actual);
    }


    @Test
    public void deleteByIdTest() {
        Patient fromDb = repository.save(patient).get();
        Patient actual = repository.findById(fromDb.getId()).get();
        boolean result = repository.deleteById(actual.getId());
        Assertions.assertTrue(result);
    }


    @Test
    void findAllTest() throws SQLException {
        List<Patient> all = repository.findAll();
        Assertions.assertFalse(all.isEmpty());
        assertEquals(4, all.size());
    }

    @Test
    void whenPatientExistsThenReturnTrue() throws SQLException {
        Assertions.assertTrue(repository.exists(PATIENT_ID));
    }


    @Test
    void whenPolicyNumberExistsThenReturnTrue() throws SQLException {
        repository.save(patient);
        boolean actual = repository.existsPolicyNumber(PATIENT_MEDICAL_NUMBER);
        Assertions.assertTrue(actual);
    }


    private void populateDb(Connection connection) throws SQLException {
        String sql = "INSERT INTO patients(name, age, policy_number)\n" +
                "VALUES ('Александр', 52, '123-456'),\n" +
                "       ('Сергей', 45, '654-321'),\n" +
                "       ('Юлия', 32, '111-222'),\n" +
                "       ('Алексей', 25, '222-333');";
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        connection.commit();
        statement.close();
    }


    private static class TestConnection implements ConnectionManager {
        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(postgresqlContainer.getJdbcUrl(), USERNAME, PASSWORD_DB);
        }
    }
}