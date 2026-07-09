package com.faculty.model;

public class Course {
    private String courseCode;
    private String courseName;
    private int credits;
    private String grade;

    public Course(String courseCode, String courseName, int credits, String grade) {
        this.courseCode = courseCode;
        this.courseName = courseName;
        this.credits = credits;
        this.grade = grade;
    }

    public String getCourseCode() { return courseCode; }
    public String getCourseName() { return courseName; }
    public int getCredits() { return credits; }
    public String getGrade() { return grade; }
}