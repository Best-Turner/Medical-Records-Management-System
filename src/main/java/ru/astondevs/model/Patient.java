package ru.astondevs.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patient patient = (Patient) o;
        return age == patient.age && Objects.equals(name, patient.name) && Objects.equals(policyNumber, patient.policyNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age, policyNumber);
    }
}
