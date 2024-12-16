package com.example.elmslayout.Teacher.AssignmentDetails;

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

public class TeacherAssignAdapter extends RecyclerView.Adapter<TeacherAssignAdapter.TeacherAssignViewHolder> {
    Context context;
    ArrayList<TeacherAssignClass> dataList;
    public TeacherAssignAdapter (Context context, ArrayList<TeacherAssignClass> dataList){
        this.context=context;
        this.dataList=dataList;

    }

    @NonNull
    @Override
    public TeacherAssignAdapter.TeacherAssignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_course_item, parent, false);
        return new TeacherAssignAdapter.TeacherAssignViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherAssignAdapter.TeacherAssignViewHolder holder, int position) {
        TeacherAssignClass dataClass = dataList.get(position);
        holder.recTitle.setText(dataClass.getSubjectName());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, teacher_assign_term.class);
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()). getSubjectName());
                intent.putExtra("term", dataList.get(holder.getAdapterPosition()).getTermName());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public class TeacherAssignViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle;
        CardView recCard;
        public TeacherAssignViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle= itemView.findViewById(R.id.recTitle);
            recCard = itemView.findViewById(R.id.recCard);
        }
    }
}
