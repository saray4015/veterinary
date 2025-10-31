package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Owner;
import co.edu.umanizales.veterinary.util.CSVManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OwnerServiceTest {

    @Mock
    private CSVManager<Owner> csvManager;

    @InjectMocks
    private OwnerService ownerService;

    private Owner owner1;
    private Owner owner2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        owner1 = new Owner();
        owner1.setId("1");
        owner1.setName("Juan Pérez");
        owner1.setEmail("juan@example.com");
        owner1.setPhone("1234567890");
        owner1.setAddress("Calle 123");

        owner2 = new Owner();
        owner2.setId("2");
        owner2.setName("María Gómez");
        owner2.setEmail("maria@example.com");
        owner2.setPhone("9876543210");
        owner2.setAddress("Carrera 45 #67-89");

        when(csvManager.readFromCSV(anyString(), eq(Owner.class))).thenReturn(Arrays.asList(owner1, owner2));
    }

    @Test
    void findAll_ShouldReturnAllOwners() {
        List<Owner> owners = ownerService.findAll();
        assertEquals(2, owners.size());
        verify(csvManager, times(1)).readFromCSV(anyString(), eq(Owner.class));
    }

    @Test
    void findById_WithValidId_ShouldReturnOwner() {
        Optional<Owner> foundOwner = ownerService.findById("1");
        assertTrue(foundOwner.isPresent());
        assertEquals("Juan Pérez", foundOwner.get().getName());
    }

    @Test
    void findById_WithInvalidId_ShouldReturnEmpty() {
        Optional<Owner> foundOwner = ownerService.findById("999");
        assertFalse(foundOwner.isPresent());
    }

    @Test
    void save_NewOwner_ShouldSaveAndReturnOwner() {
        Owner newOwner = new Owner();
        newOwner.setName("Carlos López");
        newOwner.setEmail("carlos@example.com");
        newOwner.setPhone("5551234567");
        newOwner.setAddress("Av. Principal #100");

        Owner savedOwner = ownerService.save(newOwner);
        assertNotNull(savedOwner.getId());
        assertEquals("Carlos López", savedOwner.getName());
        verify(csvManager, times(1)).writeToCSV(anyString(), anyList());
    }

    @Test
    void deleteById_ExistingId_ShouldDeleteOwner() {
        ownerService.deleteById("1");
        verify(csvManager, times(1)).writeToCSV(anyString(), anyList());
        
        Optional<Owner> deletedOwner = ownerService.findById("1");
        assertFalse(deletedOwner.isPresent());
    }

    @Test
    void existsById_WithExistingId_ShouldReturnTrue() {
        assertTrue(ownerService.existsById("1"));
    }

    @Test
    void existsById_WithNonExistingId_ShouldReturnFalse() {
        assertFalse(ownerService.existsById("999"));
    }
}
