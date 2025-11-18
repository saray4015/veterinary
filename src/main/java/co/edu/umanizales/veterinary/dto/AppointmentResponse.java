package co.edu.umanizales.veterinary.dto;

import co.edu.umanizales.veterinary.model.AppointmentStatus;
import co.edu.umanizales.veterinary.model.Veterinarian;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {
    private String id;
    private LocalDateTime dateTime;
    private PetWithoutBirthDate pet;
    private Veterinarian veterinarian;
    private String reason;
    private String diagnosis;
    private double cost;
    private AppointmentStatus status;
}
