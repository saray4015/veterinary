package co.edu.umanizales.veterinary.dto;

import co.edu.umanizales.veterinary.model.AnimalSpecies;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PetWithoutBirthDate {
    private String id;
    private String name;
    private AnimalSpecies specie;
    private String breed;
    private String ownerId;
}
