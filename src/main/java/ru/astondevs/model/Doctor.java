package ru.astondevs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class Doctor {
    private Integer id;
    private String name;
    private Speciality speciality;
    private List<Patient> patients = new ArrayList<>();
    private List<DoctorSchedule> schedule = new ArrayList<>();


    public Doctor() {
    }

    public Doctor(String name, Speciality speciality) {
        this.name = name;
        this.speciality = speciality;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public List<DoctorSchedule> getSchedule() {
        return schedule;
    }

    public void setSchedule(List<DoctorSchedule> schedule) {
        this.schedule = schedule;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Doctor doctor = (Doctor) o;
        return Objects.equals(name, doctor.name) && speciality == doctor.speciality;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, speciality);
    }

    @Override
    public String toString() {
        return "Doctor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", speciality=" + speciality +
                '}';
    }

    public enum Speciality {
        SURGEON, THERAPIST, PEDIATRICIAN
    }
}
