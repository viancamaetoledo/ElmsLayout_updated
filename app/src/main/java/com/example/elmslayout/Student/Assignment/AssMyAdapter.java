package com.example.elmslayout.Student.Assignment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;

import java.util.ArrayList;

public class AssMyAdapter extends RecyclerView.Adapter<AssMyAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<AssignmentClass> dataList;
    public AssMyAdapter(Context context, ArrayList<AssignmentClass> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.assignment_list, parent, false);
        return new AssMyAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AssMyAdapter.MyViewHolder holder, int position) {
        AssignmentClass dataClass = dataList.get(position);
        holder.assignmentNo.setText(dataClass.getAssignmentNo());
        holder.term.setText(dataClass.getTerm());
    }



    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView assignmentNo, term;

        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            assignmentNo = itemView.findViewById(R.id.assign_num);
            term = itemView.findViewById(R.id.term);
            constraintLayout = itemView.findViewById(R.id.assign_layout);

        }
    }
}
