package co.edu.umanizales.veterinary.model;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)

public class Owner extends Person {
    private List<Pet> pets = new ArrayList<>();

    public Owner(String id, String name, String email, String phone, String address) {
        super(id, name, email, phone, address);
    }

    @Override
    public String getPersonType() {
        return "Owner";
    }

    public void addPet(Pet pet) {
        pets.add(pet);
    }

    public void removePet(Pet pet) {
        pets.remove(pet);
    }
}
