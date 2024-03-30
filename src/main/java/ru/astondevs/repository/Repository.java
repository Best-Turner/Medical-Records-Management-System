package ru.astondevs.repository;

import java.util.List;
import java.util.Optional;

public interface Repository<T, K> {
    Optional<T> findById(K id);

    boolean deleteById(K id);

    List<T> findAll();

    Optional<T> save(T t);

    boolean update(T object);

    boolean exists(K id);

}
