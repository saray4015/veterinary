package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Treatment;
import co.edu.umanizales.veterinary.service.TreatmentService;
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

@WebMvcTest(TreatmentController.class)
class TreatmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TreatmentService treatmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private Treatment testTreatment;

    @BeforeEach
    void setUp() {
        // Configurar ObjectMapper para manejar fechas
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Configurar tratamiento de prueba
        testTreatment = new Treatment();
        testTreatment.setId("T1");
        testTreatment.setName("Tratamiento antiparasitario");
        testTreatment.setDescription("Tratamiento mensual contra parásitos internos y externos");
        testTreatment.setStartDate(LocalDate.now());
        testTreatment.setEndDate(LocalDate.now().plusMonths(1));
        testTreatment.setCost(25.0);
        testTreatment.setCompleted(false);
    }

    @Test
    void getAllTreatments_ShouldReturnAllTreatments() throws Exception {
        when(treatmentService.findAll()).thenReturn(Arrays.asList(testTreatment));

        mockMvc.perform(get("/api/treatments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Tratamiento antiparasitario"))
                .andExpect(jsonPath("$[0].cost").value(25.0));

        verify(treatmentService, times(1)).findAll();
    }

    @Test
    void getActiveTreatments_ShouldReturnActiveTreatments() throws Exception {
        when(treatmentService.findActiveTreatments()).thenReturn(Arrays.asList(testTreatment));

        mockMvc.perform(get("/api/treatments/active"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].completed").value(false));

        verify(treatmentService, times(1)).findActiveTreatments();
    }

    @Test
    void getExpiredTreatments_ShouldReturnExpiredTreatments() throws Exception {
        when(treatmentService.findExpiredTreatments()).thenReturn(Arrays.asList(testTreatment));

        mockMvc.perform(get("/api/treatments/expired"))
                .andExpect(status().isOk());

        verify(treatmentService, times(1)).findExpiredTreatments();
    }

    @Test
    void getTreatmentById_WithValidId_ShouldReturnTreatment() throws Exception {
        when(treatmentService.findById("T1")).thenReturn(Optional.of(testTreatment));

        mockMvc.perform(get("/api/treatments/T1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Tratamiento antiparasitario"))
                .andExpect(jsonPath("$.cost").value(25.0));

        verify(treatmentService, times(1)).findById("T1");
    }

    @Test
    void createTreatment_WithValidData_ShouldReturnCreated() throws Exception {
        when(treatmentService.save(any(Treatment.class))).thenReturn(testTreatment);

        mockMvc.perform(post("/api/treatments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTreatment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Tratamiento antiparasitario"));

        verify(treatmentService, times(1)).save(any(Treatment.class));
    }

    @Test
    void completeTreatment_WithValidId_ShouldReturnOk() throws Exception {
        when(treatmentService.findById("T1")).thenReturn(Optional.of(testTreatment));
        doNothing().when(treatmentService).completeTreatment("T1");

        mockMvc.perform(put("/api/treatments/T1/complete"))
                .andExpect(status().isOk());

        verify(treatmentService, times(1)).completeTreatment("T1");
    }

    @Test
    void updateTreatment_WithValidData_ShouldReturnUpdatedTreatment() throws Exception {
        when(treatmentService.findById("T1")).thenReturn(Optional.of(testTreatment));
        when(treatmentService.save(any(Treatment.class))).thenReturn(testTreatment);

        testTreatment.setCost(30.0);

        mockMvc.perform(put("/api/treatments/T1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testTreatment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.cost").value(30.0));

        verify(treatmentService, times(1)).save(any(Treatment.class));
    }

    @Test
    void deleteTreatment_WithValidId_ShouldReturnNoContent() throws Exception {
        when(treatmentService.findById("T1")).thenReturn(Optional.of(testTreatment));
        doNothing().when(treatmentService).deleteById("T1");

        mockMvc.perform(delete("/api/treatments/T1"))
                .andExpect(status().isNoContent());

        verify(treatmentService, times(1)).deleteById("T1");
    }
}
