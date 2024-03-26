package ru.astondevs.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "Medical_History")
@Entity
public class MedicalHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long patientId;
    private Long doctorId;
    private String medicalProceduresPerformed;
    private String doctorRecommendations;

    private LocalDateTime dateTime;


    public MedicalHistory() {

    }
}
