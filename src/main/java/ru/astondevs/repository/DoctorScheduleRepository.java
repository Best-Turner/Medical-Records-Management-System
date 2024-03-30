package ru.astondevs.repository;

import ru.astondevs.model.DoctorSchedule;

import java.util.List;

public interface DoctorScheduleRepository extends Repository<DoctorSchedule, Long> {

    boolean changeStatus(int doctorId, boolean isBooked);
}
