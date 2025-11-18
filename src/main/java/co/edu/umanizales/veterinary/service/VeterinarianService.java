package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Specialty;
import co.edu.umanizales.veterinary.model.Veterinarian;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
                .filter(v -> licenseNumber != null && licenseNumber.equals(v.getLicenseNumber()))
                .findFirst();
    }

    public List<Veterinarian> findBySpecialty(String specialtyId) {
        return entities.stream()
                .filter(v -> v.getSpecialties().stream()
                        .anyMatch(s -> s.getId().equals(specialtyId)))
                .collect(Collectors.toList());
    }

    public void addSpecialty(String veterinarianId, Specialty specialty) {
        findById(veterinarianId).ifPresent(v -> {
            if (specialty != null && specialty.getId() != null) {
                boolean exists = v.getSpecialties().stream()
                        .anyMatch(s -> specialty.getId().equals(s.getId()));
                if (!exists) {
                    v.addSpecialty(specialty);
                }
            }
            save(v);
        });
    }

    public void removeSpecialty(String veterinarianId, String specialtyId) {
        findById(veterinarianId).ifPresent(v -> {
            v.getSpecialties().removeIf(s -> s.getId().equals(specialtyId));
            save(v);
        });
    }
}
