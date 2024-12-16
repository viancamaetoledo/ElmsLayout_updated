package com.example.elmslayout.Student.Course;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.R;
import com.example.elmslayout.Teacher.CourseDetails.HandoutDet.Handouts;

public class term_Student_lists extends AppCompatActivity {

    CardView recCard, recCard2, recCard3;
    TextView titlePrelim, titleMidterm, titleFinal;
    TextView subjectTitles, term;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_term_list);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        recCard = findViewById(R.id.recCard);
        recCard2 = findViewById(R.id.recCard2);
        recCard3 = findViewById(R.id.recCard3);
        titlePrelim = findViewById(R.id.titlePrelim);
        titleMidterm = findViewById(R.id.titleMidterm);
        titleFinal = findViewById(R.id.titleFinal);
        subjectTitles = findViewById(R.id.subjectTitle);
        term = findViewById(R.id.termTitle);

        String prelims =titlePrelim.getText().toString().trim();
        String midterms =titleMidterm.getText().toString().trim();
        String finals =titleFinal.getText().toString().trim();

        String subjectName = getIntent().getStringExtra("Title");
        String termName = getIntent().getStringExtra("term");

        if (subjectName != null) {
            subjectTitles.setText(subjectName);
        } else {
            subjectTitles.setText("No subject name passed!");
        }
        if (termName != null) {
            term.setText(termName);
        } else {
            term.setText("No term name passed!");
        }

        recCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(term_Student_lists.this, Student_handout.class);
                intent.putExtra("period", prelims);
                intent.putExtra("Title", subjectName);
                intent.putExtra("term", termName);
                startActivity(intent);
            }
        });
        recCard2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(term_Student_lists.this, Student_handout.class);
                intent.putExtra("period", midterms);
                intent.putExtra("Title", subjectName);
                intent.putExtra("term", termName);
                startActivity(intent);
            }
        });
        recCard3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(term_Student_lists.this, Student_handout.class);
                intent.putExtra("period", finals);
                intent.putExtra("Title", subjectName);
                intent.putExtra("term", termName);
                startActivity(intent);
            }
        });

    }
}