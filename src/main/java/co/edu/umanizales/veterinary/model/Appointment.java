package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {
    private String id;
    private LocalDateTime dateTime;
    private Pet pet;
    private Veterinarian veterinarian;
    private String reason;
    private String diagnosis;
    private double cost;
    private AppointmentStatus status;
}
