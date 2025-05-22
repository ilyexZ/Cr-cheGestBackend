package org.ilyes.crechegest.service;

import org.ilyes.crechegest.model.Parent;

import java.util.List;
import java.util.Optional;

public interface ParentService {
    Parent save(Parent parent);
    Optional<Parent> findById(Long id);
    List<Parent> findAll();
    Optional<Parent> findByEmail(String email);
    Optional<Parent> findByPhone(String phone);
    Parent updateParent(Parent parent);
    void delete(Long id);
}