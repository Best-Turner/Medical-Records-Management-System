package ru.astondevs.repository;

import java.util.List;

public interface Repository<T, K> {
    T findById(K id);

    boolean deleteById(K id);

    List<T> findAll();

    T save(T t);

    boolean update(K object);

}
