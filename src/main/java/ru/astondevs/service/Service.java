package ru.astondevs.service;

import ru.astondevs.exception.DoctorNotFoundException;
import ru.astondevs.exception.PatientNotFoundException;
import ru.astondevs.exception.ScheduleNotFoundException;

import java.util.List;

public interface Service<T, K> {
    T findById(K id) throws PatientNotFoundException, DoctorNotFoundException, ScheduleNotFoundException;

    boolean deleteById(K id) throws PatientNotFoundException, DoctorNotFoundException;

    List<T> findAll();

    T save(T t);

    boolean update(T object, K id) throws PatientNotFoundException, DoctorNotFoundException;

}
