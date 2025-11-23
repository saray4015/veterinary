package co.edu.umanizales.veterinary.controller;

import co.edu.umanizales.veterinary.model.MedicalRecord;
import co.edu.umanizales.veterinary.model.Medication;
import co.edu.umanizales.veterinary.model.Treatment;
import co.edu.umanizales.veterinary.service.MedicalRecordService;
import co.edu.umanizales.veterinary.dto.MedicalRecordResponse;
import co.edu.umanizales.veterinary.dto.PetWithoutBirthDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {
    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @GetMapping(produces = "application/json")
    public ResponseEntity<List<MedicalRecordResponse>> getAllMedicalRecords() {
        List<MedicalRecord> list = medicalRecordService.findAll();
        List<MedicalRecordResponse> resp = list.stream()
                .map(saved -> new MedicalRecordResponse(
                        saved.getId(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getDate(),
                        saved.getDiagnosis(),
                        saved.getTreatmentNotes(),
                        saved.getTreatments(),
                        saved.getMedications()
                ))
                .toList();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicalRecordById(@PathVariable String id) {
        String tmp = (id != null) ? id.trim() : null;
        final String normalizedId = (tmp != null && tmp.length() >= 2 &&
                ((tmp.startsWith("\"") && tmp.endsWith("\"")) ||
                 (tmp.startsWith("'") && tmp.endsWith("'"))))
                ? tmp.substring(1, tmp.length() - 1).trim()
                : tmp;

        return medicalRecordService.findById(id)
                .or(() -> medicalRecordService.findById(normalizedId))
                .<ResponseEntity<?>>map(saved -> new ResponseEntity<>(new MedicalRecordResponse(
                        saved.getId(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getDate(),
                        saved.getDiagnosis(),
                        saved.getTreatmentNotes(),
                        saved.getTreatments(),
                        saved.getMedications()
                ), HttpStatus.OK))
                .orElseGet(() -> {
                    java.util.List<String> ids = medicalRecordService.findAll().stream()
                            .map(MedicalRecord::getId)
                            .filter(java.util.Objects::nonNull)
                            .toList();
                    String msg = "Medical record not found: '" + id + "'. Available IDs: " + ids;
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(msg);
                });
    }

    @GetMapping(value = "/pet/{petId}", produces = "application/json")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalRecordsByPet(@PathVariable String petId) {
        List<MedicalRecord> list = medicalRecordService.findByPetId(petId);
        List<MedicalRecordResponse> resp = list.stream()
                .map(saved -> new MedicalRecordResponse(
                        saved.getId(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getDate(),
                        saved.getDiagnosis(),
                        saved.getTreatmentNotes(),
                        saved.getTreatments(),
                        saved.getMedications()
                ))
                .toList();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @GetMapping(value = "/veterinarian/{veterinarianId}", produces = "application/json")
    public ResponseEntity<List<MedicalRecordResponse>> getMedicalRecordsByVeterinarian(@PathVariable String veterinarianId) {
        List<MedicalRecord> list = medicalRecordService.findByVeterinarianId(veterinarianId);
        List<MedicalRecordResponse> resp = list.stream()
                .map(saved -> new MedicalRecordResponse(
                        saved.getId(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getDate(),
                        saved.getDiagnosis(),
                        saved.getTreatmentNotes(),
                        saved.getTreatments(),
                        saved.getMedications()
                ))
                .toList();
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try {
            MedicalRecord saved = medicalRecordService.save(medicalRecord);
            MedicalRecordResponse resp = new MedicalRecordResponse(
                    saved.getId(),
                    new PetWithoutBirthDate(
                            saved.getPet() != null ? saved.getPet().getId() : null,
                            saved.getPet() != null ? saved.getPet().getName() : null,
                            saved.getPet() != null ? saved.getPet().getSpecie() : null,
                            saved.getPet() != null ? saved.getPet().getBreed() : null,
                            saved.getPet() != null ? saved.getPet().getOwnerId() : null
                    ),
                    saved.getVeterinarian(),
                    saved.getDate(),
                    saved.getDiagnosis(),
                    saved.getTreatmentNotes(),
                    saved.getTreatments(),
                    saved.getMedications()
            );
            return new ResponseEntity<>(resp, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping("/{id}/treatments")
    public ResponseEntity<?> addTreatment(@PathVariable String id, @RequestBody Treatment treatment) {
        String tmp = (id != null) ? id.trim() : null;
        final String normalizedId = (tmp != null && tmp.length() >= 2 &&
                ((tmp.startsWith("\"") && tmp.endsWith("\"")) ||
                 (tmp.startsWith("'") && tmp.endsWith("'"))))
                ? tmp.substring(1, tmp.length() - 1).trim()
                : tmp;
        // Upsert: crear si no existe
        String effectiveId;
        if (medicalRecordService.findById(id).isPresent()) {
            effectiveId = id;
        } else if (medicalRecordService.findById(normalizedId).isPresent()) {
            effectiveId = normalizedId;
        } else {
            MedicalRecord mr = new MedicalRecord();
            mr.setId(normalizedId != null ? normalizedId : id);
            mr.setDate(java.time.LocalDate.now());
            medicalRecordService.save(mr);
            effectiveId = mr.getId();
        }
        medicalRecordService.addTreatment(effectiveId, treatment);
        return medicalRecordService.findById(effectiveId)
                .<ResponseEntity<?>>map(saved -> new ResponseEntity<>(new MedicalRecordResponse(
                        saved.getId(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getDate(),
                        saved.getDiagnosis(),
                        saved.getTreatmentNotes(),
                        saved.getTreatments(),
                        saved.getMedications()
                ), HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @PostMapping("/{id}/medications")
    public ResponseEntity<?> addMedication(@PathVariable String id, @RequestBody Medication medication) {
        String tmp = (id != null) ? id.trim() : null;
        final String normalizedId = (tmp != null && tmp.length() >= 2 &&
                ((tmp.startsWith("\"") && tmp.endsWith("\"")) ||
                 (tmp.startsWith("'") && tmp.endsWith("'"))))
                ? tmp.substring(1, tmp.length() - 1).trim()
                : tmp;
        // Upsert: crear si no existe
        String effectiveId;
        if (medicalRecordService.findById(id).isPresent()) {
            effectiveId = id;
        } else if (medicalRecordService.findById(normalizedId).isPresent()) {
            effectiveId = normalizedId;
        } else {
            MedicalRecord mr = new MedicalRecord();
            mr.setId(normalizedId != null ? normalizedId : id);
            mr.setDate(java.time.LocalDate.now());
            medicalRecordService.save(mr);
            effectiveId = mr.getId();
        }
        medicalRecordService.addMedication(effectiveId, medication);
        return medicalRecordService.findById(effectiveId)
                .<ResponseEntity<?>>map(saved -> new ResponseEntity<>(new MedicalRecordResponse(
                        saved.getId(),
                        new PetWithoutBirthDate(
                                saved.getPet() != null ? saved.getPet().getId() : null,
                                saved.getPet() != null ? saved.getPet().getName() : null,
                                saved.getPet() != null ? saved.getPet().getSpecie() : null,
                                saved.getPet() != null ? saved.getPet().getBreed() : null,
                                saved.getPet() != null ? saved.getPet().getOwnerId() : null
                        ),
                        saved.getVeterinarian(),
                        saved.getDate(),
                        saved.getDiagnosis(),
                        saved.getTreatmentNotes(),
                        saved.getTreatments(),
                        saved.getMedications()
                ), HttpStatus.CREATED))
                .orElse(new ResponseEntity<>(HttpStatus.CREATED));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedicalRecord(@PathVariable String id, @RequestBody MedicalRecord medicalRecord) {
        if (medicalRecordService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicalRecord.setId(id);
        try {
            MedicalRecord saved = medicalRecordService.save(medicalRecord);
            MedicalRecordResponse resp = new MedicalRecordResponse(
                    saved.getId(),
                    new PetWithoutBirthDate(
                            saved.getPet() != null ? saved.getPet().getId() : null,
                            saved.getPet() != null ? saved.getPet().getName() : null,
                            saved.getPet() != null ? saved.getPet().getSpecie() : null,
                            saved.getPet() != null ? saved.getPet().getBreed() : null,
                            saved.getPet() != null ? saved.getPet().getOwnerId() : null
                    ),
                    saved.getVeterinarian(),
                    saved.getDate(),
                    saved.getDiagnosis(),
                    saved.getTreatmentNotes(),
                    saved.getTreatments(),
                    saved.getMedications()
            );
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable String id) {
        if (medicalRecordService.findById(id).isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        medicalRecordService.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
