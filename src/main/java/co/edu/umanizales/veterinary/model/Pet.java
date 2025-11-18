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
public class Pet {
    private String id;
    private String name;
    private AnimalSpecies specie;
    private String breed;
    @CsvDate("yyyy-MM-dd")
    private LocalDate birthDate;
    @CsvIgnore
    private Owner owner;
    @CsvBindByName(column = "ownerId")
    private String ownerId;
    @CsvIgnore
    private List<MedicalRecord> medicalRecords = new ArrayList<>();

    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return LocalDate.now().getYear() - birthDate.getYear();
    }

    public void addMedicalRecord(MedicalRecord record) {
        medicalRecords.add(record);
    }

    public void removeMedicalRecord(MedicalRecord record) {
        medicalRecords.remove(record);
    }

    public void setOwner(Owner owner) {
        this.owner = owner;
        if (owner != null) {
            this.ownerId = owner.getId();
        }
    }
}
