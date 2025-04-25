package com.example.studentapp.model;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class AttendanceManager {
    private final Map<String, Map<LocalDate, Boolean>> attendanceMap = new HashMap<>();

    public void markAttendance(String studentId, LocalDate date, boolean present) {
        attendanceMap
                .computeIfAbsent(studentId, k -> new HashMap<>())
                .put(date, present);
    }

    public Boolean getAttendance(String studentId, LocalDate date) {
        Map<LocalDate, Boolean> studentRecords = attendanceMap.get(studentId);
        return studentRecords != null ? studentRecords.getOrDefault(date, null) : null;
    }

    public Map<LocalDate, Boolean> getAttendanceForStudent(String studentId) {
        return attendanceMap.getOrDefault(studentId, new HashMap<>());
    }

    public Map<String, Map<LocalDate, Boolean>> getAllAttendance() {
        return attendanceMap;
    }

    public void clearAttendance() {
        attendanceMap.clear();
    }
}
