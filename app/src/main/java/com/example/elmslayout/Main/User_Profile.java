package com.example.elmslayout.Main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class User_Profile extends AppCompatActivity {

    ConstraintLayout myProfileLayout, resourcesLayout, balancesLayout, logoutLayout;
    TextView studentName, studentCourseYear;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_profile);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("User/Role");

        studentName = findViewById(R.id.admin_text);
        studentCourseYear = findViewById(R.id.student_course_year);

        myProfileLayout = findViewById(R.id.profile_layout);
        resourcesLayout = findViewById(R.id.resources_layout);
        balancesLayout = findViewById(R.id.balances_layout);
        logoutLayout = findViewById(R.id.logout_layout);

        // Fetch data from intent
        String role = getIntent().getStringExtra("role");
        String username = getIntent().getStringExtra("username");

        if (role != null && username != null && !username.isEmpty()) {
            fetchUserBasicInfo(role, username); // Fetch basic information
        } else {
            Toast.makeText(this, "Role or Username not provided", Toast.LENGTH_SHORT).show();
            finish();
        }

        myProfileLayout.setOnClickListener(v -> {
            if (role != null && username != null && !username.isEmpty()) {
                fetchDetailedUserInfo(role, username); // Fetch detailed info on click
            } else {
                Toast.makeText(User_Profile.this, "Username not provided", Toast.LENGTH_SHORT).show();
            }
        });

        resourcesLayout.setOnClickListener(v -> new AlertDialog.Builder(User_Profile.this)
                .setTitle("Feature Coming Soon")
                .setMessage("This feature will be available soon.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show());

        balancesLayout.setOnClickListener(v -> new AlertDialog.Builder(User_Profile.this)
                .setTitle("Feature Coming Soon")
                .setMessage("This feature will be available soon.")
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show());

        logoutLayout.setOnClickListener(v -> new AlertDialog.Builder(User_Profile.this)
                .setTitle("Logout")
                .setMessage("Are you sure you want to log out?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    // Clear session and redirect
                    SharedPreferences preferences = getSharedPreferences("userSession", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(User_Profile.this, Login.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("No", null)
                .show());
    }

    private void fetchUserBasicInfo(String role, String username) {
        // Fetch basic user info (name and course-year)
        databaseReference.child(role).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String course = snapshot.child("course").getValue(String.class);
                    String year = snapshot.child("year").getValue(String.class);

                    studentName.setText(name != null ? name : "N/A");
                    studentCourseYear.setText((course != null ? course : "N/A") + " - " + (year != null ? year : "N/A"));
                } else {
                    Toast.makeText(User_Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(User_Profile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchDetailedUserInfo(String role, String username) {
        // Fetch detailed user info to display in an AlertDialog
        databaseReference.child(role).child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String name = snapshot.child("name").getValue(String.class);
                    String course = snapshot.child("course").getValue(String.class);
                    String year = snapshot.child("year").getValue(String.class);
                    long subjectCount = snapshot.child("Subjects").getChildrenCount();

                    showUserInfoDialog(role, name, course, year, subjectCount);
                } else {
                    Toast.makeText(User_Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(User_Profile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUserInfoDialog(String role, String name, String course, String year, long subjectCount) {
        String message = "Role: " + role +
                "\nName: " + (name != null ? name : "N/A") +
                "\nCourse: " + (course != null ? course : "N/A") +
                "\nYear: " + (year != null ? year : "N/A") +
                "\nSubjects: " + subjectCount;

        new AlertDialog.Builder(User_Profile.this)
                .setTitle("User Information")
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }
}
