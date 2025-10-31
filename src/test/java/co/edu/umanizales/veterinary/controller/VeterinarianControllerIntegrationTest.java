package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Veterinarian;
import co.edu.umanizales.veterinary.service.VeterinarianService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VeterinarianController.class)
class VeterinarianControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private VeterinarianService veterinarianService;

    @Autowired
    private ObjectMapper objectMapper;

    private Veterinarian testVeterinarian;

    @BeforeEach
    void setUp() {
        // Configurar ObjectMapper para manejar fechas
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        testVeterinarian = new Veterinarian();
        testVeterinarian.setId("VET1");
        testVeterinarian.setName("Dr. Carlos López");
        testVeterinarian.setEmail("carlos@veterinary.com");
        testVeterinarian.setPhone("5551234567");
        testVeterinarian.setLicenseNumber("VET12345");
        testVeterinarian.setAddress("Calle Principal #100");
    }

    @Test
    void getAllVeterinarians_ShouldReturnAllVeterinarians() throws Exception {
        when(veterinarianService.findAll()).thenReturn(Arrays.asList(testVeterinarian));

        mockMvc.perform(get("/api/veterinarians"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Dr. Carlos López"))
                .andExpect(jsonPath("$[0].licenseNumber").value("VET12345"));

        verify(veterinarianService, times(1)).findAll();
    }

    @Test
    void getVeterinarianById_WithValidId_ShouldReturnVeterinarian() throws Exception {
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));

        mockMvc.perform(get("/api/veterinarians/VET1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Dr. Carlos López"))
                .andExpect(jsonPath("$.email").value("carlos@veterinary.com"));

        verify(veterinarianService, times(1)).findById("VET1");
    }

    @Test
    void getVeterinarianByLicense_WithValidLicense_ShouldReturnVeterinarian() throws Exception {
        when(veterinarianService.findByLicenseNumber("VET12345")).thenReturn(Optional.of(testVeterinarian));

        mockMvc.perform(get("/api/veterinarians/license/VET12345"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.licenseNumber").value("VET12345"));

        verify(veterinarianService, times(1)).findByLicenseNumber("VET12345");
    }

    @Test
    void createVeterinarian_WithValidData_ShouldReturnCreated() throws Exception {
        when(veterinarianService.save(any(Veterinarian.class))).thenReturn(testVeterinarian);

        mockMvc.perform(post("/api/veterinarians")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testVeterinarian)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Dr. Carlos López"));

        verify(veterinarianService, times(1)).save(any(Veterinarian.class));
    }

    @Test
    void updateVeterinarian_WithValidData_ShouldReturnUpdatedVeterinarian() throws Exception {
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));
        when(veterinarianService.save(any(Veterinarian.class))).thenReturn(testVeterinarian);

        testVeterinarian.setPhone("5559876543");

        mockMvc.perform(put("/api/veterinarians/VET1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testVeterinarian)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("5559876543"));

        verify(veterinarianService, times(1)).save(any(Veterinarian.class));
    }

    @Test
    void deleteVeterinarian_WithValidId_ShouldReturnNoContent() throws Exception {
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));
        doNothing().when(veterinarianService).deleteById("VET1");

        mockMvc.perform(delete("/api/veterinarians/VET1"))
                .andExpect(status().isNoContent());

        verify(veterinarianService, times(1)).deleteById("VET1");
    }

    @Test
    void addSpecialty_WithValidData_ShouldReturnOk() throws Exception {
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));
        doNothing().when(veterinarianService).addSpecialty(anyString(), any());

        mockMvc.perform(post("/api/veterinarians/VET1/specialties")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"S1\",\"name\":\"Cirugía\"}"))
                .andExpect(status().isOk());

        verify(veterinarianService, times(1)).addSpecialty(anyString(), any());
    }
}
