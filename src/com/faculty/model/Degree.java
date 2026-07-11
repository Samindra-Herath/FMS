package com.faculty.model;

public class Degree {
    private int degreeId;
    private String degreeName;
    private int deptId;
    private int studentCount;

    public Degree() {}

    public Degree(int degreeId, String degreeName, int deptId, int studentCount) {
        this.degreeId = degreeId;
        this.degreeName = degreeName;
        this.deptId = deptId;
        this.studentCount = studentCount;
    }

    public int getDegreeId() { return degreeId; }
    public void setDegreeId(int degreeId) { this.degreeId = degreeId; }

    public String getDegreeName() { return degreeName; }
    public void setDegreeName(String degreeName) { this.degreeName = degreeName; }

    public int getDeptId() { return deptId; }
    public void setDeptId(int deptId) { this.deptId = deptId; }

    public int getStudentCount() { return studentCount; }
    public void setStudentCount(int studentCount) { this.studentCount = studentCount; }
}