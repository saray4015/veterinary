package co.edu.umanizales.veterinary.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    protected List<T> entities;
    protected final String filename;

    public BaseServiceImpl(String filename) {
        this.filename = filename;
        this.entities = new ArrayList<>();
    }

    public void addEntity(T entity) {
        entities.add(entity);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities);
    }

    @Override
    public Optional<T> findById(String id) {
        return entities.stream()
                .filter(e -> e.toString().contains("id=" + id))
                .findFirst();
    }

    @Override
    public T save(T entity) {
        entities.add(entity);
        return entity;
    }

    @Override
    public void deleteById(String id) {
        entities.removeIf(e -> e.toString().contains("id=" + id));
    }

    @Override
    public boolean existsById(String id) {
        return entities.stream().anyMatch(e -> e.toString().contains("id=" + id));
    }

    @Override
    public void deleteAll() {
        entities.clear();
    }

    protected abstract Class<T> getEntityClass();
}
