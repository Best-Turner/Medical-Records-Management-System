package ru.astondevs.servlet.dto.incomingDto;

import java.time.LocalDate;
import java.time.LocalTime;

public class IncomingAppointmentDto {

    private long id;
    private LocalDate date;
    private LocalTime time;
    private long patientId;
    private int doctorId;

    public IncomingAppointmentDto() {
    }

    public IncomingAppointmentDto(LocalDate date, LocalTime time, long patientId, int doctorId) {
        this.date = date;
        this.time = time;
        this.patientId = patientId;
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

    public long getPatientId() {
        return patientId;
    }

    public void setPatientId(long patientId) {
        this.patientId = patientId;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
