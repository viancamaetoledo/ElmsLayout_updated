package com.example.elmslayout.Student.Course;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.R;

public class Handout_details extends AppCompatActivity {

    TextView detailedTitle, filePathTextView, textHandoutNumber;
    ImageView backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_handout_details);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        detailedTitle = findViewById(R.id.detailedTitle);
        filePathTextView = findViewById(R.id.linkFilePath);
        textHandoutNumber= findViewById(R.id.handout_number);

        backButton = findViewById(R.id.backpressed);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Handout_details.this, Student_handout.class);
                startActivity(intent);
            }
        });


        // Get the passed values
        String detailedTitles = getIntent().getStringExtra("titles");
        String filePath = getIntent().getStringExtra("filePath");
        String handoutNumber = getIntent().getStringExtra("handoutNo");

        // Set the title
        detailedTitle.setText(detailedTitles);
        textHandoutNumber.setText(handoutNumber);

        // Display and make the file path clickable
        if (filePath != null && !filePath.isEmpty()) {
            filePathTextView.setText(filePath);
            filePathTextView.setMovementMethod(LinkMovementMethod.getInstance());  // Enable clickable link
        }

    }
}