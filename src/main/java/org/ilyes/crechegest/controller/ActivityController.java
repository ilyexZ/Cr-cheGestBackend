package org.ilyes.crechegest.controller;

import org.ilyes.crechegest.model.Activity;
import org.ilyes.crechegest.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/activities")
@CrossOrigin(origins = "*")
public class ActivityController {

    @Autowired
    private ActivityService activityService;

    @GetMapping
    public List<Activity> getAllActivities() {
        return activityService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Activity> getActivityById(@PathVariable Long id) {
        Optional<Activity> activity = activityService.findById(id);
        return activity.map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Activity createActivity(@RequestBody Activity activity) {
        return activityService.save(activity);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Activity> updateActivity(@PathVariable Long id, @RequestBody Activity activity) {
        activity.setId(id);
        Activity updated = activityService.updateActivity(activity);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteActivity(@PathVariable Long id) {
        activityService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/upcoming")
    public List<Activity> getUpcomingActivities() {
        return activityService.findUpcomingActivities();
    }

    @GetMapping("/date/{date}")
    public List<Activity> getActivitiesByDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return activityService.findByDate(date);
    }

    @PostMapping("/{activityId}/children/{childId}")
    public ResponseEntity<Activity> addChildToActivity(@PathVariable Long activityId, @PathVariable Long childId) {
        Activity updated = activityService.addChildToActivity(activityId, childId);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{activityId}/children/{childId}")
    public ResponseEntity<Activity> removeChildFromActivity(@PathVariable Long activityId, @PathVariable Long childId) {
        Activity updated = activityService.removeChildFromActivity(activityId, childId);
        return updated != null ? ResponseEntity.ok(updated) : ResponseEntity.notFound().build();
    }
}