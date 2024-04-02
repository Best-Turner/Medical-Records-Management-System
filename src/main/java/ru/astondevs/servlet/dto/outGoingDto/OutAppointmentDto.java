package ru.astondevs.servlet.dto.outGoingDto;

public class OutAppointmentDto {

    private String date;
    private String time;
    private String specialityDoctor;
    private String nameDoctor;

    public OutAppointmentDto() {
    }

    public OutAppointmentDto(String date, String time, String specialityDoctor, String nameDoctor) {
        this.date = date;
        this.time = time;
        this.specialityDoctor = specialityDoctor;
        this.nameDoctor = nameDoctor;
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

    public String getSpecialityDoctor() {
        return specialityDoctor;
    }

    public void setSpecialityDoctor(String specialityDoctor) {
        this.specialityDoctor = specialityDoctor;
    }

    public String getNameDoctor() {
        return nameDoctor;
    }

    public void setNameDoctor(String nameDoctor) {
        this.nameDoctor = nameDoctor;
    }
}
