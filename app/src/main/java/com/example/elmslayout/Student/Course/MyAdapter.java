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

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<DataClass> dataList;

    public MyAdapter (Context context, ArrayList<DataClass> dataList){
        this.context=context;
        this.dataList=dataList;

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
        holder.recTitle.setText(dataClass.getDataTitle());


        holder.recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, Handout_details.class);
                intent.putExtra("Title", dataList.get(holder.getAdapterPosition()).getDataTitle());

                context.startActivity(intent);
            }
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
            recTitle= itemView.findViewById(R.id.recTitle);
            recCard = itemView.findViewById(R.id.recCard);


        }
    }
}
