package co.edu.umanizales.veterinary.dto;

import co.edu.umanizales.veterinary.model.AnimalSpecies;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PetWithoutBirthDate {
    private String id;
    private String name;
    private AnimalSpecies specie;
    private String breed;
    private String ownerId;
}
