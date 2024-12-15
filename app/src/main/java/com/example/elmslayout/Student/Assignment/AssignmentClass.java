package com.example.elmslayout.Student.Assignment;

public class AssignmentClass {

    public AssignmentClass() {
    }


    public AssignmentClass(String assignmentNo, String term) {
        this.assignmentNo = assignmentNo;
        this.term = term;
    }

    private String assignmentNo;

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getAssignmentNo() {
        return assignmentNo;
    }

    public void setAssignmentNo(String assignmentNo) {
        this.assignmentNo = assignmentNo;
    }

    private String term;

}
