package org.ilyes.crechegest.service.impl;

import org.ilyes.crechegest.model.Attendance;
import org.ilyes.crechegest.repository.AttendanceRepository;
import org.ilyes.crechegest.service.AttendanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class AttendanceServiceImpl implements AttendanceService {

    private final AttendanceRepository attendanceRepository;

    @Autowired
    public AttendanceServiceImpl(AttendanceRepository attendanceRepository) {
        this.attendanceRepository = attendanceRepository;
    }

    @Override
    public Attendance save(Attendance attendance) {
        return attendanceRepository.save(attendance);
    }

    @Override
    public Optional<Attendance> findById(Long id) {
        return attendanceRepository.findById(id);
    }

    @Override
    public List<Attendance> findAll() {
        return attendanceRepository.findAll();
    }

    @Override
    public List<Attendance> findByChildId(Long childId) {
        return attendanceRepository.findByChildId(childId);
    }

    @Override
    public List<Attendance> findByDate(LocalDate date) {
        return attendanceRepository.findByDate(date);
    }

    @Override
    public List<Attendance> findByDateBetween(LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    public List<Attendance> findByChildIdAndDateBetween(Long childId, LocalDate startDate, LocalDate endDate) {
        return attendanceRepository.findByChildIdAndDateBetween(childId, startDate, endDate);
    }

    @Override
    public Attendance updateAttendance(Attendance attendance) {
        Optional<Attendance> existingAttendance = attendanceRepository.findById(attendance.getId());
        if (existingAttendance.isPresent()) {
            Attendance updatedAttendance = existingAttendance.get();
            updatedAttendance.setChild(attendance.getChild());
            updatedAttendance.setDate(attendance.getDate());
            updatedAttendance.setPresent(attendance.isPresent());
//            updatedAttendance.setArrivalTime(attendance.getArrivalTime());
//            updatedAttendance.setDepartureTime(attendance.getDepartureTime());
            updatedAttendance.setNotes(attendance.getNotes());

            return attendanceRepository.save(updatedAttendance);
        }
        return null;
    }

    @Override
    public void delete(Long id) {
        attendanceRepository.deleteById(id);
    }

    @Override
    public long countByDate(LocalDate date) {
        return attendanceRepository.countByDateAndIsPresentTrue(date);
    }

    @Override
    public boolean existsByChildIdAndDate(Long childId, LocalDate date) {
        return attendanceRepository.existsByChildIdAndDate(childId, date);
    }
}