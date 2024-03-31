package ru.astondevs.servlet.dto.incomingDto;

import java.sql.Time;
import java.util.Date;

public class IncomingAppointmentDto {

    private long id;
    private Date date;
    private Time time;
    private long patientId;
    private int doctorId;

    public IncomingAppointmentDto() {
    }

    public IncomingAppointmentDto(long id, Date data, Time time, long patientId, int doctorId) {
        this.id = id;
        this.date = data;
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
