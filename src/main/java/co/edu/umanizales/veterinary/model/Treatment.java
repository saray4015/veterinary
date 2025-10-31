package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Treatment {
    private String id;
    private String name;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean isCompleted;
    private double cost;

    public boolean isActive() {
        LocalDate today = LocalDate.now();
        return !isCompleted && !today.isBefore(startDate) && !today.isAfter(endDate);
    }

    public void complete() {
        this.isCompleted = true;
    }
}
