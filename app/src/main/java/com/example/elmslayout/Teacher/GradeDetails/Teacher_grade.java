package com.example.elmslayout.Teacher.GradeDetails;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Teacher_grade extends AppCompatActivity {

    private String assignmentNo, title, startDate, endDate, filePath, term, subjectTitle, period, username, assignmentId;
    RecyclerView recyclerView;
    ArrayList<GradeClass> gradeList;
    StudentAdapter adapter;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_grade);

        // Apply window insets for edge-to-edge UI
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        recyclerView = findViewById(R.id.recylerView);

        gradeList = new ArrayList<>();
        adapter = new StudentAdapter(this, gradeList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Retrieve data passed via the Intent
        Intent intent = getIntent();
        assignmentNo = intent.getStringExtra("assignmentNo");
        title = intent.getStringExtra("title");
        startDate = intent.getStringExtra("startDate");
        endDate = intent.getStringExtra("endDate");
        filePath = intent.getStringExtra("filePath");
        term = intent.getStringExtra("term");
        subjectTitle = intent.getStringExtra("subjectTitle");
        period = intent.getStringExtra("period");
        username = intent.getStringExtra("username");
        assignmentId = intent.getStringExtra("assignmentId");

        // Check if subjectTitle, term, and period are not null before calling fetchGrades
        if (subjectTitle != null && term != null && period != null) {
            fetchGrades(subjectTitle, term, period);
        } else {
            showToast("Missing subject/term/period data!");
        }
    }

    private void fetchGrades(String subjectTitle, String term, String period) {
        // Adjust Firebase reference based on your database structure
        DatabaseReference gradesRef = FirebaseDatabase.getInstance()
                .getReference("Grades")
                .child(subjectTitle)
                .child(term)
                .child(period);

        gradesRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gradeList.clear();

                if (snapshot.exists()) {
                    // Iterate through each student in the grades
                    for (DataSnapshot gradeSnapshot : snapshot.getChildren()) {
                        String studentUsername = gradeSnapshot.getKey(); // e.g., "student123"
                        String score = gradeSnapshot.child("score").getValue(String.class);
                        String submittedFilePath = gradeSnapshot.child("submittedFilePath").getValue(String.class);
                        String submittedDate = gradeSnapshot.child("submittedDate").getValue(String.class);
                        String assignmentID = gradeSnapshot.child("assignmentID").getValue(String.class);

                        // Add grade to the list
                        gradeList.add(new GradeClass(assignmentID, studentUsername, score, submittedFilePath, submittedDate, subjectTitle, term, period));
                    }

                    adapter.notifyDataSetChanged(); // Notify adapter to refresh the RecyclerView
                } else {
                    showToast("No grades found for this subject.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error fetching data: " + error.getMessage());
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Modify the method to show the InsertGradeDialog on button click
    private void showInsertGradeDialog(String studentUsername, String assignmentId) {
        InsertGradeDialog gradeDialog = new InsertGradeDialog();
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        bundle.putString("term", term);
        bundle.putString("subjectTitle", subjectTitle);
        bundle.putString("period", period);
        bundle.putString("username", studentUsername);
        bundle.putString("assignmentId", assignmentId);

        gradeDialog.setArguments(bundle);
        gradeDialog.show(getSupportFragmentManager(), "insertGradeDialog");
    }

    // The StudentAdapter will call this method on button click
    public void onGradeButtonClick(String studentUsername, String assignmentId) {
        showInsertGradeDialog(studentUsername, assignmentId);
    }
}
