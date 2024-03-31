package ru.astondevs.service.Impl;

import ru.astondevs.exception.AppointmentNotFoundException;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.AppointmentRepository;
import ru.astondevs.repository.DoctorRepository;
import ru.astondevs.repository.PatientRepository;
import ru.astondevs.service.PatientService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class PatientServiceImpl implements PatientService {


    private final PatientRepository patientRepository;
    private final AppointmentRepository appointmentRepository;

    private final DoctorRepository doctorRepository;

    public PatientServiceImpl(PatientRepository patientRepository, AppointmentRepository appointmentRepository, DoctorRepository doctorRepository) {
        this.patientRepository = patientRepository;
        this.appointmentRepository = appointmentRepository;
        this.doctorRepository = doctorRepository;
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


    @Override
    public List<Appointment> getAppointments(long patientId) throws PatientNotFoundException {
        if (!patientRepository.exists(patientId)) {
            throw new PatientNotFoundException(getErrorMessage(patientId));
        }
        List<Appointment> appointments = appointmentRepository.findAll();
        List<Appointment> myAppointments = appointments.stream().filter(el ->
                el.getPatient().getId() == patientId).collect(Collectors.toList());
        return myAppointments.isEmpty() ? Collections.emptyList() : myAppointments;
    }

    @Override
    public Appointment addAppointment(long patientId, int doctorId, Appointment appointment) throws PatientNotFoundException, DoctorNotFoundException {
        Patient patient = patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(getErrorMessage(doctorId)));
        Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new DoctorNotFoundException("Доктор с ID = " + doctorId + " не найден"));
        appointment.setPatient(patient);
        appointment.setDoctor(doctor);
        return appointmentRepository.save(appointment).orElseThrow(() -> new IllegalArgumentException("Ошибка при сохранении"));
    }

    @Override
    public boolean deleteAppointment(long patientId, long appointmentId) throws PatientNotFoundException, AppointmentNotFoundException {
        patientRepository.findById(patientId).orElseThrow(() -> new PatientNotFoundException(getErrorMessage(patientId)));
        appointmentRepository.findById(appointmentId).orElseThrow(() -> new AppointmentNotFoundException("Расписание с ID = " + appointmentId + " не найдено"));
        Optional<Appointment> appointmentForDelete = getAppointments(appointmentId).stream().filter(el -> el.getId() == appointmentId).findFirst();
        appointmentForDelete.orElseThrow(() -> new AppointmentNotFoundException("Расписание не существует"));
        return appointmentRepository.deleteById(appointmentId);
    }

    private String getErrorMessage(long id) {
        return String.format("Пациент с ID = %d не найден", id);
    }

}
