package com.example.elmslayout.Teacher.CourseDetails;

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
import com.example.elmslayout.Student.Course.Handout_details;

import java.util.ArrayList;

public class TeacherCourseAdapter extends RecyclerView.Adapter<TeacherCourseAdapter.TeacherCourseViewHolder> {

    Context context;
     ArrayList<TeacherCourseClass> dataList;

    public TeacherCourseAdapter (Context context, ArrayList<TeacherCourseClass> dataList){
        this.context=context;
        this.dataList=dataList;

    }


    @NonNull
    @Override
    public TeacherCourseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.recycle_course_item, parent, false);
        return new TeacherCourseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherCourseViewHolder holder, int position) {

        TeacherCourseClass dataClass = dataList.get(position);
        holder.recTitle.setText(dataClass.getDataTitle());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Term_list.class);
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class TeacherCourseViewHolder extends RecyclerView.ViewHolder {
        TextView recTitle;
        CardView recCard;



        public TeacherCourseViewHolder(@NonNull View itemView) {
            super(itemView);
            recTitle= itemView.findViewById(R.id.recTitle);
            recCard = itemView.findViewById(R.id.recCard);


        }
    }
}

