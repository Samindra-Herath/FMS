package com.faculty.controller;

import com.faculty.dao.AdminDAO;
import com.faculty.view.AdminDashboardView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminController {

    private AdminDashboardView view;
    private AdminDAO adminDAO;
    private String currentTab = "Students";

    public AdminController(AdminDashboardView view) {
        this.view = view;
        this.adminDAO = new AdminDAO();
        initTabNavigation();
        initActionButtons();
        refreshStudentTable();
    }

    private void initTabNavigation() {
        view.getBtnStudents().addActionListener(e -> { currentTab = "Students"; view.updateTabHeaders("Students"); refreshStudentTable(); });
        view.getBtnLecturers().addActionListener(e -> { currentTab = "Lecturers"; view.updateTabHeaders("Lecturers"); refreshLecturerTable(); });
        view.getBtnCourses().addActionListener(e -> { currentTab = "Courses"; view.updateTabHeaders("Courses"); refreshCourseTable(); });
        view.getBtnDepartments().addActionListener(e -> { currentTab = "Departments"; view.updateTabHeaders("Departments"); view.getTableModel().setRowCount(0); });
        view.getBtnDegrees().addActionListener(e -> { currentTab = "Degrees"; view.updateTabHeaders("Degrees"); view.getTableModel().setRowCount(0); });
    }

    private void initActionButtons() {
        view.getBtnAdd().addActionListener(e -> {
            if ("Students".equals(currentTab)) {
                JTextField txtName = new JTextField();
                JTextField txtReg = new JTextField();
                JTextField txtDegreeId = new JTextField("1");
                JTextField txtEmail = new JTextField();
                JTextField txtMobile = new JTextField();
                Object[] message = { "Full Name:", txtName, "Registration ID:", txtReg, "Degree ID:", txtDegreeId, "Email:", txtEmail, "Mobile:", txtMobile };

                if (JOptionPane.showConfirmDialog(view, message, "Add Student", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = { txtName.getText(), txtReg.getText(), Integer.parseInt(txtDegreeId.getText()), txtEmail.getText(), txtMobile.getText() };
                        adminDAO.addRecord("Students", data);
                        refreshStudentTable();
                    } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(view, "Invalid Degree ID."); }
                }
            }

            else if ("Lecturers".equals(currentTab)) {
                JTextField txtUser = new JTextField();
                JTextField txtPass = new JPasswordField();
                JTextField txtName = new JTextField();
                JTextField txtDeptId = new JTextField("1");
                JTextField txtEmail = new JTextField();
                JTextField txtMobile = new JTextField();

                Object[] message = {
                        "System Username:", txtUser,
                        "System Password:", txtPass,
                        "Lecturer Full Name:", txtName,
                        "Department ID:", txtDeptId,
                        "Email Address:", txtEmail,
                        "Mobile Phone Number:", txtMobile
                };

                if (JOptionPane.showConfirmDialog(view, message, "Add New Lecturer Profile", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = {
                                txtUser.getText(), txtPass.getText(), txtName.getText(),
                                Integer.parseInt(txtDeptId.getText()), txtEmail.getText(), txtMobile.getText()
                        };
                        adminDAO.addRecord("Lecturers", data);
                        refreshLecturerTable();
                    } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(view, "Invalid Department ID Input formatting."); }
                }
            }

            // ADD COURSE DIALOGUE
            else if ("Courses".equals(currentTab)) {
                JTextField txtCourseCode = new JTextField();
                JTextField txtCourseName = new JTextField();
                JTextField txtCredits = new JTextField();
                JTextField txtLecturerId = new JTextField();

                Object[] message = {
                        "Course Code:", txtCourseCode,
                        "Course Name:", txtCourseName,
                        "Credits:", txtCredits,
                        "Assigned Lecturer ID (Optional):", txtLecturerId
                };

                if (JOptionPane.showConfirmDialog(view, message, "Add New Course", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Integer lecId = txtLecturerId.getText().trim().isEmpty() ? null : Integer.parseInt(txtLecturerId.getText().trim());
                        Object[] data = {
                                txtCourseCode.getText(),
                                txtCourseName.getText(),
                                Integer.parseInt(txtCredits.getText()),
                                lecId
                        };
                        adminDAO.addRecord("Courses", data);
                        refreshCourseTable();
                    } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(view, "Invalid numeric input fields for Credits or Lecturer ID."); }
                }
            }
        });

        view.getBtnEdit().addActionListener(e -> {
            if ("Students".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Select a row to edit."); return; }

                int studentId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                JTextField txtName = new JTextField(view.getTableModel().getValueAt(selectedRow, 1) != null ? view.getTableModel().getValueAt(selectedRow, 1).toString() : "");
                JTextField txtReg = new JTextField(view.getTableModel().getValueAt(selectedRow, 2) != null ? view.getTableModel().getValueAt(selectedRow, 2).toString() : "");
                JTextField txtDegreeId = new JTextField("1");
                JTextField txtEmail = new JTextField(view.getTableModel().getValueAt(selectedRow, 4) != null ? view.getTableModel().getValueAt(selectedRow, 4).toString() : "");
                JTextField txtMobile = new JTextField(view.getTableModel().getValueAt(selectedRow, 5) != null ? view.getTableModel().getValueAt(selectedRow, 5).toString() : "");

                Object[] message = { "Full Name:", txtName, "Registration ID:", txtReg, "New Degree ID:", txtDegreeId, "Email:", txtEmail, "Mobile:", txtMobile };
                if (JOptionPane.showConfirmDialog(view, message, "Edit Student", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = { txtName.getText(), txtReg.getText(), Integer.parseInt(txtDegreeId.getText()), txtEmail.getText(), txtMobile.getText(), studentId };
                        adminDAO.updateRecord("Students", data);
                        refreshStudentTable();
                    } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(view, "Invalid Degree ID."); }
                }
            }

            else if ("Lecturers".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Please select a lecturer row first from the data grid table layout views."); return; }

                int lecturerId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                JTextField txtName = new JTextField(view.getTableModel().getValueAt(selectedRow, 1) != null ? view.getTableModel().getValueAt(selectedRow, 1).toString() : "");
                JTextField txtDeptId = new JTextField("1");
                JTextField txtEmail = new JTextField(view.getTableModel().getValueAt(selectedRow, 3) != null ? view.getTableModel().getValueAt(selectedRow, 3).toString() : "");
                JTextField txtMobile = new JTextField(view.getTableModel().getValueAt(selectedRow, 4) != null ? view.getTableModel().getValueAt(selectedRow, 4).toString() : "");

                Object[] message = {
                        "Lecturer Full Name:", txtName,
                        "Target Department ID:", txtDeptId,
                        "Email Address:", txtEmail,
                        "Mobile Phone Number:", txtMobile
                };
                if (JOptionPane.showConfirmDialog(view, message, "Edit Lecturer Details", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = {
                                txtName.getText(),
                                Integer.parseInt(txtDeptId.getText()),
                                txtEmail.getText(),
                                txtMobile.getText(),
                                lecturerId
                        };
                        adminDAO.updateRecord("Lecturers", data);
                        refreshLecturerTable();
                    } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(view, "Invalid Department ID Format."); }
                }
            }

            // EDIT COURSE DIALOGUE
            else if ("Courses".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Please select a course row to edit."); return; }

                String courseCode = view.getTableModel().getValueAt(selectedRow, 0).toString();
                JTextField txtCourseName = new JTextField(view.getTableModel().getValueAt(selectedRow, 1).toString());
                JTextField txtCredits = new JTextField(view.getTableModel().getValueAt(selectedRow, 2).toString());

                String currentLec = view.getTableModel().getValueAt(selectedRow, 3).toString();
                JTextField txtLecturerId = new JTextField("Not Assigned".equals(currentLec) ? "" : currentLec);

                Object[] message = {
                        "Course Code (Read-Only):", new JLabel(courseCode),
                        "Course Name:", txtCourseName,
                        "Credits:", txtCredits,
                        "Assigned Lecturer ID (Optional):", txtLecturerId
                };

                if (JOptionPane.showConfirmDialog(view, message, "Edit Course Details", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Integer lecId = txtLecturerId.getText().trim().isEmpty() ? null : Integer.parseInt(txtLecturerId.getText().trim());
                        Object[] data = {
                                txtCourseName.getText(),
                                Integer.parseInt(txtCredits.getText()),
                                lecId,
                                courseCode
                        };
                        adminDAO.updateRecord("Courses", data);
                        refreshCourseTable();
                    } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(view, "Invalid numeric formatting inputs."); }
                }
            }
        });

        view.getBtnDelete().addActionListener(e -> {
            if ("Students".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Select a row to delete."); return; }
                int studentId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(view, "Delete this student row?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    adminDAO.deleteRecord("Students", String.valueOf(studentId));
                    refreshStudentTable();
                }
            }

            else if ("Lecturers".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Please point out and select a Lecturer record from the row display lines."); return; }
                int lecturerId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(view, "Permanently wipe this Lecturer record along with credential accounts?", "Confirm Destructive Action Execution", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    adminDAO.deleteRecord("Lecturers", String.valueOf(lecturerId));
                    refreshLecturerTable();
                }
            }

            // DELETE COURSE ACTION
            else if ("Courses".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Please select a course row to delete."); return; }
                String courseCode = view.getTableModel().getValueAt(selectedRow, 0).toString();
                if (JOptionPane.showConfirmDialog(view, "Delete course " + courseCode + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    adminDAO.deleteRecord("Courses", courseCode);
                    refreshCourseTable();
                }
            }
        });

        view.getBtnSaveChanges().addActionListener(e -> {
            if ("Students".equals(currentTab)) refreshStudentTable();
            else if ("Lecturers".equals(currentTab)) refreshLecturerTable();
            else if ("Courses".equals(currentTab)) refreshCourseTable();
            JOptionPane.showMessageDialog(view, "Database perfectly synchronized with current view.");
        });
    }

    public void refreshStudentTable() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        for (Object[] row : adminDAO.getAllStudents()) {
            tableModel.addRow(row);
        }
    }

    public void refreshLecturerTable() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        for (Object[] row : adminDAO.getAllLecturers()) {
            tableModel.addRow(row);
        }
    }

    // REFRESH TABLE UI VIEW
    public void refreshCourseTable() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        for (Object[] row : adminDAO.getAllCourses()) {
            tableModel.addRow(row);
        }
    }
}