package ru.astondevs.repository.impl;

import ru.astondevs.db.ConnectionManager;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.DoctorRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorRepositoryImpl implements DoctorRepository {
    private ConnectionManager connectionManager;
    private String sql;

    public DoctorRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public List<Patient> getPatients(int doctorId) {
        List<Patient> patients = new ArrayList<>();
        sql = "SELECT p. * FROM patients p JOIN doctor_patient dp ON p.id = dp.id_patient WHERE dp.id_doctor=?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            ResultSet resultSet = preparedStatement.executeQuery();
            Patient patient;
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                int age = resultSet.getInt("age");
                String policyNumber = resultSet.getString("policyNumber");
                patient = new Patient(name, age, policyNumber);
                patient.setId(id);
                patients.add(patient);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return patients;
    }

    @Override
    public List<DoctorSchedule> getSchedules(int doctorId) {
        Doctor doctor = findById(doctorId).get();
        DoctorSchedule schedule;
        List<DoctorSchedule> schedules = new ArrayList<>();
        sql = "SELECT * FROM doctor_schedule WHERE doctor_id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, doctorId);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                boolean isBooked = resultSet.getBoolean("is_booked");
                schedule = new DoctorSchedule(doctor, date, time);
                schedule.setId(id);
                schedule.setBooked(isBooked);
                schedules.add(schedule);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return schedules;
    }

    @Override
    public Optional<Doctor> findById(Integer id) {
        sql = "SELECT * FROM doctors WHERE id = ?";
        Doctor doctor = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                doctor = new Doctor();
                doctor.setId(resultSet.getInt("id"));
                doctor.setName(resultSet.getString("name"));
                doctor.setSpeciality(Doctor.Speciality.valueOf(resultSet.getString("speciality")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(doctor);
    }

    @Override
    public boolean deleteById(Integer id) {
        boolean result = false;
        sql = "DELETE FROM doctors WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
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
    public List<Doctor> findAll() {
        sql = "SELECT * FROM doctors;";
        List<Doctor> doctors = new ArrayList<>();
        Doctor doctor;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                Doctor.Speciality speciality = Doctor.Speciality.valueOf(resultSet.getString("speciality"));
                doctor = new Doctor(name, speciality);
                doctor.setId(id);
                doctors.add(doctor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return doctors;
    }

    @Override
    public Optional<Doctor> save(Doctor doctor) {
        int id = 0;
        sql = "INSERT INTO doctors(name, speciality) VALUES(?, ?) RETURNING id;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, doctor.getName());
            preparedStatement.setString(2, doctor.getSpeciality().toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                id = resultSet.getInt("id");
            }
            connection.commit();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return findById(id);
    }

    @Override
    public boolean update(Doctor doctor) {
        boolean result = false;
        sql = "UPDATE doctors SET name = ?, speciality = ? WHERE id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setString(1, doctor.getName());
            preparedStatement.setString(2, doctor.getSpeciality().toString());
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
    public boolean exists(Integer id) {
        boolean executeResult = false;
        sql = "select EXISTS(select * from doctors where id = ?);";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
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
