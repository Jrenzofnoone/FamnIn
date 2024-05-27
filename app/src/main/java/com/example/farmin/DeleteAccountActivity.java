package com.example.farmin;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
    private Dialog dialogAccount;

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

        // Create and adjust dialog
        dialogAccount = new Dialog(this);
        dialogAccount.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogAccount.setContentView(R.layout.dialog_account);
        dialogAccount.setCancelable(true);

        Window dialogWindow = dialogAccount.getWindow();
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.dimAmount = 0.6f; // Set dim amount for semi-transparency
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT; // Adjust width to match_parent
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT; // Adjust height to wrap_content
        dialogWindow.setAttributes(layoutParams);
        dialogWindow.setBackgroundDrawableResource(android.R.color.transparent); // Set background to transparent
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
