package com.example.farmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DeleteAccountActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextInputEditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_account);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Find views
        etPassword = findViewById(R.id.etDelete);

        // Set click listeners
        findViewById(R.id.btnCancel).setOnClickListener(view -> finish());
        findViewById(R.id.btnConfirm).setOnClickListener(view -> {
            String enteredPassword = etPassword.getText().toString().trim();
            if (!enteredPassword.isEmpty()) {
                reauthenticateAndDeleteAccount(enteredPassword);
            } else {
                Toast.makeText(DeleteAccountActivity.this, "Please enter your password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void reauthenticateAndDeleteAccount(String password) {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            AuthCredential credential = EmailAuthProvider.getCredential(email, password);

            user.reauthenticate(credential).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    user.delete().addOnCompleteListener(deleteTask -> {
                        if (deleteTask.isSuccessful()) {
                            Toast.makeText(DeleteAccountActivity.this, "Account deleted successfully", Toast.LENGTH_SHORT).show();
                            // Start landing page after successful deletion
                            Intent intent = new Intent(DeleteAccountActivity.this, landingPage.class);
                            startActivity(intent);
                            finish(); // Close the current activity
                        } else {
                            Toast.makeText(DeleteAccountActivity.this, "Account deletion failed", Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(DeleteAccountActivity.this, "Reauthentication failed. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
