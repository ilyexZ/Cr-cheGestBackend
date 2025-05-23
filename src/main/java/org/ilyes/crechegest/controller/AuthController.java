package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.Role;
import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.service.RoleService;
import org.ilyes.crechegest.service.UserService;
import org.ilyes.crechegest.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
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

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> userOpt = userService.findByUsername(loginRequest.getUsername());

        if (userOpt.isPresent()) {
            User user = userOpt.get();

            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
                if (!user.isActive()) {
                    return ResponseEntity
                            .badRequest()
                            .body(Map.of("success", false, "message", "Account is not activated"));
                }

                String token = jwtUtil.generateToken(user);

                return ResponseEntity.ok(Map.of(
                        "success", true,
                        "token", token,
                        "user", Map.of(
                                "id", user.getId(),
                                "username", user.getUsername(),
                                "fullName", user.getFullName(),
                                "email", user.getEmail(),
                                "role", user.getRole().getName(),
                                "active", user.isActive()
                        )
                ));
            }
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of("success", false, "message", "Invalid credentials"));
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> register(@RequestBody RegisterRequest req) {
        // Check duplicates
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

        // Fetch default role
        Optional<Role> parentRoleOpt = roleService.findByName("PARENT");
        if (parentRoleOpt.isEmpty()) {
            return ResponseEntity
                    .internalServerError()
                    .body(Map.of("message", "Default role not configured"));
        }

        // Build user
        User newUser = new User();
        newUser.setUsername(req.getUsername());
        newUser.setPassword(req.getPassword());
        newUser.setFullName(req.getFullName());
        newUser.setEmail(req.getEmail());
        newUser.setPhone(req.getPhone());
        newUser.setRole(parentRoleOpt.get());
        newUser.setActive(false);

        User saved = userService.save(newUser);

        return ResponseEntity.ok(Map.of(
                "message", "Registration successful. Awaiting admin activation.",
                "userId", saved.getId()
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, Object>> refreshToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                Optional<User> userOpt = userService.findByUsername(username);

                if (userOpt.isPresent()) {
                    User user = userOpt.get();
                    if (user.isActive()) {
                        String newToken = jwtUtil.generateToken(user);
                        return ResponseEntity.ok(Map.of(
                                "success", true,
                                "token", newToken
                        ));
                    }
                }
            }
        }

        return ResponseEntity
                .badRequest()
                .body(Map.of("success", false, "message", "Invalid or expired token"));
    }

    @PostMapping("/validate")
    public ResponseEntity<Map<String, Object>> validateToken(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            if (jwtUtil.validateToken(token)) {
                String username = jwtUtil.extractUsername(token);
                Long userId = jwtUtil.extractUserId(token);
                String role = jwtUtil.extractRole(token);
                Boolean active = jwtUtil.extractActive(token);

                return ResponseEntity.ok(Map.of(
                        "valid", true,
                        "user", Map.of(
                                "id", userId,
                                "username", username,
                                "role", role,
                                "active", active
                        )
                ));
            }
        }

        return ResponseEntity.ok(Map.of("valid", false));
    }

    @GetMapping("/profile")
    public ResponseEntity<Map<String, Object>> getProfile(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        String role = (String) request.getAttribute("userRole");
        String fullName = (String) request.getAttribute("fullName");
        String email = (String) request.getAttribute("email");

        if (userId != null) {
            Optional<User> userOpt = userService.findById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return ResponseEntity.ok(Map.of(
                        "id", user.getId(),
                        "username", user.getUsername(),
                        "fullName", user.getFullName(),
                        "email", user.getEmail(),
                        "phone", user.getPhone(),
                        "role", user.getRole().getName(),
                        "active", user.isActive()
                ));
            }
        }

        return ResponseEntity.badRequest().body(Map.of("message", "User not found"));
    }

    // DTOs
    public static class LoginRequest {
        private String username;
        private String password;

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
