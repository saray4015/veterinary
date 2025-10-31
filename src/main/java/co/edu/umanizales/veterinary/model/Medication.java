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
public class Medication {
    private String id;
    private String name;
    private String description;
    private String dosage;
    private int quantity;
    private double unitPrice;
    private LocalDate expirationDate;
    private String manufacturer;

    public boolean isExpired() {
        return LocalDate.now().isAfter(expirationDate);
    }

    public double getTotalCost() {
        return quantity * unitPrice;
    }
    
    // Métodos adicionales para compatibilidad
    public void setId(String id) {
        this.id = id;
    }
    
    public LocalDate getExpirationDate() {
        return this.expirationDate;
    }
    
    public int getQuantity() {
        return this.quantity;
    }
    
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
