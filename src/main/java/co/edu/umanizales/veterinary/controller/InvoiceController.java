package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Invoice;
import co.edu.umanizales.veterinary.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @Autowired
    public InvoiceController(InvoiceService invoiceService) {
        this.invoiceService = invoiceService;
    }

    @GetMapping
    public ResponseEntity<List<Invoice>> getAllInvoices() {
        return new ResponseEntity<>(invoiceService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Invoice> getInvoiceById(@PathVariable String id) {
        return invoiceService.findById(id)
                .map(invoice -> new ResponseEntity<>(invoice, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Invoice>> getInvoicesByOwner(@PathVariable String ownerId) {
        return new ResponseEntity<>(invoiceService.findByOwnerId(ownerId), HttpStatus.OK);
    }

    @GetMapping("/unpaid")
    public ResponseEntity<List<Invoice>> getUnpaidInvoices() {
        return new ResponseEntity<>(invoiceService.findUnpaidInvoices(), HttpStatus.OK);
    }

    @GetMapping("/revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        return new ResponseEntity<>(invoiceService.calculateTotalRevenue(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Invoice> createInvoice(@RequestBody Invoice invoice) {
        try {
            return new ResponseEntity<>(invoiceService.save(invoice), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/pay")
    public ResponseEntity<Void> markAsPaid(
            @PathVariable String id,
            @RequestParam String paymentMethod) {
        if (invoiceService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        invoiceService.markAsPaid(id, paymentMethod);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        if (invoiceService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        invoiceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
