package org.ilyes.crechegest.service.impl;

import org.ilyes.crechegest.model.MedicalInfo;
import org.ilyes.crechegest.repository.MedicalInfoRepository;
import org.ilyes.crechegest.service.MedicalInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class MedicalInfoServiceImpl implements MedicalInfoService {

    private final MedicalInfoRepository medicalInfoRepository;

    @Autowired
    public MedicalInfoServiceImpl(MedicalInfoRepository medicalInfoRepository) {
        this.medicalInfoRepository = medicalInfoRepository;
    }

    @Override
    public MedicalInfo save(MedicalInfo medicalInfo) {
        return medicalInfoRepository.save(medicalInfo);
    }

    @Override
    public Optional<MedicalInfo> findById(Long id) {
        return medicalInfoRepository.findById(id);
    }

    @Override
    public Optional<MedicalInfo> findByChildId(Long childId) {
        return medicalInfoRepository.findByChildId(childId);
    }

    @Override
    public List<MedicalInfo> findAll() {
        return medicalInfoRepository.findAll();
    }

    @Override
    public MedicalInfo updateMedicalInfo(MedicalInfo medicalInfo) {
        Optional<MedicalInfo> existing = medicalInfoRepository.findById(medicalInfo.getId());
        if (existing.isPresent()) {
            MedicalInfo updated = existing.get();
            updated.setBloodType(medicalInfo.getBloodType());
            updated.setAllergies(medicalInfo.getAllergies());
            updated.setMedicalConditions(medicalInfo.getMedicalConditions());
            updated.setDoctorName(medicalInfo.getDoctorName());
            updated.setDoctorPhone(medicalInfo.getDoctorPhone());
            updated.setInsuranceInfo(medicalInfo.getInsuranceInfo());
            updated.setEmergencyProtocol(medicalInfo.getEmergencyProtocol());
            updated.setVaccinations(medicalInfo.getVaccinations());

            return medicalInfoRepository.save(updated);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        medicalInfoRepository.deleteById(id);
    }

    @Override
    public List<MedicalInfo> findByAllergies(String allergy) {
        return medicalInfoRepository.findByAllergiesContaining(allergy);
    }

    @Override
    public List<MedicalInfo> findByMedicalCondition(String condition) {
        return medicalInfoRepository.findByMedicalConditionsContaining(condition);
    }
}