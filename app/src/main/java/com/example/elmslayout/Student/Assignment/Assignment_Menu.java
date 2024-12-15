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
    SearchView searchView;
    DatabaseReference databaseReference;

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

        recyclerView = findViewById(R.id.recycle_courseview);
        searchView = findViewById(R.id.searching);

        datalist = new ArrayList<>();
        adapter = new AssMyAdapter(this, datalist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);


        String name = getIntent().getStringExtra("name");

        if (name != null) {
            fetchSubjects(name); // Fetch the subjects for this name
        } else {
            Toast.makeText(this, "No name passed!", Toast.LENGTH_SHORT).show();
        }



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }

    private void fetchSubjects(String name) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                datalist.clear(); // Clear the list before adding new data

                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    String dbName = userSnapshot.child("name").getValue(String.class);

                    if (dbName != null && dbName.equalsIgnoreCase(name)) {
                        // Found the user, fetch their subjects
                        DataSnapshot subjectsSnapshot = userSnapshot.child("subject");

                        for (DataSnapshot subjectSnapshot : subjectsSnapshot.getChildren()) {
                            String subjectTitle = subjectSnapshot.getKey(); // Subject name


                            // Add the subject and terms to the list
                            AssignmentClass data = new AssignmentClass(subjectTitle, "Term 1"); //balikan kita
                            datalist.add(data);
                        }

                        // Notify adapter and break after finding the user
                        adapter.notifyDataSetChanged();
                        break;
                    }
                }

                if (datalist.isEmpty()) {
                    Toast.makeText(Assignment_Menu.this, "No subjects found for " + name, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Assignment_Menu.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}