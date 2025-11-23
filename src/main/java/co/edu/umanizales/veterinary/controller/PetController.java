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
    public ResponseEntity<?> getPetById(@PathVariable String id) {
        String tmp = (id != null) ? id.trim() : null;
        final String normalizedId = (tmp != null && tmp.length() >= 2 &&
                ((tmp.startsWith("\"") && tmp.endsWith("\"")) ||
                 (tmp.startsWith("'") && tmp.endsWith("'"))))
                ? tmp.substring(1, tmp.length() - 1).trim()
                : tmp;
        return petService.findById(id)
                .or(() -> petService.findById(normalizedId))
                .<ResponseEntity<?>>map(pet -> new ResponseEntity<>(pet, HttpStatus.OK))
                .orElseGet(() -> {
                    java.util.List<String> ids = petService.findAll().stream()
                            .map(Pet::getId)
                            .filter(java.util.Objects::nonNull)
                            .toList();
                    String msg = "Pet not found: '" + id + "'. Available IDs: " + ids;
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
                });
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
    public ResponseEntity<?> updatePet(@PathVariable String id, @RequestBody Pet pet) {
        String tmp = (id != null) ? id.trim() : null;
        final String normalizedId = (tmp != null && tmp.length() >= 2 &&
                ((tmp.startsWith("\"") && tmp.endsWith("\"")) ||
                 (tmp.startsWith("'") && tmp.endsWith("'"))))
                ? tmp.substring(1, tmp.length() - 1).trim()
                : tmp;
        boolean existsOriginal = petService.findById(id).isPresent();
        boolean existsNormalized = petService.findById(normalizedId).isPresent();
        String effectiveId = existsOriginal ? id : (existsNormalized ? normalizedId : null);

        try {
            if (effectiveId == null) {
                // Upsert: crear si no existe, requiere owner/ownerId válido
                if ((pet.getOwner() == null || pet.getOwner().getId() == null) && (pet.getOwnerId() == null)) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Owner is required for pet create/update (provide ownerId or owner.id)");
                }
                pet.setId(normalizedId != null ? normalizedId : id);
                return new ResponseEntity<>(petService.save(pet), HttpStatus.CREATED);
            } else {
                pet.setId(effectiveId);
                return new ResponseEntity<>(petService.save(pet), HttpStatus.OK);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePet(@PathVariable String id) {
        String tmp = (id != null) ? id.trim() : null;
        final String normalizedId = (tmp != null && tmp.length() >= 2 &&
                ((tmp.startsWith("\"") && tmp.endsWith("\"")) ||
                 (tmp.startsWith("'") && tmp.endsWith("'"))))
                ? tmp.substring(1, tmp.length() - 1).trim()
                : tmp;
        String effectiveId = petService.findById(id).isPresent() ? id : (petService.findById(normalizedId).isPresent() ? normalizedId : null);
        if (effectiveId == null) {
            java.util.List<String> ids = petService.findAll().stream()
                    .map(Pet::getId)
                    .filter(java.util.Objects::nonNull)
                    .toList();
            String msg = "Pet not found for delete: '" + id + "'. Available IDs: " + ids;
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
        }
        petService.deleteById(effectiveId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
