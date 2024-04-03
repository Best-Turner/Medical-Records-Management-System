package ru.astondevs.repository.impl;

import ru.astondevs.db.ConnectionManager;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.PatientRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PatientRepositoryImpl implements PatientRepository {
    private final ConnectionManager connectionManager;
    private String sql;

    public PatientRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Doctor> getMyDoctors(int patientId) {
        List<Doctor> doctorsList = new ArrayList<>();
        sql = "SELECT d. * FROM doctors d JOIN doctor_patient dp ON d.id = dp.id_doctor WHERE dp.id_patient = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Doctor doctor;
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String speciality = resultSet.getString("speciality");
                doctor = new Doctor(name, Doctor.Speciality.valueOf(speciality));
                doctor.setId(id);
                doctorsList.add(doctor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return doctorsList;
    }

    @Override
    public List<Appointment> getAllAppointments(int patientId) {
        Appointment appointment;
        List<Appointment> appointmentsList = new ArrayList<>();
        sql = "SELECT A.id,\n" +
                "       A.date,\n" +
                "       A.time,\n" +
                "       D.name as doctor_name,\n" +
                "       D.speciality\n" +
                "FROM appointments A\n" +
                "         JOIN\n" +
                "     doctors D ON A.doctor_id = D.id\n" +
                "WHERE A.patient_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, patientId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                String doctorName = resultSet.getString("doctor_name");
                String speciality = resultSet.getString("speciality");
                appointment =
                        new Appointment(date.toLocalDate(), time.toLocalTime(), new Doctor(doctorName, Doctor.Speciality.valueOf(speciality)), null);
                appointment.setId(id);
                appointmentsList.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointmentsList;
    }

    @Override
    public boolean existsPolicyNumber(String policyNumber) {
        boolean executeResult = false;
        sql = "select EXISTS(select * from patients p where p.policy_number = ?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, policyNumber);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                executeResult = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeResult;
    }


    @Override
    public Optional<Patient> findById(Long id) {
        sql = "SELECT * FROM patients WHERE id = ?";
        Patient patient = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                patient = new Patient();
                patient.setId(resultSet.getLong("id"));
                patient.setName(resultSet.getString("name"));
                patient.setAge(resultSet.getInt("age"));
                patient.setPolicyNumber(resultSet.getString("policy_number"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(patient);
    }

    @Override
    public boolean deleteById(Long id) {
        boolean result = false;
        sql = "DELETE FROM patients WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            int countDeletedRows = preparedStatement.executeUpdate();
            if (countDeletedRows != 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Patient> findAll() {
        sql = "SELECT * FROM patients;";
        List<Patient> patientList = new ArrayList<>();
        Patient patient;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String policyNumber = resultSet.getString("policy_number");
                patient = new Patient();
                patient.setId(id);
                patient.setName(name);
                patient.setAge(age);
                patient.setPolicyNumber(policyNumber);
                patientList.add(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patientList;
    }

    @Override
    public Optional<Patient> save(Patient patient) {
        long id = 0;
        sql = "INSERT INTO patients(name, age, policy_number) VALUES(?, ?, ?) RETURNING id;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setInt(2, patient.getAge());
            preparedStatement.setString(3, patient.getPolicyNumber());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(id);
    }

    @Override
    public boolean update(Patient patient) {
        boolean result = false;
        sql = "UPDATE patients SET name = ?, age = ?, policy_number = ? WHERE id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, patient.getName());
            preparedStatement.setInt(2, patient.getAge());
            preparedStatement.setString(3, patient.getPolicyNumber());
            preparedStatement.setLong(4, patient.getId());
            int countDeletedRows = preparedStatement.executeUpdate();
            connection.commit();
            if (countDeletedRows != 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean deleteAppointmentsById(long appointmentId, int doctorId) {
        boolean result = false;
        sql = "DELETE * FROM appointments a WHERE a.id = ? AND a.patient_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, appointmentId);
            preparedStatement.setLong(2, doctorId);
            int countDeletedRows = preparedStatement.executeUpdate();
            connection.commit();
            if (countDeletedRows != 0) {
                result = true;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean exists(Long id) {
        boolean executeResult = false;
        sql = "select EXISTS(select * from patients where id = ?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                executeResult = resultSet.getBoolean(1);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return executeResult;
    }
}
