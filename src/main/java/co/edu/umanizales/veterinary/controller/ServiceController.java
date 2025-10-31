package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.VeterinaryService;
import co.edu.umanizales.veterinary.model.ServiceType;
import co.edu.umanizales.veterinary.service.ServiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class ServiceController {
    private final ServiceService serviceService;

    @Autowired
    public ServiceController(ServiceService serviceService) {
        this.serviceService = serviceService;
    }

    @GetMapping
    public ResponseEntity<List<VeterinaryService>> getAllServices() {
        return new ResponseEntity<>(serviceService.findAll(), HttpStatus.OK);
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<VeterinaryService>> getServicesByType(@PathVariable ServiceType type) {
        return new ResponseEntity<>(serviceService.findByType(type), HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<VeterinaryService>> searchServices(@RequestParam String name) {
        return new ResponseEntity<>(serviceService.searchByName(name), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VeterinaryService> getServiceById(@PathVariable String id) {
        return serviceService.findById(id)
                .map(service -> new ResponseEntity<>(service, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<VeterinaryService> createService(@RequestBody VeterinaryService service) {
        return new ResponseEntity<>(serviceService.save(service), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VeterinaryService> updateService(
            @PathVariable String id, 
            @RequestBody VeterinaryService service) {
        if (serviceService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        service.setId(id);
        return new ResponseEntity<>(serviceService.save(service), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable String id) {
        if (serviceService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        serviceService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
