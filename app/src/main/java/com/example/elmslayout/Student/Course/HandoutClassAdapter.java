package com.example.elmslayout.Student.Course;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.HandoutModel;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.ViewHandoutDetails;

import java.util.ArrayList;

public class HandoutClassAdapter extends RecyclerView.Adapter<HandoutClassAdapter.HandoutViewHolder> {

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
            Intent intent = new Intent(context, ViewHandoutDetails.class);
            intent.putExtra("handoutNo", handout.getHandoutNo());
            intent.putExtra("title", handout.getTitle());
            intent.putExtra("period", handout.getPeriod());
            intent.putExtra("filePath", handout.getFilePath()); // Optional: Pass file path
            context.startActivity(intent); // Use context to start the activity
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
