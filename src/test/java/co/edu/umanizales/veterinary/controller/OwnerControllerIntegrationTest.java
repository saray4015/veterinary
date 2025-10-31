package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Owner;
import co.edu.umanizales.veterinary.service.OwnerService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(OwnerController.class)
class OwnerControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OwnerService ownerService;

    @Autowired
    private ObjectMapper objectMapper;

    private Owner testOwner;

    @BeforeEach
    void setUp() {
        testOwner = new Owner();
        testOwner.setId("1");
        testOwner.setName("Juan Pérez");
        testOwner.setEmail("juan@example.com");
        testOwner.setPhone("1234567890");
        testOwner.setAddress("Calle 123");
    }

    @Test
    void getAllOwners_ShouldReturnAllOwners() throws Exception {
        when(ownerService.findAll()).thenReturn(Arrays.asList(testOwner));

        mockMvc.perform(get("/api/owners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Juan Pérez"))
                .andExpect(jsonPath("$[0].email").value("juan@example.com"));

        verify(ownerService, times(1)).findAll();
    }

    @Test
    void getOwnerById_WithValidId_ShouldReturnOwner() throws Exception {
        when(ownerService.findById("1")).thenReturn(Optional.of(testOwner));

        mockMvc.perform(get("/api/owners/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Juan Pérez"))
                .andExpect(jsonPath("$.email").value("juan@example.com"));

        verify(ownerService, times(1)).findById("1");
    }

    @Test
    void getOwnerById_WithInvalidId_ShouldReturnNotFound() throws Exception {
        when(ownerService.findById("999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/owners/999"))
                .andExpect(status().isNotFound());

        verify(ownerService, times(1)).findById("999");
    }

    @Test
    void createOwner_WithValidData_ShouldReturnCreated() throws Exception {
        when(ownerService.save(any(Owner.class))).thenReturn(testOwner);

        mockMvc.perform(post("/api/owners")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOwner)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Juan Pérez"));

        verify(ownerService, times(1)).save(any(Owner.class));
    }

    @Test
    void updateOwner_WithValidData_ShouldReturnUpdatedOwner() throws Exception {
        when(ownerService.findById("1")).thenReturn(Optional.of(testOwner));
        when(ownerService.save(any(Owner.class))).thenReturn(testOwner);

        testOwner.setPhone("9876543210");

        mockMvc.perform(put("/api/owners/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testOwner)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phone").value("9876543210"));

        verify(ownerService, times(1)).findById("1");
        verify(ownerService, times(1)).save(any(Owner.class));
    }

    @Test
    void deleteOwner_WithValidId_ShouldReturnNoContent() throws Exception {
        when(ownerService.findById("1")).thenReturn(Optional.of(testOwner));
        doNothing().when(ownerService).deleteById("1");

        mockMvc.perform(delete("/api/owners/1"))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).deleteById("1");
    }
}
