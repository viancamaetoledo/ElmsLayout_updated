package com.example.elmslayout.Student.Assignment;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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

public class Assignment_Menu extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<AssignmentClass> datalist;
    AssMyAdapter adapter;
    String term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_assignment_menu);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        term = getIntent().getStringExtra("term");

        // Initialize UI components
        recyclerView = findViewById(R.id.recycle_courseview);
        datalist = new ArrayList<>();
        adapter = new AssMyAdapter(this, datalist , term);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch username from intent
        String username = getIntent().getStringExtra("username");

        if (username != null) {
            fetchSubjects(username); // Fetch subjects for this username
        } else {
            Toast.makeText(this, "Error: Missing username!", Toast.LENGTH_SHORT).show();
        }
    }

    // Function to get Firebase reference for the student
    private DatabaseReference getStudentReference(String username) {
        return FirebaseDatabase.getInstance()
                .getReference("User")
                .child("Role")
                .child("Student")
                .child(username)
                .child("Subjects");
    }

    // Fetch subjects for the specified user
    private void fetchSubjects(String username) {
        DatabaseReference reference = getStudentReference(username);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();

                if (snapshot.exists()) {
                    // Loop through each term (child)
                    for (DataSnapshot termSnapshot : snapshot.getChildren()) {
                        String termName = termSnapshot.getKey(); // Get the term name

                        // Loop through each subject in the term
                        for (DataSnapshot subjectSnapshot : termSnapshot.getChildren()) {
                            String subjectTitle = subjectSnapshot.getKey(); // Get the subject name
                            Boolean isEnrolled = subjectSnapshot.getValue(Boolean.class); // Check enrollment status

                            if (isEnrolled != null && isEnrolled) {
                                // Add the subject and term to the list
                                datalist.add(new AssignmentClass(subjectTitle, termName));
                            }
                        }
                    }
                    adapter.notifyDataSetChanged(); // Notify adapter about the data change
                } else {
                    Toast.makeText(Assignment_Menu.this, "No subjects found for this user.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Assignment_Menu.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}
