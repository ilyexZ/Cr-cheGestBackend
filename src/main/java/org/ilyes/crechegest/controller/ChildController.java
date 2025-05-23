package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.Child;
import org.ilyes.crechegest.service.ChildService;
import org.ilyes.crechegest.util.AuthUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
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
    public ResponseEntity<List<Child>> getAllChildren(HttpServletRequest request) {
        String role = AuthUtil.getCurrentUserRole(request);
        Long userId = AuthUtil.getCurrentUserId(request);

        if ("ADMIN".equals(role) || "EDUCATOR".equals(role)) {
            return ResponseEntity.ok(childService.findAll());
        } else if ("PARENT".equals(role)) {
            // Parents should only see their own children
            // You'll need to implement a method to find children by parent user ID
            // This assumes you have a relationship between User and Parent entities
            return ResponseEntity.ok(childService.findByParentUserId(userId));
        }

        return ResponseEntity.forbidden().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Child> getChildById(@PathVariable Long id, HttpServletRequest request) {
        Optional<Child> child = childService.findById(id);

        if (child.isPresent()) {
            // Check if user has permission to view this child
            if (canAccessChild(request, child.get())) {
                return ResponseEntity.ok(child.get());
            } else {
                return ResponseEntity.forbidden().build();
            }
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<Child> createChild(@RequestBody Child child, HttpServletRequest request) {
        // Only admin and educators can create children
        if (AuthUtil.isAdminOrEducator(request)) {
            return ResponseEntity.ok(childService.save(child));
        }

        return ResponseEntity.forbidden().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Child> updateChild(@PathVariable Long id, @RequestBody Child child,
                                             HttpServletRequest request) {
        if (canModifyChild(request, id)) {
            child.setId(id);
            Child updated = childService.updateChild(child);
            return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
        }

        return ResponseEntity.forbidden().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteChild(@PathVariable Long id, HttpServletRequest request) {
        // Only admin can delete children
        if (AuthUtil.isAdmin(request)) {
            childService.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.forbidden().build();
    }

    @GetMapping("/active")
    public ResponseEntity<List<Child>> getActiveChildren(HttpServletRequest request) {
        if (AuthUtil.isAdminOrEducator(request)) {
            return ResponseEntity.ok(childService.findActiveChildren());
        }
        return ResponseEntity.forbidden().build();
    }

    @GetMapping("/parent/{parentId}")
    public ResponseEntity<List<Child>> getChildrenByParent(@PathVariable Long parentId,
                                                           HttpServletRequest request) {
        if (AuthUtil.canAccessUserData(request, parentId)) {
            return ResponseEntity.ok(childService.findByParentId(parentId));
        }
        return ResponseEntity.forbidden().build();
    }

    private boolean canAccessChild(HttpServletRequest request, Child child) {
        String role = AuthUtil.getCurrentUserRole(request);
        Long userId = AuthUtil.getCurrentUserId(request);

        // Admin and educators can access all children
        if ("ADMIN".equals(role) || "EDUCATOR".equals(role)) {
            return true;
        }

        // Parents can only access their own children
        if ("PARENT".equals(role)) {
            // You'll need to implement logic to check if the child belongs to the parent
            // This depends on your entity relationships
            return isChildBelongsToParent(child, userId);
        }

        return false;
    }

    private boolean canModifyChild(HttpServletRequest request, Long childId) {
        String role = AuthUtil.getCurrentUserRole(request);

        // Admin and educators can modify children
        if ("ADMIN".equals(role) || "EDUCATOR".equals(role)) {
            return true;
        }

        // Parents might have limited modification rights
        if ("PARENT".equals(role)) {
            Optional<Child> child = childService.findById(childId);
            if (child.isPresent()) {
                return isChildBelongsToParent(child.get(), AuthUtil.getCurrentUserId(request));
            }
        }

        return false;
    }

    private boolean isChildBelongsToParent(Child child, Long parentUserId) {
        // Implement this method based on your entity relationships
        // This is a placeholder implementation
        return child.getParent() != null &&
                child.getParent().getUserId() != null &&
                child.getParent().getUserId().equals(parentUserId);
    }
}