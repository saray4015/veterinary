package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Medication;
import co.edu.umanizales.veterinary.service.MedicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medications")
public class MedicationController {
    private final MedicationService medicationService;

    @Autowired
    public MedicationController(MedicationService medicationService) {
        this.medicationService = medicationService;
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications() {
        return new ResponseEntity<>(medicationService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Medication>> getExpiredMedications() {
        return new ResponseEntity<>(medicationService.findExpiredMedications(), HttpStatus.OK);
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Medication>> getLowStockMedications(
            @RequestParam(defaultValue = "10") int threshold) {
        return new ResponseEntity<>(medicationService.findLowStock(threshold), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medication> getMedicationById(@PathVariable String id) {
        return medicationService.findById(id)
                .map(medication -> new ResponseEntity<>(medication, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Medication> createMedication(@RequestBody Medication medication) {
        return new ResponseEntity<>(medicationService.save(medication), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/stock")
    public ResponseEntity<Void> updateStock(
            @PathVariable String id, 
            @RequestParam int quantityChange) {
        if (medicationService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        try {
            medicationService.updateStock(id, quantityChange);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalStateException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Medication> updateMedication(
            @PathVariable String id, 
            @RequestBody Medication medication) {
        if (medicationService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medication.setId(id);
        return new ResponseEntity<>(medicationService.save(medication), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable String id) {
        if (medicationService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicationService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
