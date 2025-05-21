package org.ilyes.crechegest.repository;



import org.ilyes.crechegest.model.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByDate(LocalDate date);

    List<Meal> findByDateBetween(LocalDate startDate, LocalDate endDate);

    List<Meal> findByMealType(String mealType);

    @Query("SELECT m FROM Meal m WHERE m.date = :date AND m.mealType = :mealType")
    List<Meal> findByDateAndMealType(@Param("date") LocalDate date, @Param("mealType") String mealType);

    @Query("SELECT m FROM Meal m WHERE m.date >= :date ORDER BY m.date, m.time")
    List<Meal> findUpcomingMeals(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT m.mealType FROM Meal m")
    List<String> findAllMealTypes();
}
