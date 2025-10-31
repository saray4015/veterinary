package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Specialty;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyService extends BaseServiceImpl<Specialty> {
    
    public SpecialtyService() {
        super("specialties.csv");
    }

    @Override
    protected Class<Specialty> getEntityClass() {
        return Specialty.class;
    }

    public Optional<Specialty> findByName(String name) {
        return entities.stream()
                .filter(specialty -> specialty.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public List<Specialty> searchByName(String name) {
        String searchTerm = name.toLowerCase();
        return entities.stream()
                .filter(specialty -> specialty.getName().toLowerCase().contains(searchTerm) ||
                                   specialty.getDescription().toLowerCase().contains(searchTerm))
                .toList();
    }
}
