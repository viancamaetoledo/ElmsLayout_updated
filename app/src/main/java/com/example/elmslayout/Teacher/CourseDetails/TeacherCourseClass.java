package com.example.elmslayout.Teacher.CourseDetails;

public class TeacherCourseClass {
    private String subjectName;
    private String termName;

    public TeacherCourseClass(String subjectName, String termName) {
        this.subjectName = subjectName;
        this.termName = termName;
    }

    public TeacherCourseClass() {
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
}
