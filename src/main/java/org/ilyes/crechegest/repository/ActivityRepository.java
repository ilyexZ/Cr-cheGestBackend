package org.ilyes.crechegest.repository;


import org.ilyes.crechegest.model.Activity;
import org.ilyes.crechegest.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ActivityRepository extends JpaRepository<Activity, Long> {
    List<Activity> findByDate(LocalDate date);

    List<Activity> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Activity> findByEducator(User educator);

    @Query("SELECT a FROM Activity a WHERE a.date >= :date ORDER BY a.date, a.startTime")
    List<Activity> findUpcomingActivities(@Param("date") LocalDate date);

    @Query("SELECT a FROM Activity a JOIN a.participants p WHERE p.id = :childId")
    List<Activity> findByChildId(@Param("childId") Long childId);

    @Query("SELECT a FROM Activity a WHERE a.ageGroup = :ageGroup")
    List<Activity> findByAgeGroup(@Param("ageGroup") String ageGroup);
}
