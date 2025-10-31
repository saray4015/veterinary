package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import co.edu.umanizales.veterinary.model.Appointment;
import co.edu.umanizales.veterinary.model.Treatment;
import co.edu.umanizales.veterinary.model.Medication;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {
    private String id;
    private LocalDateTime dateTime;
    private Owner owner;
    private List<Appointment> appointments = new ArrayList<>();
    private List<Treatment> treatments = new ArrayList<>();
    private List<Medication> medications = new ArrayList<>();
    private double subtotal;
    private double tax;
    private double total;
    private boolean isPaid;
    private String paymentMethod;

    public void calculateTotal() {
        double appointmentsTotal = appointments.stream().mapToDouble(Appointment::getCost).sum();
        double treatmentsTotal = treatments.stream().mapToDouble(Treatment::getCost).sum();
        double medicationsTotal = medications.stream().mapToDouble(Medication::getTotalCost).sum();
        
        this.subtotal = appointmentsTotal + treatmentsTotal + medicationsTotal;
        this.tax = subtotal * 0.19; // 19% de IVA
        this.total = subtotal + tax;
    }

    public void markAsPaid(String paymentMethod) {
        this.isPaid = true;
        this.paymentMethod = paymentMethod;
    }
}
