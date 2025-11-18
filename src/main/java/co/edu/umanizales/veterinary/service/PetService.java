package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.AnimalSpecies;
import co.edu.umanizales.veterinary.model.Owner;
import co.edu.umanizales.veterinary.model.Pet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PetService extends BaseServiceImpl<Pet> {
    
    private final OwnerService ownerService;

    public PetService(OwnerService ownerService) {
        super("pets.csv");
        this.ownerService = ownerService;
        // Rehidratar referencias de dueño a partir de ownerId después de cargar desde CSV
        for (Pet pet : this.entities) {
            if (pet.getOwner() == null && pet.getOwnerId() != null) {
                ownerService.findById(pet.getOwnerId()).ifPresent(pet::setOwner);
            }
        }
    }

    @Override
    protected Class<Pet> getEntityClass() {
        return Pet.class;
    }

    @Override
    public Pet save(Pet pet) {
        // Verificar que el dueño existe usando owner o ownerId
        String ownerId = null;
        if (pet.getOwner() != null && pet.getOwner().getId() != null) {
            ownerId = pet.getOwner().getId();
        } else if (pet.getOwnerId() != null) {
            ownerId = pet.getOwnerId();
        }

        if (ownerId != null) {
            Optional<Owner> owner = ownerService.findById(ownerId);
            if (owner.isPresent()) {
                pet.setOwner(owner.get());
                return super.save(pet);
            }
            throw new IllegalArgumentException("Owner not found: " + ownerId);
        }
        throw new IllegalArgumentException("Owner is required for pet");
    }

    public List<Pet> findBySpecies(AnimalSpecies species) {
        return entities.stream()
                .filter(pet -> species == pet.getSpecie())
                .toList();
    }

    public List<Pet> findByOwnerId(String ownerId) {
        return entities.stream()
                .filter(pet -> ownerId.equals(pet.getOwnerId()))
                .toList();
    }
}
