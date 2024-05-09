package com.example.farmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {
    private DatabaseReference userDataRef;
    private EditText etEmail, etPassword;
    FirebaseAuth mAuth;
    private Button btnLogIn;
    private TextView tvClickMe;

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
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
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        tvClickMe = findViewById(R.id.tvClickMe);
        userDataRef = FirebaseDatabase.getInstance().getReference().child("userData");
        btnLogIn = findViewById(R.id.btnLogIn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);

        tvClickMe.setOnClickListener(view -> {
            Intent login = new Intent(getApplicationContext(), SignUp.class);
            startActivity(login);
        });

        btnLogIn.setOnClickListener(view -> {
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            if (email.isEmpty() ) {
                etEmail.setError("Cannot be Empty");
            } else if (password.isEmpty()) {
                etPassword.setError("Cannot be Empty");
            } else {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                    if(user.isEmailVerified()){
                                        Toast.makeText(SignIn.this, "Login Successfully ", Toast.LENGTH_SHORT).show();
                                        Intent dashboard = new Intent(getApplicationContext(), DashBoard.class);
                                        startActivity(dashboard);
                                        finish();
                                    } else  {
                                        Toast.makeText(SignIn.this, "verify it you bitch ", Toast.LENGTH_SHORT).show();
                                    }

                                } else {
                                    Toast.makeText(SignIn.this, "Wrond Password", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }
}