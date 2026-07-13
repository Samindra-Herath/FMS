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
        view.getBtnDepartments().addActionListener(e -> { currentTab = "Departments"; view.updateTabHeaders("Departments"); refreshDepartmentTable(); });
        view.getBtnDegrees().addActionListener(e -> { currentTab = "Degrees"; view.updateTabHeaders("Degrees"); refreshDegreeTable(); });
    }

    private void initActionButtons() {
        view.getBtnAdd().addActionListener(e -> {
            if ("Students".equals(currentTab)) {
                JTextField txtUser = new JTextField();
                JTextField txtPass = new JPasswordField();
                JTextField txtName = new JTextField();
                JTextField txtReg = new JTextField();
                JTextField txtDegreeId = new JTextField("1");
                JTextField txtEmail = new JTextField();
                JTextField txtMobile = new JTextField();
                Object[] message = {
                        "System Username:", txtUser,
                        "System Password:", txtPass,
                        "Full Name:", txtName,
                        "Registration ID:", txtReg,
                        "Degree ID:", txtDegreeId,
                        "Email:", txtEmail,
                        "Mobile:", txtMobile
                };

                if (JOptionPane.showConfirmDialog(view, message, "Add New Student Profile", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = {
                                txtUser.getText(), txtPass.getText(),
                                txtName.getText(), txtReg.getText(),
                                Integer.parseInt(txtDegreeId.getText()), txtEmail.getText(), txtMobile.getText()
                        };
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

            else if ("Departments".equals(currentTab)) {
                JTextField txtName = new JTextField();
                JTextField txtHod = new JTextField();
                JTextField txtDegreeId = new JTextField("1");
                JTextField txtStaffCount = new JTextField();

                Object[] message = {
                        "Department Name:", txtName,
                        "Head of Department:", txtHod,
                        "Linked Degree ID:", txtDegreeId,
                        "Staff Count:", txtStaffCount
                };

                if (JOptionPane.showConfirmDialog(view, message, "Add New Department", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = {
                                txtName.getText(),
                                txtHod.getText(),
                                Integer.parseInt(txtDegreeId.getText().trim()),
                                Integer.parseInt(txtStaffCount.getText().trim())
                        };
                        adminDAO.addRecord("Departments", data);
                        refreshDepartmentTable();
                        JOptionPane.showMessageDialog(view, "✅ Department added!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "❌ Invalid input. Degree ID and Staff Count must be numbers.");
                    }
                }
            }

            else if ("Degrees".equals(currentTab)) {
                JTextField txtName = new JTextField();
                JTextField txtDeptId = new JTextField("1");
                JTextField txtStudentCount = new JTextField();

                Object[] message = {
                        "Degree Name:", txtName,
                        "Linked Department ID:", txtDeptId,
                        "Student Count:", txtStudentCount
                };

                if (JOptionPane.showConfirmDialog(view, message, "Add New Degree", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = {
                                txtName.getText(),
                                Integer.parseInt(txtDeptId.getText().trim()),
                                Integer.parseInt(txtStudentCount.getText().trim())
                        };
                        adminDAO.addRecord("Degrees", data);
                        refreshDegreeTable();
                        JOptionPane.showMessageDialog(view, "✅ Degree added!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "❌ Invalid input. Department ID and Student Count must be numbers.");
                    }
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

            else if ("Departments".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Select a department row to edit."); return; }

                int deptId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                JTextField txtName = new JTextField(view.getTableModel().getValueAt(selectedRow, 1).toString());
                JTextField txtHod = new JTextField(view.getTableModel().getValueAt(selectedRow, 2) != null ? view.getTableModel().getValueAt(selectedRow, 2).toString() : "");
                JTextField txtDegreeId = new JTextField("1");
                JTextField txtStaffCount = new JTextField(view.getTableModel().getValueAt(selectedRow, 4).toString());

                Object[] message = {
                        "Department Name:", txtName,
                        "Head of Department:", txtHod,
                        "New Degree ID:", txtDegreeId,
                        "Staff Count:", txtStaffCount
                };

                if (JOptionPane.showConfirmDialog(view, message, "Edit Department", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = {
                                txtName.getText(),
                                txtHod.getText(),
                                Integer.parseInt(txtDegreeId.getText().trim()),
                                Integer.parseInt(txtStaffCount.getText().trim()),
                                deptId
                        };
                        adminDAO.updateRecord("Departments", data);
                        refreshDepartmentTable();
                        JOptionPane.showMessageDialog(view, "✅ Department updated!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "❌ Invalid number format for Degree ID or Staff Count.");
                    }
                }
            }

            else if ("Degrees".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Select a degree row to edit."); return; }

                int degreeId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                JTextField txtName = new JTextField(view.getTableModel().getValueAt(selectedRow, 1).toString());
                JTextField txtDeptId = new JTextField("1");
                JTextField txtStudentCount = new JTextField(view.getTableModel().getValueAt(selectedRow, 3).toString());

                Object[] message = {
                        "Degree Name:", txtName,
                        "New Department ID:", txtDeptId,
                        "Student Count:", txtStudentCount
                };

                if (JOptionPane.showConfirmDialog(view, message, "Edit Degree", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
                    try {
                        Object[] data = {
                                txtName.getText(),
                                Integer.parseInt(txtDeptId.getText().trim()),
                                Integer.parseInt(txtStudentCount.getText().trim()),
                                degreeId
                        };
                        adminDAO.updateRecord("Degrees", data);
                        refreshDegreeTable();
                        JOptionPane.showMessageDialog(view, "✅ Degree updated!");
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(view, "❌ Invalid number format for Department ID or Student Count.");
                    }
                }
            }
        });

        view.getBtnDelete().addActionListener(e -> {
            if ("Students".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Select a row to delete."); return; }
                int studentId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(view, "Permanently wipe this Student record along with credential accounts?", "Confirm Destructive Action", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
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

            else if ("Courses".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Please select a course row to delete."); return; }
                String courseCode = view.getTableModel().getValueAt(selectedRow, 0).toString();
                if (JOptionPane.showConfirmDialog(view, "Delete course " + courseCode + "?", "Confirm Deletion", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    adminDAO.deleteRecord("Courses", courseCode);
                    refreshCourseTable();
                }
            }

            else if ("Departments".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Select a department row to delete."); return; }
                int deptId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(view, "Delete this department?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    adminDAO.deleteRecord("Departments", String.valueOf(deptId));
                    refreshDepartmentTable();
                }
            }

            else if ("Degrees".equals(currentTab)) {
                int selectedRow = view.getTblData().getSelectedRow();
                if (selectedRow == -1) { JOptionPane.showMessageDialog(view, "Select a degree row to delete."); return; }
                int degreeId = (int) view.getTableModel().getValueAt(selectedRow, 0);
                if (JOptionPane.showConfirmDialog(view, "Delete this degree?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    adminDAO.deleteRecord("Degrees", String.valueOf(degreeId));
                    refreshDegreeTable();
                }
            }
        });

        view.getBtnSaveChanges().addActionListener(e -> {
            if ("Students".equals(currentTab)) refreshStudentTable();
            else if ("Lecturers".equals(currentTab)) refreshLecturerTable();
            else if ("Courses".equals(currentTab)) refreshCourseTable();
            else if ("Departments".equals(currentTab)) refreshDepartmentTable();
            else if ("Degrees".equals(currentTab)) refreshDegreeTable();
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

    public void refreshCourseTable() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        for (Object[] row : adminDAO.getAllCourses()) {
            tableModel.addRow(row);
        }
    }

    public void refreshDepartmentTable() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        for (Object[] row : adminDAO.getAllDepartments()) {
            tableModel.addRow(row);
        }
    }

    public void refreshDegreeTable() {
        DefaultTableModel tableModel = view.getTableModel();
        tableModel.setRowCount(0);
        for (Object[] row : adminDAO.getAllDegrees()) {
            tableModel.addRow(row);
        }
    }
}