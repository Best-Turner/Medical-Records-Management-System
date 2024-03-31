package ru.astondevs.servlet.dto.incomingDto;

import java.sql.Time;
import java.util.Date;

public class IncomingScheduleDto {

    private long id;
    private Date date;
    private Time time;
    private int doctorId;

    public IncomingScheduleDto() {
    }

    public IncomingScheduleDto(long id, Date data, Time time, int doctorId) {
        this.id = id;
        this.date = data;
        this.time = time;
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

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}
