package ru.astondevs.servlet.dto.incomingDto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class IncomingScheduleDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    private String date;
    @JsonFormat(pattern = "HH:mm:ss")
    private String time;
    private int doctorId;

    public IncomingScheduleDto() {
    }

    public IncomingScheduleDto(String date, String time, int doctorId) {
        this.date = date;
        this.time = time;
        this.doctorId = doctorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }
}