package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Specialty;
import co.edu.umanizales.veterinary.model.Veterinarian;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VeterinarianService extends BaseServiceImpl<Veterinarian> {
    
    public VeterinarianService() {
        super("veterinarians.csv");
    }

    @Override
    protected Class<Veterinarian> getEntityClass() {
        return Veterinarian.class;
    }

    public Optional<Veterinarian> findByLicenseNumber(String licenseNumber) {
        return entities.stream()
                .filter(v -> licenseNumber.equals(v.getLicenseNumber()))
                .findFirst();
    }

    public List<Veterinarian> findBySpecialty(String specialtyId) {
        return entities.stream()
                .filter(v -> v.getSpecialties().stream()
                        .anyMatch(s -> s.getId().equals(specialtyId)))
                .toList();
    }

    public void addSpecialty(String veterinarianId, Specialty specialty) {
        findById(veterinarianId).ifPresent(v -> {
            v.addSpecialty(specialty);
            save(v);
        });
    }

    public void removeSpecialty(String veterinarianId, String specialtyId) {
        findById(veterinarianId).ifPresent(v -> {
            v.setSpecialties(
                v.getSpecialties().stream()
                    .filter(s -> !s.getId().equals(specialtyId))
                    .toList()
            );
            save(v);
        });
    }
}
