package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Specialty;
import co.edu.umanizales.veterinary.service.SpecialtyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/specialties")
public class SpecialtyController {
    private final SpecialtyService specialtyService;

    @Autowired
    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public ResponseEntity<List<Specialty>> getAllSpecialties() {
        return new ResponseEntity<>(specialtyService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Specialty> getSpecialtyById(@PathVariable String id) {
        return specialtyService.findById(id)
                .map(specialty -> new ResponseEntity<>(specialty, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/search")
    public ResponseEntity<List<Specialty>> searchSpecialties(@RequestParam String name) {
        return new ResponseEntity<>(specialtyService.searchByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Specialty> createSpecialty(@RequestBody Specialty specialty) {
        return new ResponseEntity<>(specialtyService.save(specialty), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Specialty> updateSpecialty(
            @PathVariable String id, 
            @RequestBody Specialty specialty) {
        if (specialtyService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        specialty.setId(id);
        return new ResponseEntity<>(specialtyService.save(specialty), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable String id) {
        if (specialtyService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        specialtyService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
