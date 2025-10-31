package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Specialty;
import co.edu.umanizales.veterinary.model.Veterinarian;
import co.edu.umanizales.veterinary.service.VeterinarianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/veterinarians")
public class VeterinarianController {
    private final VeterinarianService veterinarianService;

    @Autowired
    public VeterinarianController(VeterinarianService veterinarianService) {
        this.veterinarianService = veterinarianService;
    }

    @GetMapping
    public ResponseEntity<List<Veterinarian>> getAllVeterinarians() {
        return new ResponseEntity<>(veterinarianService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Veterinarian> getVeterinarianById(@PathVariable String id) {
        return veterinarianService.findById(id)
                .map(vet -> new ResponseEntity<>(vet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/license/{licenseNumber}")
    public ResponseEntity<Veterinarian> getVeterinarianByLicense(@PathVariable String licenseNumber) {
        return veterinarianService.findByLicenseNumber(licenseNumber)
                .map(vet -> new ResponseEntity<>(vet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/specialty/{specialtyId}")
    public ResponseEntity<List<Veterinarian>> getVeterinariansBySpecialty(@PathVariable String specialtyId) {
        return new ResponseEntity<>(veterinarianService.findBySpecialty(specialtyId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Veterinarian> createVeterinarian(@RequestBody Veterinarian veterinarian) {
        return new ResponseEntity<>(veterinarianService.save(veterinarian), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/specialties")
    public ResponseEntity<Void> addSpecialty(@PathVariable String id, @RequestBody Specialty specialty) {
        if (veterinarianService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        veterinarianService.addSpecialty(id, specialty);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/{id}/specialties/{specialtyId}")
    public ResponseEntity<Void> removeSpecialty(@PathVariable String id, @PathVariable String specialtyId) {
        if (veterinarianService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        veterinarianService.removeSpecialty(id, specialtyId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Veterinarian> updateVeterinarian(@PathVariable String id, @RequestBody Veterinarian veterinarian) {
        if (veterinarianService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        veterinarian.setId(id);
        return new ResponseEntity<>(veterinarianService.save(veterinarian), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVeterinarian(@PathVariable String id) {
        if (veterinarianService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        veterinarianService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
