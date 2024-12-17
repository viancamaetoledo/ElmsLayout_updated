package com.example.elmslayout.Student.Assignment;

public class AssignmentClass {
    public AssignmentClass(String subjectName, String termName, String username) {
        this.subjectName = subjectName;
        this.termName = termName;
        this.username = username;
    }

    public AssignmentClass() {
    }

    public String getSubjectName() {
        return subjectName;
    }

    public void setSubjectName(String subjectName) {
        this.subjectName = subjectName;
    }

    public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

     String subjectName;
     String termName;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    String username;





}
