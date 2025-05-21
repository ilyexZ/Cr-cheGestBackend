package org.ilyes.crechegest.repository;


import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
    List<User> findByRole(Role role);
    List<User> findByActiveTrue();
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
