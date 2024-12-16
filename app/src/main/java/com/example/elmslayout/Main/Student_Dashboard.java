package com.example.elmslayout.Main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.Student.Announcement.Announcement_Menu;
import com.example.elmslayout.Student.Assignment.Assignment_Menu;
import com.example.elmslayout.Student.Course.Course_Menu;
import com.example.elmslayout.Student.Grade.Grade_Menu;
import com.example.elmslayout.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Student_Dashboard extends AppCompatActivity {

    private static final String NAME_KEY = "name";
    private static final String COURSE_KEY = "course";
    private static final String YEAR_KEY = "year";

    ImageView userProfile;
    ConstraintLayout courseLayout, assignmentLayout, announcementLayout, gradesLayout;
    TextView studentName, studentCourseYear;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    String term = "";  // Store the term
    boolean isDataLoaded = false; // Flag to ensure data is loaded

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_student_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/Role");

        studentName = findViewById(R.id.admin_text);
        studentCourseYear = findViewById(R.id.student_course_year);

        String username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            getData(username); // Fetch data for this username
        } else {
            Toast.makeText(this, "Username not provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        // Course Menu Click
        courseLayout = findViewById(R.id.student_layout);
        courseLayout.setOnClickListener(v -> navigateToMenu(username, Course_Menu.class));

        // Assignment Menu Click
        assignmentLayout = findViewById(R.id.addTeacherSubject_layout);
        assignmentLayout.setOnClickListener(v -> navigateToMenu(username, Assignment_Menu.class));

        // Announcement Menu Click
        announcementLayout = findViewById(R.id.announce_layout);
        announcementLayout.setOnClickListener(v -> navigateToMenu(username, Announcement_Menu.class));

        // Grades Menu Click
        gradesLayout = findViewById(R.id.grades_layout);
        gradesLayout.setOnClickListener(v -> navigateToMenu(username, Grade_Menu.class));

        // Profile Click
        userProfile = findViewById(R.id.user_profile_img);
        userProfile.setOnClickListener(v -> {
            Intent intent = new Intent(Student_Dashboard.this, User_Profile.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }

    private void navigateToMenu(String username, Class<?> destination) {
        if (!isDataLoaded || term.isEmpty()) {
            Toast.makeText(this, "Please wait, data is still loading...", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Student_Dashboard.this, destination);
        intent.putExtra("username", username);
        intent.putExtra("term", term);
        startActivity(intent);
    }

    // Updated getData method
    private void getData(String username) {
        databaseReference.child("Student").child(username)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String studentNameFromDB = snapshot.child("name").getValue(String.class);
                            String studentProgramFromDB = snapshot.child("Program").getValue(String.class);
                            String studentYearFromDB = snapshot.child("Year").getValue(String.class);
                            term = snapshot.child("Semester").getValue(String.class);  // Retrieve term from the database

                            if (term == null || term.isEmpty()) {
                                term = "Default Term";  // Fallback value
                            }

                            studentName.setText(studentNameFromDB);
                            studentCourseYear.setText(studentProgramFromDB + " - " + studentYearFromDB);

                            isDataLoaded = true; // Set flag to true
                        } else {
                            Toast.makeText(Student_Dashboard.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("FirebaseError", error.getMessage());
                        Toast.makeText(Student_Dashboard.this, "Failed to load data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
