package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Appointment;
import co.edu.umanizales.veterinary.model.AppointmentStatus;
import co.edu.umanizales.veterinary.service.AppointmentService;
import co.edu.umanizales.veterinary.dto.AppointmentResponse;
import co.edu.umanizales.veterinary.dto.AppointmentRequest;
import co.edu.umanizales.veterinary.dto.PetWithoutBirthDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import co.edu.umanizales.veterinary.model.Pet;
import co.edu.umanizales.veterinary.model.Veterinarian;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<AppointmentResponse>> getAllAppointments() {
        List<Appointment> list = appointmentService.findAll();
        List<AppointmentResponse> resp = list.stream()
                .map(saved -> new AppointmentResponse(
                        saved.getId(),
                        saved.getDateTime(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getReason(),
                        saved.getDiagnosis(),
                        saved.getCost(),
                        saved.getStatus()
                ))
                .toList();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getAppointmentById(@PathVariable String id) {
        // Normalizar posibles comillas o espacios pegados al ID
        String tmp = (id != null) ? id.trim() : null;
        final String normalizedId = (tmp != null && tmp.length() >= 2 &&
                ((tmp.startsWith("\"") && tmp.endsWith("\"")) ||
                 (tmp.startsWith("'") && tmp.endsWith("'"))))
                ? tmp.substring(1, tmp.length() - 1).trim()
                : tmp;

        return appointmentService.findById(id)
                .or(() -> appointmentService.findById(normalizedId))
                .<ResponseEntity<?>>map(appointment -> new ResponseEntity<>(appointment, HttpStatus.OK))
                .orElseGet(() -> {
                    java.util.List<String> ids = appointmentService.findAll().stream()
                            .map(Appointment::getId)
                            .filter(java.util.Objects::nonNull)
                            .toList();
                    String msg = "Appointment not found: '" + id + "'. Available IDs: " + ids;
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
                });
    }

    @GetMapping(value = "/pet/{petId}", produces = "application/json")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByPet(@PathVariable String petId) {
        List<Appointment> list = appointmentService.findByPetId(petId);
        List<AppointmentResponse> resp = list.stream()
                .map(saved -> new AppointmentResponse(
                        saved.getId(),
                        saved.getDateTime(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getReason(),
                        saved.getDiagnosis(),
                        saved.getCost(),
                        saved.getStatus()
                ))
                .toList();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(value = "/veterinarian/{veterinarianId}", produces = "application/json")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByVeterinarian(@PathVariable String veterinarianId) {
        List<Appointment> list = appointmentService.findByVeterinarianId(veterinarianId);
        List<AppointmentResponse> resp = list.stream()
                .map(saved -> new AppointmentResponse(
                        saved.getId(),
                        saved.getDateTime(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getReason(),
                        saved.getDiagnosis(),
                        saved.getCost(),
                        saved.getStatus()
                ))
                .toList();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(value = "/status/{status}", produces = "application/json")
    public ResponseEntity<List<AppointmentResponse>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        List<Appointment> list = appointmentService.findByStatus(status);
        List<AppointmentResponse> resp = list.stream()
                .map(saved -> new AppointmentResponse(
                        saved.getId(),
                        saved.getDateTime(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getReason(),
                        saved.getDiagnosis(),
                        saved.getCost(),
                        saved.getStatus()
                ))
                .toList();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        try {
            Appointment toSave = new Appointment();
            toSave.setId(request.getId());
            if (request.getDateTime() == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("dateTime is required in format yyyy-MM-dd'T'HH:mm:ss");
            }
            toSave.setDateTime(LocalDateTime.parse(request.getDateTime()));
            // Mapear Pet desde id o objeto anidado
            String petId = request.getPetId() != null ? request.getPetId() :
                    (request.getPet() != null ? request.getPet().getId() : null);
            if (petId != null) {
                Pet p = new Pet();
                p.setId(petId);
                toSave.setPet(p);
            }
            // Mapear Veterinarian desde id o objeto anidado
            String vetId = request.getVeterinarianId() != null ? request.getVeterinarianId() :
                    (request.getVeterinarian() != null ? request.getVeterinarian().getId() : null);
            if (vetId != null) {
                Veterinarian v = new Veterinarian();
                v.setId(vetId);
                toSave.setVeterinarian(v);
            }
            toSave.setReason(request.getReason());
            toSave.setDiagnosis(request.getDiagnosis());
            toSave.setCost(request.getCost() != null ? request.getCost() : 0.0);
            toSave.setStatus(request.getStatus() != null ? request.getStatus() : AppointmentStatus.SCHEDULED);

            Appointment saved = appointmentService.save(toSave);
            AppointmentResponse resp = new AppointmentResponse(
                saved.getId(),
                saved.getDateTime(),
                new PetWithoutBirthDate(
                    saved.getPet() != null ? saved.getPet().getId() : null,
                    saved.getPet() != null ? saved.getPet().getName() : null,
                    saved.getPet() != null ? saved.getPet().getSpecie() : null,
                    saved.getPet() != null ? saved.getPet().getBreed() : null,
                    saved.getPet() != null ? saved.getPet().getOwnerId() : null
                ),
                saved.getVeterinarian(),
                saved.getReason(),
                saved.getDiagnosis(),
                saved.getCost(),
                saved.getStatus()
            );
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateAppointment(@PathVariable String id, @RequestBody AppointmentRequest request) {
        if (appointmentService.findById(id).isEmpty()) {
            // Si no existe, crear (upsert)
            try {
                Appointment toCreate = new Appointment();
                toCreate.setId(id);
                if (request.getDateTime() == null) {
                    toCreate.setDateTime(LocalDateTime.now());
                } else {
                    toCreate.setDateTime(LocalDateTime.parse(request.getDateTime()));
                }

                String petIdCreate = request.getPetId() != null ? request.getPetId() :
                        (request.getPet() != null ? request.getPet().getId() : null);
                if (petIdCreate != null) {
                    Pet p = new Pet();
                    p.setId(petIdCreate);
                    toCreate.setPet(p);
                }

                String vetIdCreate = request.getVeterinarianId() != null ? request.getVeterinarianId() :
                        (request.getVeterinarian() != null ? request.getVeterinarian().getId() : null);
                if (vetIdCreate != null) {
                    Veterinarian v = new Veterinarian();
                    v.setId(vetIdCreate);
                    toCreate.setVeterinarian(v);
                }

                toCreate.setReason(request.getReason());
                toCreate.setDiagnosis(request.getDiagnosis());
                toCreate.setCost(request.getCost() != null ? request.getCost() : 0.0);
                toCreate.setStatus(request.getStatus() != null ? request.getStatus() : AppointmentStatus.SCHEDULED);

                Appointment saved = appointmentService.save(toCreate);
                AppointmentResponse resp = new AppointmentResponse(
                        saved.getId(),
                        saved.getDateTime(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getReason(),
                        saved.getDiagnosis(),
                        saved.getCost(),
                        saved.getStatus()
                );
                return new ResponseEntity<>(resp, HttpStatus.CREATED);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
            }
        }
        // Partir del existente para soportar actualizaciones parciales
        Appointment existing = appointmentService.findById(id).get();
        Appointment toSave = new Appointment();
        toSave.setId(id);
        toSave.setDateTime(request.getDateTime() != null
                ? LocalDateTime.parse(request.getDateTime())
                : existing.getDateTime());

        // Pet: mantener si no se envía; reemplazar si llega id
        String petId = request.getPetId() != null ? request.getPetId() :
                (request.getPet() != null ? request.getPet().getId() : null);
        if (petId != null) {
            Pet p = new Pet();
            p.setId(petId);
            toSave.setPet(p);
        } else {
            toSave.setPet(existing.getPet());
        }

        // Veterinarian: mantener si no se envía; reemplazar si llega id
        String vetId = request.getVeterinarianId() != null ? request.getVeterinarianId() :
                (request.getVeterinarian() != null ? request.getVeterinarian().getId() : null);
        if (vetId != null) {
            Veterinarian v = new Veterinarian();
            v.setId(vetId);
            toSave.setVeterinarian(v);
        } else {
            toSave.setVeterinarian(existing.getVeterinarian());
        }

        // Asegurar IDs escalares para salvar correctamente incluso si los objetos están null
        if (toSave.getPet() != null && toSave.getPet().getId() != null) {
            toSave.setPetId(toSave.getPet().getId());
        } else {
            toSave.setPetId(existing.getPetId());
        }
        if (toSave.getVeterinarian() != null && toSave.getVeterinarian().getId() != null) {
            toSave.setVeterinarianId(toSave.getVeterinarian().getId());
        } else {
            toSave.setVeterinarianId(existing.getVeterinarianId());
        }

        toSave.setReason(request.getReason() != null ? request.getReason() : existing.getReason());
        toSave.setDiagnosis(request.getDiagnosis() != null ? request.getDiagnosis() : existing.getDiagnosis());
        toSave.setCost(request.getCost() != null ? request.getCost() : existing.getCost());
        toSave.setStatus(request.getStatus() != null ? request.getStatus() : existing.getStatus());
        try {
            Appointment saved = appointmentService.save(toSave);
            AppointmentResponse resp = new AppointmentResponse(
                    saved.getId(),
                    saved.getDateTime(),
                    new PetWithoutBirthDate(
                            saved.getPet() != null ? saved.getPet().getId() : null,
                            saved.getPet() != null ? saved.getPet().getName() : null,
                            saved.getPet() != null ? saved.getPet().getSpecie() : null,
                            saved.getPet() != null ? saved.getPet().getBreed() : null,
                            saved.getPet() != null ? saved.getPet().getOwnerId() : null
                    ),
                    saved.getVeterinarian(),
                    saved.getReason(),
                    saved.getDiagnosis(),
                    saved.getCost(),
                    saved.getStatus()
            );
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable String id) {
        if (appointmentService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointmentService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
