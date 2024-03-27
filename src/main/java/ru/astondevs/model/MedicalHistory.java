package ru.astondevs.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "Medical_History")
@Entity
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "patient_id", referencedColumnName = "id", nullable = false)
    private Patient patientOwner;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;
    @Column(name = "medical_procedures_performed")
    private String medicalProceduresPerformed;
    @Column(name = "doctor_recommendations")
    private String doctorRecommendations;
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "date", nullable = false)
    private LocalDateTime date;


    public MedicalHistory() {
    }

    public MedicalHistory(Patient patientOwner, Doctor doctor, String medicalProceduresPerformed, String doctorRecommendations) {
        this.doctor = doctor;
        date = LocalDateTime.now();
        this.patientOwner = patientOwner;
        this.doctorRecommendations = doctorRecommendations;
        this.medicalProceduresPerformed = medicalProceduresPerformed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Patient getPatientOwner() {
        return patientOwner;
    }

    public void setPatientOwner(Patient patientOwner) {
        this.patientOwner = patientOwner;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getMedicalProceduresPerformed() {
        return medicalProceduresPerformed;
    }

    public void setMedicalProceduresPerformed(String medicalProceduresPerformed) {
        this.medicalProceduresPerformed = medicalProceduresPerformed;
    }

    public String getDoctorRecommendations() {
        return doctorRecommendations;
    }

    public void setDoctorRecommendations(String doctorRecommendations) {
        this.doctorRecommendations = doctorRecommendations;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }
}
