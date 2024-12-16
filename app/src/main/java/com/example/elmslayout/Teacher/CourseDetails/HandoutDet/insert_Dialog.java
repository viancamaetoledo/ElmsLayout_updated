package com.example.elmslayout.Teacher.CourseDetails.HandoutDet;

import android.app.AlertDialog;
import android.app.Dialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class insert_Dialog extends AppCompatDialogFragment {

    private static final int PICK_PDF_REQUEST = 1;

     EditText editTextHandoutNo, editTextTitle, editTextFilePath;
    Button browseButton;

   DatabaseReference databaseReference;

     String term, subject, period;
     Uri selectedPdfUri; // To store the PDF file URI

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
        View view = inflater.inflate(R.layout.insert_handout_dialog, null);

        databaseReference = FirebaseDatabase.getInstance().getReference("Handouts");

        editTextHandoutNo = view.findViewById(R.id.handout_number);
        editTextTitle = view.findViewById(R.id.handout_titles);
        editTextFilePath = view.findViewById(R.id.pathFile);
        browseButton = view.findViewById(R.id.browse);

        // Browse button to open file picker
        browseButton.setOnClickListener(v -> openFilePicker());

        builder.setView(view)
                .setTitle("Insert Handout")
                .setNegativeButton("Cancel", (dialog, which) -> {})
                .setPositiveButton("Insert", (dialog, which) -> insertHandout());

        return builder.create();
    }

    // Open file picker for PDF files
    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf"); // Filter for PDFs
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(Intent.createChooser(intent, "Select PDF"), PICK_PDF_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_PDF_REQUEST && data != null && data.getData() != null) {
            selectedPdfUri = data.getData(); // Get the PDF URI
            String filePath = selectedPdfUri.toString(); // Convert to String
            editTextFilePath.setText(filePath); // Set file path to EditText
        }
    }

    private void insertHandout() {
        String handoutNo = editTextHandoutNo.getText().toString().trim();
        String title = editTextTitle.getText().toString().trim();
        String filePath = editTextFilePath.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(handoutNo) || TextUtils.isEmpty(title) || TextUtils.isEmpty(filePath)) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique ID for the handout
        String id = databaseReference.push().getKey();

        if (id != null && term != null && subject != null && period != null) {
            // Create a new Handout object
            Handout handout = new Handout(id, handoutNo, title, filePath);

            // Define the full path for the handout
            DatabaseReference handoutRef = databaseReference.child(subject).child(term).child(period).child(id);

            // Ensure the reference exists and set the value
            handoutRef.setValue(handout)
                    .addOnSuccessListener(aVoid ->
                            Toast.makeText(requireContext(), "Handout inserted successfully!", Toast.LENGTH_SHORT).show())
                    .addOnFailureListener(e ->
                            Toast.makeText(requireContext(), "Failed to insert: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        } else {
            Toast.makeText(getContext(), "Missing required arguments!", Toast.LENGTH_SHORT).show();
        }
    }


    // Handout model class
    public static class Handout {
        public String id, handoutNo, title, filePath;

        public Handout() {}

        public Handout(String id, String handoutNo, String title, String filePath) {
            this.id = id;
            this.handoutNo = handoutNo;
            this.title = title;
            this.filePath = filePath;
        }
    }
}
