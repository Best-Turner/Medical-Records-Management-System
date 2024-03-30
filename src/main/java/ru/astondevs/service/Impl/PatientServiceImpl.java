package ru.astondevs.service.Impl;

import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.PatientRepository;
import ru.astondevs.service.PatientService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;

    public PatientServiceImpl(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    @Override
    public Patient findById(Long id) throws PatientNotFoundException {
        Optional<Patient> byId = patientRepository.findById(id);
        return byId.orElseThrow(() -> new PatientNotFoundException(getErrorMessage(id)));
    }

    @Override
    public boolean deleteById(Long id) throws PatientNotFoundException {
        if (!patientRepository.exists(id)) {
            throw new PatientNotFoundException(getErrorMessage(id));
        }
        return patientRepository.deleteById(id);
    }

    @Override
    public List<Patient> findAll() {
        List<Patient> all = patientRepository.findAll();
        return all.isEmpty() ? Collections.emptyList() : all;
    }

    @Override
    public Patient save(Patient patient) {
        if (patientRepository.existsPolicyNumber(patient.getPolicyNumber())) {
            throw new IllegalArgumentException("Пациент с данным номером полиса уже зарегистрирован");
        }
        return patientRepository.save(patient).orElseThrow(() -> new IllegalArgumentException("Ошибка при сохранении"));
    }

    @Override
    public boolean update(Patient toUpdated, Long patientId) throws PatientNotFoundException {
        if (!patientRepository.exists(patientId)) {
            throw new PatientNotFoundException(getErrorMessage(patientId));
        }
        Patient patientFromDb = patientRepository.findById(patientId).get();
        if (!toUpdated.getName().isBlank()) {
            patientFromDb.setName(toUpdated.getName());
        }
        if (toUpdated.getAge() >= 0 && toUpdated.getAge() < 150) {
            patientFromDb.setAge(toUpdated.getAge());
        }
        String newPolicyNumber = toUpdated.getPolicyNumber();
        if (!(newPolicyNumber.isBlank() && patientRepository.existsPolicyNumber(newPolicyNumber))) {
            patientFromDb.setPolicyNumber(newPolicyNumber);
        }
        return patientRepository.update(patientFromDb);
    }

    private String getErrorMessage(long id) {
        return String.format("Пациент с ID = %d не найден", id);
    }
}
