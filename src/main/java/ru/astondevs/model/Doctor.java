package ru.astondevs.model;

import javax.persistence.*;

@Table(name = "Doctors")
@Entity
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "work_experience")
    private int workExperience;
    @Enumerated(EnumType.STRING)
    private Speciality speciality;

    public enum Speciality {
        SURGEON, THERAPIST, PEDIATRICIAN
    }

    public Doctor() {
    }

    public Doctor(String name, String surname, int workExperience, Speciality speciality) {
        this.name = name;
        this.surname = surname;
        this.workExperience = workExperience;
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public int getWorkExperience() {
        return workExperience;
    }

    public void setWorkExperience(int workExperience) {
        this.workExperience = workExperience;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    public void setSpeciality(Speciality speciality) {
        this.speciality = speciality;
    }
}
