package com.example.farmin;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class SignUp extends AppCompatActivity {
    private Button btnSignUp;
    private TextView tvClickMe;
    FirebaseAuth mAuth;
    private TextInputEditText etEmail, etPassword, etConfirmPassword;
    private CheckBox cbPassword;

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            if(currentUser.isEmailVerified()){
                Intent dashboard = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(dashboard);
                finish();
            } else {
                Toast.makeText(this, "Please verify your Email", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        btnSignUp = findViewById(R.id.btnSignUp);
        cbPassword = findViewById(R.id.cbPassword);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        tvClickMe = findViewById(R.id.tvClickMe);
        btnSignUp.setOnClickListener(view -> {
                String email = etEmail.getText().toString();
                String password = etPassword.getText().toString();
                String thing = "[a-zA-Z0-9]*$";
            Pattern pattern = Pattern.compile(thing);
                String confirmPassword = etConfirmPassword.getText().toString();
                if (email.isEmpty()) {
                    etEmail.setError("Cannot be Empty");
                } else if (password.isEmpty()) {
                    etPassword.setError("Cannot be Empty");
                } else if(password.length() < 8) {
                    etPassword.setError("Length must be more than 8 letters");
                } else if(pattern.matcher(password).matches()) {
                    etPassword.setError("Must contain special letters");
                } else if (!password.equals(confirmPassword)) {
                    Toast.makeText(SignUp.this, "Password does not match", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(SignUp.this, "Please verify ", Toast.LENGTH_SHORT).show();
                                    }
                                });

                            } else {
                                Toast.makeText(SignUp.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
        });
        tvClickMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent login = new Intent(getApplicationContext(), SignIn.class);
                startActivity(login);
            }
        });
        cbPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPassword.setTransformationMethod(null);
                etConfirmPassword.setTransformationMethod(null);
            } else {
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
                etConfirmPassword.setTransformationMethod(new PasswordTransformationMethod());
            }});
    }
}