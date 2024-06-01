package com.example.farmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Objects;

public class account extends AppCompatActivity {
    private AppCompatButton btnDeleteAccount, btnChangePassword;
    private EditText etNickname,etNumber, etAge;
    private FirebaseAuth mAuth;
    private ImageView ivEdit;
    private Boolean editState = false;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_account);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        ivEdit = findViewById(R.id.ivEdit);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        etNickname = findViewById(R.id.etNickname);
        etNumber = findViewById(R.id.etNumber);
        etAge = findViewById(R.id.etAge);
        mAuth = FirebaseAuth.getInstance();
        etNickname.setEnabled(false);
        etAge.setEnabled(false);
        etNumber.setEnabled(false);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference userref = FirebaseDatabase.getInstance().getReference("Users");
        userref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot post : snapshot.getChildren()) {
                    anotherUserUploads anotherUserUploads = post.getValue(anotherUserUploads.class);
                    if(user.getEmail().equals(anotherUserUploads.getUserEmail())){
                        etNickname.setText(anotherUserUploads.getUserNickname());
                        etAge.setText(anotherUserUploads.getUserAge());
                        etNumber.setText(anotherUserUploads.getUserNumber());
                        key = anotherUserUploads.getKey();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ivEdit.setOnClickListener(view -> {
            if(editState == false) {
                etNickname.setEnabled(true);
                etAge.setEnabled(true);
                etNumber.setEnabled(true);
                ivEdit.setImageResource(R.drawable.check);
                editState = true;
            } else {
                update(key, etNickname.getText().toString(), etAge.getText().toString(), etNumber.getText().toString());
                editState = false;
                etNickname.setEnabled(false);
                etAge.setEnabled(false);
                etNumber.setEnabled(false);
                ivEdit.setImageResource(R.drawable.edit);
            }
        });
        btnChangePassword.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to change Password?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAuth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(task -> {
                        if(task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Check Email", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Unable to Send", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
        btnDeleteAccount.setOnClickListener(view -> {
            Toast.makeText(getApplicationContext(), "Clicked", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Confirmation");
            builder.setMessage("Are you sure you want to delete your Account?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    user.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Intent intent = new Intent(getApplicationContext(), SignUp.class);
                            startActivity(intent);
                            finish();
                        }
                    });
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });
    }
    private void update(String key,String userNickname, String userAge, String userNumber) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Users");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        anotherUserUploads anotherUserUploads = new anotherUserUploads(key,user.getEmail(), userNickname, userAge, userNumber);
        productRef.child(key).setValue(anotherUserUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getApplicationContext(), "It works if this appear", Toast.LENGTH_SHORT).show();
            }
        });
    }
}