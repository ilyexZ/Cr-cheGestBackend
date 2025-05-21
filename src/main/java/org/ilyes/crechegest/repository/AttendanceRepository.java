package org.ilyes.crechegest.repository;



import org.ilyes.crechegest.model.Attendance;
import org.ilyes.crechegest.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {
    List<Attendance> findByDate(LocalDate date);

    List<Attendance> findByChildAndDateBetween(Child child, LocalDate startDate, LocalDate endDate);

    Optional<Attendance> findByChildAndDate(Child child, LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.child.id = :childId AND a.date = :date")
    Optional<Attendance> findByChildIdAndDate(@Param("childId") Long childId, @Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.date = :date AND a.isPresent = true")
    List<Attendance> findPresentByDate(@Param("date") LocalDate date);

    @Query("SELECT a FROM Attendance a WHERE a.date = :date AND a.isPresent = false")
    List<Attendance> findAbsentByDate(@Param("date") LocalDate date);

    @Query("SELECT COUNT(a) FROM Attendance a WHERE a.date = :date AND a.isPresent = true")
    Long countPresentByDate(@Param("date") LocalDate date);
}
