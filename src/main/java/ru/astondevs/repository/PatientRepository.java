package ru.astondevs.repository;

import ru.astondevs.model.Appointment;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.Patient;

import java.util.List;

public interface PatientRepository extends Repository<Patient, Long> {

    List<Doctor> getMyDoctors(int patientId);

    List<Appointment> getAllAppointments(int patientId);
    boolean existsPolicyNumber(String policyNumber);
}
