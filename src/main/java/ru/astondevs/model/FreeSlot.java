package ru.astondevs.model;

import javax.persistence.*;
import java.time.LocalTime;

@Table(name = "Free_slots")
@Entity
public class FreeSlot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Temporal(TemporalType.TIME)
    private LocalTime slot;

    @Column(name = "booked")
    private boolean isBooked;
    @ManyToOne
    @JoinColumn(name = "schedule_id", referencedColumnName = "id")
    private DoctorSchedule schedule;

    public FreeSlot() {
    }

    public FreeSlot(LocalTime slot, boolean isBooked, DoctorSchedule schedule) {
        this.slot = slot;
        this.isBooked = isBooked;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalTime getSlot() {
        return slot;
    }

    public void setSlot(LocalTime slot) {
        this.slot = slot;
    }

    public boolean isBooked() {
        return isBooked;
    }

    public void setBooked(boolean booked) {
        isBooked = booked;
    }

    public DoctorSchedule getSchedule() {
        return schedule;
    }

    public void setSchedule(DoctorSchedule schedule) {
        this.schedule = schedule;
    }
}
