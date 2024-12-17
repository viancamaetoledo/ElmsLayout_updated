package com.example.elmslayout.Student.Assignment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.example.elmslayout.Student.Course.Student_handout;
import com.example.elmslayout.Teacher.AssignmentDetails.AssignmentModel;
import com.example.elmslayout.Teacher.AssignmentDetails.TakeAssignment;
import com.example.elmslayout.Teacher.AssignmentDetails.TeacherAssignAdapter;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.HandoutModel;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.ViewHandoutDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_assignment extends AppCompatActivity {

    RecyclerView recyclerView;
    TeacherAssignAdapter assignAdapter;
    ArrayList<AssignmentModel> assignList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_assignment);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recylerView1);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize assignment list and adapter
        assignList = new ArrayList<>();
        assignAdapter = new TeacherAssignAdapter(this, assignList);
        recyclerView.setAdapter(assignAdapter);

        // Get term and subjectTitle from Intent
        String term = getIntent().getStringExtra("term");
        String subjectTitle = getIntent().getStringExtra("Title");
        String username = getIntent().getStringExtra("username");

        if (term != null && subjectTitle != null ) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Assignments")
                    .child(subjectTitle)
                    .child(term);

            fetchHandoutsData();
        } else {
            Toast.makeText(this, "Error: Missing term or subject information.", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchHandoutsData() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                assignList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot periodSnapshot : snapshot.getChildren()) { // "Prelims", "Midterms", etc.
                        String periodKey = periodSnapshot.getKey();
                        for (DataSnapshot assignmentSnapshot : periodSnapshot.getChildren()) {
                            AssignmentModel assignment = assignmentSnapshot.getValue(AssignmentModel.class);
                            if (assignment != null) {
                                assignment.setPeriod(periodKey);
                                assignList.add(assignment);
                            }
                        }
                    }
                    assignAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Student_assignment.this, "No Assignments found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Student_assignment.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    // Add these methods here, inside Student_assignment
    public String getSubjectTitle() {
        return getIntent().getStringExtra("Title");
    }

    public String getTerm() {
        return getIntent().getStringExtra("term");
    }

    public static class TeacherAssignAdapter extends RecyclerView.Adapter<TeacherAssignAdapter.AssignmentViewHolder> {

        Context context;
        ArrayList<AssignmentModel> assignments;

        public TeacherAssignAdapter(Context context, ArrayList<AssignmentModel> assignments) {
            this.context = context;
            this.assignments = assignments;
        }

        @NonNull
        @Override
        public TeacherAssignAdapter.AssignmentViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.assignment_list, parent, false);
            return new TeacherAssignAdapter.AssignmentViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull TeacherAssignAdapter.AssignmentViewHolder holder, int position) {
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
                intent.putExtra("filePath", assignment.getFilePath());
                intent.putExtra("assignmentId",assignment.getId());


                // Pass subjectTitle and term from Student_assignment
                if (context instanceof Student_assignment) {
                    Student_assignment activity = (Student_assignment) context;
                    intent.putExtra("subjectTitle", activity.getSubjectTitle());
                    intent.putExtra("term", activity.getTerm());
                    intent.putExtra("username", activity.getIntent().getStringExtra("username"));
                }

                context.startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return assignments.size();
        }

        public static class AssignmentViewHolder extends RecyclerView.ViewHolder {
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
