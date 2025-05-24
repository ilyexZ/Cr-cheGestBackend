package org.ilyes.crechegest.controller;

import org.apache.commons.lang3.RandomStringUtils;
import org.ilyes.crechegest.model.Parent;
import org.ilyes.crechegest.model.ParentRequest;
import org.ilyes.crechegest.model.Role;
import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.service.EmailService;
import org.ilyes.crechegest.service.ParentRequestService;
import org.ilyes.crechegest.service.ParentService;
import org.ilyes.crechegest.service.impl.RoleServiceImpl;
import org.ilyes.crechegest.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/parent-requests")
@CrossOrigin(origins = "*")
public class ParentRequestController {

    @Autowired
    private ParentRequestService parentRequestService;

    @Autowired
    private ParentService parentService;

    @Autowired
    private UserServiceImpl userServiceImpl;

    @Autowired
    private RoleServiceImpl roleServiceImpl;

    @Autowired
    private EmailService emailService;

    // Get all parent requests
    @GetMapping
    public List<ParentRequest> getAllParentRequests() {
        return parentRequestService.findAll();
    }

    // Get parent request by ID
    @GetMapping("/{id}")
    public ResponseEntity<ParentRequest> getParentRequestById(@PathVariable Long id) {
        Optional<ParentRequest> request = parentRequestService.findById(id);
        return request.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    // Get parent requests by status
    @GetMapping("/status/{status}")
    public List<ParentRequest> getParentRequestsByStatus(@PathVariable String status) {
        try {
            ParentRequest.RequestStatus requestStatus = ParentRequest.RequestStatus.valueOf(status.toUpperCase());
            return parentRequestService.findByStatus(requestStatus);
        } catch (IllegalArgumentException e) {
            return List.of(); // Return empty list for invalid status
        }
    }

    // Create a new parent request
    @PostMapping
    public ResponseEntity<?> createParentRequest(@RequestBody CreateParentRequestDto request) {
        try {
            // Check if email already exists in requests
            if (parentRequestService.existsByEmail(request.getEmail())) {
                return ResponseEntity.badRequest().body("A request with this email already exists");
            }

            // Create new parent request
            ParentRequest parentRequest = new ParentRequest(
                    request.getFirstName(),
                    request.getLastName(),
                    request.getEmail(),
                    request.getPhone(),
                    request.getAddress(),
                    request.isEmergencyContact()
            );

            ParentRequest savedRequest = parentRequestService.save(parentRequest);
            return ResponseEntity.ok(savedRequest);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error creating parent request: " + e.getMessage());
        }
    }

    // Process a parent request (approve/reject)
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateRequestStatus(@PathVariable Long id, @RequestBody UpdateStatusRequest request) {
        try {
            ParentRequest.RequestStatus status = ParentRequest.RequestStatus.valueOf(request.getStatus().toUpperCase());
            ParentRequest updatedRequest = parentRequestService.updateStatus(id, status, request.getNotes());

            if (updatedRequest == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updatedRequest);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Invalid status: " + request.getStatus());
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error updating request status: " + e.getMessage());
        }
    }

    // Process approved request to create actual parent
    @PostMapping("/{id}/process")
    public ResponseEntity<?> processParentRequest(@PathVariable Long id) {
        try {
            Optional<ParentRequest> optionalRequest = parentRequestService.findById(id);
            if (optionalRequest.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            ParentRequest request = optionalRequest.get();

            // Check if request is approved
            if (request.getStatus() != ParentRequest.RequestStatus.APPROVED) {
                return ResponseEntity.badRequest().body("Request must be approved before processing");
            }

            // Generate random password
            String generatedPassword = RandomStringUtils.randomAlphanumeric(8);

            // Create User
            User newUser = new User();
            newUser.setUsername(request.getEmail()); // Use email as username
            newUser.setPassword(generatedPassword);
            newUser.setFullName(request.getFirstName() + " " + request.getLastName());
            newUser.setEmail(request.getEmail());
            newUser.setPhone(request.getPhone());

            // Assign PARENT role
            Optional<Role> parentRole = roleServiceImpl.findByName("PARENT");
            if (parentRole.isEmpty()) {
                return ResponseEntity.badRequest().body("PARENT role not found");
            }
            newUser.setRole(parentRole.get());

            User savedUser = userServiceImpl.save(newUser);

            // Create Parent
            Parent parent = new Parent();
            parent.setFirstName(request.getFirstName());
            parent.setLastName(request.getLastName());
            parent.setEmail(request.getEmail());
            parent.setPhone(request.getPhone());
            parent.setAddress(request.getAddress());
            parent.setEmergencyContact(request.isEmergencyContact());
            parent.setUser(savedUser);

            Parent savedParent = parentService.save(parent);

            // Update request status to PROCESSED
            parentRequestService.updateStatus(id, ParentRequest.RequestStatus.PROCESSED, "Successfully created parent account");

            // Send email with credentials
            emailService.sendCredentialsEmail(request.getEmail(), generatedPassword);

            return ResponseEntity.ok(savedParent);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error processing parent request: " + e.getMessage());
        }
    }

    // Delete parent request
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParentRequest(@PathVariable Long id) {
        parentRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // DTOs
    public static class CreateParentRequestDto {
        private String firstName;
        private String lastName;
        private String email;
        private String phone;
        private String address;
        private boolean emergencyContact;

        // Getters
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public String getPhone() { return phone; }
        public String getAddress() { return address; }
        public boolean isEmergencyContact() { return emergencyContact; }

        // Setters
        public void setFirstName(String firstName) { this.firstName = firstName; }
        public void setLastName(String lastName) { this.lastName = lastName; }
        public void setEmail(String email) { this.email = email; }
        public void setPhone(String phone) { this.phone = phone; }
        public void setAddress(String address) { this.address = address; }
        public void setEmergencyContact(boolean emergencyContact) { this.emergencyContact = emergencyContact; }
    }

    public static class UpdateStatusRequest {
        private String status;
        private String notes;

        // Getters
        public String getStatus() { return status; }
        public String getNotes() { return notes; }

        // Setters
        public void setStatus(String status) { this.status = status; }
        public void setNotes(String notes) { this.notes = notes; }
    }
}