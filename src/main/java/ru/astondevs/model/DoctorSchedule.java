package ru.astondevs.model;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;


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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DoctorSchedule schedule = (DoctorSchedule) o;
        return Objects.equals(doctor, schedule.doctor) && Objects.equals(date, schedule.date) && Objects.equals(time, schedule.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctor, date, time);
    }

    @Override
    public String toString() {
        return "DoctorSchedule{" +
                "id=" + id +
                ", doctor=" + doctor +
                ", date=" + date +
                ", time=" + time +
                ", isBooked=" + isBooked +
                '}';
    }
}


