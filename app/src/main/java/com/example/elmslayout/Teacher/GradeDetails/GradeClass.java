package com.example.elmslayout.Teacher.GradeDetails;

public class GradeClass {
    private String subjectTitle;
    private String studentUsername;
    private String score;
    private String assignmentID;
    private String submittedFilePath;
    private String submittedDate;
    private String period;
    private String term;

    // Default constructor
    public GradeClass() {
        // Empty constructor required for Firebase
    }

    // Constructor
    public GradeClass(String subjectTitle, String studentUsername, String score,
                        String assignmentID, String submittedFilePath, String submittedDate,
                        String period, String term) {
        this.subjectTitle = subjectTitle;
        this.studentUsername = studentUsername;
        this.score = score;
        this.assignmentID = assignmentID;
        this.submittedFilePath = submittedFilePath;
        this.submittedDate = submittedDate;
        this.period = period;
        this.term = term;
    }

    // Getters
    public String getSubjectTitle() {
        return subjectTitle;
    }

    public String getStudentUsername() {
        return studentUsername;
    }

    public String getScore() {
        return score;
    }

    public String getAssignmentID() {
        return assignmentID;
    }

    public String getSubmittedFilePath() {
        return submittedFilePath;
    }

    public String getSubmittedDate() {
        return submittedDate;
    }

    public String getPeriod() {
        return period;
    }

    public String getTerm() {
        return term;
    }
}
