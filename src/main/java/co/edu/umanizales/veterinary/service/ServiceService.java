package co.edu.umanizales.veterinary.service;

import co.edu.umanizales.veterinary.model.VeterinaryService;
import co.edu.umanizales.veterinary.model.ServiceType;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ServiceService extends BaseServiceImpl<VeterinaryService> {
    
    public ServiceService() {

        super("services.csv");
    }

    @Override
    protected Class<VeterinaryService> getEntityClass() {
        return VeterinaryService.class;
    }

    public List<VeterinaryService> findByType(ServiceType type) {
        return entities.stream()
                .filter(service -> type == service.getType())
                .toList();
    }

    public List<VeterinaryService> searchByName(String name) {
        String searchTerm = name.toLowerCase();
        return entities.stream()
                .filter(service -> service.getName().toLowerCase().contains(searchTerm))
                .toList();
    }
}
