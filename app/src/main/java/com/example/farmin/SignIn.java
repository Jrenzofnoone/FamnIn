package com.example.farmin;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignIn extends AppCompatActivity {
    private DatabaseReference userDataRef;
    private TextInputEditText etEmail, etPassword;
    FirebaseAuth mAuth;
    private Button btnLogIn, btnCancel,btnReset;
    private TextView tvClickMe, tvForgetpassword;
    private CheckBox cbPassword;
    private Dialog forgotDialog;
    private FirebaseUser currentUser;

    @Override
    public void onStart() {
        super.onStart();
        currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            if(currentUser.isEmailVerified()){
                Intent dashboard = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(dashboard);
                finish();
            } else {
                Toast.makeText(this, "Please verify your Email", Toast.LENGTH_SHORT).show();
            }
        } else {
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
        tvForgetpassword = findViewById(R.id.tvForgetpassword);
        userDataRef = FirebaseDatabase.getInstance().getReference().child("userData");
        btnLogIn = findViewById(R.id.btnLogIn);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbPassword = findViewById(R.id.cbPassword);
        forgotDialog = new Dialog(SignIn.this);
        forgotDialog.setContentView(R.layout.dialog_forgot);
        forgotDialog.setCancelable(true);
        forgotDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        forgotDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogbg));
        btnReset = forgotDialog.findViewById(R.id.btnReset);
        btnCancel = forgotDialog.findViewById(R.id.btnCancel);
        etEmail = forgotDialog.findViewById(R.id.etEmail);

        tvClickMe.setOnClickListener(view -> {
            Intent login = new Intent(getApplicationContext(), SignUp.class);
            startActivity(login);
        });
        tvForgetpassword.setOnClickListener(view -> {
            forgotDialog.show();
        });
        btnCancel.setOnClickListener(view -> {
            forgotDialog.dismiss();
        });
        btnReset.setOnClickListener(view -> {
            String userEmail = etEmail.getText().toString();
            if(TextUtils.isEmpty(userEmail) && !Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                Toast.makeText(this, "Enter your registered Email", Toast.LENGTH_SHORT).show();
                return;
            }
            mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
                if(task.isSuccessful()) {
                    Toast.makeText(this, "Check Email", Toast.LENGTH_SHORT).show();
                    forgotDialog.dismiss();
                }else {
                    Toast.makeText(this, "Unable to Send", Toast.LENGTH_SHORT).show();
                }
            });
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
        cbPassword.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                etPassword.setTransformationMethod(null);
            } else {
                etPassword.setTransformationMethod(new PasswordTransformationMethod());
            }
        });
    }

}