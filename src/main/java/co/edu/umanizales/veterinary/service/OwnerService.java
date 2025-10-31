package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Owner;
import co.edu.umanizales.veterinary.model.Pet;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OwnerService extends BaseServiceImpl<Owner> {
    
    public OwnerService() {
        super("owners.csv");
    }

    @Override
    protected Class<Owner> getEntityClass() {
        return Owner.class;
    }

    public Optional<Owner> findByEmail(String email) {
        return entities.stream()
                .filter(owner -> email.equals(owner.getEmail()))
                .findFirst();
    }

    public List<Pet> findPetsByOwnerId(String ownerId) {
        return findById(ownerId)
                .map(Owner::getPets)
                .orElse(List.of());
    }
}
