package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.Parent;
import org.ilyes.crechegest.model.Role;
import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.service.ParentService;
import org.ilyes.crechegest.service.impl.RoleServiceImpl;
import org.ilyes.crechegest.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parents")
@CrossOrigin(origins = "*")
public class ParentController {

    @Autowired
    private ParentService parentService;
    @Autowired
    private UserServiceImpl userServiceImpl;
    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @GetMapping
    public List<Parent> getAllParents() {
        return parentService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Parent> getParentById(@PathVariable Long id) {
        Optional<Parent> parent = parentService.findById(id);
        return parent.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createParent(@RequestBody CreateParentRequest request) {
        // 1. Create User first
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(request.getPassword());
        newUser.setFullName(request.getFirstName() + " " + request.getLastName());
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());

        // Assign PARENT role (fetch from DB)
        Optional<Role> parentRole = roleServiceImpl.findByName("PARENT");
        if (parentRole.isEmpty()) {
            return ResponseEntity.badRequest().body("PARENT role not found");
        }
        newUser.setRole(parentRole.get());
        User savedUser = userServiceImpl.save(newUser);

        // 2. Create Parent and link to the User
        Parent parent = new Parent();
        parent.setFirstName(request.getFirstName());
        parent.setLastName(request.getLastName());
        parent.setEmail(request.getEmail());
        parent.setPhone(request.getPhone());
        parent.setAddress(request.getAddress());
        parent.setEmergencyContact(request.isEmergencyContact());
        parent.setUser(savedUser); // Link to the new User

        Parent savedParent = parentService.save(parent);
        return ResponseEntity.ok(savedParent);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Parent> updateParent(@PathVariable Long id, @RequestBody Parent parent) {
        parent.setId(id);
        Parent updated = parentService.updateParent(parent);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParent(@PathVariable Long id) {
        parentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Parent> getParentByEmail(@PathVariable String email) {
        Optional<Parent> parent = parentService.findByEmail(email);
        return parent.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }
    public static class CreateParentRequest {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private boolean emergencyContact;
        private String username;
        private String password;

        // Getters
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public boolean isEmergencyContact() { return emergencyContact; }
        public String getUsername() { return username; }
        public String getPassword() { return password; }

        // Setters
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setEmail(String email) { this.email = email; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setAddress(String address) { this.address = address; }
        public void setEmergencyContact(boolean emergencyContact) { this.emergencyContact = emergencyContact; }
        public void setUsername(String username) { this.username = username; }
        public void setPassword(String password) { this.password = password; }
    }
}
