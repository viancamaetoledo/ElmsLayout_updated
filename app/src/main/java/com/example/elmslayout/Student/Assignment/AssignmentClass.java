package com.example.elmslayout.Student.Assignment;

public class AssignmentClass {
    public AssignmentClass(String subjectName, String termName) {
        this.subjectName = subjectName;
        this.termName = termName;
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





}
