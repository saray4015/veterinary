package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Medication;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MedicationService extends BaseServiceImpl<Medication> {
    
    public MedicationService() {
        super("medications.csv");
    }

    @Override
    protected Class<Medication> getEntityClass() {
        return Medication.class;
    }

    public List<Medication> findExpiredMedications() {
        LocalDate today = LocalDate.now();
        return entities.stream()
                .filter(medication -> today.isAfter(medication.getExpirationDate()))
                .toList();
    }

    public List<Medication> findLowStock(int threshold) {
        return entities.stream()
                .filter(medication -> medication.getQuantity() <= threshold)
                .toList();
    }

    public void updateStock(String medicationId, int quantityChange) {
        findById(medicationId).ifPresent(medication -> {
            int newQuantity = medication.getQuantity() + quantityChange;
            if (newQuantity < 0) {
                throw new IllegalStateException("Insufficient stock for medication: " + medicationId);
            }
            medication.setQuantity(newQuantity);
            save(medication);
        });
    }
}
