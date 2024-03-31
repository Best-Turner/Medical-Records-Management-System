package ru.astondevs.service;

import ru.astondevs.exception.AppointmentNotFoundException;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Patient;

import java.util.List;

public interface PatientService extends Service<Patient, Long> {

    List<Appointment> getAppointments(long patientId) throws PatientNotFoundException;
    Appointment addAppointment(long patientId, int doctorId, Appointment appointment) throws PatientNotFoundException, DoctorNotFoundException;

    boolean deleteAppointment(long patientId, long appointmentId) throws PatientNotFoundException, AppointmentNotFoundException;
}
