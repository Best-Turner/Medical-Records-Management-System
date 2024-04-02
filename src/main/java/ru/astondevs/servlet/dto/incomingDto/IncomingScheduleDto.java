package ru.astondevs.servlet.dto.incomingDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class IncomingScheduleDto {

    private long id;
    private LocalDate date;
    private LocalTime time;
    private int doctorId;

    public IncomingScheduleDto() {
    }

    public IncomingScheduleDto(LocalDate date, LocalTime time, int doctorId) {
        this.date = date;
        this.time = time;
        this.doctorId = doctorId;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
