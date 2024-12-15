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
        databaseReference = firebaseDatabase.getReference("users");

        studentName = findViewById(R.id.admin_text);
        studentCourseYear = findViewById(R.id.student_course_year);


        String username = getIntent().getStringExtra("username");
        if (username != null && !username.isEmpty()) {
            fetchUserBasicInfo(username); // Only update name and course-year
        } else {
            Toast.makeText(this, "Username not provided", Toast.LENGTH_SHORT).show();
            finish();
        }



        myProfileLayout = findViewById(R.id.profile_layout);
        resourcesLayout = findViewById(R.id.resources_layout);
        balancesLayout = findViewById(R.id.balances_layout);
        logoutLayout = findViewById(R.id.logout_layout);

        myProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (username != null && !username.isEmpty()) {
                    getData(username); // Fetch data for this username
                } else {
                    Toast.makeText(User_Profile.this, "username not provided", Toast.LENGTH_SHORT).show();
                    finish();
                }

            }
        });

        resourcesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(User_Profile.this)
                        .setTitle("Feature Coming Soon")
                        .setMessage("This feature will be available soon.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Dismiss the dialog
                            dialog.dismiss();
                        })
                        .show();

            }

        });

        balancesLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(User_Profile.this)
                        .setTitle("Feature Coming Soon")
                        .setMessage("This feature will be available soon.")
                        .setPositiveButton("OK", (dialog, which) -> {
                            // Dismiss the dialog
                            dialog.dismiss();
                        })
                        .show();

            }
        });

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new AlertDialog.Builder(User_Profile.this)
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
                        .show();
            }
        });

    }

    private void getData(String username) {
        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    // Fetch the data from the snapshot
                    String roleFromDB = snapshot.child("role").getValue(String.class);
                    String studentNameFromDB = snapshot.child("name").getValue(String.class);
                    String studentCourseFromDB = snapshot.child("course").getValue(String.class);
                    String studentYearFromDB = snapshot.child("year").getValue(String.class);

                    // Calculate subject size
                    long subjectCount = snapshot.child("subject").getChildrenCount();

                    // Update the UI with the retrieved data
                    studentName.setText(studentNameFromDB);
                    studentCourseYear.setText(studentCourseFromDB + " - " + studentYearFromDB);

                    // Show AlertDialog with user information
                    showUserInfoDialog(roleFromDB, studentNameFromDB, studentCourseFromDB, studentYearFromDB, subjectCount);
                } else {
                    Toast.makeText(User_Profile.this, "User data not found", Toast.LENGTH_SHORT).show();
                }
            }

            private void showUserInfoDialog(String roleFromDB, String studentNameFromDB, String studentCourseFromDB, String studentYearFromDB, long subjectCount) {
                String message = "Role: " + (roleFromDB != null ? roleFromDB : "N/A") +
                        "\nName: " + (studentNameFromDB != null ? studentNameFromDB : "N/A") +
                        "\nCourse: " + (studentCourseFromDB != null ? studentCourseFromDB : "N/A") +
                        "\nYear: " + (studentYearFromDB != null ? studentYearFromDB : "N/A") +
                        "\nSubjects: " + subjectCount;

                new AlertDialog.Builder(User_Profile.this)
                        .setTitle("User Information")
                        .setMessage(message)
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", error.getMessage());
                Toast.makeText(User_Profile.this, "Failed to load data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchUserBasicInfo(String username) {
        databaseReference.child(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String studentNameFromDB = snapshot.child("name").getValue(String.class);
                    String studentCourseFromDB = snapshot.child("course").getValue(String.class);
                    String studentYearFromDB = snapshot.child("year").getValue(String.class);

                    // Update the UI immediately with name and course-year
                    studentName.setText(studentNameFromDB);
                    studentCourseYear.setText(studentCourseFromDB + " - " + studentYearFromDB);
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


}