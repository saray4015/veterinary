package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VeterinaryService {
    private String id;
    private String name;
    private String description;
    private ServiceType type;
    private double baseCost;
    private int durationMinutes;

    public double calculateCost() {
        return baseCost;
    }
    
    // Métodos adicionales para compatibilidad
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public ServiceType getType() {
        return this.type;
    }
}
