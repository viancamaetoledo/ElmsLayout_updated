package com.example.elmslayout.Admin;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.elmslayout.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class addTeacherSubject extends AppCompatActivity {

    Spinner teacherSpinner, subjectSpinner, termSpinner;
    Button addButton;

    DatabaseReference databaseReference;
    ArrayList<String> teacherList, usernameList, subjectList, termList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher_subject);

        // Initialize Views
        teacherSpinner = findViewById(R.id.student_choices);
        subjectSpinner = findViewById(R.id.course_choices);
        termSpinner = findViewById(R.id.term_choices);
        addButton = findViewById(R.id.add_button);

        // Firebase Reference
        databaseReference = FirebaseDatabase.getInstance().getReference();

        // Initialize Lists
        teacherList = new ArrayList<>();
        usernameList = new ArrayList<>(); // Store usernames
        subjectList = new ArrayList<>();
        termList = new ArrayList<>();

        // Load Data
        loadStudents();
        loadSubjects();
        loadSemesters();

        // Add Button Click Listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addSubjectToTeacher();
            }
        });
    }

    // Load Students with Usernames
    private void loadStudents() {
        databaseReference.child("User").child("Role").child("Teacher")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        teacherList.clear();
                        usernameList.clear();

                        for (DataSnapshot studentSnapshot : snapshot.getChildren()) {
                            String teacherName = studentSnapshot.child("name").getValue(String.class);
                            String username = studentSnapshot.child("username").getValue(String.class);

                            if (teacherName != null && username != null) {
                                teacherList.add(teacherName);
                                usernameList.add(username);
                            }
                        }
                        populateSpinner(teacherSpinner, teacherList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast("Failed to load students.");
                    }
                });
    }

    private void loadSubjects() {
        databaseReference.child("Subject")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        subjectList.clear();
                        for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                            String subject = subjectSnapshot.getValue(String.class);
                            if (subject != null) {
                                subjectList.add(subject);
                            }
                        }
                        populateSpinner(subjectSpinner, subjectList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast("Failed to load subjects.");
                    }
                });
    }

    private void loadSemesters() {
        databaseReference.child("Semester")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        termList.clear();
                        for (DataSnapshot termSnapshot : snapshot.getChildren()) {
                            String term = termSnapshot.getValue(String.class);
                            if (term != null) {
                                termList.add(term);
                            }
                        }
                        populateSpinner(termSpinner, termList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        showToast("Failed to load terms.");
                    }
                });
    }

    // Add Subject to Student's Term using Username
    private void addSubjectToTeacher() {
        int studentPosition = teacherSpinner.getSelectedItemPosition();
        String username = usernameList.get(studentPosition);
        String term = termSpinner.getSelectedItem().toString();
        String subject = subjectSpinner.getSelectedItem().toString();

        DatabaseReference studentSubjectsRef = databaseReference.child("User")
                .child("Role").child("Teacher").child(username).child("Subjects").child(term);

        // Check for Redundancy
        studentSubjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> subjects = new ArrayList<>();
                if (snapshot.exists()) {
                    for (DataSnapshot subjectSnapshot : snapshot.getChildren()) {
                        String existingSubject = subjectSnapshot.getValue(String.class);
                        subjects.add(existingSubject);
                    }
                }

                if (subjects.contains(subject)) {
                    showToast("Subject already added to this term.");
                } else {
                    subjects.add(subject);
                    studentSubjectsRef.setValue(subjects)
                            .addOnSuccessListener(aVoid -> showToast("Subject added successfully!"))
                            .addOnFailureListener(e -> showToast("Failed to add subject."));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error: " + error.getMessage());
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
