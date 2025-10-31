package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Treatment;
import co.edu.umanizales.veterinary.service.TreatmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/treatments")
public class TreatmentController {
    private final TreatmentService treatmentService;

    @Autowired
    public TreatmentController(TreatmentService treatmentService) {
        this.treatmentService = treatmentService;
    }

    @GetMapping
    public ResponseEntity<List<Treatment>> getAllTreatments() {
        return new ResponseEntity<>(treatmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/active")
    public ResponseEntity<List<Treatment>> getActiveTreatments() {
        return new ResponseEntity<>(treatmentService.findActiveTreatments(), HttpStatus.OK);
    }

    @GetMapping("/expired")
    public ResponseEntity<List<Treatment>> getExpiredTreatments() {
        return new ResponseEntity<>(treatmentService.findExpiredTreatments(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Treatment> getTreatmentById(@PathVariable String id) {
        return treatmentService.findById(id)
                .map(treatment -> new ResponseEntity<>(treatment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Treatment> createTreatment(@RequestBody Treatment treatment) {
        return new ResponseEntity<>(treatmentService.save(treatment), HttpStatus.CREATED);
    }

    @PutMapping("/{id}/complete")
    public ResponseEntity<Void> completeTreatment(@PathVariable String id) {
        if (treatmentService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        treatmentService.completeTreatment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Treatment> updateTreatment(@PathVariable String id, @RequestBody Treatment treatment) {
        if (treatmentService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        treatment.setId(id);
        return new ResponseEntity<>(treatmentService.save(treatment), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable String id) {
        if (treatmentService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        treatmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
