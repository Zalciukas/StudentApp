package com.example.studentapp;

import com.example.studentapp.model.Group;
import com.example.studentapp.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class MainController {

    @FXML private BorderPane mainBorderPane;
    @FXML private TabPane tabPane;
    @FXML private MenuItem importCsvMenuItem;
    @FXML private MenuItem importExcelMenuItem;
    @FXML private MenuItem exportCsvMenuItem;
    @FXML private MenuItem exportExcelMenuItem;
    @FXML private MenuItem logoutMenuItem;

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    // Reference to the tab controllers
    private StudentsTabController studentsTabController;
    private ReportsTabController reportsTabController;

    @FXML
    public void initialize() {
        // Initialize tab controllers will be handled when loading the tabs
        loadTabs();
    }

    @FXML
    public void onExit(ActionEvent event) {
        Stage stage = (Stage) mainBorderPane.getScene().getWindow();
        stage.close();
    }

    @FXML
    public void onImportCsv() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Students from CSV");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
        File file = fileChooser.showOpenDialog(mainBorderPane.getScene().getWindow());

        if (file != null) {
//            importStudentsFromFile(file);
        }
    }

    @FXML
    public void onImportExcel() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Import Students from Excel");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(mainBorderPane.getScene().getWindow());

        if (file != null) {
//            importStudentsFromFile(file);
        }
    }

//    @FXML
//    public void onExportCsv() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Export Students to CSV");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
//        File file = fileChooser.showSaveDialog(mainBorderPane.getScene().getWindow());
//
//        if (file != null) {
//            DataImportExport exporter = new DataImportExport();
//            exporter.exportToCsv(students, file);
//            new Alert(Alert.AlertType.INFORMATION, "Students exported to CSV successfully").show();
//        }
//    }

//    @FXML
//    public void onExportExcel() {
//        FileChooser fileChooser = new FileChooser();
//        fileChooser.setTitle("Export Students to Excel");
//        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
//        File file = fileChooser.showSaveDialog(mainBorderPane.getScene().getWindow());
//
//        if (file != null) {
//            DataImportExport exporter = new DataImportExport();
//            exporter.exportToExcel(students, file);
//            new Alert(Alert.AlertType.INFORMATION, "Students exported to Excel successfully").show();
//        }
//    }

    @FXML
    public void onLogout() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/studentapp/login-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 500, 300);
            Stage stage = (Stage) mainBorderPane.getScene().getWindow();
            stage.setTitle("Student Registration System");
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error logging out: " + e.getMessage()).show();
        }
    }

    // Fun feature: Random student picker
    @FXML
    public void onPickRandomStudent() {
        if (students.isEmpty()) {
            new Alert(Alert.AlertType.WARNING, "No students available").show();
            return;
        }

        int randomIndex = (int) (Math.random() * students.size());
        Student selectedStudent = students.get(randomIndex);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Random Student Picker");
        alert.setHeaderText("The randomly selected student is:");
        alert.setContentText(selectedStudent.getFullName() + " from " + selectedStudent.getGroupName());
        alert.showAndWait();
    }

//    private void importStudentsFromFile(File file) {
//        DataImportExport importer = new DataImportExport();
//        List<Student> importedStudents = importer.importFromFile(file);
//
//        if (importedStudents.isEmpty()) {
//            new Alert(Alert.AlertType.WARNING, "No students found in the file").show();
//            return;
//        }
//
//        // Add imported students to the model
//        students.addAll(importedStudents);
//
//        // Update student groups
//        for (Student student : importedStudents) {
//            boolean groupFound = false;
//            for (Group group : groups) {
//                if (group.getName().equals(student.getGroupName())) {
//                    group.addStudent(student);
//                    groupFound = true;
//                    break;
//                }
//            }
//
//            if (!groupFound) {
//                Group newGroup = new Group(student.getGroupName());
//                newGroup.addStudent(student);
//                groups.add(newGroup);
//            }
//        }
//
//        new Alert(Alert.AlertType.INFORMATION,
//                "Successfully imported " + importedStudents.size() + " students").show();
//    }

    private void loadTabs() {
        try {
            // Load the tabs from their respective FXML files
            // In a real app, we'd implement proper tab loading and controller injection
            // For simplicity in this example, we're just creating the controllers

            // The real implementation would load FXML files and get controller instances
            studentsTabController = new StudentsTabController();
            reportsTabController = new ReportsTabController();

        } catch (Exception e) {
            e.printStackTrace();
            new Alert(Alert.AlertType.ERROR, "Error loading tabs: " + e.getMessage()).show();
        }
    }
}