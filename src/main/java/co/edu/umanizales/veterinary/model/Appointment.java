package co.edu.umanizales.veterinary.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import com.opencsv.bean.CsvIgnore;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Appointment {
    private String id;
    @CsvDate("yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    // IDs planos para persistencia en CSV
    @CsvBindByName(column = "petId")
    private String petId;
    @CsvBindByName(column = "veterinarianId")
    private String veterinarianId;

    // Objetos anidados ignorados en CSV
    @CsvIgnore
    private Pet pet;
    @CsvIgnore
    private Veterinarian veterinarian;
    private String reason;
    private String diagnosis;
    private double cost;
    private AppointmentStatus status;
}

