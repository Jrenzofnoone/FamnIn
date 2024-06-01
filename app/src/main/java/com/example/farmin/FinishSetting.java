package com.example.farmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FinishSetting extends AppCompatActivity {
    private TextInputEditText etAge,etNumber, etUsername;
    private AppCompatButton btnFinish;
    private String username = "", age = "", number = "";

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_finish_setting);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnFinish = findViewById(R.id.btnFinish);
        etUsername = findViewById(R.id.etUsername);
        etNumber = findViewById(R.id.etNumber);
        etAge = findViewById(R.id.etAge);

        btnFinish.setOnClickListener(view -> {
            username = etUsername.getText().toString();
            age = etAge.getText().toString();
            number = etNumber.getText().toString();
            if(username.equals("")) {
                etUsername.setError("Must not be Empty");
            } else {
                addUser(username, age, number);
            }
        });
    }
    private void addUser(String musername, String mage, String mnumber) {
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String key = userref.push().getKey();
        anotherUserUploads uploads = new anotherUserUploads(key,user.getEmail(),musername, mage, mnumber);

        userref.child(key).setValue(uploads).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(FinishSetting.this, "Successfully Setup", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                startActivity(intent);
                finish();
            }
        });
    }
}