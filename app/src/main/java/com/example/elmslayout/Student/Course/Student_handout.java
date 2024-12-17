package com.example.elmslayout.Student.Course;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.HandoutModel;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.Handouts;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.ViewHandoutDetails;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Student_handout extends AppCompatActivity {

    RecyclerView recyclerView;
    HandoutClassAdapter handoutAdapter;
    ArrayList<HandoutModel> handoutList;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_handout);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize handout list and adapter
        handoutList = new ArrayList<>();
        handoutAdapter = new HandoutClassAdapter(Student_handout.this, handoutList);
        recyclerView.setAdapter(handoutAdapter);

        // Get term and subjectTitle from Intent
        String term = getIntent().getStringExtra("term");
        String subjectTitle = getIntent().getStringExtra("Title");

        if (term != null && subjectTitle != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("Handouts")
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
                handoutList.clear();
                if (snapshot.exists()) {
                    for (DataSnapshot periodSnapshot : snapshot.getChildren()) { // "Prelims", "Midterms", etc.
                        String periodKey = periodSnapshot.getKey();
                        for (DataSnapshot handoutSnapshot : periodSnapshot.getChildren()) {
                            HandoutModel handout = handoutSnapshot.getValue(HandoutModel.class);
                            if (handout != null) {
                                handout.setPeriod(periodKey);
                                handoutList.add(handout);
                            }
                        }
                    }
                    handoutAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(Student_handout.this, "No handouts found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Student_handout.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showToast(String message) {
        Toast.makeText(Student_handout.this, message, Toast.LENGTH_SHORT).show();
    }

    // Corrected HandoutClassAdapter class
    public static class HandoutClassAdapter extends RecyclerView.Adapter<HandoutClassAdapter.HandoutViewHolder> {

        Context context;
        ArrayList<HandoutModel> handouts;

        public HandoutClassAdapter(Context context, ArrayList<HandoutModel> handouts) {
            this.context = context;
            this.handouts = handouts;
        }

        @NonNull
        @Override
        public HandoutClassAdapter.HandoutViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.handout_list, parent, false);
            return new HandoutViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HandoutClassAdapter.HandoutViewHolder holder, int position) {
            HandoutModel handout = handouts.get(position);
            holder.handoutNo.setText(handout.getHandoutNo());
            holder.title.setText(handout.getTitle());
            holder.period.setText(handout.getPeriod());

            // View Button Click Listener
            holder.viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, Handout_details.class);
                intent.putExtra("handoutNo", handout.getHandoutNo());
                intent.putExtra("titles", handout.getTitle());
                intent.putExtra("period", handout.getPeriod());
                intent.putExtra("filePath", handout.getFilePath());
                // Optional: Pass file path
                context.startActivity(intent); // Use context to start the activity
            });
        }

        @Override
        public int getItemCount() {
            return handouts.size();
        }

        public static class HandoutViewHolder extends RecyclerView.ViewHolder {
            TextView handoutNo, title, period;
            Button viewButton;

            public HandoutViewHolder(@NonNull View itemView) {
                super(itemView);
                handoutNo = itemView.findViewById(R.id.handout_number);
                title = itemView.findViewById(R.id.handout_title);
                period = itemView.findViewById(R.id.period_title);
                viewButton = itemView.findViewById(R.id.view_button);
            }
        }
    }
}
