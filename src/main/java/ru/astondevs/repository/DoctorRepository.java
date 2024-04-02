package ru.astondevs.repository;

import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.model.Patient;

import java.util.List;

public interface DoctorRepository extends Repository<Doctor, Integer> {

    List<Patient> getPatients(int doctorId);


    List<DoctorSchedule> getSchedules(int doctorId);

    void deleteSchedule(int doctorId, long scheduleId);
}
