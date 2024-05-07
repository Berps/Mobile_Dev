package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class signUP extends AppCompatActivity {

    private EditText emailEditText, passwordEditText;
    private Button signUpButton;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        emailEditText = findViewById(R.id.EditText3);
        passwordEditText = findViewById(R.id.EditText6);
        signUpButton = findViewById(R.id.button1);

        signUpButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            if (!email.isEmpty() && !password.isEmpty()) {
                registerUser(email, password);
            } else {
                Toast.makeText(signUP.this, "Email and password must not be empty", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        if (user != null) {
                            saveUserToFirestore(user);
                        }
                    } else {
                        Toast.makeText(signUP.this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("signUP", "Registration failure: ", task.getException());
                    }
                });
    }

    private void saveUserToFirestore(FirebaseUser user) {
        User userData = new User(user.getEmail());
        db.collection("users").document(user.getUid()).set(userData)
                .addOnSuccessListener(aVoid -> {
                    Log.d("Firestore", "User successfully written to Firestore");
                    SharedPreferences preferences = getSharedPreferences("AppPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("email", user.getEmail());
                    editor.apply();

                    Intent intent = new Intent(signUP.this, DashBoard.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Log.e("Firestore", "Failed to write user to Firestore", e);
                    Toast.makeText(signUP.this, "Failed to register user in database: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    public static class User {
        private String email;

        public User(String email) {
            this.email = email;
        }

        public String getEmail() {
            return email;
        }
    }
}
