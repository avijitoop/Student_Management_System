
package com.student.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.sql.SQLException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.DefaultTableModel;

import com.student.model.Student;
import com.student.service.StudentService;

public class StudentDashboard extends JFrame {

    private final StudentService service = new StudentService();

    private JTextField txtStudentId;
    private JTextField txtName;
    private JTextField txtAge;
    private JTextField txtDepartment;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtSearch;

    private JComboBox<String> cmbGender;

    private JTable table;
    private DefaultTableModel tableModel;

    private String selectedStudentId = null;

    public StudentDashboard() {

        setTitle("Student Management System");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        buildDashboard();
        loadStudents();
    }

    private void buildDashboard() {

        setLayout(new BorderLayout(10, 10));

        JLabel title = new JLabel(
                "STUDENT MANAGEMENT SYSTEM",
                SwingConstants.CENTER
        );

        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(Color.WHITE);
        title.setOpaque(true);
        title.setBackground(new Color(11, 79, 156));
        title.setBorder(
                BorderFactory.createEmptyBorder(20, 10, 20, 10)
        );

        add(title, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel(
                new BorderLayout(10, 10)
        );

        centerPanel.setBorder(
                BorderFactory.createEmptyBorder(10, 15, 10, 15)
        );

        JPanel formPanel = new JPanel(
                new GridLayout(4, 4, 10, 12)
        );

        formPanel.setBorder(
                BorderFactory.createTitledBorder("Student Information")
        );

        txtStudentId = new JTextField();
        txtName = new JTextField();
        txtAge = new JTextField();
        txtDepartment = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();

        cmbGender = new JComboBox<>(
                new String[]{"Male", "Female", "Other"}
        );

        formPanel.add(new JLabel("Student ID:"));
        formPanel.add(txtStudentId);

        formPanel.add(new JLabel("Full Name:"));
        formPanel.add(txtName);

        formPanel.add(new JLabel("Age:"));
        formPanel.add(txtAge);

        formPanel.add(new JLabel("Gender:"));
        formPanel.add(cmbGender);

        formPanel.add(new JLabel("Department:"));
        formPanel.add(txtDepartment);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(txtEmail);

        formPanel.add(new JLabel("Phone:"));
        formPanel.add(txtPhone);

        JButton btnAdd = new JButton("Add Student");
        JButton btnUpdate = new JButton("Update Student");
        JButton btnDelete = new JButton("Delete Student");
        JButton btnClear = new JButton("Clear Form");

        formPanel.add(btnAdd);
        formPanel.add(btnUpdate);
        formPanel.add(btnDelete);
        formPanel.add(btnClear);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(
                new FlowLayout(FlowLayout.LEFT)
        );

        txtSearch = new JTextField(25);

        JButton btnSearch = new JButton("Search");
        JButton btnShowAll = new JButton("Show All");

        searchPanel.add(new JLabel("Search Student:"));
        searchPanel.add(txtSearch);
        searchPanel.add(btnSearch);
        searchPanel.add(btnShowAll);

        String[] columns = {
            "Student ID",
            "Name",
            "Age",
            "Gender",
            "Department",
            "Email",
            "Phone"
        };

        tableModel = new DefaultTableModel(columns, 0) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setFont(new Font("Arial", Font.PLAIN, 13));

        JScrollPane scrollPane = new JScrollPane(table);

        JPanel tablePanel = new JPanel(
                new BorderLayout(5, 5)
        );

        tablePanel.setBorder(
                BorderFactory.createTitledBorder("Student Records")
        );

        tablePanel.add(searchPanel, BorderLayout.NORTH);
        tablePanel.add(scrollPane, BorderLayout.CENTER);

        centerPanel.add(tablePanel, BorderLayout.CENTER);

        add(centerPanel, BorderLayout.CENTER);

        // Button Actions

        btnAdd.addActionListener(e -> addStudent());

        btnUpdate.addActionListener(e -> updateStudent());

        btnDelete.addActionListener(e -> deleteStudent());

        btnClear.addActionListener(e -> clearForm());

        btnSearch.addActionListener(e -> searchStudents());

        btnShowAll.addActionListener(e -> {
            txtSearch.setText("");
            loadStudents();
        });

        // Table Row Selection

        table.getSelectionModel().addListSelectionListener(e -> {

            if (!e.getValueIsAdjusting()) {

                int row = table.getSelectedRow();

                if (row >= 0) {
                    fillFormFromTable(row);
                }
            }
        });
    }

    // Read Form Data

    private Student getStudentFromForm() {

        String studentId = txtStudentId.getText().trim();
        String name = txtName.getText().trim();
        String ageText = txtAge.getText().trim();
        String department = txtDepartment.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();

        if (studentId.isEmpty() || name.isEmpty()
                || ageText.isEmpty()) {

            throw new IllegalArgumentException(
                    "Student ID, Name and Age are required!"
            );
        }

        int age;

        try {
            age = Integer.parseInt(ageText);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException(
                    "Age must be a valid number!"
            );
        }

        if (age < 1 || age > 120) {
            throw new IllegalArgumentException(
                    "Age must be between 1 and 120!"
            );
        }

        return new Student(
                studentId,
                name,
                age,
                (String) cmbGender.getSelectedItem(),
                department,
                email,
                phone
        );
    }

    // Add Student

    private void addStudent() {

        try {

            Student student = getStudentFromForm();

            service.addStudent(student);

            JOptionPane.showMessageDialog(
                    this,
                    "Student Added Successfully!"
            );

            clearForm();
            loadStudents();

        } catch (IllegalArgumentException | SQLException ex) {

            showError(ex);
        }
    }

    // Update Student

    private void updateStudent() {

        if (selectedStudentId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student from the table!"
            );

            return;
        }

        try {

            Student student = getStudentFromForm();

            service.updateStudent(student, selectedStudentId);

            JOptionPane.showMessageDialog(
                    this,
                    "Student Updated Successfully!"
            );

            clearForm();
            loadStudents();

        } catch (IllegalArgumentException | SQLException ex) {

            showError(ex);
        }
    }

    // Delete Student

    private void deleteStudent() {

        if (selectedStudentId == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please select a student first!"
            );

            return;
        }

        int choice = JOptionPane.showConfirmDialog(
                this,
                "Are you sure you want to delete this student?",
                "Confirm Delete",
                JOptionPane.YES_NO_OPTION
        );

        if (choice == JOptionPane.YES_OPTION) {

            try {

                service.deleteStudent(selectedStudentId);

                JOptionPane.showMessageDialog(
                        this,
                        "Student Deleted Successfully!"
                );

                clearForm();
                loadStudents();

            } catch (SQLException ex) {

                showError(ex);
            }
        }
    }

    // Load All Students

    private void loadStudents() {

        try {

            List<Student> students = service.getAllStudents();

            displayStudents(students);

        } catch (SQLException ex) {

            showError(ex);
        }
    }

    // Search Students

    private void searchStudents() {

        try {

            List<Student> students =
                    service.searchStudents(txtSearch.getText().trim());

            displayStudents(students);

        } catch (SQLException ex) {

            showError(ex);
        }
    }

    // Display Data in JTable

    private void displayStudents(List<Student> students) {

        tableModel.setRowCount(0);

        for (Student student : students) {

            Object[] row = {
                student.getStudentId(),
                student.getName(),
                student.getAge(),
                student.getGender(),
                student.getDepartment(),
                student.getEmail(),
                student.getPhone()
            };

            tableModel.addRow(row);
        }
    }

    // Fill Form from Selected Table Row

    private void fillFormFromTable(int row) {

        selectedStudentId =
                tableModel.getValueAt(row, 0).toString();

        txtStudentId.setText(selectedStudentId);

        txtName.setText(
                tableModel.getValueAt(row, 1).toString()
        );

        txtAge.setText(
                tableModel.getValueAt(row, 2).toString()
        );

        cmbGender.setSelectedItem(
                tableModel.getValueAt(row, 3).toString()
        );

        txtDepartment.setText(
                tableModel.getValueAt(row, 4).toString()
        );

        txtEmail.setText(
                tableModel.getValueAt(row, 5).toString()
        );

        txtPhone.setText(
                tableModel.getValueAt(row, 6).toString()
        );
    }

    // Clear Form

    private void clearForm() {

        txtStudentId.setText("");
        txtName.setText("");
        txtAge.setText("");
        txtDepartment.setText("");
        txtEmail.setText("");
        txtPhone.setText("");

        cmbGender.setSelectedIndex(0);

        selectedStudentId = null;

        table.clearSelection();

        txtStudentId.requestFocus();
    }

    // Error Message

    private void showError(Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                ex.getMessage(),
                "Error",
                JOptionPane.ERROR_MESSAGE
        );

        ex.printStackTrace();
    }
}
