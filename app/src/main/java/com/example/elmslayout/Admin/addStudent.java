package com.example.elmslayout.Admin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addStudent extends AppCompatActivity {
    Spinner programSpinner, yearSpinner;
    EditText signUpName, signUpEmail, signUsername, signUpPassword;
    Button signUpButton;
    RadioGroup rg;
    FirebaseDatabase database;

    DatabaseReference reference;
    ArrayList<String> programList, yearList;
    String selectedProgram, selectedYear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_student);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        signUpName = findViewById(R.id.login_fullname);
        signUpEmail = findViewById(R.id.login_email);
        signUsername = findViewById(R.id.login_username);
        signUpPassword = findViewById(R.id.login_password);
        signUpButton = findViewById(R.id.add_button);
        programSpinner = findViewById(R.id.program_choices);
        yearSpinner = findViewById(R.id.year_choices);

        programList = new ArrayList<>();
        yearList = new ArrayList<>();
        loadPrograms();
        loadYears();

        // Firebase reference
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("User").child("Role");

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String role = "Student";

                // Get input values
                String name = signUpName.getText().toString();
                String email = signUpEmail.getText().toString();
                String username = signUsername.getText().toString();
                String password = signUpPassword.getText().toString();

                // Get selected program and year
                selectedProgram = programSpinner.getSelectedItem() != null ? programSpinner.getSelectedItem().toString() : "";
                selectedYear = yearSpinner.getSelectedItem() != null ? yearSpinner.getSelectedItem().toString() : "";

                // Validate input
                if (name.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(addStudent.this, "All fields are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedProgram.isEmpty()) {
                    Toast.makeText(addStudent.this, "Please select a program", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedYear.isEmpty()) {
                    Toast.makeText(addStudent.this, "Please select a year", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Check role and set data to correct path
                DatabaseReference roleReference;
                if (role.equalsIgnoreCase("Admin")) {
                    roleReference = reference.child("Admin");
                } else if (role.equalsIgnoreCase("Teacher")) {
                    roleReference = reference.child("Teacher");
                } else {
                    roleReference = reference.child("Student");
                }

                HelperClass helperClass = new HelperClass(name, email, username, password, role, selectedProgram, selectedYear);
                roleReference.child(username).setValue(helperClass)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(addStudent.this, "User added successfully as " + role, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(addStudent.this, Admin_Dashboard.class);
                            startActivity(intent);
                        })
                        .addOnFailureListener(e -> Toast.makeText(addStudent.this, "Failed to add user. Try again.", Toast.LENGTH_SHORT).show());
            }
        });
    }

    private void loadYears() {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Year")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        yearList.clear();
                        for (DataSnapshot yearSnapshot : snapshot.getChildren()) {
                            String year = yearSnapshot.getValue(String.class);
                            if (year != null) {
                                yearList.add(year);
                            }
                        }
                        populateSpinner(yearSpinner, yearList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast("Failed to load years.");
                    }
                });
    }

    private void loadPrograms() {
        reference = FirebaseDatabase.getInstance().getReference();
        reference.child("Program")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        programList.clear();
                        for (DataSnapshot programSnapshot : snapshot.getChildren()) {
                            String program = programSnapshot.getValue(String.class);
                            if (program != null) {
                                programList.add(program);
                            }
                        }
                        populateSpinner(programSpinner, programList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast("Failed to load programs.");
                    }
                });
    }

    private void populateSpinner(Spinner spinner, ArrayList<String> list) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
