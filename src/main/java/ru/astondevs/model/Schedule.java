package ru.astondevs.model;

import javax.persistence.*;
import java.util.List;

@Table(name = "schedules")
@Entity
public class Schedule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false)
    private Doctor doctor;
    @OneToMany
    @OneToOne
    private List<FreeSlot> slotList;
    private Patient patient;

}
