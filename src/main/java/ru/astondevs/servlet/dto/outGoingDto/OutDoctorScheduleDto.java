package ru.astondevs.servlet.dto.outGoingDto;

public class OutDoctorScheduleDto {

    private String date;

    private String time;

    private boolean status;

    public OutDoctorScheduleDto() {
    }

    public OutDoctorScheduleDto(String date, String time, boolean status) {
        this.date = date;
        this.time = time;
        this.status = status;
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

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
