package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.Child;
import org.ilyes.crechegest.model.Parent;
import org.ilyes.crechegest.service.ChildService;
import org.ilyes.crechegest.service.ParentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/children")
@CrossOrigin(origins = "*")
public class ChildController {
    @Autowired
    private ChildService childService;

    @Autowired
    private ParentService parentService;

    @GetMapping
    public List<Child> getAllChildren() {
        return childService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id) {
        Optional<Child> child = childService.findById(id);
        return child.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Child> createChild(@RequestBody CreateChildRequest request) {
        // Validate parent exists
        Optional<Parent> parentOpt = parentService.findById(request.getParentId());
        if (parentOpt.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        Child child = new Child();
        child.setFirstName(request.getFirstName());
        child.setLastName(request.getLastName());
        child.setBirthDate(request.getBirthDate());
        child.setGender(request.getGender());
        child.setNotes(request.getNotes());
        child.setParent(parentOpt.get());
        child.setRegistrationDate(LocalDate.now());
        child.setActive(true);

        Child saved = childService.save(child);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody UpdateChildRequest request) {
        Optional<Child> childOpt = childService.findById(id);
        if (childOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        Child child = childOpt.get();
        child.setFirstName(request.getFirstName());
        child.setLastName(request.getLastName());
        child.setBirthDate(request.getBirthDate());
        child.setGender(request.getGender());
        child.setNotes(request.getNotes());
        child.setActive(request.getActive());

        // Update parent if provided
        if (request.getParentId() != null) {
            Optional<Parent> parentOpt = parentService.findById(request.getParentId());
            if (parentOpt.isPresent()) {
                child.setParent(parentOpt.get());
            }
        }

        Child updated = childService.save(child);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id) {
        childService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/active")
    public List<Child> getActiveChildren() {
        return childService.findActiveChildren();
    }

    @GetMapping("/parent/{parentId}")
    public List<Child> getChildrenByParent(@PathVariable Long parentId) {
        return childService.findByParentId(parentId);
    }

    @GetMapping("/age-range")
    public List<Child> getChildrenByAgeRange(@RequestParam LocalDate startDate, @RequestParam LocalDate endDate) {
        return childService.findByBirthDateBetween(startDate, endDate);
    }

    // --- DTOs for requests ---
    public static class CreateChildRequest {
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String gender;
        private String notes;
        private Long parentId;

        // Getters and Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }
    }

    public static class UpdateChildRequest {
        private String firstName;
        private String lastName;
        private LocalDate birthDate;
        private String gender;
        private String notes;
        private Long parentId;
        private Boolean active;

        // Getters and Setters
        public String getFirstName() { return firstName; }
        public void setFirstName(String firstName) { this.firstName = firstName; }

        public String getLastName() { return lastName; }
        public void setLastName(String lastName) { this.lastName = lastName; }

        public LocalDate getBirthDate() { return birthDate; }
        public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }

        public String getNotes() { return notes; }
        public void setNotes(String notes) { this.notes = notes; }

        public Long getParentId() { return parentId; }
        public void setParentId(Long parentId) { this.parentId = parentId; }

        public Boolean getActive() { return active; }
        public void setActive(Boolean active) { this.active = active; }
    }
}