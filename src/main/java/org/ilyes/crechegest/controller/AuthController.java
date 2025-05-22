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
        Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());

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

        // 2) Fetch default role
        Optional<Role> parentRoleOpt = roleService.findByName("PARENT");
        if (parentRoleOpt.isEmpty()) {
            return ResponseEntity
                    .internalServerError()
                    .body(Map.of("message", "Default role not configured"));
        }

        // 3) Build user
        User newUser = new User();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(req.getPassword());       // save() will encode
        newUser.setFullName(req.getFullName());
        newUser.setEmail(req.getEmail());
        newUser.setPhone(req.getPhone());
        newUser.setRole(parentRoleOpt.get());
        newUser.setActive(false);                     // inactive until admin activates

        // 4) Persist
        User saved = userService.save(newUser);

        return ResponseEntity.ok(Map.of(
                "message", "Registration successful. Awaiting admin activation.",
                "userId", saved.getId()
        ));
    }

    // --- DTOs for requests ---

    public static class LoginRequest {
        private String username;
        private String password;
        // getters & setters...
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }

    public static class RegisterRequest {
        private String username;
        private String password;
        private String fullName;
        private String email;
        private String phone;
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
    }
}
