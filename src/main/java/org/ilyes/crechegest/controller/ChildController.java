package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.Child;
import org.ilyes.crechegest.service.ChildService;
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
    public Child createChild(@RequestBody Child child) {
        return childService.save(child);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody Child child) {
        child.setId(id);
        Child updated = childService.updateChild(child);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
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
}