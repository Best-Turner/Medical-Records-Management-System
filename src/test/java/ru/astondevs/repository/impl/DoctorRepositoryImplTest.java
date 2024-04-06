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
import ru.astondevs.model.Patient;
import ru.astondevs.repository.DoctorRepository;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
            }
        } catch (SQLException e) {

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
    void whenSaveDoctorWhenReturnThisDoctor() {
        Doctor actual = repository.save(doctor).get();
        assertEquals(doctor.getName(), actual.getName());
        assertEquals(doctor.getSpeciality(), actual.getSpeciality());
    }


    @Test
    void findByIdTest() {
        Doctor fromDb = repository.save(doctor).get();
        Doctor actual = repository.findById(fromDb.getId()).get();
        assertEquals(fromDb, actual);
    }


    @Test
    void deleteByIdTest() {
        Doctor fromDb = repository.save(doctor).get();
        Doctor actual = repository.findById(fromDb.getId()).get();
        boolean result = repository.deleteById(actual.getId());
        Assertions.assertTrue(result);
    }


    @Test
    void findAllTest() {
        List<Doctor> all = repository.findAll();
        Assertions.assertFalse(all.isEmpty());
        assertEquals(3, all.size());
    }

    @Test
    void whenDoctorExistsThenReturnTrue() {
        Assertions.assertTrue(repository.exists(1));
    }

    @Test
    void whenGetAllPatientsThenMustBeReturnAllYourPatients() {
        createPatientsTable();
        populatePatientsDb();
        createPatientsDoctorsTable();
        populatePatientsDoctorsTable();
        List<Patient> patients = repository.getPatients(1);
        dropPatientsDoctorTable();
        dropPatientsTable();
        assertEquals(3, patients.size());
    }

    @Test
    void whenGetAllDoctorScheduleThenReturnAllSchedule() {
        createDoctorScheduleTable();
        populateDoctorSchedule();
        List<DoctorSchedule> schedules = repository.getSchedules(1);
        dropDoctorScheduleTable();
        assertEquals(3, schedules.size());
    }

    @Test
    void whenDeleteScheduleThenCountScheduleMustBuDecrease() {
        createDoctorScheduleTable();
        populateDoctorSchedule();
        List<DoctorSchedule> schedules = repository.getSchedules(1);
        assertEquals(3, schedules.size());
        repository.deleteSchedule(1, 1);
        schedules = repository.getSchedules(1);
        dropDoctorScheduleTable();
        assertEquals(2, schedules.size());
    }

    @Test
    void whenUpdateDoctorThenReturnTrue() {
        int doctorId = repository.save(doctor).get().getId();
        doctor.setName("anotherName");
        doctor.setSpeciality(Doctor.Speciality.PEDIATRICIAN);
        doctor.setId(doctorId);
        boolean actual = repository.update(doctor);
        assertTrue(actual);
    }

    private void createDoctorScheduleTable() {
        final String sql = "CREATE TABLE doctor_schedule\n" +
                "(\n" +
                "    id        serial primary key not null,\n" +
                "    date      DATE               not null,\n" +
                "    time      TIME               not null,\n" +
                "    is_booked boolean            not null,\n" +
                "    doctor_id int                not null,\n" +
                "    foreign key (doctor_id) references doctors (id) on delete CASCADE\n" +
                ")";
        templateForInsertingIntoDb(sql);
    }

    private void populateDoctorSchedule() {
        final String sql = "INSERT INTO doctor_schedule(date, time, is_booked, doctor_id)\n" +
                "VALUES ('2024-01-10', '13:45:00', false, 1),\n" +
                "       ('2024-01-10', '14:00:00', false, 1),\n" +
                "       ('2024-01-10', '14:15:00', true, 1),\n" +
                "       ('2024-01-10', '10:30:00', true, 2);";
        templateForInsertingIntoDb(sql);
    }

    private void dropDoctorScheduleTable() {
        final String sql = "DROP TABLE doctor_schedule";
        templateForInsertingIntoDb(sql);
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


    private void createPatientsTable() {
        final String createTable = "CREATE TABLE patients" +
                "( id serial primary key not null," +
                "name  varchar(100) not null," +
                "age int not null," +
                "policy_number varchar(50) unique not null);";
        templateForInsertingIntoDb(createTable);
    }

    private void populatePatientsDb() {
        String sql = "INSERT INTO patients(name, age, policy_number)\n" +
                "VALUES ('Александр', 52, '123-456'),\n" +
                "       ('Сергей', 45, '654-321'),\n" +
                "       ('Юлия', 32, '111-222'),\n" +
                "       ('Алексей', 25, '222-333');";
        templateForInsertingIntoDb(sql);
    }

    private void createPatientsDoctorsTable() {
        final String createTable = "CREATE TABLE doctor_patient\n" +
                "(\n" +
                "    id_doctor  INT,\n" +
                "    id_patient INT,\n" +
                "    PRIMARY KEY (id_doctor, id_patient),\n" +
                "    FOREIGN KEY (id_doctor) REFERENCES doctors (id) on DELETE CASCADE,\n" +
                "    FOREIGN KEY (id_patient) REFERENCES patients (id) ON DELETE CASCADE\n" +
                ");";
        templateForInsertingIntoDb(createTable);
    }

    private void populatePatientsDoctorsTable() {
        final String sql = "INSERT INTO doctor_patient (id_doctor, id_patient)\n" +
                "VALUES (1, 1),\n" +
                "       (1, 2),\n" +
                "       (1, 3),\n" +
                "       (2, 1),\n" +
                "       (2, 2),\n" +
                "       (3, 1);";

        templateForInsertingIntoDb(sql);
    }

    private void dropPatientsDoctorTable() {
        final String sql = "DROP TABLE doctor_patient";
        templateForInsertingIntoDb(sql);
    }

    private void dropPatientsTable() {
        final String sql = "DROP TABLE patients";
        templateForInsertingIntoDb(sql);
    }

    private void templateForInsertingIntoDb(String sql) {
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
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
