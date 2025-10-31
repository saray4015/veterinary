package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.AnimalSpecies;
import co.edu.umanizales.veterinary.model.Owner;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.service.OwnerService;
import co.edu.umanizales.veterinary.service.PetService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PetController.class)
class PetControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PetService petService;

    @MockBean
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Pet testPet;
    private Owner testOwner;

    @BeforeEach
    void setUp() {
        testOwner = new Owner();
        testOwner.setId("1");
        testOwner.setName("Juan Pérez");
        testOwner.setEmail("juan@example.com");
        testOwner.setPhone("1234567890");
        testOwner.setAddress("Calle 123");

        testPet = new Pet();
        testPet.setId("1");
        testPet.setName("Firulais");
        testPet.setSpecies(AnimalSpecies.DOG);
        testPet.setBreed("Labrador");
        testPet.setBirthDate(LocalDate.of(2018, 5, 15));
        testPet.setOwner(testOwner);
    }

    @Test
    void getAllPets_ShouldReturnAllPets() throws Exception {
        when(petService.findAll()).thenReturn(Arrays.asList(testPet));

        mockMvc.perform(get("/api/pets"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Firulais"))
                .andExpect(jsonPath("$[0].species").value("DOG"));

        verify(petService, times(1)).findAll();
    }

    @Test
    void getPetById_WithValidId_ShouldReturnPet() throws Exception {
        when(petService.findById("1")).thenReturn(Optional.of(testPet));

        mockMvc.perform(get("/api/pets/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Firulais"))
                .andExpect(jsonPath("$.breed").value("Labrador"));

        verify(petService, times(1)).findById("1");
    }

    @Test
    void getPetsBySpecies_WithValidSpecies_ShouldReturnPets() throws Exception {
        when(petService.findBySpecies(AnimalSpecies.DOG)).thenReturn(Arrays.asList(testPet));

        mockMvc.perform(get("/api/pets/species/DOG"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].species").value("DOG"));

        verify(petService, times(1)).findBySpecies(AnimalSpecies.DOG);
    }

    @Test
    void getPetsByOwner_WithValidOwnerId_ShouldReturnPets() throws Exception {
        when(petService.findByOwnerId("1")).thenReturn(Arrays.asList(testPet));

        mockMvc.perform(get("/api/pets/owner/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].owner.id").value("1"));

        verify(petService, times(1)).findByOwnerId("1");
    }

    @Test
    void createPet_WithValidData_ShouldReturnCreated() throws Exception {
        when(ownerService.findById("1")).thenReturn(Optional.of(testOwner));
        when(petService.save(any(Pet.class))).thenReturn(testPet);

        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Firulais"));

        verify(petService, times(1)).save(any(Pet.class));
    }

    @Test
    void createPet_WithInvalidOwner_ShouldReturnBadRequest() throws Exception {
        when(ownerService.findById("999")).thenReturn(Optional.empty());

        Pet invalidPet = new Pet();
        invalidPet.setName("Rex");
        invalidPet.setSpecies(AnimalSpecies.DOG);
        invalidPet.setOwner(new Owner("999"));

        mockMvc.perform(post("/api/pets")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidPet)))
                .andExpect(status().isBadRequest());

        verify(petService, never()).save(any(Pet.class));
    }

    @Test
    void updatePet_WithValidData_ShouldReturnUpdatedPet() throws Exception {
        when(petService.findById("1")).thenReturn(Optional.of(testPet));
        when(ownerService.findById("1")).thenReturn(Optional.of(testOwner));
        when(petService.save(any(Pet.class))).thenReturn(testPet);

        testPet.setBreed("Golden Retriever");

        mockMvc.perform(put("/api/pets/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testPet)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.breed").value("Golden Retriever"));

        verify(petService, times(1)).save(any(Pet.class));
    }

    @Test
    void deletePet_WithValidId_ShouldReturnNoContent() throws Exception {
        when(petService.findById("1")).thenReturn(Optional.of(testPet));
        doNothing().when(petService).deleteById("1");

        mockMvc.perform(delete("/api/pets/1"))
                .andExpect(status().isNoContent());

        verify(petService, times(1)).deleteById("1");
    }
}
