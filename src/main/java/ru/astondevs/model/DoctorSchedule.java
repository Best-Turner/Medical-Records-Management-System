package ru.astondevs.model;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Table(name = "Schedule")
@Entity
public class DoctorSchedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_work")
    private LocalTime startWork;
    @Column(name = "end_work")
    private LocalTime endWork;
    @OneToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id")
    private Doctor doctor;
    @Column(name = "date")
    @Temporal(TemporalType.DATE)
    private Date date;
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private final List<FreeSlot> freeSlotList = new ArrayList<>();

    public DoctorSchedule() {
    }

    public DoctorSchedule(LocalTime startWork, LocalTime endWork, Doctor doctor, Date date) {
        this.startWork = startWork;
        this.endWork = endWork;
        this.doctor = doctor;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getStartWork() {
        return startWork;
    }

    public void setStartWork(LocalTime startWork) {
        this.startWork = startWork;
    }

    public LocalTime getEndWork() {
        return endWork;
    }

    public void setEndWork(LocalTime endWork) {
        this.endWork = endWork;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<FreeSlot> getFreeSlotList() {
        return freeSlotList;
    }
}
