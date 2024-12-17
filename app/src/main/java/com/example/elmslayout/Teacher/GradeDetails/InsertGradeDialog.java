package com.example.elmslayout.Teacher.GradeDetails;

import android.app.Dialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.elmslayout.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class InsertGradeDialog extends AppCompatDialogFragment {

    private EditText gradeEditText;
    private Button giveGradeButton;
    String filePath ,submittedDates;
    private String term, subject, period, assignmentId, studentUsername;
    TextView submittedDate, submmittedFilePath, termTitle, subjectTitle, periodTitle, studentUsernameText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        if (getArguments() != null) {
            term = getArguments().getString("term");
            subject = getArguments().getString("subject");
            period = getArguments().getString("period");
            assignmentId = getArguments().getString("assignmentId");
            studentUsername = getArguments().getString("studentUsername");
            filePath = getArguments().getString("filePath");
             submittedDates = getArguments().getString("submittedDate");
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.insert_grade_dialog, null);

        gradeEditText = view.findViewById(R.id.grade);
        giveGradeButton = view.findViewById(R.id.giveGradeButton);
        submittedDate = view.findViewById(R.id.submittedDate);
        submmittedFilePath = view.findViewById(R.id.submmittedFilePath);
        termTitle = view.findViewById(R.id.term_Title);
        subjectTitle = view.findViewById(R.id.subjectTitle);
        periodTitle = view.findViewById(R.id.period_title);
        studentUsernameText = view.findViewById(R.id.studentUsername);

        submittedDate.setText(submittedDates);
        submmittedFilePath.setText(filePath);
        termTitle.setText(term);
        subjectTitle.setText(subject);
        periodTitle.setText(period);


        giveGradeButton.setOnClickListener(v -> giveGrade());

        builder.setView(view)
                .setTitle("Insert Grade");

        return builder.create();
    }

    private void giveGrade() {
        // Your logic for giving the grade
        String grade = gradeEditText.getText().toString().trim();

        if (TextUtils.isEmpty(grade)) {
            Toast.makeText(requireContext(), "Grade is required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Here you can insert the grade into Firebase or any other backend you're using
        DatabaseReference gradeRef = FirebaseDatabase.getInstance().getReference("Grades")
                .child(subject).child(term).child(period).child(assignmentId).child(studentUsername);

        gradeRef.setValue(grade).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Grade inserted successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to insert grade", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
