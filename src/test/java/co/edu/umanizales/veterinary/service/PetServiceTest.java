package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.AnimalSpecies;
import co.edu.umanizales.veterinary.model.Owner;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.util.CSVManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class PetServiceTest {

    @Mock
    private CSVManager<Pet> csvManager;
    
    @Mock
    private OwnerService ownerService;

    @InjectMocks
    private PetService petService;

    private Pet pet1;
    private Pet pet2;
    private Owner owner1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        owner1 = new Owner();
        owner1.setId("1");
        owner1.setName("Juan Pérez");
        owner1.setEmail("juan@example.com");
        owner1.setPhone("1234567890");
        owner1.setAddress("Calle 123");

        pet1 = new Pet();
        pet1.setId("1");
        pet1.setName("Firulais");
        pet1.setSpecies(AnimalSpecies.DOG);
        pet1.setBreed("Labrador");
        pet1.setBirthDate(LocalDate.of(2018, 5, 15));
        pet1.setOwner(owner1);

        pet2 = new Pet();
        pet2.setId("2");
        pet2.setName("Michi");
        pet2.setSpecies(AnimalSpecies.CAT);
        pet2.setBreed("Siamés");
        pet2.setBirthDate(LocalDate.of(2020, 2, 20));
        pet2.setOwner(owner1);

        when(csvManager.readFromCSV(anyString(), eq(Pet.class))).thenReturn(Arrays.asList(pet1, pet2));
        when(ownerService.findById("1")).thenReturn(Optional.of(owner1));
    }

    @Test
    void findAll_ShouldReturnAllPets() {
        List<Pet> pets = petService.findAll();
        assertEquals(2, pets.size());
        verify(csvManager, times(1)).readFromCSV(anyString(), eq(Pet.class));
    }

    @Test
    void findById_WithValidId_ShouldReturnPet() {
        Optional<Pet> foundPet = petService.findById("1");
        assertTrue(foundPet.isPresent());
        assertEquals("Firulais", foundPet.get().getName());
    }

    @Test
    void findByOwnerId_WithValidOwnerId_ShouldReturnPets() {
        List<Pet> ownerPets = petService.findByOwnerId("1");
        assertEquals(2, ownerPets.size());
        assertTrue(ownerPets.stream().allMatch(pet -> pet.getOwner().getId().equals("1")));
    }

    @Test
    void findBySpecies_WithValidSpecies_ShouldReturnMatchingPets() {
        List<Pet> dogs = petService.findBySpecies(AnimalSpecies.DOG);
        assertEquals(1, dogs.size());
        assertEquals("Firulais", dogs.get(0).getName());
    }

    @Test
    void save_NewPetWithValidOwner_ShouldSaveAndReturnPet() {
        Pet newPet = new Pet();
        newPet.setName("Rex");
        newPet.setSpecies(AnimalSpecies.DOG);
        newPet.setBreed("Pastor Alemán");
        newPet.setBirthDate(LocalDate.of(2019, 3, 10));
        
        Owner owner = new Owner();
        owner.setId("1");
        newPet.setOwner(owner);

        Pet savedPet = petService.save(newPet);
        assertNotNull(savedPet.getId());
        assertEquals("Rex", savedPet.getName());
        verify(csvManager, times(1)).writeToCSV(anyString(), anyList());
    }

    @Test
    void save_NewPetWithInvalidOwner_ShouldThrowException() {
        Pet newPet = new Pet();
        newPet.setName("Rex");
        newPet.setSpecies(AnimalSpecies.DOG);
        newPet.setBreed("Pastor Alemán");
        newPet.setBirthDate(LocalDate.of(2019, 3, 10));
        
        Owner owner = new Owner();
        owner.setId("999"); // Owner no existente
        newPet.setOwner(owner);

        when(ownerService.findById("999")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> petService.save(newPet));
    }

    @Test
    void deleteById_ExistingId_ShouldDeletePet() {
        petService.deleteById("1");
        verify(csvManager, times(1)).writeToCSV(anyString(), anyList());
    }
}
