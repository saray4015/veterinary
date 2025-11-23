package co.edu.umanizales.veterinary.dto;

import co.edu.umanizales.veterinary.model.AppointmentStatus;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.model.Veterinarian;
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
public class AppointmentRequest {
    private String id;
    private String dateTime;

    // Permitir ambas formas de enviar referencias
    private String petId;
    private String veterinarianId;

    private Pet pet; // opcional
    private Veterinarian veterinarian; // opcional

    private String reason;
    private String diagnosis;
    private Double cost;
    private AppointmentStatus status;
}
