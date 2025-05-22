package org.ilyes.crechegest.service;

import org.ilyes.crechegest.model.MedicalInfo;

import java.util.List;
import java.util.Optional;

public interface MedicalInfoService {
    MedicalInfo save(MedicalInfo medicalInfo);
    Optional<MedicalInfo> findById(Long id);
    Optional<MedicalInfo> findByChildId(Long childId);
    List<MedicalInfo> findAll();
    MedicalInfo updateMedicalInfo(MedicalInfo medicalInfo);
    void delete(Long id);
    List<MedicalInfo> findByAllergies(String allergy);
    List<MedicalInfo> findByMedicalCondition(String condition);
}
