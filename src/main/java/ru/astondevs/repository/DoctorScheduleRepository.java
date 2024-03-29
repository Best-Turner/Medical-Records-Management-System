package ru.astondevs.repository;

import ru.astondevs.model.DoctorSchedule;

import java.util.List;

public interface DoctorScheduleRepository extends Repository<DoctorSchedule, Long> {

    List<DoctorSchedule> findAllByDoctorId(long doctorId);

    boolean changeStatus(int doctorId, boolean isBooked);
}
