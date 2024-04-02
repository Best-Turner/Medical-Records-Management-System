package ru.astondevs.model;

import java.util.ArrayList;
import java.util.List;

public class Patient {
    private final List<Appointment> appointments = new ArrayList<>();
    private final List<Doctor> doctors = new ArrayList<>();
    private Long id;
    private String name;
    private int age;
    private String policyNumber;

    public Patient() {
    }

    public Patient(String name, int age, String policyNumber) {
        this.name = name;
        this.age = age;
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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    public List<Appointment> getAppointments() {
        return appointments;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }

}
