package ru.astondevs.repository.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.utility.DockerImageName;
import ru.astondevs.db.ConnectionManager;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.AppointmentRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AppointmentRepositoryImplTest {

    private static final String DB_NAME = "testDB";
    private static final String USERNAME = "testUser";
    private static final String PASSWORD_DB = "testPass";
    private static final String CREATE_TABLE_DOCTORS_SQL = "CREATE TABLE doctors" +
            "(id serial primary key not null," +
            "name  varchar(50) not null, " +
            "speciality varchar(50));";
    private static final String CREATE_TABLE_PATIENTS_SQL = "CREATE TABLE patients" +
            "( id serial primary key not null," +
            "name  varchar(100) not null," +
            "age int not null," +
            "policy_number varchar(50) unique not null);";
    private static final String CREATE_TABLE_APPOINTMENTS_SQL = "CREATE TABLE appointments\n" +
            "(\n" +
            "    id         SERIAL PRIMARY KEY,\n" +
            "    date       DATE NOT NULL,\n" +
            "    time       TIME NOT NULL,\n" +
            "    doctor_id  INT  NOT NULL,\n" +
            "    patient_id INT  NOT NULL,\n" +
            "    FOREIGN KEY (doctor_id) REFERENCES doctors (id) on delete cascade,\n" +
            "    FOREIGN KEY (patient_id) REFERENCES patients (id) on delete cascade\n" +
            ");";
    @Container
    public static PostgreSQLContainer<?> postgresqlContainer = new PostgreSQLContainer<>(
            DockerImageName.parse("postgres:latest"))
            .withDatabaseName(DB_NAME)
            .withUsername(USERNAME)
            .withPassword(PASSWORD_DB)
            .waitingFor(Wait.forLogMessage(".*database system is ready to accept connections.*\\n", 2));
    private ConnectionManager connectionManager;
    private AppointmentRepository repository;
    private Appointment appointment;
    private Doctor doctor;
    private Patient patient;

    @BeforeEach
    void setUp() {
        postgresqlContainer.start();
        connectionManager = new TestConnection();
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(true);
            repository = new AppointmentRepositoryImpl(connectionManager);

            try (PreparedStatement createTableDoctors = connection.prepareStatement(CREATE_TABLE_DOCTORS_SQL);
                 PreparedStatement createTablePatients = connection.prepareStatement(CREATE_TABLE_PATIENTS_SQL);
                 PreparedStatement createTableAppointments = connection.prepareStatement(CREATE_TABLE_APPOINTMENTS_SQL)) {
                createTableDoctors.executeUpdate();
                createTablePatients.executeUpdate();
                createTableAppointments.executeUpdate();
                populateDoctorsDb(connection);
                populatePatientsDb(connection);
                populateAppointmentsDb(connection);
                System.out.println("База данных создана");
            }
        } catch (SQLException e) {
            System.out.println("База данных не создана");
            throw new RuntimeException(e);
        }
    }

    @AfterEach
    public void tearDown() {
        String dropDoctorsTableSQL = "DROP table public.doctors";
        String dropPatientsTableSQL = "DROP table public.patients";
        String dropAppointmentsTableSQL = "DROP table public.appointments";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement dropAppointmentTableStatement = connection.prepareStatement(dropAppointmentsTableSQL);
             PreparedStatement dropDoctorsTableStatement = connection.prepareStatement(dropDoctorsTableSQL);
             PreparedStatement dropPatientsTableStatement = connection.prepareStatement(dropPatientsTableSQL)) {
            dropAppointmentTableStatement.executeUpdate();
            dropDoctorsTableStatement.executeUpdate();
            dropPatientsTableStatement.executeUpdate();
            System.out.println("Удалена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    void whenAppointmentNotExistsThenReturnFalse() {
        deleteAllFromAppointmentTable(connectionManager);
        assertFalse(repository.exists(1L));
    }

    @Test
    void whenAppointmentExistsThenReturnTrue() throws SQLException {
        populateDoctorsDb(connectionManager.getConnection());
        assertTrue(repository.exists(1L));
    }

    @Test
    void whenGetAllAppointmentsThenMustBeReturnAllAppointments() throws SQLException {
        List<Appointment> allAppointments = repository.findAll();
        assertEquals(4, allAppointments.size());
    }

    @Test
    void whenSaveNewAppointmentThenReturnThisAppointment() throws SQLException {
        initAppointment();
        Appointment actual = repository.save(appointment).get();
        assertEquals(appointment.getDate(), actual.getDate());
        assertEquals(appointment.getTime(), actual.getTime());
        assertEquals(patient.getId(), appointment.getPatient().getId());
        assertEquals(doctor.getId(), appointment.getDoctor().getId());
    }

    @Test
    void whenDeleteExistsAppointmentThenReturnTrue() throws SQLException {
        assertTrue(repository.deleteById(1L));
    }

    @Test
    void whenDeleteNotExistsAppointmentThenReturnFalse() throws SQLException {
        assertFalse(repository.deleteById(100L));
    }

    @Test
    void whenUpdateAppointmentThenReturnTrue() throws SQLException {
        initAppointment();
        long id = repository.save(appointment).get().getId();
        LocalDate newDate = LocalDate.parse("2024-10-10");
        LocalTime newTime = LocalTime.parse("20:00:00");
        appointment.setDate(newDate);
        appointment.setTime(newTime);
        appointment.setId(id);
        boolean actual = repository.update(appointment);
        assertTrue(actual);
    }


    private void initAppointment() {
        LocalTime time = LocalTime.parse("13:30:00");
        LocalDate date = LocalDate.parse("2023-08-12");
        doctor = new Doctor("someName", Doctor.Speciality.SURGEON);
        patient = new Patient("testName", 18, "777-777");
        doctor.setId(1);
        patient.setId(1L);
        appointment = new Appointment(date, time, doctor, patient);
    }

    private void populateDoctorsDb(Connection connection) throws SQLException {
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

    private void populatePatientsDb(Connection connection) throws SQLException {
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

    private void populateAppointmentsDb(Connection connection) throws SQLException {
        String sql = "INSERT INTO appointments(date, time, doctor_id, patient_id)\n" +
                "VALUES ('2024-01-10', '14:15:00', 1, 1),\n" +
                "       ('2024-01-10', '10:30:00', 2, 1),\n" +
                "       ('2024-01-11', '13:30:00', 3, 1),\n" +
                "       ('2023-08-12', '13:30:00', 3, 2);";
        connection.setAutoCommit(false);
        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
        connection.commit();
        statement.close();
    }

    private void deleteAllFromAppointmentTable(ConnectionManager connectionManager) {
        final String dropAllFromAppointments = "DELETE FROM appointments;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(dropAllFromAppointments)) {
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    private static class TestConnection implements ConnectionManager {
        @Override
        public Connection getConnection() throws SQLException {
            return DriverManager.getConnection(postgresqlContainer.getJdbcUrl(), USERNAME, PASSWORD_DB);
        }
    }

}