package com.example.elmslayout.Student.Assignment;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.example.elmslayout.Student.Course.term_Student_lists;

import java.util.ArrayList;

public class AssMyAdapter extends RecyclerView.Adapter<AssMyAdapter.MyViewHolder> {
     Context context;
     ArrayList<AssignmentClass> dataList;
    String term;
    public AssMyAdapter(Context context, ArrayList<AssignmentClass> dataList, String term) {
        this.context = context;
        this.dataList = dataList;
        this.term = term;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_course_item, parent, false);
        return new AssMyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssMyAdapter.MyViewHolder holder, int position) {
        AssignmentClass dataClass = dataList.get(position);
        holder.recTitle.setText(dataClass.getSubjectName());

        holder.recCard.setOnClickListener(v -> {
            Intent intent = new Intent(context, term_Student_assignment_list.class);
            intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getSubjectName());
            intent.putExtra("term", dataList.get(holder.getAdapterPosition()).getTermName());
            intent.putExtra("username", dataList.get(holder.getAdapterPosition()).getUsername());// Pass the term to Student_handout
            context.startActivity(intent);
        });
    }



    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView recTitle;
        CardView recCard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle = itemView.findViewById(R.id.recTitle);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}
