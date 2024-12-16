package com.example.elmslayout.Teacher.AssignmentDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.HandoutModel;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.Handouts;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class assignment_dashboard extends AppCompatActivity {

    RecyclerView recyclerView;
    AssignmentAdapter assignmentAdapter;
    ArrayList<AssignmentModel> assignmentList;
    DatabaseReference databaseReference;
    ImageView addAssignmentButton, backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assignment_dashboard);

        // Initialize views
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addAssignmentButton = findViewById(R.id.addAssignmentButton);
        backButton = findViewById(R.id.backpressed);

        assignmentList = new ArrayList<>();
        assignmentAdapter = new AssignmentAdapter(this, assignmentList);
        recyclerView.setAdapter(assignmentAdapter);

        // Back button functionality
        backButton.setOnClickListener(v -> finish());

        // Retrieve intent data
        String period = getIntent().getStringExtra("period");
        String term = getIntent().getStringExtra("term");
        String subjectTitle = getIntent().getStringExtra("Title");

        if (term != null && subjectTitle != null) {
            databaseReference = FirebaseDatabase.getInstance()
                    .getReference("Assignments")
                    .child(subjectTitle)
                    .child(term);

            fetchHandoutsData();
        } else {
            Toast.makeText(this, "Error: Missing term or subject information.", Toast.LENGTH_SHORT).show();
        }

        // Add assignment button functionality
        addAssignmentButton.setOnClickListener(v -> {
            if (term != null && subjectTitle != null && period != null) {
                showInsertDialog(term, subjectTitle, period);
            } else {
                Toast.makeText(this, "Cannot add assignment: Missing details.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInsertDialog(String term, String subject, String period) {
        InsertAssignmentDialog insertDialog = new InsertAssignmentDialog();
        Bundle args = new Bundle();
        args.putString("term", term);
        args.putString("subject", subject);
        args.putString("period", period);
        insertDialog.setArguments(args);
        insertDialog.show(getSupportFragmentManager(), "InsertAssignmentDialog");
    }

    private void fetchHandoutsData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assignmentList.clear(); // Clear the list at the start
                boolean dataExists = false; // Flag to track if data is available

                if (snapshot.exists() && snapshot.hasChildren()) {
                    for (DataSnapshot periodSnapshot : snapshot.getChildren()) { // Loop through periods ("Prelims", "Midterms", etc.)
                        String periodKey = periodSnapshot.getKey();
                        if (periodKey != null && periodSnapshot.hasChildren()) {
                            for (DataSnapshot assignmentSnapshot : periodSnapshot.getChildren()) {
                                AssignmentModel assignment = assignmentSnapshot.getValue(AssignmentModel.class);
                                if (assignment != null) {
                                    assignment.setPeriod(periodKey); // Set the period for the assignment
                                    assignmentList.add(assignment);
                                    dataExists = true; // Data was found
                                }
                            }
                        } else {
                            Log.d("FetchAssignments", "No assignments found for period: " + periodKey);
                        }
                    }
                }

                // Notify adapter if data exists, or show a toast if not
                if (dataExists) {
                    assignmentAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(assignment_dashboard.this, "No Assignments found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FetchAssignments", "Database error: " + error.getMessage(), error.toException());
                Toast.makeText(assignment_dashboard.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    // Adapter Class
    public class AssignmentAdapter extends RecyclerView.Adapter<AssignmentAdapter.AssignmentViewHolder> {

        Context context;
        ArrayList<AssignmentModel> assignments;

        public AssignmentAdapter(Context context, ArrayList<AssignmentModel> assignments) {
            this.context = context;
            this.assignments = assignments;
        }

        @NonNull
        @Override
        public AssignmentViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.assignment_list, parent, false);
            return new AssignmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull AssignmentViewHolder holder, int position) {
            AssignmentModel assignment = assignments.get(position);
            holder.assignmentNo.setText(assignment.getAssignmentNo());
            holder.title.setText(assignment.getTitle());
            holder.startDate.setText("Start Date: " + assignment.getStartDate());
            holder.endDate.setText("End Date: " + assignment.getEndDate());
            holder.period.setText(assignment.getPeriod());

            // View Button Click Listener
            holder.viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, TakeAssignment.class);
                intent.putExtra("assignmentNo", assignment.getAssignmentNo());
                intent.putExtra("title", assignment.getTitle());
                intent.putExtra("startDate", assignment.getStartDate());
                intent.putExtra("period", assignment.getPeriod());
                intent.putExtra("endDate", assignment.getEndDate());
                intent.putExtra("filePath", assignment.getFilePath()); // Optional: Pass file path
                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return assignments.size();
        }

        public class AssignmentViewHolder extends RecyclerView.ViewHolder {
            TextView assignmentNo, title, startDate, endDate, period;
            Button viewButton;

            public AssignmentViewHolder(@NonNull View itemView) {
                super(itemView);
                assignmentNo = itemView.findViewById(R.id.assignment_number);
                title = itemView.findViewById(R.id.assignment_titles);
                period = itemView.findViewById(R.id.period_title);
                startDate = itemView.findViewById(R.id.startDate);
                endDate = itemView.findViewById(R.id.endDate);
                viewButton = itemView.findViewById(R.id.take_button);
            }
        }
    }
}
