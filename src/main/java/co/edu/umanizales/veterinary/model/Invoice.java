package co.edu.umanizales.veterinary.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Invoice {

    private String id;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;

    private Owner owner;

    private List<Appointment> appointments = new ArrayList<>();
    private List<Treatment> treatments = new ArrayList<>();
    private List<Medication> medications = new ArrayList<>();

    private double subtotal;
    private double tax;
    private double total;

    private boolean paid;
    private String paymentMethod;

    public void calculateTotal() {
        double appointmentsTotal = appointments.stream()
                .mapToDouble(Appointment::getCost)
                .sum();

        double treatmentsTotal = treatments.stream()
                .mapToDouble(Treatment::getCost)
                .sum();

        double medicationsTotal = medications.stream()
                .mapToDouble(Medication::getTotalCost)
                .sum();

        this.subtotal = appointmentsTotal + treatmentsTotal + medicationsTotal;
        this.tax = subtotal * 0.19;
        this.total = subtotal + tax;
    }

    public void markAsPaid(String paymentMethod) {
        this.paid = true;
        this.paymentMethod = paymentMethod;
    }
}
