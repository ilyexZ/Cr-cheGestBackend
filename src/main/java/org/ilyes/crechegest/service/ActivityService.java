package org.ilyes.crechegest.service;

import org.ilyes.crechegest.model.Activity;
import org.ilyes.crechegest.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ActivityService {
    Activity save(Activity activity);
    Optional<Activity> findById(Long id);
    List<Activity> findAll();
    List<Activity> findByDate(LocalDate date);
    List<Activity> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Activity> findByEducator(User educator);
    List<Activity> findUpcomingActivities();
    List<Activity> findByChildId(Long childId);
    List<Activity> findByAgeGroup(String ageGroup);
    Activity updateActivity(Activity activity);
    void delete(Long id);
    Activity addChildToActivity(Long activityId, Long childId);
    Activity removeChildFromActivity(Long activityId, Long childId);
}
