package org.ilyes.crechegest.service;

import org.ilyes.crechegest.model.Child;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ChildService {
    Child save(Child child);
    Optional<Child> findById(Long id);
    List<Child> findAll();
    List<Child> findByParentId(Long parentId);
    List<Child> findActiveChildren();
    List<Child> findByBirthDateBetween(LocalDate startDate, LocalDate endDate);
    Child updateChild(Child child);
    void delete(Long id);
    void deactivateChild(Long id);
    void activateChild(Long id);
}