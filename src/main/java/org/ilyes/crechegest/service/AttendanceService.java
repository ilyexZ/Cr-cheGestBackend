package org.ilyes.crechegest.service;

import org.ilyes.crechegest.model.Attendance;
import org.ilyes.crechegest.model.Child;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface AttendanceService {
    Attendance save(Attendance attendance);
    Optional<Attendance> findById(Long id);
    List<Attendance> findAll();
    List<Attendance> findByChildId(Long childId);
    List<Attendance> findByDate(LocalDate date);
    List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate);
    List<Attendance> findByChildIdAndDateBetween(Long childId, LocalDate startDate, LocalDate endDate);
    Attendance updateAttendance(Attendance attendance);
    void delete(Long id);
    long countByDate(LocalDate date);
    boolean existsByChildIdAndDate(Long childId, LocalDate date);
}