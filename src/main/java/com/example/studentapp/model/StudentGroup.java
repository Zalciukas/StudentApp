package com.example.studentapp.model;

import java.util.ArrayList;
import java.util.List;

public class StudentGroup {
    private String name;
    private List<Student> students;

    public StudentGroup(String name) {
        this.name = name;
        this.students = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void addStudent(Student student) {
        if (!students.contains(student)) {
            students.add(student);
            student.setGroupName(name);
        }
    }

    public void removeStudent(Student student) {
        students.remove(student);
        student.setGroupName("");
    }

    @Override
    public String toString() {
        return name + " (" + students.size() + " students)";
    }
}
