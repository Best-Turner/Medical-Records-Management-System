package ru.astondevs.repository.impl;

import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.DoctorRepository;

import java.util.List;

public class DoctorRepositoryImpl implements DoctorRepository {
    @Override
    public List<Patient> getPatients(int doctorId) {
        return null;
    }

    @Override
    public List<DoctorSchedule> getSchedules(int doctorId) {
        return null;
    }

    @Override
    public Doctor findById(Integer id) {
        return null;
    }

    @Override
    public boolean deleteById(Integer id) {
        return false;
    }

    @Override
    public List<Doctor> findAll() {
        return null;
    }

    @Override
    public Doctor save(Doctor doctor) {
        return null;
    }

    @Override
    public boolean update(Integer object) {
        return false;
    }
}
