package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.Role;
import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.service.RoleService;
import org.ilyes.crechegest.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        // Changed from findByUsername to findByEmail
        Optional<User> userOpt = userService.findByEmail(loginRequest.getEmail());
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "user", user,
                        "role", user.getRole().getName()
                ));
            }
        }
        return ResponseEntity
                .badRequest()
                .body(Map.of("success", false, "message", "Invalid credentials"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest req) {
        // 1) Check duplicates
        if (userService.existsByUsername(req.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Username already taken"));
        }
        if (userService.existsByEmail(req.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Email already in use"));
        }

        // 2) Fetch role by ID or default to PARENT
        Role role;
        if (req.getRoleId() != null) {
            Optional<Role> roleOpt = roleService.findById(req.getRoleId());
            if (roleOpt.isEmpty()) {
                return ResponseEntity
                        .badRequest()
                        .body(Map.of("message", "Invalid role ID"));
            }
            role = roleOpt.get();
        } else {
            // Default to PARENT role if no role specified
            Optional<Role> parentRoleOpt = roleService.findByName("PARENT");
            if (parentRoleOpt.isEmpty()) {
                return ResponseEntity
                        .internalServerError()
                        .body(Map.of("message", "Default role not configured"));
            }
            role = parentRoleOpt.get();
        }

        // 3) Build user
        User newUser = new User();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(req.getPassword());       // save() will encode
        newUser.setFullName(req.getFullName());
        newUser.setEmail(req.getEmail());
        newUser.setPhone(req.getPhone());
        newUser.setRole(role);
        newUser.setActive(req.getActive() != null ? req.getActive() : false); // inactive by default

        // 4) Persist
        User saved = userService.save(newUser);
        return ResponseEntity.ok(Map.of(
                "message", "Registration successful",
                "userId", saved.getId(),
                "role", saved.getRole().getName(),
                "active", saved.isActive()
        ));
    }

    // --- DTOs for requests ---
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String fullName;
        private String email;
        private String phone;
        private Long roleId;
        private Boolean active;

        // getters & setters...
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        public String getFullName() { return fullName; }
        public void setFullName(String fullName) { this.fullName = fullName; }
        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }
        public Long getRoleId() { return roleId; }
        public void setRoleId(Long roleId) { this.roleId = roleId; }
        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }
}
