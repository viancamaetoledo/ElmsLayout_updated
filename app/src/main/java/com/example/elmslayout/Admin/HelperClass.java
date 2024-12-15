package com.example.elmslayout.Admin;

public class HelperClass {
    String name;
    String email;
    String username;
    String password;
    String role;

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    String course;
    String year;

    // Constructor including the role
    public HelperClass(String name, String email, String username, String password, String role, String course, String year) {
        this.name = name;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.course = course;
        this.year = year;
    }

    // Default constructor (needed for Firebase)
    public HelperClass() {
    }

    // Getters and setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
