package ru.astondevs.servlet.dto.outGoingDto;

import java.util.List;

public class OutDoctorDto {
    private String name;
    private String speciality;


    public OutDoctorDto() {
    }

    public OutDoctorDto(String name, String speciality) {
        this.name = name;
        this.speciality = speciality;

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
}
