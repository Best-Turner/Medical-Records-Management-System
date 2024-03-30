package ru.astondevs.service.Impl;

import ru.astondevs.model.Doctor;
import ru.astondevs.service.DoctorService;

import java.util.List;

public class DoctorServiceImpl implements DoctorService {
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
    public boolean update(Doctor toUpdated, Integer object) {
        return false;
    }
}
