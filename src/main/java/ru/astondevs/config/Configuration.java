package ru.astondevs.config;

import ru.astondevs.db.ConnectionManager;
import ru.astondevs.repository.AppointmentRepository;
import ru.astondevs.repository.DoctorRepository;
import ru.astondevs.repository.DoctorScheduleRepository;
import ru.astondevs.repository.PatientRepository;
import ru.astondevs.repository.impl.AppointmentRepositoryImpl;
import ru.astondevs.repository.impl.DoctorRepositoryImpl;
import ru.astondevs.repository.impl.DoctorScheduleRepositoryImpl;
import ru.astondevs.repository.impl.PatientRepositoryImpl;
import ru.astondevs.service.DoctorService;
import ru.astondevs.service.Impl.DoctorServiceImpl;
import ru.astondevs.service.Impl.PatientServiceImpl;
import ru.astondevs.service.PatientService;

public class Configuration {
    private PatientRepository patientRepository;
    private DoctorRepository doctorRepository;
    private AppointmentRepository appointmentRepository;
    private DoctorScheduleRepository scheduleRepository;
    private ConnectionManager connectionManager;
    private DoctorService doctorService;
    private PatientService patientService;

    public Configuration(ConnectionManager connectionManager) {
        this.connectionManager = connectionManager;
        init();
    }

    private void init() {
        appointmentRepository = new AppointmentRepositoryImpl(connectionManager);
        scheduleRepository = new DoctorScheduleRepositoryImpl(connectionManager);
        doctorRepository = new DoctorRepositoryImpl(connectionManager);
        patientRepository = new PatientRepositoryImpl(connectionManager);
        doctorService = new DoctorServiceImpl(doctorRepository, scheduleRepository);
        patientService = new PatientServiceImpl(patientRepository, appointmentRepository, doctorRepository);
    }

    public DoctorService getDoctorService() {
        return doctorService;
    }

    public PatientService getPatientService() {
        return patientService;
    }
}
