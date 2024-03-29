package ru.astondevs.repository.impl;

import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.repository.DoctorScheduleRepository;

import java.util.List;

public class DoctorScheduleRepositoryImpl implements DoctorScheduleRepository {
    @Override
    public List<DoctorSchedule> findAllByDoctorId(long doctorId) {
        return null;
    }

    @Override
    public boolean changeStatus(int doctorId, boolean isBooked) {
        return false;
    }

    @Override
    public DoctorSchedule findById(Long id) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public List<DoctorSchedule> findAll() {
        return null;
    }

    @Override
    public DoctorSchedule save(DoctorSchedule schedule) {
        return null;
    }

    @Override
    public boolean update(Long object) {
        return false;
    }
}
