package ru.astondevs.repository.impl;

import ru.astondevs.db.ConnectionManager;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.repository.DoctorScheduleRepository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorScheduleRepositoryImpl implements DoctorScheduleRepository {
    private ConnectionManager connectionManager;
    private String sql;

    public DoctorScheduleRepositoryImpl(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
    }

    @Override
    public boolean changeStatus(int doctorId, long scheduleId, boolean isBooked) {
        sql = "UPDATE doctor_schedule SET is_booked = ? WHERE schedule_id = ? AND doctor_id = ?;";
        boolean result = false;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setBoolean(1, isBooked);
            preparedStatement.setLong(2, scheduleId);
            preparedStatement.setLong(3, doctorId);
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
    public Optional<DoctorSchedule> findById(Long id) {
        sql = "select sc.id, sc.time, sc.date, sc.is_booked, d.id as doctor_id, d.name, d.speciality\n" +
                "FROM doctor_schedule sc\n" +
                "         JOIN doctors d ON sc.doctor_id = d.id\n" +
                "WHERE sc.id =?";
        DoctorSchedule schedule = null;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                schedule = new DoctorSchedule();
                schedule.setId(resultSet.getLong("id"));
                schedule.setDate(resultSet.getDate("date").toLocalDate());
                schedule.setTime(resultSet.getTime("time").toLocalTime());
                schedule.setBooked(resultSet.getBoolean("is_booked"));
                int doctorId = resultSet.getInt("doctor_id");
                String doctorName = resultSet.getString("name");
                String speciality = resultSet.getString("speciality");
                Doctor doctor = new Doctor(doctorName, Doctor.Speciality.valueOf(speciality));
                doctor.setId(doctorId);
                schedule.setDoctor(doctor);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return Optional.of(schedule);
    }

    @Override
    public boolean deleteById(Long id) {
        boolean result = false;
        sql = "DELETE FROM doctor_schedule WHERE id = ?";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setLong(1, id);
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
    public List<DoctorSchedule> findAll() {
        sql = "SELECT * FROM doctor_schedule;";
        List<DoctorSchedule> scheduleList = new ArrayList<>();
        DoctorSchedule schedule;
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            Doctor doctor = new Doctor();
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                Date date = resultSet.getDate("date");
                Time time = resultSet.getTime("time");
                boolean isBooked = resultSet.getBoolean("is_booked");
                int doctorId = resultSet.getInt("doctor_id");
                doctor.setId(doctorId);
                schedule = new DoctorSchedule();
                schedule.setId(id);
                schedule.setDate(date.toLocalDate());
                schedule.setTime(time.toLocalTime());
                schedule.setBooked(isBooked);
                schedule.setDoctor(doctor);
                scheduleList.add(schedule);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return scheduleList;
    }

    @Override
    public Optional<DoctorSchedule> save(DoctorSchedule schedule) {
        long id = 0;
        sql = "INSERT INTO doctor_schedule(date, time, is_booked, doctor_id) VALUES(?,?,?,?) RETURNING id;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setDate(1, Date.valueOf(schedule.getDate()));
            preparedStatement.setTime(2, Time.valueOf(schedule.getTime()));
            preparedStatement.setBoolean(3, schedule.isBooked());
            preparedStatement.setInt(4, schedule.getDoctor().getId());
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
    public boolean update(DoctorSchedule schedule) {
        boolean result = false;
        sql = "UPDATE doctor_schedule SET date = ?, time = ?, is_booked = ? WHERE id = ?;";
        try (Connection connection = connectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            connection.setAutoCommit(false);
            preparedStatement.setDate(1, Date.valueOf(schedule.getDate()));
            preparedStatement.setTime(2, Time.valueOf(schedule.getTime()));
            preparedStatement.setBoolean(3, schedule.isBooked());
            preparedStatement.setLong(4, schedule.getId());
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
        sql = "select EXISTS(select * from doctor_schedule where id = ?);";
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
