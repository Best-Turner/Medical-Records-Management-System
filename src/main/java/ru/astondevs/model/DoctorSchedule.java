package ru.astondevs.model;

import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;


public class DoctorSchedule {

    private Long id;
    private Doctor doctor;
    private Date date;
    private Time time;
    private boolean isBooked;

    public DoctorSchedule() {
    }

    public DoctorSchedule(Doctor doctor, Date date, Time time) {
        this.doctor = doctor;
        this.date = date;
        this.time = time;
        isBooked = false;
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }
}


