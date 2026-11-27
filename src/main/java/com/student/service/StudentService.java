
package com.student.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.student.DBConnection;
import com.student.model.Student;

public class StudentService {

    // Add Student
    public void addStudent(Student student) throws SQLException {

        String sql = "INSERT INTO students "
                + "(student_id, name, age, gender, department, email, phone) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getGender());
            ps.setString(5, student.getDepartment());
            ps.setString(6, student.getEmail());
            ps.setString(7, student.getPhone());

            ps.executeUpdate();
        }
    }

    // View All Students
    public List<Student> getAllStudents() throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                students.add(mapStudent(rs));
            }
        }

        return students;
    }

    // Search Student
    public List<Student> searchStudents(String keyword)
            throws SQLException {

        List<Student> students = new ArrayList<>();

        String sql = "SELECT * FROM students "
                + "WHERE student_id LIKE ? "
                + "OR name LIKE ? "
                + "OR phone LIKE ? "
                + "ORDER BY id DESC";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            String search = "%" + keyword + "%";

            ps.setString(1, search);
            ps.setString(2, search);
            ps.setString(3, search);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    students.add(mapStudent(rs));
                }
            }
        }

        return students;
    }

    // Update Student
    public void updateStudent(Student student, String oldStudentId)
            throws SQLException {

        String sql = "UPDATE students SET "
                + "student_id=?, name=?, age=?, gender=?, "
                + "department=?, email=?, phone=? "
                + "WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, student.getStudentId());
            ps.setString(2, student.getName());
            ps.setInt(3, student.getAge());
            ps.setString(4, student.getGender());
            ps.setString(5, student.getDepartment());
            ps.setString(6, student.getEmail());
            ps.setString(7, student.getPhone());
            ps.setString(8, oldStudentId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Student not found!");
            }
        }
    }

    // Delete Student
    public void deleteStudent(String studentId) throws SQLException {

        String sql = "DELETE FROM students WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, studentId);

            int rows = ps.executeUpdate();

            if (rows == 0) {
                throw new SQLException("Student not found!");
            }
        }
    }

    // Convert ResultSet into Student Object
    private Student mapStudent(ResultSet rs) throws SQLException {

        return new Student(
                rs.getString("student_id"),
                rs.getString("name"),
                rs.getInt("age"),
                rs.getString("gender"),
                rs.getString("department"),
                rs.getString("email"),
                rs.getString("phone")
        );
    }
}
