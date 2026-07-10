package com.faculty.controller;

import com.faculty.dao.AdminDAO;
import com.faculty.view.AdminDashboardView;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;

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
        view.getBtnLecturers().addActionListener(e -> { currentTab = "Lecturers"; view.updateTabHeaders("Lecturers"); view.getTableModel().setRowCount(0); });
        view.getBtnCourses().addActionListener(e -> { currentTab = "Courses"; view.updateTabHeaders("Courses"); view.getTableModel().setRowCount(0); });
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
        });

        view.getBtnSaveChanges().addActionListener(e -> {
            if ("Students".equals(currentTab)) refreshStudentTable();
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
}