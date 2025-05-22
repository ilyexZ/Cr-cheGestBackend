package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.MedicalInfo;
import org.ilyes.crechegest.service.MedicalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-info")
@CrossOrigin(origins = "*")
public class MedicalInfoController {

    @Autowired
    private MedicalInfoService medicalInfoService;

    @GetMapping
    public List<MedicalInfo> getAllMedicalInfo() {
        return medicalInfoService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalInfo> getMedicalInfoById(@PathVariable Long id) {
        Optional<MedicalInfo> medicalInfo = medicalInfoService.findById(id);
        return medicalInfo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/child/{childId}")
    public ResponseEntity<MedicalInfo> getMedicalInfoByChildId(@PathVariable Long childId) {
        Optional<MedicalInfo> medicalInfo = medicalInfoService.findByChildId(childId);
        return medicalInfo.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MedicalInfo createMedicalInfo(@RequestBody MedicalInfo medicalInfo) {
        return medicalInfoService.save(medicalInfo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MedicalInfo> updateMedicalInfo(@PathVariable Long id, @RequestBody MedicalInfo medicalInfo) {
        medicalInfo.setId(id);
        MedicalInfo updated = medicalInfoService.updateMedicalInfo(medicalInfo);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalInfo(@PathVariable Long id) {
        medicalInfoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}