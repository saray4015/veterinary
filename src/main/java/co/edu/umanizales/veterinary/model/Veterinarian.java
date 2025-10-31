package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Veterinarian extends Person {
    private String licenseNumber;
    private List<Specialty> specialties = new ArrayList<>();

    public Veterinarian(String id, String name, String email, String phone, String address, String licenseNumber) {
        super(id, name, email, phone, address);
        this.licenseNumber = licenseNumber;
    }

    @Override
    public String getPersonType() {
        return "Veterinarian";
    }

    public void addSpecialty(Specialty specialty) {
        specialties.add(specialty);
    }

    public void removeSpecialty(Specialty specialty) {
        specialties.remove(specialty);
    }
}
