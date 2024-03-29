package ru.astondevs.repository.impl;

import ru.astondevs.model.Appointment;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.PatientRepository;

import java.util.List;

public class PatientRepositoryImpl implements PatientRepository {
    @Override
    public List<Doctor> getMyDoctors(int patientId) {
        return null;
    }

    @Override
    public List<Appointment> getAllAppointments(int patientId) {
        return null;
    }

    @Override
    public Patient findById(Long id) {
        return null;
    }

    @Override
    public boolean deleteById(Long id) {
        return false;
    }

    @Override
    public List<Patient> findAll() {
        return null;
    }

    @Override
    public Patient save(Patient patient) {
        return null;
    }

    @Override
    public boolean update(Long object) {
        return false;
    }
}
