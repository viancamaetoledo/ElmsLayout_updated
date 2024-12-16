package com.example.elmslayout.Student.Course;

public class DataClass {
    public DataClass(String subjectName, String termName) {
        this.subjectName = subjectName;
        this.termName = termName;
    }

    public DataClass() {
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

    private String subjectName;
    private String termName;





}
