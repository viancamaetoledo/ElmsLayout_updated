package com.example.elmslayout.Main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.elmslayout.Admin.Admin_Dashboard;
import com.example.elmslayout.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Arrays;
import java.util.List;

public class Login extends AppCompatActivity {

    EditText loginUsername, loginPass;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        loginUsername = findViewById(R.id.login_username);
        loginPass = findViewById(R.id.login_password);
        loginButton = findViewById(R.id.add_button);

        loginButton.setOnClickListener(v -> {
            String enteredUsername = loginUsername.getText().toString().trim();
            String enteredPassword = loginPass.getText().toString().trim();

            if (validateUserEmail() && validateUserPassword()) {
                checkUser(enteredUsername, enteredPassword);
            }
        });
    }

    public boolean validateUserEmail() {
        String val = loginUsername.getText().toString();
        if (val.isEmpty()) {
            loginUsername.setError("Username cannot be empty");
            return false;
        } else {
            loginUsername.setError(null);
            return true;
        }
    }

    public boolean validateUserPassword() {
        String val = loginPass.getText().toString();
        if (val.isEmpty()) {
            loginPass.setError("Password cannot be empty");
            return false;
        } else {
            loginPass.setError(null);
            return true;
        }
    }

    public void checkUser(String username, String password) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User/Role");

        // List of roles to check
        List<String> roles = Arrays.asList("Admin", "Student", "Teacher");

        for (String role : roles) {
            reference.child(role).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.hasChild(username)) {
                        String passwordFromDb = snapshot.child(username).child("password").getValue(String.class);

                        if (passwordFromDb != null && passwordFromDb.equals(password)) {
                            redirectToDashboard(role, username);
                        } else {
                            loginPass.setError("Invalid Password");
                            loginPass.requestFocus();
                        }
                        return;
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(Login.this, "Database Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public void redirectToDashboard(String role, String username) {
        Intent intent;
        switch (role) {
            case "Student":
                intent = new Intent(Login.this, com.example.elmslayout.Main.Student_Dashboard.class);
                break;
            case "Teacher":
                intent = new Intent(Login.this, com.example.elmslayout.Main.Teachers_Dashboard.class);
                break;
            case "Admin":
                intent = new Intent(Login.this, Admin_Dashboard.class);
                break;
            default:
                Toast.makeText(this, "Role not recognized", Toast.LENGTH_SHORT).show();
                return;
        }
        intent.putExtra("username", username);
        intent.putExtra("role", role);
        startActivity(intent);
        finish();
    }
}
