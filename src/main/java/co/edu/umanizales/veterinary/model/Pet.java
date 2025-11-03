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
public class Pet {
    private String id;
    private String name;
    private AnimalSpecies specie;
    private String breed;
    private LocalDate birthDate;
    private Owner owner;
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public int getAge() {
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    public void removeMedicalRecord(MedicalRecord record) {
        medicalRecords.remove(record);
    }
}
