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
import ru.astondevs.model.Doctor;
import ru.astondevs.repository.DoctorRepository;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class DoctorRepositoryImplTest {
    private static final String DOCTOR_NAME = "name";
    private static final Doctor.Speciality SPECIALITY = Doctor.Speciality.THERAPIST;
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
    private DoctorRepository repository;
    private Doctor doctor;

    @BeforeEach
    void setUp() {
        postgresqlContainer.start();
        connectionManager = new TestConnection();
        doctor = new Doctor(DOCTOR_NAME, SPECIALITY);
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(true);
            repository = new DoctorRepositoryImpl(connectionManager);
            String createTableSQL =
                    "CREATE TABLE doctors( " +
                            "id         serial primary key not null," +
                            "name       varchar(50)        not null," +
                            "speciality varchar(50));";
            try (PreparedStatement createTable = connection.prepareStatement(createTableSQL)) {
                createTable.executeUpdate();
                populateDb(connection);
                System.out.println("База данных создана");
            }
        } catch (SQLException e) {
            System.out.println("База данных не создана");
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        String dropTableSQL = "DROP table public.doctors";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement dropTableStatement = connection.prepareStatement(dropTableSQL)) {
            dropTableStatement.execute();
            System.out.println("Удалена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void whenSaveDoctorWhenReturnThisDoctor() {
        Doctor actual = repository.save(doctor).get();
        assertEquals(doctor.getName(), actual.getName());
        assertEquals(doctor.getSpeciality(), actual.getSpeciality());
    }


    @Test
    public void findByIdTest() {
        Doctor fromDb = repository.save(doctor).get();
        Doctor actual = repository.findById(fromDb.getId()).get();
        assertEquals(fromDb, actual);
    }


    @Test
    public void deleteByIdTest() {
        Doctor fromDb = repository.save(doctor).get();
        Doctor actual = repository.findById(fromDb.getId()).get();
        boolean result = repository.deleteById(actual.getId());
        assertTrue(result);
    }


    @Test
    void findAllTest() throws SQLException {
        List<Doctor> all = repository.findAll();
        Assertions.assertFalse(all.isEmpty());
        assertEquals(3, all.size());
    }

    @Test
    void whenDoctorExistsThenReturnTrue() throws SQLException {
        assertTrue(repository.exists(1));
    }


    private void populateDb(Connection connection) throws SQLException {
        String sql = "INSERT INTO doctors(name, speciality) " +
                "VALUES ('Василий', 'SURGEON')," +
                "('Юрий', 'THERAPIST')," +
                "('Светлана', 'PEDIATRICIAN');";
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
