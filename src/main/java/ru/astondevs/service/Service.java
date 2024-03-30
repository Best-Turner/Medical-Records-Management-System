package ru.astondevs.service;

import ru.astondevs.exception.PatientNotFoundException;

import java.util.List;

public interface Service<T, K> {
    T findById(K id) throws PatientNotFoundException;

    boolean deleteById(K id) throws PatientNotFoundException;

    List<T> findAll();

    T save(T t);

    boolean update(T object, K id) throws PatientNotFoundException;

}
