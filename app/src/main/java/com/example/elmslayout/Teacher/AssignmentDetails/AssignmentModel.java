package com.example.elmslayout.Teacher.AssignmentDetails;

public class AssignmentModel {
    private String assignmentNo;
    private String title;
    private String period;
    private String filePath;
    private String id;
    private String startDate; // New field for start date
    private String endDate;   // New field for end date
    private String score;     // New field for score

    // Default Constructor (Required for Firebase)
    public AssignmentModel() {
    }

    // Full Constructor
    public AssignmentModel(String assignmentNo, String title, String period, String filePath, String id, String startDate, String endDate, String score) {
        this.assignmentNo = assignmentNo;
        this.title = title;
        this.period = period;
        this.filePath = filePath;
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.score = score;
    }

    // Getters and Setters
    public String getAssignmentNo() {
        return assignmentNo;
    }

    public void setAssignmentNo(String assignmentNo) {
        this.assignmentNo = assignmentNo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    // Getter and Setter for score
    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }
}
