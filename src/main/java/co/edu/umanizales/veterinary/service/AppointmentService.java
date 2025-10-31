package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.Appointment;
import co.edu.umanizales.veterinary.model.AppointmentStatus;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.model.Veterinarian;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService extends BaseServiceImpl<Appointment> {
    
    private final PetService petService;
    private final VeterinarianService veterinarianService;

    public AppointmentService(PetService petService, VeterinarianService veterinarianService) {
        super("appointments.csv");
        this.petService = petService;
        this.veterinarianService = veterinarianService;
    }

    @Override
    protected Class<Appointment> getEntityClass() {
        return Appointment.class;
    }

    @Override
    public Appointment save(Appointment appointment) {
        // Validar que la mascota existe
        if (appointment.getPet() == null || appointment.getPet().getId() == null) {
            throw new IllegalArgumentException("Pet is required for appointment");
        }
        
        // Validar que el veterinario existe
        if (appointment.getVeterinarian() == null || appointment.getVeterinarian().getId() == null) {
            throw new IllegalArgumentException("Veterinarian is required for appointment");
        }
        
        // Verificar disponibilidad del veterinario
        if (isVeterinarianBusy(appointment.getVeterinarian().getId(), appointment.getDateTime())) {
            throw new IllegalStateException("Veterinarian is not available at the selected time");
        }
        
        return super.save(appointment);
    }

    public boolean isVeterinarianBusy(String veterinarianId, LocalDateTime dateTime) {
        return entities.stream()
                .filter(a -> a.getVeterinarian().getId().equals(veterinarianId))
                .anyMatch(a -> a.getDateTime().equals(dateTime) && 
                        a.getStatus() != AppointmentStatus.CANCELLED);
    }

    public List<Appointment> findByVeterinarianId(String veterinarianId) {
        return entities.stream()
                .filter(a -> veterinarianId.equals(a.getVeterinarian().getId()))
                .toList();
    }

    public List<Appointment> findByPetId(String petId) {
        return entities.stream()
                .filter(a -> petId.equals(a.getPet().getId()))
                .toList();
    }

    public List<Appointment> findByStatus(AppointmentStatus status) {
        return entities.stream()
                .filter(a -> status == a.getStatus())
                .toList();
    }
}
