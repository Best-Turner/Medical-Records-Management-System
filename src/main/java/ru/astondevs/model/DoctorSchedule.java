package ru.astondevs.model;

import java.time.LocalDate;
import java.time.LocalTime;


public class DoctorSchedule {

    private Long id;
    private Doctor doctor;
    private LocalDate date;
    private LocalTime time;
    private boolean isBooked;

    public DoctorSchedule() {
    }

    public DoctorSchedule(Doctor doctor, LocalDate date, LocalTime time) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        this.isBooked = false;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}


