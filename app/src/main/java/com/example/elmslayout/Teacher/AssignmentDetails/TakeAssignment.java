package com.example.elmslayout.Teacher.AssignmentDetails;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.R;
import com.example.elmslayout.Student.Assignment.submit_Assignment;

public class TakeAssignment extends AppCompatActivity implements submit_Assignment.SubmissionListener {

    // Existing UI elements
    TextView termTextView, subjectTitleTextView, assignmentNoTextView, titleTextView;
    TextView startDateTextView, endDateTextView, scoreTextView, submittedTimeTextView, submitteFileTextView;
    Button prepareAnswerButton;
    TextView linkFileTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_take_assignment);

        // Initialize views (already done in your code)
        termTextView = findViewById(R.id.terms);
        subjectTitleTextView = findViewById(R.id.subjectTitle);
        assignmentNoTextView = findViewById(R.id.assignment_number);
        titleTextView = findViewById(R.id.detailedTitle);
        submittedTimeTextView = findViewById(R.id.SubmittedTime);
        scoreTextView = findViewById(R.id.seeScore);
        linkFileTextView = findViewById(R.id.linkFilePath);
        startDateTextView = findViewById(R.id.StartDate);
        endDateTextView = findViewById(R.id.EndDate);
        submitteFileTextView = findViewById(R.id.submittedLinkFilePath);

        prepareAnswerButton = findViewById(R.id.prepareAnswerButton);

        // Pass data
        String term = getIntent().getStringExtra("term");
        String subjectTitle = getIntent().getStringExtra("subjectTitle");
        String period = getIntent().getStringExtra("period");
        String assignmentId = getIntent().getStringExtra("assignmentId");

        // Set data
        termTextView.setText(term);
        subjectTitleTextView.setText(subjectTitle);
        assignmentNoTextView.setText(getIntent().getStringExtra("assignmentNo"));

        prepareAnswerButton.setOnClickListener(v -> {
            openPrepareAnswerDialog(term, subjectTitle, period, assignmentId);
        });
    }

    private void openPrepareAnswerDialog(String term, String subject, String period, String assignmentId) {
        submit_Assignment dialog = new submit_Assignment();
        Bundle args = new Bundle();
        args.putString("term", term);
        args.putString("subject", subject);
        args.putString("period", period);
        args.putString("assignmentId", assignmentId);
        args.putString("username", getIntent().getStringExtra("username"));
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "PrepareAnswerDialog");
    }

    // Callback method from submit_Assignment
    @Override
    public void onSubmissionComplete(String filePath, String submitTime, String score) {
        updateSubmittedFile(filePath, submitTime, score);
    }

    // Update the UI
    public void updateSubmittedFile(String filePath, String submitTime, String score) {
        submitteFileTextView.setText(filePath);  // Display submitted file path
        submittedTimeTextView.setText("Submitted Time: " + submitTime);
        scoreTextView.setText("Score: " + score);
    }
}