package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private String id;
    private Pet pet;
    private Veterinarian veterinarian;
    private LocalDate date;
    private String diagnosis;
    private String treatmentNotes;
    private List<Treatment> treatments = new ArrayList<>();
    private List<Medication> medications = new ArrayList<>();

    public void addTreatment(Treatment treatment) {
        treatments.add(treatment);
    }

    public void addMedication(Medication medication) {
        medications.add(medication);
    }

    public void removeTreatment(Treatment treatment) {
        treatments.remove(treatment);
    }

    public void removeMedication(Medication medication) {
        medications.remove(medication);
    }
}
