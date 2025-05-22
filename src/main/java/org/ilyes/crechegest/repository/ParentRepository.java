package org.ilyes.crechegest.repository;


import org.ilyes.crechegest.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParentRepository extends JpaRepository<Parent, Long> {
    List<Parent> findByLastName(String lastName);

    Optional<Parent> findByEmail(String email);
    Optional<Parent> findByPhone(String phone);

    @Query("SELECT p FROM Parent p WHERE p.firstName LIKE %:name% OR p.lastName LIKE %:name%")
    List<Parent> findByName(@Param("name") String name);

    @Query("SELECT p FROM Parent p JOIN p.children c WHERE c.id = :childId")
    List<Parent> findByChildId(@Param("childId") Long childId);

    List<Parent> findByEmergencyContactTrue();

    @Query("SELECT p FROM Parent p JOIN p.user u WHERE u.id = :userId")
    Optional<Parent> findByUserId(@Param("userId") Long userId);
}
