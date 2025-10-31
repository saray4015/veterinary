package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.*;
import co.edu.umanizales.veterinary.service.AppointmentService;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AppointmentController.class)
class AppointmentControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AppointmentService appointmentService;

    @MockBean
    private PetService petService;

    @MockBean
    private VeterinarianService veterinarianService;

    @Autowired
    private ObjectMapper objectMapper;

    private Appointment testAppointment;
    private Pet testPet;
    private Veterinarian testVeterinarian;

    @BeforeEach
    void setUp() {
        // Configurar ObjectMapper para manejar LocalDateTime
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

        testAppointment = new Appointment();
        testAppointment.setId("A1");
        testAppointment.setDateTime(LocalDateTime.now().plusDays(1));
        testAppointment.setPet(testPet);
        testAppointment.setVeterinarian(testVeterinarian);
        testAppointment.setServiceType(ServiceType.CONSULTATION);
        testAppointment.setStatus(AppointmentStatus.SCHEDULED);
        testAppointment.setCost(50.0);
    }

    @Test
    void getAllAppointments_ShouldReturnAllAppointments() throws Exception {
        when(appointmentService.findAll()).thenReturn(Arrays.asList(testAppointment));

        mockMvc.perform(get("/api/appointments"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("A1"))
                .andExpect(jsonPath("$[0].serviceType").value("CONSULTATION"));

        verify(appointmentService, times(1)).findAll();
    }

    @Test
    void getAppointmentById_WithValidId_ShouldReturnAppointment() throws Exception {
        when(appointmentService.findById("A1")).thenReturn(Optional.of(testAppointment));

        mockMvc.perform(get("/api/appointments/A1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("A1"))
                .andExpect(jsonPath("$.status").value("SCHEDULED"));

        verify(appointmentService, times(1)).findById("A1");
    }

    @Test
    void getAppointmentsByPet_WithValidPetId_ShouldReturnAppointments() throws Exception {
        when(appointmentService.findByPetId("1")).thenReturn(Arrays.asList(testAppointment));

        mockMvc.perform(get("/api/appointments/pet/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].pet.id").value("1"));

        verify(appointmentService, times(1)).findByPetId("1");
    }

    @Test
    void getAppointmentsByVeterinarian_WithValidVetId_ShouldReturnAppointments() throws Exception {
        when(appointmentService.findByVeterinarianId("VET1")).thenReturn(Arrays.asList(testAppointment));

        mockMvc.perform(get("/api/appointments/veterinarian/VET1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].veterinarian.id").value("VET1"));

        verify(appointmentService, times(1)).findByVeterinarianId("VET1");
    }

    @Test
    void getAppointmentsByStatus_WithValidStatus_ShouldReturnAppointments() throws Exception {
        when(appointmentService.findByStatus(AppointmentStatus.SCHEDULED))
                .thenReturn(Arrays.asList(testAppointment));

        mockMvc.perform(get("/api/appointments/status/SCHEDULED"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].status").value("SCHEDULED"));

        verify(appointmentService, times(1)).findByStatus(AppointmentStatus.SCHEDULED);
    }

    @Test
    void createAppointment_WithValidData_ShouldReturnCreated() throws Exception {
        when(petService.findById("1")).thenReturn(Optional.of(testPet));
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));
        when(appointmentService.save(any(Appointment.class))).thenReturn(testAppointment);

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAppointment)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.serviceType").value("CONSULTATION"));

        verify(appointmentService, times(1)).save(any(Appointment.class));
    }

    @Test
    void createAppointment_WithInvalidPet_ShouldReturnBadRequest() throws Exception {
        when(petService.findById("999")).thenReturn(Optional.empty());

        testAppointment.getPet().setId("999");

        mockMvc.perform(post("/api/appointments")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAppointment)))
                .andExpect(status().isBadRequest());

        verify(appointmentService, never()).save(any(Appointment.class));
    }

    @Test
    void updateAppointment_WithValidData_ShouldReturnUpdatedAppointment() throws Exception {
        when(appointmentService.findById("A1")).thenReturn(Optional.of(testAppointment));
        when(petService.findById("1")).thenReturn(Optional.of(testPet));
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(testVeterinarian));
        when(appointmentService.save(any(Appointment.class))).thenReturn(testAppointment);

        testAppointment.setStatus(AppointmentStatus.COMPLETED);

        mockMvc.perform(put("/api/appointments/A1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testAppointment)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("COMPLETED"));

        verify(appointmentService, times(1)).save(any(Appointment.class));
    }

    @Test
    void deleteAppointment_WithValidId_ShouldReturnNoContent() throws Exception {
        when(appointmentService.findById("A1")).thenReturn(Optional.of(testAppointment));
        doNothing().when(appointmentService).deleteById("A1");

        mockMvc.perform(delete("/api/appointments/A1"))
                .andExpect(status().isNoContent());

        verify(appointmentService, times(1)).deleteById("A1");
    }
}
