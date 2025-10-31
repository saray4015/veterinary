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
    }

    @Override
    protected Class<MedicalRecord> getEntityClass() {
        return MedicalRecord.class;
    }

    @Override
    public MedicalRecord save(MedicalRecord record) {
        // Validar que la mascota existe
        if (record.getPet() == null || record.getPet().getId() == null || 
            petService.findById(record.getPet().getId()).isEmpty()) {
            throw new IllegalArgumentException("Valid pet is required for medical record");
        }
        
        // Validar que el veterinario existe
        if (record.getVeterinarian() == null || record.getVeterinarian().getId() == null || 
            veterinarianService.findById(record.getVeterinarian().getId()).isEmpty()) {
            throw new IllegalArgumentException("Valid veterinarian is required for medical record");
        }
        
        return super.save(record);
    }

    public List<MedicalRecord> findByPetId(String petId) {
        return entities.stream()
                .filter(record -> petId.equals(record.getPet().getId()))
                .toList();
    }

    public List<MedicalRecord> findByVeterinarianId(String veterinarianId) {
        return entities.stream()
                .filter(record -> veterinarianId.equals(record.getVeterinarian().getId()))
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
