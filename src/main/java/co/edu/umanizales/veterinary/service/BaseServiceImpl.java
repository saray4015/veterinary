package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.util.CSVManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class BaseServiceImpl<T> implements BaseService<T> {
    protected List<T> entities;
    protected final String filename;
    private final CSVManager<T> csvManager = new CSVManager<>();

    public BaseServiceImpl(String filename) {
        this.filename = filename;
        // Cargar datos existentes desde CSV si el archivo existe
        List<T> loaded = csvManager.readFromCSV(filename, getEntityClass());
        this.entities = (loaded != null) ? new ArrayList<>(loaded) : new ArrayList<>();
    }

    public void addEntity(T entity) {
        entities.add(entity);
        csvManager.writeToCSV(filename, entities);
    }

    @Override
    public List<T> findAll() {
        return new ArrayList<>(entities);
    }

    @Override
    public Optional<T> findById(String id) {
        return entities.stream()
                .filter(e -> {
                    try {
                        Object value = e.getClass().getMethod("getId").invoke(e);
                        String sid = (id != null) ? id.trim() : null;
                        String val = (value != null) ? value.toString().trim() : null;
                        return sid != null && val != null && sid.equalsIgnoreCase(val);
                    } catch (Exception ex) {
                        return false;
                    }
                })
                .findFirst();
    }

    @Override
    public T save(T entity) {
        entities.add(entity);
        csvManager.writeToCSV(filename, entities);
        return entity;
    }

    @Override
    public void deleteById(String id) {
        entities.removeIf(e -> e.toString().contains("id=" + id));
        csvManager.writeToCSV(filename, entities);
    }

    @Override
    public boolean existsById(String id) {
        return entities.stream().anyMatch(e -> e.toString().contains("id=" + id));
    }

    @Override
    public void deleteAll() {
        entities.clear();
        csvManager.writeToCSV(filename, entities);
    }

    protected abstract Class<T> getEntityClass();
}
