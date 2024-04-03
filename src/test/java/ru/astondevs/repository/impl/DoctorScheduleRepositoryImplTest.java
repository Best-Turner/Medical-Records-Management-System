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
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.repository.DoctorScheduleRepository;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Testcontainers
class DoctorScheduleRepositoryImplTest {
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
    private DoctorScheduleRepository repository;
    private Doctor doctor;
    private DoctorSchedule schedule;

    @BeforeEach
    void setUp() {
        postgresqlContainer.start();
        connectionManager = new TestConnection();
        doctor = new Doctor(DOCTOR_NAME, SPECIALITY);
        schedule = new DoctorSchedule(doctor, LocalDate.now(), LocalTime.now());
        try (Connection connection = connectionManager.getConnection()) {
            connection.setAutoCommit(true);
            repository = new DoctorScheduleRepositoryImpl(connectionManager);
            String createDoctorTableSQL =
                    "CREATE TABLE doctors\n" +
                            "(\n" +
                            "    id         serial primary key not null,\n" +
                            "    name       varchar(50)        not null,\n" +
                            "    speciality varchar(50)\n" +
                            ");";
            String createDoctorScheduleTableSQL =
                    "CREATE TABLE doctor_schedule\n" +
                            "(\n" +
                            "    id        serial primary key not null,\n" +
                            "    date      DATE               not null,\n" +
                            "    time      TIME               not null,\n" +
                            "    is_booked boolean            not null,\n" +
                            "    doctor_id int                not null,\n" +
                            "    foreign key (doctor_id) references doctors (id) on delete CASCADE\n" +
                            ");";

            try (PreparedStatement createDoctorTable = connection.prepareStatement(createDoctorTableSQL);
                 PreparedStatement createDoctorScheduleTable = connection.prepareStatement(createDoctorScheduleTableSQL)) {
                createDoctorTable.executeUpdate();
                createDoctorScheduleTable.executeUpdate();
                populateDoctorDb(connection);
                populateDoctorScheduleDb(connection);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @AfterEach
    void tearDown() throws SQLException {
        String dropDoctorTableSQL = "DROP table public.doctors";
        String dropDoctorScheduleTableSQL = "DROP table public.doctor_schedule";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement dropDoctorTableStatement = connection.prepareStatement(dropDoctorTableSQL);
             PreparedStatement dropTDoctorScheduleTableStatement = connection.prepareStatement(dropDoctorScheduleTableSQL)) {
            dropTDoctorScheduleTableStatement.execute();
            dropDoctorTableStatement.execute();
            System.out.println("Удалена");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Test
    public void whenSaveDoctorScheduleWhenReturnThisDoctorSchedule() {
        Doctor doctorFromDb = repository.findById(1L).get().getDoctor();
        schedule.setDoctor(doctorFromDb);
        DoctorSchedule schedulefromDb = repository.save(schedule).get();
        assertEquals(schedule.getDate(), schedulefromDb.getDate());
        assertEquals(schedule.getDoctor(), schedulefromDb.getDoctor());
    }


    @Test
    public void findByIdTest() {
        Doctor doctorFromDb = repository.findById(1L).get().getDoctor();
        schedule.setDoctor(doctorFromDb);
        DoctorSchedule fromDb = repository.save(schedule).get();
        DoctorSchedule actual = repository.findById(fromDb.getId()).get();
        assertEquals(fromDb, actual);
    }


    @Test
    public void deleteByIdTest() {
        Doctor doctorFromDb = repository.findById(1L).get().getDoctor();
        schedule.setDoctor(doctorFromDb);
        DoctorSchedule fromDb = repository.save(schedule).get();
        DoctorSchedule actual = repository.findById(fromDb.getId()).get();
        boolean result = repository.deleteById(actual.getId());
        Assertions.assertTrue(result);
    }


    @Test
    void findAllTest() throws SQLException {
        List<DoctorSchedule> all = repository.findAll();
        Assertions.assertFalse(all.isEmpty());
        assertEquals(8, all.size());
    }

    @Test
    void whenDoctorScheduleExistsThenReturnTrue() throws SQLException {
        Assertions.assertTrue(repository.exists(1L));
    }


    private void populateDoctorDb(Connection connection) throws SQLException {
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

    private void populateDoctorScheduleDb(Connection connection) throws SQLException {
        String sql = "INSERT INTO doctor_schedule(date, time, is_booked, doctor_id)\n" +
                "VALUES ('2024-01-10', '13:45:00', false, 1),\n" +
                "       ('2024-01-10', '14:00:00', false, 1),\n" +
                "       ('2024-01-10', '14:15:00', true, 1),\n" +
                "       ('2024-01-10', '10:30:00', true, 2),\n" +
                "       ('2024-01-10', '11:00:00', false, 2),\n" +
                "       ('2024-01-11', '12:10:00', false, 3),\n" +
                "       ('2024-01-11', '13:00:00', false, 3),\n" +
                "       ('2024-01-11', '13:30:00', true, 3);";
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