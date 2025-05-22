package org.ilyes.crechegest.service;

import org.ilyes.crechegest.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {
    Role save(Role role);
    Optional<Role> findById(Long id);
    Optional<Role> findByName(String name);
    List<Role> findAll();
    Role updateRole(Role role);
    void delete(Long id);
    boolean existsByName(String name);
}