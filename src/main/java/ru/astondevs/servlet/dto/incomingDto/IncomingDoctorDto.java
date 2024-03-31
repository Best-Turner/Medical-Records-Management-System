package ru.astondevs.servlet.dto.incomingDto;

import ru.astondevs.model.Doctor;

public class IncomingDoctorDto {
    private int id;
    private String name;
    private Doctor.Speciality speciality;

    public IncomingDoctorDto() {
    }

    public IncomingDoctorDto(int id, String name, Doctor.Speciality speciality) {
        this.id = id;
        this.name = name;
        this.speciality = speciality;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Doctor.Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Doctor.Speciality speciality) {
        this.speciality = speciality;
    }
}
