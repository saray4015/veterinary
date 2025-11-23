package co.edu.umanizales.veterinary.dto;

import co.edu.umanizales.veterinary.model.Medication;
import co.edu.umanizales.veterinary.model.Veterinarian;
import co.edu.umanizales.veterinary.model.Treatment;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MedicalRecordResponse {
    private String id;
    private PetWithoutBirthDate pet;
    private Veterinarian veterinarian;
    private LocalDate date;
    private String diagnosis;
    private String treatmentNotes;
    private List<Treatment> treatments;
    private List<Medication> medications;
}
