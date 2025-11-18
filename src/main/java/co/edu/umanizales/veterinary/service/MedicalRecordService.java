package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.MedicalRecord;
import co.edu.umanizales.veterinary.model.Medication;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.model.Treatment;
import co.edu.umanizales.veterinary.model.Veterinarian;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
        // Validar que la mascota existe (por objeto o por ID)
        String petId = record.getPet() != null ? record.getPet().getId() : record.getPetId();
        if (petId == null || petService.findById(petId).isEmpty()) {
            throw new IllegalArgumentException("Valid pet is required for medical record");
        }
        
        // Validar que el veterinario existe (por objeto o por ID)
        String vetId = record.getVeterinarian() != null ? record.getVeterinarian().getId() : record.getVeterinarianId();
        if (vetId == null || veterinarianService.findById(vetId).isEmpty()) {
            throw new IllegalArgumentException("Valid veterinarian is required for medical record");
        }
        
        // Asegurar referencias sincronizadas
        petService.findById(petId).ifPresent(record::setPet);
        veterinarianService.findById(vetId).ifPresent(record::setVeterinarian);

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
