package org.ilyes.crechegest.service.impl;

import org.ilyes.crechegest.model.Activity;
import org.ilyes.crechegest.model.Child;
import org.ilyes.crechegest.model.User;
import org.ilyes.crechegest.repository.ActivityRepository;
import org.ilyes.crechegest.repository.ChildRepository;
import org.ilyes.crechegest.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ActivityServiceImpl implements ActivityService {

    private final ActivityRepository activityRepository;
    private final ChildRepository childRepository;

    @Autowired
    public ActivityServiceImpl(ActivityRepository activityRepository, ChildRepository childRepository) {
        this.activityRepository = activityRepository;
        this.childRepository = childRepository;
    }

    @Override
    public Activity save(Activity activity) {
        return activityRepository.save(activity);
    }

    @Override
    public Optional<Activity> findById(Long id) {
        return activityRepository.findById(id);
    }

    @Override
    public List<Activity> findAll() {
        return activityRepository.findAll();
    }

    @Override
    public List<Activity> findByDate(LocalDate date) {
        return activityRepository.findByDate(date);
    }

    @Override
    public List<Activity> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return activityRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public List<Activity> findByEducator(User educator) {
        return activityRepository.findByEducator(educator);
    }

    @Override
    public List<Activity> findUpcomingActivities() {
        return activityRepository.findUpcomingActivities(LocalDate.now());
    }

    @Override
    public List<Activity> findByChildId(Long childId) {
        return activityRepository.findByChildId(childId);
    }

    @Override
    public List<Activity> findByAgeGroup(String ageGroup) {
        return activityRepository.findByAgeGroup(ageGroup);
    }

    @Override
    public Activity updateActivity(Activity activity) {
        Optional<Activity> existingActivity = activityRepository.findById(activity.getId());
        if (existingActivity.isPresent()) {
            Activity updatedActivity = existingActivity.get();
            updatedActivity.setName(activity.getName());
            updatedActivity.setDescription(activity.getDescription());
            updatedActivity.setDate(activity.getDate());
            updatedActivity.setStartTime(activity.getStartTime());
            updatedActivity.setEndTime(activity.getEndTime());
            updatedActivity.setLocation(activity.getLocation());
            updatedActivity.setMaterialsNeeded(activity.getMaterialsNeeded());
            updatedActivity.setAgeGroup(activity.getAgeGroup());
            updatedActivity.setMaxParticipants(activity.getMaxParticipants());
            updatedActivity.setEducator(activity.getEducator());

            return activityRepository.save(updatedActivity);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        activityRepository.deleteById(id);
    }

    @Override
    public Activity addChildToActivity(Long activityId, Long childId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        Optional<Child> childOpt = childRepository.findById(childId);

        if (activityOpt.isPresent() && childOpt.isPresent()) {
            Activity activity = activityOpt.get();
            Child child = childOpt.get();

            if (!activity.getParticipants().contains(child)) {
                activity.getParticipants().add(child);
                return activityRepository.save(activity);
            }
        }
        return null;
    }

    @Override
    public Activity removeChildFromActivity(Long activityId, Long childId) {
        Optional<Activity> activityOpt = activityRepository.findById(activityId);
        Optional<Child> childOpt = childRepository.findById(childId);

        if (activityOpt.isPresent() && childOpt.isPresent()) {
            Activity activity = activityOpt.get();
            Child child = childOpt.get();

            activity.getParticipants().remove(child);
            return activityRepository.save(activity);
        }
        return null;
    }
}