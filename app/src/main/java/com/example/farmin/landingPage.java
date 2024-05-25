package com.example.farmin;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class landingPage extends AppCompatActivity {
    private AppCompatButton btnSignup,btnSignin;
    private FirebaseUser currentUser;
    @Override
    public void onStart() {
        super.onStart();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
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
        setContentView(R.layout.activity_landing_page);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnSignin = findViewById(R.id.btnSignin);
        btnSignup = findViewById(R.id.btnSignup);
        btnSignin.setOnClickListener(view -> {
            intent(SignIn.class);
        });
        btnSignup.setOnClickListener(view -> {
            intent(SignUp.class);
        });
    }
    public void intent(Class intetnclass) {
        Intent intent = new Intent(getApplicationContext(), intetnclass);
        startActivity(intent);
        finish();
    }
}