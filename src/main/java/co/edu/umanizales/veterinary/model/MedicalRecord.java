package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvIgnore;
import com.opencsv.bean.CsvBindByName;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MedicalRecord {
    private String id;
    @CsvIgnore
    private Pet pet;
    @CsvIgnore
    private Veterinarian veterinarian;
    @CsvBindByName(column = "petId")
    private String petId;
    @CsvBindByName(column = "veterinarianId")
    private String veterinarianId;
    @CsvDate("yyyy-MM-dd")
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

    public void setPet(Pet pet) {
        this.pet = pet;
        if (pet != null) {
            this.petId = pet.getId();
        }
    }

    public void setVeterinarian(Veterinarian veterinarian) {
        this.veterinarian = veterinarian;
        if (veterinarian != null) {
            this.veterinarianId = veterinarian.getId();
        }
    }
}
