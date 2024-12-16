package com.example.elmslayout.Student.Course;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.Term_list;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

     Context context;
     ArrayList<DataClass> dataList;
     String term;  // Add term field

    // Modify constructor to accept term as well
    public MyAdapter(Context context, ArrayList<DataClass> dataList, String term) {
        this.context = context;
        this.dataList = dataList;
        this.term = term;  // Assign the passed term
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_course_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DataClass dataClass = dataList.get(position);
        holder.recTitle.setText(dataClass.getSubjectName());

        // Pass the term along with the course title to Student_handout
        holder.recCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, term_Student_lists.class);
            intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getSubjectName());
            intent.putExtra("term", dataList.get(holder.getAdapterPosition()).getTermName());  // Pass the term to Student_handout
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle = itemView.findViewById(R.id.recTitle);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}
