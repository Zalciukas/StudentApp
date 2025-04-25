package com.example.studentapp.model;

public class Student extends Person {
    private String groupName;

    public Student(String id, String firstName, String lastName, String groupName) {
        super(id, firstName, lastName);
        this.groupName = groupName;
    }

    @Override
    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
