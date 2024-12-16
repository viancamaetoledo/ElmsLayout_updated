package com.example.elmslayout.Teacher.AssignmentDetails;

public class TeacherAssignClass {
    private String subjectName;

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

    private String termName;

    public TeacherAssignClass(String subjectName, String termName) {
        this.subjectName = subjectName;
        this.termName = termName;
    }

}
