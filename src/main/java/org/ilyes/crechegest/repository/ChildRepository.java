package org.ilyes.crechegest.repository;


import org.ilyes.crechegest.model.Child;
import org.ilyes.crechegest.model.Parent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ChildRepository extends JpaRepository<Child, Long> {
    List<Child> findByLastName(String lastName);
    List<Child> findByActiveTrue();

    @Query("SELECT c FROM Child c WHERE c.firstName LIKE %:name% OR c.lastName LIKE %:name%")
    List<Child> findByName(@Param("name") String name);

    List<Child> findByBirthDateBetween(LocalDate start, LocalDate end);

    List<Child> findByParentsContaining(Parent parent);

    @Query("SELECT c FROM Child c JOIN c.parents p WHERE p.id = :parentId")
    List<Child> findByParentId(@Param("parentId") Long parentId);

    @Query("SELECT DISTINCT c FROM Child c JOIN c.attendances a WHERE a.date = :date AND a.isPresent = true")
    List<Child> findPresentChildrenByDate(@Param("date") LocalDate date);
}
