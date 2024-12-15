package com.example.elmslayout.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.Main.User_Profile;
import com.example.elmslayout.R;

public class Admin_Dashboard extends AppCompatActivity {

    ConstraintLayout addStudentLayout, addStudentSubjectLayout, addTeacherSubjectLayout, addTeacherLayout;
    ImageView profileImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_dashboard);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        addStudentLayout = findViewById(R.id.student_layout);
        addStudentSubjectLayout = findViewById(R.id.addStudentSubject_layout);
        profileImg = findViewById(R.id.user_profile_img);
        addTeacherSubjectLayout = findViewById(R.id.addTeacherSubject_layout);
        addTeacherLayout = findViewById(R.id.addTeacher_layout);

        profileImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Dashboard.this, User_Profile.class);
                startActivity(intent);
            }
        });

        addTeacherSubjectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Dashboard.this, addTeacherSubject.class);
                startActivity(intent);
            }
        });

        addTeacherLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Dashboard.this, addTeacher.class);
                startActivity(intent);
            }
        });

        addStudentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Dashboard.this, addStudent.class);
                startActivity(intent);
            }
        });

        addStudentSubjectLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Admin_Dashboard.this, addSubject.class);
                startActivity(intent);
            }
        });

    }
}