package com.example.elmslayout.Teacher.AssignmentDetails;

import android.content.Intent;
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
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.Term_list;
import com.example.elmslayout.Teacher.CourseDetails.TeacherCourseClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Teacher_assignment extends AppCompatActivity {

    RecyclerView recyclerView;
    ArrayList<TeacherAssignClass> datalist;
    TeacherAssignAdapter adapter;
    SearchView searchView;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_teacher_assignment);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        recyclerView = findViewById(R.id.recycle_courseview);
        searchView = findViewById(R.id.searching);

        // Setup RecyclerView
        datalist = new ArrayList<>();
        adapter = new TeacherAssignAdapter(this, datalist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Fetch "teacher name" passed via intent
        String teacherName = getIntent().getStringExtra("name");
        if (teacherName != null) {
            fetchSubjects(teacherName);
        } else {
            showToast("No teacher name passed!");
        }

        // Set up SearchView
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filterSubjects(newText);
//                return true;
//            }
//        });
    }

    private void fetchSubjects(String teacherName) {
        // Adjust Firebase reference based on the new structure
        DatabaseReference teacherSubjectsRef = FirebaseDatabase.getInstance()
                .getReference("User")
                .child("Role")
                .child("Teacher")
                .child(teacherName)
                .child("Subjects");

        teacherSubjectsRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear();

                if (snapshot.exists()) {
                    // Iterate through terms like "First Term"
                    for (DataSnapshot termSnapshot : snapshot.getChildren()) {
                        String termName = termSnapshot.getKey(); // e.g., "First Term"

                        // Iterate through each subject under the term
                        for (DataSnapshot subjectSnapshot : termSnapshot.getChildren()) {
                            String subjectName = subjectSnapshot.getKey(); // e.g., "Advanced Database System"

                            datalist.add(new TeacherAssignClass(subjectName, termName));
                        }
                    }

                    adapter.notifyDataSetChanged(); // Notify adapter
                } else {
                    showToast("No subjects found for this teacher.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                showToast("Error fetching data: " + error.getMessage());
            }
        });
    }

//    private void filterSubjects(String query) {
//        ArrayList<TeacherCourseClass> filteredList = new ArrayList<>();
//        for (TeacherCourseClass data : datalist) {
//           if (data.getSubjectName().toLowerCase().contains(query.toLowerCase())) {
//                filteredList.add(data);
//            }
//       }
//       adapter.setFilteredList(filteredList); // Update adapter with filtered list
//    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
