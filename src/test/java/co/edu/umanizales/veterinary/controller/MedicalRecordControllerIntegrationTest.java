package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.*;
import co.edu.umanizales.veterinary.service.MedicalRecordService;
import co.edu.umanizales.veterinary.service.PetService;
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

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MedicalRecordController.class)
class MedicalRecordControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @MockBean
    private PetService petService;

    @MockBean
    private VeterinarianService veterinarianService;

    @Autowired
    private ObjectMapper objectMapper;

    private MedicalRecord testMedicalRecord;
    private Pet testPet;
    private Veterinarian testVeterinarian;

    @BeforeEach
    void setUp() {
        // Configurar ObjectMapper para manejar fechas
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Configurar datos de prueba
        Owner testOwner = new Owner();
        testOwner.setId("1");
        testOwner.setName("Juan Pérez");
        testOwner.setEmail("juan@example.com");

        testPet = new Pet();
        testPet.setId("1");
        testPet.setName("Firulais");
        testPet.setSpecies(AnimalSpecies.DOG);
        testPet.setOwner(testOwner);

        testVeterinarian = new Veterinarian();
        testVeterinarian.setId("VET1");
        testVeterinarian.setName("Dr. Carlos López");
        testVeterinarian.setLicenseNumber("VET12345");

        testMedicalRecord = new MedicalRecord();
        testMedicalRecord.setId("MR1");
        testMedicalRecord.setDate(LocalDate.now());
        testMedicalRecord.setPet(testPet);
        testMedicalRecord.setVeterinarian(testVeterinarian);
        testMedicalRecord.setDiagnosis("Control de rutina");
        testMedicalRecord.setTreatmentNotes("Vacunación anual aplicada");
    }

    @Test
    void getAllMedicalRecords_ShouldReturnAllRecords() throws Exception {
        when(medicalRecordService.findAll()).thenReturn(Arrays.asList(testMedicalRecord));

        mockMvc.perform(get("/api/medical-records"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("MR1"))
                .andExpect(jsonPath("$[0].diagnosis").value("Control de rutina"));

        verify(medicalRecordService, times(1)).findAll();
    }

    @Test
    void getMedicalRecordById_WithValidId_ShouldReturnRecord() throws Exception {
        when(medicalRecordService.findById("MR1")).thenReturn(Optional.of(testMedicalRecord));

        mockMvc.perform(get("/api/medical-records/MR1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("MR1"))
                .andExpect(jsonPath("$.diagnosis").value("Control de rutina"));

        verify(medicalRecordService, times(1)).findById("MR1");
    }

    @Test
    void getMedicalRecordsByPet_WithValidPetId_ShouldReturnRecords() throws Exception {
        when(medicalRecordService.findByPetId("1")).thenReturn(Arrays.asList(testMedicalRecord));

        mockMvc.perform(get("/api/medical-records/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pet.id").value("1"));

        verify(medicalRecordService, times(1)).findByPetId("1");
    }

    @Test
    void getMedicalRecordsByVeterinarian_WithValidVetId_ShouldReturnRecords() throws Exception {
        when(medicalRecordService.findByVeterinarianId("VET1")).thenReturn(Arrays.asList(testMedicalRecord));

        mockMvc.perform(get("/api/medical-records/veterinarian/VET1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].veterinarian.id").value("VET1"));

        verify(medicalRecordService, times(1)).findByVeterinarianId("VET1");
    }

    @Test
    void createMedicalRecord_WithValidData_ShouldReturnCreated() throws Exception {
        when(petService.findById("1")).thenReturn(Optional.of(testPet));
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));
        when(medicalRecordService.save(any(MedicalRecord.class))).thenReturn(testMedicalRecord);

        mockMvc.perform(post("/api/medical-records")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMedicalRecord)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.diagnosis").value("Control de rutina"));

        verify(medicalRecordService, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void addTreatmentToMedicalRecord_WithValidData_ShouldReturnOk() throws Exception {
        when(medicalRecordService.findById("MR1")).thenReturn(Optional.of(testMedicalRecord));
        doNothing().when(medicalRecordService).addTreatment(anyString(), any());

        mockMvc.perform(post("/api/medical-records/MR1/treatments")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"T1\",\"name\":\"Antiparasitario\"}"))
                .andExpect(status().isCreated());

        verify(medicalRecordService, times(1)).addTreatment(anyString(), any());
    }

    @Test
    void addMedicationToMedicalRecord_WithValidData_ShouldReturnOk() throws Exception {
        when(medicalRecordService.findById("MR1")).thenReturn(Optional.of(testMedicalRecord));
        doNothing().when(medicalRecordService).addMedication(anyString(), any());

        mockMvc.perform(post("/api/medical-records/MR1/medications")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"M1\",\"name\":\"Antibiótico\"}"))
                .andExpect(status().isCreated());

        verify(medicalRecordService, times(1)).addMedication(anyString(), any());
    }

    @Test
    void updateMedicalRecord_WithValidData_ShouldReturnUpdatedRecord() throws Exception {
        when(medicalRecordService.findById("MR1")).thenReturn(Optional.of(testMedicalRecord));
        when(petService.findById("1")).thenReturn(Optional.of(testPet));
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));
        when(medicalRecordService.save(any(MedicalRecord.class))).thenReturn(testMedicalRecord);

        testMedicalRecord.setDiagnosis("Diagnóstico actualizado");

        mockMvc.perform(put("/api/medical-records/MR1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testMedicalRecord)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.diagnosis").value("Diagnóstico actualizado"));

        verify(medicalRecordService, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    void deleteMedicalRecord_WithValidId_ShouldReturnNoContent() throws Exception {
        when(medicalRecordService.findById("MR1")).thenReturn(Optional.of(testMedicalRecord));
        doNothing().when(medicalRecordService).deleteById("MR1");

        mockMvc.perform(delete("/api/medical-records/MR1"))
                .andExpect(status().isNoContent());

        verify(medicalRecordService, times(1)).deleteById("MR1");
    }
}
