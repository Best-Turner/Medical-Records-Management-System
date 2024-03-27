package ru.astondevs.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Table(name = "Patients")
@Entity
public class Patient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "age")
    private int age;
    @Column(name = "gender")
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(name = "policy_number", unique = true, nullable = false)
    private int policyNumber;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "patientOwner")
    private final List<MedicalHistory> medicalHistory = new ArrayList<>();

    public Patient() {
    }

    public Patient(String name, String surname, int age, Gender gender, int policyNumber) {
        this.name = name;
        this.surname = surname;
        this.age = age;
        this.gender = gender;
        this.policyNumber = policyNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public List<MedicalHistory> getMedicalHistory() {
        return medicalHistory;
    }

    public int getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(int policyNumber) {
        this.policyNumber = policyNumber;
    }

    public enum Gender {
        MALE, FEMALE
    }
}
