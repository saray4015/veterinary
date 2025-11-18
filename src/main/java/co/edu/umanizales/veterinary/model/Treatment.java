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
public class Treatment {

    private String id;
    private String name;
    private String description;

    @CsvDate("yyyy-MM-dd")
    private LocalDate startDate;

    @CsvDate("yyyy-MM-dd")
    private LocalDate endDate;

    private boolean isCompleted;
    private double cost;

    public boolean isActive() {
        if (startDate == null || endDate == null) {
            return false;
        }
        LocalDate today = LocalDate.now();
        return !isCompleted && !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public void complete() {
        this.isCompleted = true;
    }
}
