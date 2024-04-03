package ru.astondevs.service;

import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.model.Patient;

import java.util.List;

public interface DoctorService extends Service<Doctor, Integer> {

    boolean changeStatusSchedule(int doctorId, long scheduleId, boolean status) throws DoctorNotFoundException, ScheduleNotFoundException;

    List<Patient> getPatients(int doctorId) throws DoctorNotFoundException;

    List<DoctorSchedule> getSchedules(int doctorId) throws DoctorNotFoundException;

    DoctorSchedule addSchedule(DoctorSchedule schedule, int doctorId) throws DoctorNotFoundException, ScheduleNotFoundException;

    void deleteSchedule(int doctorId, long scheduleId) throws DoctorNotFoundException, ScheduleNotFoundException;
}
