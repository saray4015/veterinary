package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Treatment;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class TreatmentService extends BaseServiceImpl<Treatment> {
    
    public TreatmentService() {
        super("treatments.csv");
    }

    @Override
    protected Class<Treatment> getEntityClass() {
        return Treatment.class;
    }

    public List<Treatment> findActiveTreatments() {
        LocalDate today = LocalDate.now();
        return entities.stream()
                .filter(treatment -> !treatment.isCompleted() && 
                                   !today.isBefore(treatment.getStartDate()) && 
                                   !today.isAfter(treatment.getEndDate()))
                .toList();
    }

    public List<Treatment> findExpiredTreatments() {
        LocalDate today = LocalDate.now();
        return entities.stream()
                .filter(treatment -> !treatment.isCompleted() && 
                                   today.isAfter(treatment.getEndDate()))
                .toList();
    }

    public void completeTreatment(String treatmentId) {
        findById(treatmentId).ifPresent(treatment -> {
            treatment.setCompleted(true);
            save(treatment);
        });
    }
}
