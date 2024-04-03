package ru.astondevs.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.astondevs.exception.AppointmentNotFoundException;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.model.Appointment;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.AppointmentRepository;
import ru.astondevs.repository.PatientRepository;
import ru.astondevs.repository.impl.DoctorRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;


class PatientServiceImplTest {
    private final static long PATIENT_ID = 1L;
    private final static String PATIENT_NAME = "SomeName";
    private final static String PATIENT_MEDICAL_NUMBER = "123-456";
    private final static int PATIENT_AGE = 20;

    @Mock
    private PatientRepository patientRepository;
    @Mock
    private AppointmentRepository appointmentRepository;
    @Mock
    private DoctorRepositoryImpl doctorRepository;
    @InjectMocks
    private PatientServiceImpl service;

    private List<Patient> patientList;
    private List<Appointment> appointmentList;

    private Patient patient;

    @BeforeEach
    void setUp() {
        patient = new Patient(PATIENT_NAME, PATIENT_AGE, PATIENT_MEDICAL_NUMBER);
        patient.setId(PATIENT_ID);
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void whenFindExistsPatientByIdThenReturnPatient() throws PatientNotFoundException {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        Patient actual = service.findById(PATIENT_ID);
        assertEquals(patient, actual);
    }


    @Test
    void whenDeleteExistsPatientThenReturnTrue() {
        when(patientRepository.deleteById(PATIENT_ID)).thenReturn(true);
        assertTrue(patientRepository.deleteById(PATIENT_ID));
    }

    @Test
    void whenDeleteNotExistsPatientThenThrowPatientNotFoundException() {
        when(patientRepository.deleteById(PATIENT_ID)).thenReturn(false);
        assertThrows(PatientNotFoundException.class, () -> service.deleteById(PATIENT_ID));
    }

    @Test
    void whenGetListPatientsThenReturnListPatients() {
        initPatientList();
        when(patientRepository.findAll()).thenReturn(patientList);
        List<Patient> actual = service.findAll();
        assertEquals(patientList, actual);
    }

    @Test
    void whenGetListAppointmentsThenReturnListAppointments() throws PatientNotFoundException {
        initAppointmentsList();
        when(patientRepository.getAllAppointments((int) PATIENT_ID)).thenReturn(appointmentList);
        when(patientRepository.exists(PATIENT_ID)).thenReturn(true);
        List<Appointment> actual = service.getAppointments(PATIENT_ID);
        assertEquals(appointmentList, actual);
    }

    @Test
    void whenAddAppointmentsThenReturnThisAppointment() throws PatientNotFoundException, DoctorNotFoundException {
        //initAppointmentsList();
        Doctor doctor = new Doctor("name", Doctor.Speciality.THERAPIST);
        doctor.setId(1);
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        Appointment appointment = new Appointment(LocalDate.now(), LocalTime.now(), doctor, patient);
        when(appointmentRepository.save(appointment)).thenReturn(Optional.of(appointment));
        when(doctorRepository.findById(1)).thenReturn(Optional.of(doctor));
        Appointment actual = service.addAppointment(PATIENT_ID, appointment);
        assertEquals(appointment, actual);
    }

    @Test
    void whenDeleteExistsAppointmentThenReturnTrue() throws PatientNotFoundException, DoctorNotFoundException, AppointmentNotFoundException {
        Doctor doctor = new Doctor("name", Doctor.Speciality.THERAPIST);
        doctor.setId(1);
        Appointment appointment = new Appointment(LocalDate.now(), LocalTime.now(), doctor, patient);
        appointment.setId(1L);
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(appointmentRepository.findById(1L)).thenReturn(Optional.of(appointment));
        when(patientRepository.exists(PATIENT_ID)).thenReturn(true);
        initAppointmentsList();
        when(patientRepository.getAllAppointments(1)).thenReturn(appointmentList);
        boolean actual = service.deleteAppointment(PATIENT_ID, 1L);
        assertTrue(true);
    }


    @Test
    void whenSavePatientThenReturnThisPatient() {
        when(patientRepository.save(patient)).thenReturn(Optional.of(patient));
        Patient actual = service.save(patient);
        assertEquals(patient, actual);
    }

    @Test
    void whenChangeNamePatientThenReturnTrue() throws PatientNotFoundException {
        when(patientRepository.findById(PATIENT_ID)).thenReturn(Optional.of(patient));
        when(patientRepository.exists(PATIENT_ID)).thenReturn(true);
        when(patientRepository.update(patient)).thenReturn(true);
        patient.setName("anotherName");
        patient.setAge(33);
        patient.setPolicyNumber("anotherPolicyNumber");
        boolean actual = service.update(patient, PATIENT_ID);
        assertTrue(actual);
    }


    private void initPatientList() {
        patientList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            patientList.add(new Patient("testName" + i, PATIENT_AGE + 1, "number" + i));
        }
    }

    private void initAppointmentsList() {
        appointmentList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Appointment appointment = new Appointment(LocalDate.now(), LocalTime.now(), null, patient);
            appointment.setId((long) i);
            appointmentList.add(appointment);
        }
    }
}