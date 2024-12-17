package com.example.elmslayout.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.CourseDetails.Teacher_course;
import com.example.elmslayout.Teacher.AssignmentDetails.Teacher_assignment;
import com.example.elmslayout.Teacher.GradeDetails.Teacher_grade;
import com.example.elmslayout.Teacher.Teacher_student;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Teachers_Dashboard extends AppCompatActivity {

    private static final String ROLE_KEY = "role"; // Key for verifying the role

    ImageView userProfile;
    TextView teacherName, teacherCourseInfo;
    ConstraintLayout course, assignment, news, grades, students;

    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teachers_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI components
        course = findViewById(R.id.course);
        assignment = findViewById(R.id.assignment);
        news = findViewById(R.id.news);
        grades = findViewById(R.id.grades);
        students = findViewById(R.id.student);
        userProfile = findViewById(R.id.user_profile_img);

        teacherName = findViewById(R.id.teacher_name_text);
        teacherCourseInfo = findViewById(R.id.teacher_course_info);


        // Firebase reference
        databaseReference = FirebaseDatabase.getInstance().getReference("User")
                .child("Role").child("Teacher");

        // Fetch username from Intent
        String username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            getTeacherData(username); // Fetch teacher's data
        } else {
            Toast.makeText(this, "Username not provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        String name = teacherName.getText().toString().trim();

        // Click listeners to navigate to other activities
        course.setOnClickListener(v -> openActivity(Teacher_course.class, username));
        assignment.setOnClickListener(v -> openActivity(Teacher_assignment.class, username));
        news.setOnClickListener(v ->  {
                new AlertDialog.Builder(Teachers_Dashboard.this)
                .setTitle("Feature Coming Soon")
                .setMessage("This feature will be available soon.")
                .setPositiveButton("OK", (dialog, which) -> {
                    // Dismiss the dialog
                    dialog.dismiss();
                })
                .show();
    });

        grades.setOnClickListener(v -> {Intent intent = new Intent(Teachers_Dashboard.this, Teacher_grade.class);
            intent.putExtra("period", "Prelims");
            intent.putExtra("subjectTitle", "Advanced Database System");
            intent.putExtra("term", "First Term");

            // Pass username instead of name
            startActivity(intent);});
        students.setOnClickListener(v -> openActivity(Teacher_student.class, username));

        userProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Teachers_Dashboard.this, User_Profile.class);
            intent.putExtra("username", username);
            intent.putExtra("role", "Teacher");
            startActivity(intent);
        });
    }

    // Method to fetch teacher data from Firebase
    private void getTeacherData(String username) {
        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch and set teacher details
                    String teacherNameFromDB = snapshot.child("name").getValue(String.class);
                    String teacherCourseFromDB = snapshot.child("course").getValue(String.class);

                    // Update the UI
                    if (teacherNameFromDB != null) {
                        teacherName.setText(teacherNameFromDB);
                    } else {
                        teacherName.setText("Unknown Teacher");
                    }

                    if (teacherCourseFromDB != null) {
                        teacherCourseInfo.setText(teacherCourseFromDB);
                    } else {
                        teacherCourseInfo.setText("No Course Info");
                    }
                } else {
                    Toast.makeText(Teachers_Dashboard.this, "Teacher data not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(Teachers_Dashboard.this, "Failed to load teacher data", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void openActivity(Class<?> targetActivity, String name) {

        Intent intent = new Intent(Teachers_Dashboard.this, targetActivity);
        intent.putExtra("period", "Prelims");
        intent.putExtra("subjectTitle", "Advanced Database System");
        intent.putExtra("term", "First Term");

        // Pass username instead of name
        startActivity(intent);
    }
}
