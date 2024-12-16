package com.example.elmslayout.Teacher.CourseDetails.HandoutDet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Handouts extends AppCompatActivity {

    RecyclerView recyclerView;
    HandoutAdapter handoutAdapter;
    ArrayList<HandoutModel> handoutList;
    DatabaseReference databaseReference;
    ImageView addHandoutButton, onbackpressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handouts);

        onbackpressed=findViewById(R.id.backpressed);
        onbackpressed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String term = getIntent().getStringExtra("term");
                String subjectTitle = getIntent().getStringExtra("Title");

                Intent intent = new Intent(Handouts.this, Term_list.class);
                intent.putExtra("term", term);
                intent.putExtra("Title", subjectTitle);
                startActivity(intent);
                finish();
            }
        });

        // Initialize views
        recyclerView = findViewById(R.id.recylerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        addHandoutButton = findViewById(R.id.addHandoutbut);
        handoutList = new ArrayList<>();
        handoutAdapter = new HandoutAdapter(Handouts.this, handoutList);
        recyclerView.setAdapter(handoutAdapter);

        // Retrieve data from intent
        String period = getIntent().getStringExtra("period");
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

        // Add Handout button click listener
        addHandoutButton.setOnClickListener(v -> {
            if (term != null && subjectTitle != null && period != null) {
                showInsertDialog(term, subjectTitle, period);
            } else {
                Toast.makeText(this, "Cannot add handout: Missing term or subject.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showInsertDialog(String term, String subject, String period) {
        insert_Dialog insertDialog = new insert_Dialog();
        Bundle args = new Bundle();
        args.putString("term", term);
        args.putString("subject", subject);
        args.putString("period", period);
        insertDialog.setArguments(args);
        insertDialog.show(getSupportFragmentManager(), "InsertDialog");
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
                    Toast.makeText(Handouts.this, "No handouts found.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Handouts.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Adapter Class
    public class HandoutAdapter extends RecyclerView.Adapter<HandoutAdapter.HandoutViewHolder> {

        Context context;
        ArrayList<HandoutModel> handouts;

        public HandoutAdapter(Context context, ArrayList<HandoutModel> handouts) {
            this.context = context;
            this.handouts = handouts;
        }

        @NonNull
        @Override
        public HandoutViewHolder onCreateViewHolder(@NonNull android.view.ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.handout_list, parent, false);
            return new HandoutViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull HandoutViewHolder holder, int position) {
            HandoutModel handout = handouts.get(position);
            holder.handoutNo.setText(handout.getHandoutNo());
            holder.title.setText(handout.getTitle());
            holder.period.setText(handout.getPeriod());

            // View Button Click Listener
            holder.viewButton.setOnClickListener(v -> {
                Intent intent = new Intent(Handouts.this, ViewHandoutDetails.class);
                intent.putExtra("handoutNo", handout.getHandoutNo());
                intent.putExtra("title", handout.getTitle());
                intent.putExtra("period", handout.getPeriod());
                intent.putExtra("filePath", handout.getFilePath()); // Optional: Pass file path
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return handouts.size();
        }

        public class HandoutViewHolder extends RecyclerView.ViewHolder {
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
