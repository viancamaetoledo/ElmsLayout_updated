package com.example.elmslayout;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserSession {
        public static void fetchStudentNameByID(String username, OnNameFetchedListener listener) {
            DatabaseReference studentRef = FirebaseDatabase.getInstance()
                    .getReference("User")
                    .child("Role")
                    .child("Student")
                    .child(username); // studentID is the key (e.g., student123)

            studentRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        String name = snapshot.child("name").getValue(String.class); // Get "name"
                        if (name != null) {
                            listener.onFetched(name);
                        } else {
                            listener.onError("Name not found for student username: " + username);
                        }
                    } else {
                        listener.onError("Student username not found: " + username);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    listener.onError(error.getMessage());
                }
            });
        }

        public interface OnNameFetchedListener {
            void onFetched(String name);

            void onError(String error);
        }
}

