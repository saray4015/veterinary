package co.edu.umanizales.veterinary.service;

import java.util.List;
import java.util.Optional;

public interface BaseService<T> {
    List<T> findAll();
    Optional<T> findById(String id);
    T save(T entity);
    void deleteById(String id);
    boolean existsById(String id);
    void deleteAll();
}
