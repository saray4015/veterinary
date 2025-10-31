package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.Appointment;
import co.edu.umanizales.veterinary.model.AppointmentStatus;
import co.edu.umanizales.veterinary.service.AppointmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/appointments")
public class AppointmentController {
    private final AppointmentService appointmentService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @GetMapping
    public ResponseEntity<List<Appointment>> getAllAppointments() {
        return new ResponseEntity<>(appointmentService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Appointment> getAppointmentById(@PathVariable String id) {
        return appointmentService.findById(id)
                .map(appointment -> new ResponseEntity<>(appointment, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByPet(@PathVariable String petId) {
        return new ResponseEntity<>(appointmentService.findByPetId(petId), HttpStatus.OK);
    }

    @GetMapping("/veterinarian/{veterinarianId}")
    public ResponseEntity<List<Appointment>> getAppointmentsByVeterinarian(@PathVariable String veterinarianId) {
        return new ResponseEntity<>(appointmentService.findByVeterinarianId(veterinarianId), HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Appointment>> getAppointmentsByStatus(@PathVariable AppointmentStatus status) {
        return new ResponseEntity<>(appointmentService.findByStatus(status), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody Appointment appointment) {
        try {
            return new ResponseEntity<>(appointmentService.save(appointment), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Appointment> updateAppointment(@PathVariable String id, @RequestBody Appointment appointment) {
        if (appointmentService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        appointment.setId(id);
        try {
            return new ResponseEntity<>(appointmentService.save(appointment), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
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
