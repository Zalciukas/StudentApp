package com.example.studentapp;

import com.example.studentapp.model.Group;
import com.example.studentapp.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;

public class StudentsTabController {

    @FXML private TextField idField;
    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private ComboBox<Group> groupComboBox;

    @FXML private TableView<Student> studentTable;
    @FXML private TableColumn<Student, String> idColumn;
    @FXML private TableColumn<Student, String> firstNameColumn;
    @FXML private TableColumn<Student, String> lastNameColumn;
    @FXML private TableColumn<Student, String> groupColumn;

    @FXML private TextField newGroupNameField;
    @FXML private Button createGroupButton;
    @FXML private ListView<Group> groupListView;

    private ObservableList<Student> students = FXCollections.observableArrayList();
    private ObservableList<Group> groups = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Setup table columns
        idColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getId()));
        firstNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getFirstName()));
        lastNameColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getLastName()));
        groupColumn.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(data.getValue().getGroupName()));

        studentTable.setItems(students);
        groupComboBox.setItems(groups);
        groupListView.setItems(groups);

        // Load initial data
        loadGroups();

        // Handle student selection
        studentTable.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                idField.setText(newSelection.getId());
                firstNameField.setText(newSelection.getFirstName());
                lastNameField.setText(newSelection.getLastName());

                // Find and select the group in the combo box
                for (Group group : groups) {
                    if (group.getName().equals(newSelection.getGroupName())) {
                        groupComboBox.setValue(group);
                        break;
                    }
                }
            }
        });
    }

    @FXML
    public void onAddStudent() {
        String id = idField.getText();
        String first = firstNameField.getText();
        String last = lastNameField.getText();
        Group selectedGroup = groupComboBox.getValue();

        if (!id.isEmpty() && !first.isEmpty() && !last.isEmpty() && selectedGroup != null) {
            Student s = new Student(id, first, last, selectedGroup.getName());
            selectedGroup.addStudent(s);
            students.add(s);
            clearFields();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please fill in all fields").show();
        }
    }

    @FXML
    public void onUpdateStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            String id = idField.getText();
            String first = firstNameField.getText();
            String last = lastNameField.getText();
            Group selectedGroup = groupComboBox.getValue();

            if (!id.isEmpty() && !first.isEmpty() && !last.isEmpty() && selectedGroup != null) {
                // Update student details
                selectedStudent.setFirstName(first);
                selectedStudent.setLastName(last);
                selectedStudent.setGroupName(selectedGroup.getName());

                // Refresh table
                studentTable.refresh();
                clearFields();
            } else {
                new Alert(Alert.AlertType.WARNING, "Please fill in all fields").show();
            }
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a student to update").show();
        }
    }

    @FXML
    public void onDeleteStudent() {
        Student selectedStudent = studentTable.getSelectionModel().getSelectedItem();
        if (selectedStudent != null) {
            students.remove(selectedStudent);
            // Also remove from the group
            for (Group group : groups) {
                group.getStudents().remove(selectedStudent);
            }
            clearFields();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please select a student to delete").show();
        }
    }

    @FXML
    public void onCreateGroup() {
        String groupName = newGroupNameField.getText();
        if (!groupName.isEmpty()) {
            Group group = new Group(groupName);
            groups.add(group);
            newGroupNameField.clear();
        } else {
            new Alert(Alert.AlertType.WARNING, "Please enter a group name").show();
        }
    }

    private void clearFields() {
        idField.clear();
        firstNameField.clear();
        lastNameField.clear();
        groupComboBox.setValue(null);
    }

    private void loadGroups() {
        // Sample groups for now
        Group group1 = new Group("CS101");
        Group group2 = new Group("Math202");
        groups.addAll(group1, group2);
    }
}