package ru.astondevs.repository.impl;

import ru.astondevs.db.ConnectionManager;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.AppointmentRepository;

import java.sql.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class AppointmentRepositoryImpl implements AppointmentRepository {

    private final ConnectionManager connectionManager;
    private String sql;

    public AppointmentRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public Optional<Appointment> findById(Long id) {
        sql = "SELECT a.id, a.date, a.time, d.id as doctor_id, d.name, d.speciality, p.id as patient_id\n" +
                "FROM appointments a\n" +
                "         JOIN doctors d on a.doctor_id = d.id JOIN patients p on a.patient_id = p.id\n" +
                "WHERE a.id =?;";
        Appointment appointment = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                appointment = new Appointment();
                appointment.setId(resultSet.getLong("id"));
                Date date = resultSet.getDate("date");
                appointment.setTime(resultSet.getTime("time").toLocalTime());
                long patientId = resultSet.getLong("patient_id");
                String doctorName = resultSet.getString("name");
                Doctor.Speciality speciality = Doctor.Speciality.valueOf(resultSet.getString("speciality"));
                appointment.setDoctor(new Doctor(doctorName, speciality));
                Patient patient = new Patient();
                patient.setId(patientId);
                appointment.setPatient(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(appointment);
    }

    @Override
    public boolean deleteById(Long id) {
        boolean result = false;
        sql = "DELETE FROM appointments WHERE id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
            int countDeletedRows = preparedStatement.executeUpdate();
            if (countDeletedRows != 0) {
                result = true;
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public List<Appointment> findAll() {
        sql = "SELECT * FROM appointments;";
        List<Appointment> appointmentList = new ArrayList<>();
        Appointment appointment;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Doctor doctor = new Doctor();
            Patient patient = new Patient();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                int doctorId = resultSet.getInt("doctor_id");
                long patientId = resultSet.getLong("patient_id");
                doctor.setId(doctorId);
                patient.setId(patientId);
                appointment = new Appointment();
                appointment.setId(id);
                appointment.setDate(date.toLocalDate());
                appointment.setTime(time.toLocalTime());
                appointment.setPatient(patient);
                appointment.setDoctor(doctor);
                appointmentList.add(appointment);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return appointmentList;
    }

    @Override
    public Optional<Appointment> save(Appointment appointment) {
        long id = 0;
        sql = "INSERT INTO appointment(date, time, patient_id, doctor_id) VALUES(?,?,?,?) RETURNING id;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            Date date = Date.valueOf(appointment.getDate());
            Time time = Time.valueOf(LocalTime.now());
            preparedStatement.setDate(1, date);
            Time sqlTime = Time.valueOf(appointment.getTime());
            preparedStatement.setTime(2, sqlTime);
            preparedStatement.setLong(3, appointment.getPatient().getId());
            preparedStatement.setInt(4, appointment.getDoctor().getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getLong("id");
                connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(id);
    }

    @Override
    public boolean update(Appointment appointment) {
        boolean result = false;
        sql = "UPDATE appointments SET date = ?, time = ? WHERE id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setDate(1, Date.valueOf(appointment.getDate()));
            preparedStatement.setTime(2, Time.valueOf(appointment.getTime()));
            int countDeletedRows = preparedStatement.executeUpdate();
            if (countDeletedRows != 0) {
                result = true;
                connection.commit();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return result;
    }

    @Override
    public boolean exists(Long id) {
        boolean executeResult = false;
        sql = "select EXISTS(select * from appointments where id = ?);";
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
