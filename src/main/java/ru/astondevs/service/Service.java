package ru.astondevs.service;

import java.util.List;

public interface Service<T, K> {
    T findById(K id);

    boolean deleteById(K id);

    List<T> findAll();

    T save(T t);

    boolean update(K object);

}
