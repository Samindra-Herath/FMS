package com.faculty.model;

public class Lecturer {
    private int lecturerId;
    private String fullName;
    private String departmentName;
    private String email;
    private String mobileNumber;

    public Lecturer(int lecturerId, String fullName, String departmentName, String email, String mobileNumber) {
        this.lecturerId = lecturerId;
        this.fullName = fullName;
        this.departmentName = departmentName;
        this.email = email;
        this.mobileNumber = mobileNumber;
}
    public int getLecturerId() {
        return lecturerId;
    }
    public String getFullName() {

        return fullName;
    }
    public void setFullName(String fullName) {

        this.fullName = fullName;
    }
    public String getDepartmentName() {
        return departmentName;
    }
    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getMobile() {
        return mobileNumber;
    }
    public void setMobile(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
