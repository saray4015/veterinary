package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Invoice;
import co.edu.umanizales.veterinary.model.Owner;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceService extends BaseServiceImpl<Invoice> {
    
    private final OwnerService ownerService;

    public InvoiceService(OwnerService ownerService) {
        super("invoices.csv");
        this.ownerService = ownerService;
    }

    @Override
    protected Class<Invoice> getEntityClass() {
        return Invoice.class;
    }

    @Override
    public Invoice save(Invoice invoice) {
        // Validar que el dueño existe
        if (invoice.getOwner() == null || invoice.getOwner().getId() == null || 
            ownerService.findById(invoice.getOwner().getId()).isEmpty()) {
            throw new IllegalArgumentException("Valid owner is required for invoice");
        }
        
        // Establecer la fecha actual si no está definida
        if (invoice.getDateTime() == null) {
            invoice.setDateTime(LocalDateTime.now());
        }
        
        // Calcular totales
        invoice.calculateTotal();
        
        return super.save(invoice);
    }

    public List<Invoice> findByOwnerId(String ownerId) {
        return entities.stream()
                .filter(invoice -> ownerId.equals(invoice.getOwner().getId()))
                .toList();
    }

    public List<Invoice> findUnpaidInvoices() {
        return entities.stream()
                .filter(invoice -> !invoice.isPaid())
                .toList();
    }

    public void markAsPaid(String invoiceId, String paymentMethod) {
        findById(invoiceId).ifPresent(invoice -> {
            invoice.markAsPaid(paymentMethod);
            save(invoice);
        });
    }

    public double calculateTotalRevenue() {
        return entities.stream()
                .filter(Invoice::isPaid)
                .mapToDouble(Invoice::getTotal)
                .sum();
    }
}
