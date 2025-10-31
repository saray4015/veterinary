package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.MedicalRecord;
import co.edu.umanizales.veterinary.model.Medication;
import co.edu.umanizales.veterinary.model.Treatment;
import co.edu.umanizales.veterinary.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping
    public ResponseEntity<List<MedicalRecord>> getAllMedicalRecords() {
        return new ResponseEntity<>(medicalRecordService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecordById(@PathVariable String id) {
        return medicalRecordService.findById(id)
                .map(record -> new ResponseEntity<>(record, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPet(@PathVariable String petId) {
        return new ResponseEntity<>(medicalRecordService.findByPetId(petId), HttpStatus.OK);
    }

    @GetMapping("/veterinarian/{veterinarianId}")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByVeterinarian(@PathVariable String veterinarianId) {
        return new ResponseEntity<>(medicalRecordService.findByVeterinarianId(veterinarianId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try {
            return new ResponseEntity<>(medicalRecordService.save(medicalRecord), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{id}/treatments")
    public ResponseEntity<Void> addTreatment(@PathVariable String id, @RequestBody Treatment treatment) {
        if (medicalRecordService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicalRecordService.addTreatment(id, treatment);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PostMapping("/{id}/medications")
    public ResponseEntity<Void> addMedication(@PathVariable String id, @RequestBody Medication medication) {
        if (medicalRecordService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicalRecordService.addMedication(id, medication);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@PathVariable String id, @RequestBody MedicalRecord medicalRecord) {
        if (medicalRecordService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicalRecord.setId(id);
        try {
            return new ResponseEntity<>(medicalRecordService.save(medicalRecord), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String id) {
        if (medicalRecordService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicalRecordService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
