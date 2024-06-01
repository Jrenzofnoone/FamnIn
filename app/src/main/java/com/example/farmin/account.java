package com.example.farmin;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class account extends AppCompatActivity {
    private AppCompatButton btnDeleteAccount, btnChangePassword;
    private EditText etNickname, etNumber, etAge;
    private FirebaseAuth mAuth;
    private ImageView ivEdit, ivGoBack, userImage;
    private Boolean editState = false;
    private String key;
    private static final int PICK_IMAGE_REQUEST = 1;
    private StorageReference userStorageRef;

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

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            startActivity(new Intent(this, SignUp.class));
            finish();
            return;
        }

        initializeViews();
        loadUserImage();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot post : snapshot.getChildren()) {
                    anotherUserUploads anotherUser = post.getValue(anotherUserUploads.class);
                    if (user.getEmail().equals(anotherUser.getUserEmail())) {
                        etNickname.setText(anotherUser.getUserNickname());
                        etAge.setText(anotherUser.getUserAge());
                        etNumber.setText(anotherUser.getUserNumber());
                        key = anotherUser.getKey();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        ivGoBack.setOnClickListener(view -> {
            startActivity(new Intent(getApplicationContext(), DashBoard.class));
            finish();
        });

        ivEdit.setOnClickListener(view -> {
            if (editState == false) {
                enableEditing();
                editProfile();
            } else {
                disableEditing();
                updateProfile();
            }
        });

        btnChangePassword.setOnClickListener(view -> {
            promptForPasswordAndChangePassword(user.getEmail());
        });

        btnDeleteAccount.setOnClickListener(view -> {
            promptForPasswordAndDeleteAccount(user);
        });
    }

    private void initializeViews() {
        ivEdit = findViewById(R.id.ivEdit);
        btnDeleteAccount = findViewById(R.id.btnDeleteAccount);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        etNickname = findViewById(R.id.etNickname);
        etNumber = findViewById(R.id.etNumber);
        etAge = findViewById(R.id.etAge);
        ivGoBack = findViewById(R.id.ivGoBack);
        userImage = findViewById(R.id.userImage);
        userImage.setOnClickListener(v -> openImagePicker());
    }

    private void loadUserImage() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userStorageRef = FirebaseStorage.getInstance().getReference("User").child(user.getUid() + ".jpg");
            userStorageRef.getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(getApplicationContext()).load(uri).fitCenter().into(userImage);
            }).addOnFailureListener(e -> {
                Toast.makeText(account.this, "Failed to load image", Toast.LENGTH_SHORT).show();
            });
        }
    }

    private void enableEditing() {
        etNickname.setEnabled(true);
        etAge.setEnabled(true);
        etNumber.setEnabled(true);
        ivEdit.setImageResource(R.drawable.check);
        editState = true;
    }

    private void disableEditing() {
        etNickname.setEnabled(false);
        etAge.setEnabled(false);
        etNumber.setEnabled(false);
        ivEdit.setImageResource(R.drawable.edit);
        editState = false;
    }
    private void editProfile() {
        String nickname = etNickname.getText().toString();
        String age = etAge.getText().toString();
        String number = etNumber.getText().toString();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(key).child("userNickname").setValue(nickname);
        userRef.child(key).child("userAge").setValue(age);
        userRef.child(key).child("userNumber").setValue(number);
        Toast.makeText(getApplicationContext(), "Profile edit successfully", Toast.LENGTH_SHORT).show();
    }
    private void updateProfile() {
        String nickname = etNickname.getText().toString();
        String age = etAge.getText().toString();
        String number = etNumber.getText().toString();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.child(key).child("userNickname").setValue(nickname);
        userRef.child(key).child("userAge").setValue(age);
        userRef.child(key).child("userNumber").setValue(number);
        Toast.makeText(getApplicationContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
    }

    private void promptForPasswordAndChangePassword(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Change Password");
        builder.setMessage("A password reset link will be sent to your email address.");

        builder.setPositiveButton("Send Reset Link", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mAuth.sendPasswordResetEmail(email)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Toast.makeText(getApplicationContext(), "Password reset email sent", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }





    private void promptForPasswordAndDeleteAccount(FirebaseUser user) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Please enter your password to confirm deletion");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String password = input.getText().toString();
                if (!password.isEmpty()) {
                    // Attempt to re-authenticate the user
                    mAuth.signInWithEmailAndPassword(user.getEmail(), password)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    // If re-authentication is successful, delete the account
                                    deleteAccount(user);
                                } else {
                                    Toast.makeText(getApplicationContext(), "Authentication failed. Account not deleted.", Toast.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    Toast.makeText(getApplicationContext(), "Please enter your password", Toast.LENGTH_SHORT).show();
                }
            }
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri selectedImageUri = data.getData();
            uploadImageToFirebase(selectedImageUri);
        }
    }

    private void uploadImageToFirebase(Uri imageUri) {
        if (userStorageRef != null) {
            userStorageRef.putFile(imageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Handle successful upload
                        loadUserImage(); // Reload user image after upload
                    })
                    .addOnFailureListener(e -> {
                        // Handle upload failure
                        Toast.makeText(account.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                    });
        }
    }  private void deleteAccount(FirebaseUser user) {
        user.delete().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(getApplicationContext(), "Account deleted successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), SignIn.class));
                finish();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to delete account", Toast.LENGTH_SHORT).show();
            }
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
