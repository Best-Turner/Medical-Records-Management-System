package ru.astondevs.service.Impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;
import ru.astondevs.model.Doctor;
import ru.astondevs.model.DoctorSchedule;
import ru.astondevs.model.Patient;
import ru.astondevs.repository.DoctorRepository;
import ru.astondevs.repository.impl.DoctorScheduleRepositoryImpl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DoctorServiceImplTest {

    private static final int DOCTOR_ID = 1;
    private static final String DOCTOR_NAME = "SomeName";
    private static final Doctor.Speciality SPECIALITY = Doctor.Speciality.THERAPIST;
    private List<Patient> patientList;
    private List<Doctor> doctorList;
    private List<DoctorSchedule> scheduleList;
    private Doctor doctor;

    @Mock
    private DoctorRepository doctorRepository;
    @Mock
    private DoctorScheduleRepositoryImpl scheduleRepository;
    @InjectMocks
    private DoctorServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        doctor = new Doctor(DOCTOR_NAME, SPECIALITY);
    }

    @Test
    void whenGetExistsDoctorByIdThenReturnDoctorWithThisId() throws DoctorNotFoundException {
        returnTrueWhenRepositoryCheckFor();
        when(doctorRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(doctor));
        Doctor actual = service.findById(DOCTOR_ID);
        assertEquals(doctor, actual);
    }

    @Test
    void whenDeleteExistsDoctorThenReturnTrue() throws DoctorNotFoundException {
        returnTrueWhenRepositoryCheckFor();
        when(doctorRepository.deleteById(DOCTOR_ID)).thenReturn(true);
        assertTrue(service.deleteById(DOCTOR_ID));
    }

    @Test
    void whenGetDoctorListThenReturnDoctorList() throws DoctorNotFoundException {
        initDoctorList();
        when(doctorRepository.findAll()).thenReturn(doctorList);
        List<Doctor> actual = service.findAll();
        assertEquals(doctorList, actual);
    }


    @Test
    void whenSaveDoctorThenReturnThisDoctor() throws DoctorNotFoundException {
        when(doctorRepository.save(doctor)).thenReturn(Optional.of(doctor));
        Doctor actual = service.save(doctor);
        assertEquals(doctor, actual);
    }

    @Test
    void whenUpdateDoctorThenReturnTrue() throws DoctorNotFoundException {
        Doctor newDoctor = new Doctor();
        returnTrueWhenRepositoryCheckFor();
        when(doctorRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(doctor));
        newDoctor.setName("newName");
        newDoctor.setSpeciality(Doctor.Speciality.PEDIATRICIAN);
        when(doctorRepository.update(newDoctor)).thenReturn(true);
        assertTrue(service.update(newDoctor, DOCTOR_ID));
    }

    @Test
    void whenChangeStatusThenReturnTrue() throws ScheduleNotFoundException, DoctorNotFoundException {
        returnTrueWhenRepositoryCheckFor();
        when(scheduleRepository.findById(1L)).thenReturn(Optional.of(new DoctorSchedule()));
        when(scheduleRepository.changeStatus(DOCTOR_ID, 1L, true)).thenReturn(true);
        assertTrue(service.changeStatusSchedule(DOCTOR_ID, 1L, true));
    }

    @Test
    void whenGetScheduleListThenReturnThisList() throws DoctorNotFoundException {
        returnTrueWhenRepositoryCheckFor();
        initScheduleList();
        when(doctorRepository.getSchedules(DOCTOR_ID)).thenReturn(scheduleList);
        List<DoctorSchedule> actual = service.getSchedules(DOCTOR_ID);
        assertArrayEquals(scheduleList.toArray(), actual.toArray());
    }

    @Test
    void whenSaveScheduleThenReturnThisSchedule() throws ScheduleNotFoundException, DoctorNotFoundException {
        initScheduleList();
        returnTrueWhenRepositoryCheckFor();
        DoctorSchedule schedule = scheduleList.get(1);
        when(doctorRepository.findById(DOCTOR_ID)).thenReturn(Optional.of(doctor));
        when(scheduleRepository.save(schedule)).thenReturn(Optional.of(schedule));
        DoctorSchedule actual = service.addSchedule(schedule, DOCTOR_ID);
        assertEquals(schedule, actual);
    }

    @Test
    void whenDeleteNotExistScheduleThenMustBeCalledMethodFromRepository() {
        returnTrueWhenRepositoryCheckFor();
        when(scheduleRepository.exists(1L)).thenReturn(false);
        verify(doctorRepository, never()).deleteSchedule(DOCTOR_ID, 1L);
    }


    private void returnTrueWhenRepositoryCheckFor() {
        when(doctorRepository.exists(DOCTOR_ID)).thenReturn(true);
    }


    private void initDoctorList() {
        doctorList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            doctorList.add(new Doctor(DOCTOR_NAME + i, SPECIALITY));
        }
    }

    private void initPatientList() {
        patientList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            patientList.add(new Patient("testName" + i, 1, "number" + i));
        }
    }

    private void initScheduleList() {
        scheduleList = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            scheduleList.add(new DoctorSchedule(doctor, LocalDate.now(), LocalTime.now()));
        }
    }
}