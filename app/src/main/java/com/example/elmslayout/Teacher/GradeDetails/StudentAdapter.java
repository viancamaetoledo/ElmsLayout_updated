package com.example.elmslayout.Teacher.GradeDetails;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.AssignmentDetails.TakeAssignment;

import java.util.List;

public class StudentAdapter extends RecyclerView.Adapter<StudentAdapter.ViewHolder> {

    private List<GradeClass> studentGradeList;
    private Context context;  // Add context to the adapter

    public StudentAdapter(Context context, List<GradeClass> studentGradeList) {
        this.context = context;
        this.studentGradeList = studentGradeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the view for each item in the RecyclerView
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.grade_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        GradeClass studentGrade = studentGradeList.get(position);

        // Set values to your views from studentGrade (GradeClass)
        holder.subjectTitle.setText(studentGrade.getSubjectTitle());
        holder.studentUsername.setText(studentGrade.getStudentUsername());
        holder.score.setText(studentGrade.getScore());
        holder.submittedDate.setText(studentGrade.getSubmittedDate());
        holder.period.setText(studentGrade.getPeriod());
        holder.term.setText(studentGrade.getTerm());

        // Set onClickListener for the "Give Grade" button
        holder.takeButton.setOnClickListener(v -> {
            // Trigger the dialog from the activity
            ((Teacher_grade) context).onGradeButtonClick(studentGrade.getStudentUsername(), studentGrade.getAssignmentID());
        });
    }


    private void showInsertGradeDialog(Bundle args) {
        // Create an instance of the dialog and pass the arguments
        InsertGradeDialog insertGrade = new InsertGradeDialog();
        insertGrade.setArguments(args);

        // Show the dialog using the AppCompatActivity context
        insertGrade.show(((AppCompatActivity) context).getSupportFragmentManager(), "InsertGradeDialog");
    }

    @Override
    public int getItemCount() {
        return studentGradeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        // Define the views that represent each item in the list
        public TextView subjectTitle, studentUsername, score, submittedFilePath, submittedDate, period, term;
        Button takeButton;

        public ViewHolder(View view) {
            super(view);
            // Initialize the views
            subjectTitle = view.findViewById(R.id.subject_title);
            studentUsername = view.findViewById(R.id.studentUsername);
            score = view.findViewById(R.id.valueScore);
            submittedDate = view.findViewById(R.id.submittedDate);
            period = view.findViewById(R.id.period_title);
            term = view.findViewById(R.id.term_Title);
            takeButton = view.findViewById(R.id.grade_button);
        }
    }
}
