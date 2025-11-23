package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.MedicalRecord;
import co.edu.umanizales.veterinary.model.Medication;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.model.Treatment;
import co.edu.umanizales.veterinary.model.Veterinarian;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;

@Service
public class MedicalRecordService extends BaseServiceImpl<MedicalRecord> {
    
    private final PetService petService;
    private final VeterinarianService veterinarianService;

    public MedicalRecordService(PetService petService, VeterinarianService veterinarianService) {
        super("medical_records.csv");
        this.petService = petService;
        this.veterinarianService = veterinarianService;
        // Rehidratar referencias a partir de IDs después de cargar desde CSV
        for (MedicalRecord record : this.entities) {
            if (record.getPet() == null && record.getPetId() != null) {
                petService.findById(record.getPetId()).ifPresent(record::setPet);
            }
            if (record.getVeterinarian() == null && record.getVeterinarianId() != null) {
                veterinarianService.findById(record.getVeterinarianId()).ifPresent(record::setVeterinarian);
            }
        }
    }

    @Override
    protected Class<MedicalRecord> getEntityClass() {
        return MedicalRecord.class;
    }

    @Override
    public MedicalRecord save(MedicalRecord record) {
        // Asegurar ID
        if (record.getId() == null || record.getId().isBlank()) {
            record.setId(java.util.UUID.randomUUID().toString());
        }

        // Permitir entrada por objetos o por IDs escalares
        String petId = record.getPet() != null ? record.getPet().getId() : record.getPetId();
        String vetId = record.getVeterinarian() != null ? record.getVeterinarian().getId() : record.getVeterinarianId();

        // Merge con existente si aplica (update parcial)
        findById(record.getId()).ifPresent(existing -> {
            if (petId == null) {
                // Completar desde existente
                if (record.getPet() == null) record.setPet(existing.getPet());
                if (record.getPetId() == null) record.setPetId(existing.getPetId());
            }
            if (vetId == null) {
                if (record.getVeterinarian() == null) record.setVeterinarian(existing.getVeterinarian());
                if (record.getVeterinarianId() == null) record.setVeterinarianId(existing.getVeterinarianId());
            }
            if (record.getDate() == null) record.setDate(existing.getDate());
            if (record.getDiagnosis() == null) record.setDiagnosis(existing.getDiagnosis());
            if (record.getTreatmentNotes() == null) record.setTreatmentNotes(existing.getTreatmentNotes());
            if (record.getTreatments() == null || record.getTreatments().isEmpty()) record.setTreatments(existing.getTreatments());
            if (record.getMedications() == null || record.getMedications().isEmpty()) record.setMedications(existing.getMedications());
            // Reemplazar existente para evitar duplicados
            entities.removeIf(r -> {
                try {
                    Object value = r.getClass().getMethod("getId").invoke(r);
                    return record.getId().equals(value);
                } catch (Exception ex) { return false; }
            });
        });

        // Hidratar si existen
        // Recalcular IDs finales a partir del propio record tras el merge
        String finalPetId = record.getPet() != null ? record.getPet().getId() : (record.getPetId());
        String finalVetId = record.getVeterinarian() != null ? record.getVeterinarian().getId() : (record.getVeterinarianId());

        if (finalPetId != null) {
            final String pid = finalPetId; // effectively final for lambda
            petService.findById(pid).ifPresent(record::setPet);
        }
        if (finalVetId != null) {
            final String vid = finalVetId;
            veterinarianService.findById(vid).ifPresent(record::setVeterinarian);
        }

        // Asignar siempre IDs escalares para CSV
        record.setPetId(finalPetId);
        record.setVeterinarianId(finalVetId);

        // Fecha por defecto hoy si no viene
        if (record.getDate() == null) {
            record.setDate(LocalDate.now());
        }

        return super.save(record);
    }

    public List<MedicalRecord> findByPetId(String petId) {
        return entities.stream()
                .filter(record -> petId.equals(record.getPetId()))
                .toList();
    }

    public List<MedicalRecord> findByVeterinarianId(String veterinarianId) {
        return entities.stream()
                .filter(record -> veterinarianId.equals(record.getVeterinarianId()))
                .toList();
    }

    public void addTreatment(String recordId, Treatment treatment) {
        findById(recordId).ifPresent(record -> {
            record.addTreatment(treatment);
            save(record);
        });
    }

    public void addMedication(String recordId, Medication medication) {
        findById(recordId).ifPresent(record -> {
            record.addMedication(medication);
            save(record);
        });
    }
}
