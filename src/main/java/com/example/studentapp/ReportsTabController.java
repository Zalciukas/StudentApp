package com.example.studentapp;

import com.example.studentapp.model.AttendanceManager;
import com.example.studentapp.model.Group;
import com.example.studentapp.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;

import java.io.File;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ReportsTabController {

    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private ComboBox<Group> groupComboBox;
    @FXML private ComboBox<Student> studentComboBox;
    @FXML private Button generateReportButton;
    @FXML private Button exportPdfButton;
    @FXML private TableView<AttendanceRecord> reportTable;
    @FXML private TableColumn<AttendanceRecord, String> studentNameColumn;
    @FXML private TableColumn<AttendanceRecord, String> groupNameColumn;
    @FXML private TableColumn<AttendanceRecord, String> dateColumn;
    @FXML private TableColumn<AttendanceRecord, String> statusColumn;

    private ObservableList<Group> groups = FXCollections.observableArrayList();
    private ObservableList<Student> allStudents = FXCollections.observableArrayList();
    private ObservableList<AttendanceRecord> attendanceRecords = FXCollections.observableArrayList();
    private AttendanceManager attendanceManager = new AttendanceManager();

    // Inner class to represent an attendance record in the table
    public static class AttendanceRecord {
        private final String studentName;
        private final String groupName;
        private final LocalDate date;
        private final boolean present;

        public AttendanceRecord(String studentName, String groupName, LocalDate date, boolean present) {
            this.studentName = studentName;
            this.groupName = groupName;
            this.date = date;
            this.present = present;
        }

        public String getStudentName() { return studentName; }
        public String getGroupName() { return groupName; }
        public LocalDate getDate() { return date; }
        public boolean isPresent() { return present; }
    }

    @FXML
    public void initialize() {
        // Set up date pickers
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());

        // Set up table columns
        studentNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getStudentName()));
        groupNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGroupName()));
        dateColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getDate().toString()));
        statusColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().isPresent() ? "Present" : "Absent"));

        reportTable.setItems(attendanceRecords);

        // Load initial data
        loadGroups();
        loadStudents();

        // Add listener to group filter
        groupComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldGroup, newGroup) -> {
            if (newGroup != null) {
                updateStudentFilter(newGroup);
            }
        });
    }

    @FXML
    public void onGenerateReport() {
        attendanceRecords.clear();

        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        Group selectedGroup = groupComboBox.getValue();
        Student selectedStudent = studentComboBox.getValue();

        if (startDate == null || endDate == null) {
            new Alert(Alert.AlertType.WARNING, "Please select start and end dates").show();
            return;
        }

        if (endDate.isBefore(startDate)) {
            new Alert(Alert.AlertType.WARNING, "End date cannot be before start date").show();
            return;
        }

        List<Student> studentsToReport = FXCollections.observableArrayList();

        if (selectedStudent != null) {
            studentsToReport.add(selectedStudent);
        } else if (selectedGroup != null) {
            studentsToReport.addAll(selectedGroup.getStudents());
        } else {
            studentsToReport.addAll(allStudents);
        }

        // Generate attendance records for each student and date
        for (Student student : studentsToReport) {
            Map<LocalDate, Boolean> attendanceData = attendanceManager.getAttendanceForStudent(student.getId());

            // If there's no data for this student, create placeholders
            if (attendanceData.isEmpty()) {
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    attendanceRecords.add(new AttendanceRecord(
                            student.getFullName(),
                            student.getGroupName(),
                            date,
                            false  // Default to absent if no record
                    ));
                }
            } else {
                // Add actual attendance records
                for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                    Boolean present = attendanceData.getOrDefault(date, false);
                    attendanceRecords.add(new AttendanceRecord(
                            student.getFullName(),
                            student.getGroupName(),
                            date,
                            present != null && present
                    ));
                }
            }
        }

        if (attendanceRecords.isEmpty()) {
            new Alert(Alert.AlertType.INFORMATION, "No attendance records found for the selected criteria").show();
        }
    }

//    @FXML
//    public void onExportPdf() {
//        if (attendanceRecords.isEmpty()) {
//            new Alert(Alert.AlertType.WARNING, "Please generate a report first").show();
//            return;
//        }
//
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Save PDF Report");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PDF files", "*.pdf"));
//        File file = fileChooser.showSaveDialog(null);
//
//        if (file != null) {
//            try {
//                PdfGenerator pdfGenerator = new PdfGenerator();
//                pdfGenerator.generateAttendanceReport(file.getAbsolutePath(), attendanceRecords);
//                new Alert(Alert.AlertType.INFORMATION, "Report exported to PDF successfully").show();
//            } catch (Exception e) {
//                new Alert(Alert.AlertType.ERROR, "Error exporting report: " + e.getMessage()).show();
//                e.printStackTrace();
//            }
//        }
//    }

    private void updateStudentFilter(Group group) {
        // Get students from the selected group
        List<Student> groupStudents = group.getStudents();
        studentComboBox.getItems().clear();
        studentComboBox.getItems().addAll(groupStudents);
    }

    private void loadGroups() {
        // Sample groups for now
        Group group1 = new Group("CS101");
        Group group2 = new Group("Math202");
        groups.addAll(group1, group2);
        groupComboBox.setItems(groups);
    }

    private void loadStudents() {
        // Sample students for now
        Student student1 = new Student("1", "John", "Doe", "CS101");
        Student student2 = new Student("2", "Jane", "Smith", "CS101");
        Student student3 = new Student("3", "Bob", "Johnson", "Math202");

        allStudents.addAll(student1, student2, student3);

        // Assign students to groups
        for (Group group : groups) {
            for (Student student : allStudents) {
                if (student.getGroupName().equals(group.getName())) {
                    group.addStudent(student);
                }
            }
        }
    }
}