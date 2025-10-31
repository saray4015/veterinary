package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.AnimalSpecies;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pets")
public class PetController {
    private final PetService petService;

    @Autowired
    public PetController(PetService petService) {
        this.petService = petService;
    }

    @GetMapping
    public ResponseEntity<List<Pet>> getAllPets() {
        return new ResponseEntity<>(petService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable String id) {
        return petService.findById(id)
                .map(pet -> new ResponseEntity<>(pet, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/species/{species}")
    public ResponseEntity<List<Pet>> getPetsBySpecies(@PathVariable AnimalSpecies species) {
        return new ResponseEntity<>(petService.findBySpecies(species), HttpStatus.OK);
    }

    @GetMapping("/owner/{ownerId}")
    public ResponseEntity<List<Pet>> getPetsByOwner(@PathVariable String ownerId) {
        return new ResponseEntity<>(petService.findByOwnerId(ownerId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Pet> createPet(@RequestBody Pet pet) {
        return new ResponseEntity<>(petService.save(pet), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Pet> updatePet(@PathVariable String id, @RequestBody Pet pet) {
        if (petService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        pet.setId(id);
        return new ResponseEntity<>(petService.save(pet), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePet(@PathVariable String id) {
        if (petService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        petService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
