package com.faculty.model;

public class Student {
    private int studentId;
    private String fullName;
    private String studentRegId;
    private String degreeName;
    private String email;
    private String mobileNumber;

    public Student(int studentId, String fullName, String studentRegId, String degreeName, String email, String mobileNumber) {
        this.studentId = studentId;
        this.fullName = fullName;
        this.studentRegId = studentRegId;
        this.degreeName = degreeName;
        this.email = email;
        this.mobileNumber = mobileNumber;
    }

    // Getters and Setters
    public int getStudentId() { return studentId; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getStudentRegId() { return studentRegId; }
    public void setStudentRegId(String studentRegId) { this.studentRegId = studentRegId; }
    public String getDegreeName() { return degreeName; }
    public void setDegreeName(String degreeName) { this.degreeName = degreeName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getMobileNumber() { return mobileNumber; }
    public void setMobileNumber(String mobileNumber) { this.mobileNumber = mobileNumber; }
}