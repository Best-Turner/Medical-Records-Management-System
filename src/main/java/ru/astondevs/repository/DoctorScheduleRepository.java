package ru.astondevs.repository;

import ru.astondevs.model.DoctorSchedule;

public interface DoctorScheduleRepository extends Repository<DoctorSchedule, Long> {

    boolean changeStatus(int doctorId, long scheduleId, boolean isBooked);
}
