package com.example.elmslayout.Student.Assignment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.AssignmentDetails.TakeAssignment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class submit_Assignment extends AppCompatDialogFragment {
    private static final int PICK_FILE_REQUEST = 1;

    private EditText editTextFilePath; // To display file path
    private Button browseFileButton;
    private Button prepareAnswerButton; // New button for "Prepare Answer"

    private DatabaseReference databaseReference;

     String term, subject, period, assignmentId ,username;
    private String submitDate;
    private Uri selectedFileUri; // Selected file URI

    public interface SubmissionListener {
        void onSubmissionComplete(String filePath, String submitTime, String score);
    }

    private SubmissionListener submissionListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            submissionListener = (SubmissionListener) getActivity(); // Cast the parent activity
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement SubmissionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());


        // Receive arguments
        if (getArguments() != null) {
            term = getArguments().getString("term");
            subject = getArguments().getString("subject");
            period = getArguments().getString("period");
            username = getArguments().getString("username");
            assignmentId = getArguments().getString("assignmentId"); // Unique assignment ID
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.submit_assignment, null);

        // Initialize views
        editTextFilePath = view.findViewById(R.id.pathFile);
        browseFileButton = view.findViewById(R.id.browse);
         // New button

        // Handle file browsing
        browseFileButton.setOnClickListener(v -> openFilePicker());

        // Handle "Prepare Answer" button

        // Get current date as submit date
        submitDate = getCurrentDateTime();

        builder.setView(view)
                .setTitle("Submit Assignment")
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("Submit", (dialog, which) -> insertSubmission());

        return builder.create();
    }

    // Open file picker for file selection
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && data != null && data.getData() != null) {
            selectedFileUri = data.getData();
            editTextFilePath.setText(selectedFileUri.toString());
        }
    }

    // Get the current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    // Insert student submission into Firebase
    // In submit_Assignment dialog:
    // In submit_Assignment dialog:
    // Insert student submission into Firebase
    private void insertSubmission() {
        String filePath = editTextFilePath.getText().toString().trim();

        if (TextUtils.isEmpty(filePath)) {
            Toast.makeText(requireContext(), "Please select a file to submit.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Get current time as submit time
        String submitTime = getCurrentDateTime();
        String score = "Waiting for Grade";

        // Update Firebase path
        DatabaseReference submissionRef = FirebaseDatabase.getInstance().getReference("Grades")
                .child(subject)
                .child(term)
                .child(period)
                .child(getArguments().getString("username")); // username passed from activity

        // Add the assignment ID to the database entry
        submissionRef.child("assignmentId").setValue(assignmentId); // Adding the assignmentId field
        submissionRef.child("submittedFilePath").setValue(filePath);
        submissionRef.child("submitDate").setValue(submitTime);
        submissionRef.child("score").setValue(score);

        submissionRef.child("score").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (submissionListener != null) {
                    // Notify the parent activity when submission is complete
                    submissionListener.onSubmissionComplete(filePath, submitTime, score);
                }
            }
        }).addOnFailureListener(e ->
                Toast.makeText(getContext(), "Submission failed: " + e.getMessage(), Toast.LENGTH_SHORT).show()
        );
    }



}
