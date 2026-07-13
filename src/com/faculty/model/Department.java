package com.faculty.model;

public class Department {
    private int deptId;
    private String deptName;
    private String hod;
    private int degreeId;
    private int staffCount;

    public Department() {}

    public Department(int deptId, String deptName, String hod, int degreeId, int staffCount) {
        this.deptId = deptId;
        this.deptName = deptName;
        this.hod = hod;
        this.degreeId = degreeId;
        this.staffCount = staffCount;
    }

    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }

    public String getDeptName() { return deptName; }
    public void setDeptName(String deptName) { this.deptName = deptName; }

    public String getHod() { return hod; }
    public void setHod(String hod) { this.hod = hod; }

    public int getDegreeId() { return degreeId; }
    public void setDegreeId(int degreeId) { this.degreeId = degreeId; }

    public int getStaffCount() { return staffCount; }
    public void setStaffCount(int staffCount) { this.staffCount = staffCount; }
}