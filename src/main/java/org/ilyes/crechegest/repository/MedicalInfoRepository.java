package org.ilyes.crechegest.repository;

import org.ilyes.crechegest.model.MedicalInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicalInfoRepository extends JpaRepository<MedicalInfo, Long> {
    Optional<MedicalInfo> findByChildId(Long childId);

    @Query("SELECT m FROM MedicalInfo m WHERE m.allergies LIKE %:allergy%")
    List<MedicalInfo> findByAllergiesContaining(@Param("allergy") String allergy);

    @Query("SELECT m FROM MedicalInfo m WHERE m.medicalConditions LIKE %:condition%")
    List<MedicalInfo> findByMedicalConditionsContaining(@Param("condition") String condition);
}