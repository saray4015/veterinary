package co.edu.umanizales.veterinary.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.opencsv.bean.CsvDate;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Medication {

    private String id;
    private String name;
    private String description;
    private String dosage;
    private int quantity;
    private double unitPrice;

    @CsvDate("yyyy-MM-dd")
    private LocalDate expirationDate;

    private String manufacturer;

    public boolean isExpired() {
        return expirationDate != null && LocalDate.now().isAfter(expirationDate);
    }

    public double getTotalCost() {
        return quantity * unitPrice;
    }
}

