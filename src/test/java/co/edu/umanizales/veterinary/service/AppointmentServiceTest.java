package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.*;
import co.edu.umanizales.veterinary.util.CSVManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class AppointmentServiceTest {

    @Mock
    private CSVManager<Appointment> csvManager;
    
    @Mock
    private PetService petService;
    
    @Mock
    private VeterinarianService veterinarianService;

    @InjectMocks
    private AppointmentService appointmentService;

    private Appointment appointment1;
    private Appointment appointment2;
    private Pet pet1;
    private Veterinarian vet1;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        // Configurar datos de prueba
        Owner owner = new Owner();
        owner.setId("1");
        owner.setName("Juan Pérez");
        owner.setEmail("juan@example.com");
        owner.setPhone("1234567890");
        owner.setAddress("Calle 123");

        pet1 = new Pet();
        pet1.setId("1");
        pet1.setName("Firulais");
        pet1.setSpecies(AnimalSpecies.DOG);
        pet1.setBreed("Labrador");
        pet1.setOwner(owner);

        vet1 = new Veterinarian();
        vet1.setId("VET1");
        vet1.setName("Dr. Carlos López");
        vet1.setEmail("carlos@veterinary.com");
        vet1.setPhone("5551234567");
        vet1.setLicenseNumber("VET12345");

        LocalDateTime now = LocalDateTime.now();
        
        appointment1 = new Appointment();
        appointment1.setId("A1");
        appointment1.setDateTime(now.plusDays(1));
        appointment1.setPet(pet1);
        appointment1.setVeterinarian(vet1);
        appointment1.setServiceType(ServiceType.CONSULTATION);
        appointment1.setStatus(AppointmentStatus.SCHEDULED);
        appointment1.setCost(50.0);

        appointment2 = new Appointment();
        appointment2.setId("A2");
        appointment2.setDateTime(now.plusDays(2));
        appointment2.setPet(pet1);
        appointment2.setVeterinarian(vet1);
        appointment2.setServiceType(ServiceType.VACCINATION);
        appointment2.setStatus(AppointmentStatus.SCHEDULED);
        appointment2.setCost(30.0);

        when(csvManager.readFromCSV(anyString(), eq(Appointment.class)))
            .thenReturn(Arrays.asList(appointment1, appointment2));
            
        when(petService.findById("1")).thenReturn(Optional.of(pet1));
        when(veterinarianService.findById("VET1")).thenReturn(Optional.of(vet1));
    }

    @Test
    void findAll_ShouldReturnAllAppointments() {
        List<Appointment> appointments = appointmentService.findAll();
        assertEquals(2, appointments.size());
        verify(csvManager, times(1)).readFromCSV(anyString(), eq(Appointment.class));
    }

    @Test
    void findById_WithValidId_ShouldReturnAppointment() {
        Optional<Appointment> foundAppointment = appointmentService.findById("A1");
        assertTrue(foundAppointment.isPresent());
        assertEquals(ServiceType.CONSULTATION, foundAppointment.get().getServiceType());
    }

    @Test
    void findByPetId_WithValidPetId_ShouldReturnAppointments() {
        List<Appointment> petAppointments = appointmentService.findByPetId("1");
        assertEquals(2, petAppointments.size());
        assertTrue(petAppointments.stream().allMatch(a -> a.getPet().getId().equals("1")));
    }

    @Test
    void findByVeterinarianId_WithValidVetId_ShouldReturnAppointments() {
        List<Appointment> vetAppointments = appointmentService.findByVeterinarianId("VET1");
        assertEquals(2, vetAppointments.size());
        assertTrue(vetAppointments.stream().allMatch(a -> a.getVeterinarian().getId().equals("VET1")));
    }

    @Test
    void findByStatus_WithValidStatus_ShouldReturnMatchingAppointments() {
        List<Appointment> scheduledAppointments = appointmentService.findByStatus(AppointmentStatus.SCHEDULED);
        assertEquals(2, scheduledAppointments.size());
        assertTrue(scheduledAppointments.stream().allMatch(a -> a.getStatus() == AppointmentStatus.SCHEDULED));
    }

    @Test
    void save_NewAppointment_ShouldSaveAndReturnAppointment() {
        Appointment newAppointment = new Appointment();
        newAppointment.setDateTime(LocalDateTime.now().plusDays(3));
        newAppointment.setPet(pet1);
        newAppointment.setVeterinarian(vet1);
        newAppointment.setServiceType(ServiceType.GROOMING);
        newAppointment.setStatus(AppointmentStatus.SCHEDULED);
        newAppointment.setCost(40.0);

        Appointment savedAppointment = appointmentService.save(newAppointment);
        assertNotNull(savedAppointment.getId());
        assertEquals(ServiceType.GROOMING, savedAppointment.getServiceType());
        verify(csvManager, times(1)).writeToCSV(anyString(), anyList());
    }

    @Test
    void save_AppointmentWithInvalidPet_ShouldThrowException() {
        Appointment newAppointment = new Appointment();
        newAppointment.setDateTime(LocalDateTime.now().plusDays(3));
        
        Pet invalidPet = new Pet();
        invalidPet.setId("999"); // Mascota no existente
        newAppointment.setPet(invalidPet);
        newAppointment.setVeterinarian(vet1);
        newAppointment.setServiceType(ServiceType.GROOMING);
        
        when(petService.findById("999")).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> appointmentService.save(newAppointment));
    }

    @Test
    void isVeterinarianBusy_WithBusyTimeSlot_ShouldReturnTrue() {
        LocalDateTime busyTime = appointment1.getDateTime();
        assertTrue(appointmentService.isVeterinarianBusy("VET1", busyTime));
    }

    @Test
    void isVeterinarianBusy_WithAvailableTimeSlot_ShouldReturnFalse() {
        LocalDateTime availableTime = LocalDateTime.now().plusDays(10);
        assertFalse(appointmentService.isVeterinarianBusy("VET1", availableTime));
    }
}
