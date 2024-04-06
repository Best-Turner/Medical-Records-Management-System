package ru.astondevs.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class Appointment {

    private Long id;
    private LocalDate date;
    private LocalTime time;
    private Doctor doctor;
    private Patient patient;

    public Appointment() {
    }

    public Appointment(LocalDate date, LocalTime time, Doctor doctor, Patient patient) {
        this.date = date;
        this.time = time;
        this.doctor = doctor;
        this.patient = patient;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Appointment that = (Appointment) o;
        return Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(doctor, that.doctor) && Objects.equals(patient, that.patient);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, doctor, patient);
    }

    @Override
    public String toString() {
        return "Appointment{" +
                "id=" + id +
                ", date=" + date +
                ", time=" + time +
                ", doctor=" + doctor +
                ", patient=" + patient +
                '}';
    }
}

