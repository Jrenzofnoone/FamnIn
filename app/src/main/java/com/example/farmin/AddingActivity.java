package com.example.farmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddingActivity extends AppCompatActivity {
    private ImageView ivCalendar, ivCheck, ivBox, ivImage;
    private Dialog calendarDialog;
    private EditText etDate, etName, etYield, etNotes;
    private FloatingActionButton btnGoBack;
    private CalendarView calendarView;
    private int myear, mmonth, mday;
    private String name, descrip, type, notes, stringUrl;
    private Boolean isSelected ;
    private final static int pickImageRequest = 1;
    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding_activity);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnGoBack = findViewById(R.id.btnGoBack);
        etName = findViewById(R.id.etName);
        etNotes = findViewById(R.id.etNotes);
        etYield = findViewById(R.id.etYield);
        ivCalendar = findViewById(R.id.ivCalendar);
        ivCheck = findViewById(R.id.ivCheck);
        ivBox = findViewById(R.id.ivBox);
        ivImage = findViewById(R.id.ivImage);
        etDate = findViewById(R.id.etDate);
        calendarDialog = new Dialog(AddingActivity.this);
        calendarDialog.setContentView(R.layout.dialog_calendar);
        calendarDialog.setCancelable(true);
        calendarDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        calendarDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogbg));
        calendarView = calendarDialog.findViewById(R.id.cvCalendar);
        ivCalendar.setOnClickListener(view -> {
            calendarDialog.show();
        });
        calendarView.setMinDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                myear = year;
                mmonth = month;
                mday = day;
                calendarDialog.dismiss();
                etDate.setText(myear + "/"+ mmonth +"/"+mday);
            }
        });
        int depends = getIntent().getIntExtra("depends", 1);

        if(depends ==1 ) {

        } else {
            Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            name = getIntent().getStringExtra("Name");
            descrip = getIntent().getStringExtra("Descrip");
            type = getIntent().getStringExtra("Type");
            notes = getIntent().getStringExtra("Notes");
            stringUrl = getIntent().getStringExtra("Image");
                etName.setText(name);
                etNotes.setText(notes);
                isSelected = true;
                if(!isFinishing() && !isDestroyed()) {
                    Glide.with(getApplicationContext())
                            .load(stringUrl)
                            .fitCenter()
                            .into(ivImage);
                }
        }

        ivCheck.setOnClickListener(view -> {
            if(isSelected == true) {
                addActivity(name,etYield.getText().toString(),String.valueOf(myear),String.valueOf(mmonth), String.valueOf(mday), type, descrip, notes, stringUrl);
            } else {
                addActivity(etName.getText().toString(), etYield.getText().toString(),String.valueOf(myear),String.valueOf(mmonth), String.valueOf(mday), "", "", etNotes.getText().toString(), "");
            }
        });
        ivImage.setOnClickListener(view -> {
            openFileChooser();
        });
        btnGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
            startActivity(intent);
            finish();
        });
        ivBox.setOnClickListener(view -> {
            Intent intent = new Intent(this, AllCropSeed.class);
            startActivity(intent);
            finish();
        });
    }
    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, pickImageRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == pickImageRequest && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            isSelected = false;
            ivImage.setImageURI(imageUri);
        }
    }
    private  String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    private void addActivity(String name, String yield,String year, String month, String day, String type, String descrip, String notes, String urlImage) {
        DatabaseReference activityRef = FirebaseDatabase.getInstance().getReference("Activity");
        String key = activityRef.push().getKey();
        if(isSelected == true) {
            activityUploads activityUploads = new activityUploads(key,name, type, descrip, year, month, day, yield, notes, urlImage);
            activityRef.child(key).setValue(activityUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(AddingActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                    startActivity(intent);
                    finish();
                }
            });
        } else {
            StorageReference storageRef = FirebaseStorage.getInstance().getReference("Activity");
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            activityUploads activityUploads = new activityUploads(key,name, type, descrip, year, month, day, yield, notes, uri.toString());
                            activityRef.child(key).setValue(activityUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(AddingActivity.this, "Completed", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(getApplicationContext(), DashBoard.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                        }
                    });
                }
            });
        }
    }
}