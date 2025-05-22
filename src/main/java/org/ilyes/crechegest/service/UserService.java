package org.ilyes.crechegest.service;


import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.model.Role;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User save(User user);
    Optional<User> findById(Long id);
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findAll();
    List<User> findByRole(Role role);
    List<User> findActiveUsers();
    void delete(Long id);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User updateUser(User user);
    void deactivateUser(Long id);
    void activateUser(Long id);
}
