package ru.astondevs.service.Impl;

import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.DoctorRepository;
import ru.astondevs.repository.DoctorScheduleRepository;
import ru.astondevs.service.DoctorService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class DoctorServiceImpl implements DoctorService {
    private final DoctorRepository doctorRepository;
    private final DoctorScheduleRepository scheduleRepository;

    public DoctorServiceImpl(DoctorRepository repository, DoctorScheduleRepository scheduleRepository) {
        this.doctorRepository = repository;
        this.scheduleRepository = scheduleRepository;
    }

    @Override
    public Doctor findById(Integer id) throws DoctorNotFoundException {
        checkDoctorId(id);
        return doctorRepository.findById(id).get();
    }

    @Override
    public boolean deleteById(Integer id) throws DoctorNotFoundException {
        checkDoctorId(id);
        return doctorRepository.deleteById(id);
    }

    @Override
    public List<Doctor> findAll() {
        List<Doctor> doctors = doctorRepository.findAll();
        return doctors.isEmpty() ? Collections.emptyList() : doctors;
    }

    @Override
    public Doctor save(Doctor doctor) {
        return doctorRepository.save(doctor).orElseThrow(() -> new IllegalArgumentException("Ошибка при сохранении"));
    }

    @Override
    public boolean update(Doctor toUpdated, Integer doctorId) throws DoctorNotFoundException {
        checkDoctorId(doctorId);
        Doctor doctorFromDb = doctorRepository.findById(doctorId).get();
        if (!toUpdated.getName().isEmpty()) {
            doctorFromDb.setName(toUpdated.getName());
        }
        if (!toUpdated.getSpeciality().name().isEmpty()) {
            doctorFromDb.setSpeciality(toUpdated.getSpeciality());
        }
        return doctorRepository.update(doctorFromDb);
    }


    @Override
    public boolean changeStatusSchedule(int doctorId, long scheduleId, boolean status) throws DoctorNotFoundException, ScheduleNotFoundException {
        checkDoctorId(doctorId);
        return scheduleRepository.changeStatus(doctorId, scheduleId, status);
    }


    @Override
    public List<Patient> getPatients(int doctorId) throws DoctorNotFoundException {
        checkDoctorId(doctorId);
        List<Patient> patients = doctorRepository.getPatients(doctorId);
        return patients.isEmpty() ? Collections.emptyList() : patients;
    }


    @Override
    public List<DoctorSchedule> getSchedules(int doctorId) throws DoctorNotFoundException {
        checkDoctorId(doctorId);
        List<DoctorSchedule> schedules = doctorRepository.getSchedules(doctorId);
        return schedules.isEmpty() ? Collections.emptyList() : schedules;
    }

    @Override
    public DoctorSchedule addSchedule(DoctorSchedule schedule, int doctorId) throws DoctorNotFoundException, ScheduleNotFoundException {
        checkDoctorId(doctorId);
        Doctor doctor = doctorRepository.findById(doctorId).get();
        schedule.setDoctor(doctor);
        return scheduleRepository.save(schedule).orElseThrow(() ->new ScheduleNotFoundException("Расписание не сохранилось"));
    }


    @Override
    public void  deleteSchedule(int doctorId, long scheduleId) throws DoctorNotFoundException, ScheduleNotFoundException {
        checkDoctorId(doctorId);
        boolean exists = scheduleRepository.exists(scheduleId);
        if (!exists) {
            throw new ScheduleNotFoundException("Расписание не существует");
        }
        doctorRepository.deleteSchedule(doctorId, scheduleId);
    }


    private void checkDoctorId(int doctorId) throws DoctorNotFoundException {
        boolean exists = doctorRepository.exists(doctorId);
        if (!exists) {
            throw new DoctorNotFoundException("Доктор с ID = " + doctorId + " не найден");
        }
    }
}
