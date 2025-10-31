package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Specialty {
    private String id;
    private String name;
    private String description;
    private double consultationFee;

    @Override
    public String toString() {
        return name;
    }
}
