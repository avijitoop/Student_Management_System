
package com.student.model;

public class Student extends Person {

    private String studentId;
    private String gender;
    private String department;
    private String email;
    private String phone;

    public Student(String studentId, String name, int age,
                   String gender, String department,
                   String email, String phone) {

        super(name, age);

        this.studentId = studentId;
        this.gender = gender;
        this.department = department;
        this.email = email;
        this.phone = phone;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public String getDetails() {

        return "Student ID: " + studentId
                + ", Name: " + getName()
                + ", Department: " + department;
    }
}
