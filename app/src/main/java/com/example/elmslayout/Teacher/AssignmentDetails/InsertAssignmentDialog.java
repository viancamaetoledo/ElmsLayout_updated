package com.example.elmslayout.Teacher.AssignmentDetails;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.elmslayout.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InsertAssignmentDialog extends AppCompatDialogFragment {

    private static final int PICK_FILE_REQUEST = 1;

    private EditText editTextAssignmentNo, editTextTitle, editTextStartDate, editTextEndDate, editTextFilePath;
    private Button selectEndDateButton, browseFileButton, saveButton;

    private DatabaseReference databaseReference;

    private String term, subject, period;
    private Calendar selectedCalendar = Calendar.getInstance(); // For the date picker
    private String startDate;
    private Uri selectedFileUri; // To store the file URI

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());

        if (getArguments() != null) {
            term = getArguments().getString("term");
            subject = getArguments().getString("subject");
            period = getArguments().getString("period");
        }

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.insert_assignment, null);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("Assignments");

        // Initialize views
        editTextAssignmentNo = view.findViewById(R.id.assignment_number);
        editTextTitle = view.findViewById(R.id.assignment_title);
        editTextStartDate = view.findViewById(R.id.start);
        editTextEndDate = view.findViewById(R.id.end);
        editTextFilePath = view.findViewById(R.id.pathFile); // File path field
        selectEndDateButton = view.findViewById(R.id.saveDates);
        browseFileButton = view.findViewById(R.id.browse);


        // Set current date/time as start date
        startDate = getCurrentDateTime();
        editTextStartDate.setText(startDate);

        // Handle end date selection
        selectEndDateButton.setOnClickListener(v -> openDatePicker());

        // Browse file for file picker
        browseFileButton.setOnClickListener(v -> openFilePicker());


        builder.setView(view)
                .setTitle("Insert Assignment")
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("Insert", (dialog, which) -> insertAssignment());

        return builder.create();
    }

    // Method to get the current date and time
    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(Calendar.getInstance().getTime());
    }

    // Open DatePickerDialog for selecting the end date
    private void openDatePicker() {
        int year = selectedCalendar.get(Calendar.YEAR);
        int month = selectedCalendar.get(Calendar.MONTH);
        int day = selectedCalendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                (DatePicker view, int selectedYear, int selectedMonth, int selectedDay) -> {
                    selectedCalendar.set(selectedYear, selectedMonth, selectedDay);
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                    editTextEndDate.setText(sdf.format(selectedCalendar.getTime()));
                }, year, month, day);

        datePickerDialog.show();
    }

    // Open file picker for selecting a file
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*"); // Allow all file types
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select a file"), PICK_FILE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_FILE_REQUEST && data != null && data.getData() != null) {
            selectedFileUri = data.getData(); // Get the file URI
            String filePath = selectedFileUri.toString(); // Convert to String
            editTextFilePath.setText(filePath); // Set file path to EditText
        }
    }

    // Insert assignment into Firebase
    private void insertAssignment() {
        String assignmentNo = editTextAssignmentNo.getText().toString().trim();
        String title = editTextTitle.getText().toString().trim();
        String endDate = editTextEndDate.getText().toString().trim();
        String filePath = editTextFilePath.getText().toString().trim();

        // Validate inputs
        if (TextUtils.isEmpty(assignmentNo) || TextUtils.isEmpty(title) || TextUtils.isEmpty(endDate) || TextUtils.isEmpty(filePath)) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String id = databaseReference.push().getKey();
        if (id != null && term != null && subject != null && period != null) {
            Assignment assignment = new Assignment(id, assignmentNo, title, startDate, endDate, filePath);

            DatabaseReference assignmentRef = databaseReference.child(subject).child(term).child(period).child(id);

            assignmentRef.setValue(assignment)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(requireContext(), "Assignment inserted successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(), "Failed to insert: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Missing required arguments!", Toast.LENGTH_SHORT).show();
        }
    }

    // Assignment model class
    public static class Assignment {
        public String id, assignmentNo, title, startDate, endDate, filePath;

        public Assignment() {
        }

        public Assignment(String id, String assignmentNo, String title, String startDate, String endDate, String filePath) {
            this.id = id;
            this.assignmentNo = assignmentNo;
            this.title = title;
            this.startDate = startDate;
            this.endDate = endDate;
            this.filePath = filePath;
        }
    }
}
