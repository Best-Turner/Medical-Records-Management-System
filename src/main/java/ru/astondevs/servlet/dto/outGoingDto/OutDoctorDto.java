package ru.astondevs.servlet.dto.outGoingDto;

import java.util.List;

public class OutDoctorDto {
    private String name;
    private String speciality;

    private List<OutDoctorScheduleDto> schedule;


    public OutDoctorDto() {
    }

    public OutDoctorDto(String name, String speciality, List<OutDoctorScheduleDto> schedule) {
        this.name = name;
        this.speciality = speciality;
        this.schedule = schedule;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeciality() {
        return speciality;
    }

    public void setSpeciality(String speciality) {
        this.speciality = speciality;
    }

    public List<OutDoctorScheduleDto> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<OutDoctorScheduleDto> schedule) {
        this.schedule = schedule;
    }
}
