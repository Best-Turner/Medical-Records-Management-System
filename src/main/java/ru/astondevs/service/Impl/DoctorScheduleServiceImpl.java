package ru.astondevs.service.Impl;

import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.service.DoctorScheduleService;

import java.util.List;

public class DoctorScheduleServiceImpl implements DoctorScheduleService {
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
    public boolean update(DoctorSchedule toUpdated, Long object) {
        return false;
    }
}
