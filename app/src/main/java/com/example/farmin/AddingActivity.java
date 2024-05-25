package com.example.farmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class AddingActivity extends AppCompatActivity {
    private ImageView ivFinishCalendar, ivStartCalendar, ivCheck, ivBox, ivImage;
    private Dialog calendarDialog;
    private EditText etFinishDate, etStartingDate, etName, etYield, etNotes;
    private FloatingActionButton btnGoBack;
    private CalendarView calendarView;
    private int startYear, startMonth, startDay, finishYear, finishMonth, finishDay;
    private String name, descrip, type, notes, stringUrl;
    private Boolean isSelected, isState;
    private final static int pickImageRequest = 1;
    private Uri imageUri;
    private ProgressDialog progressDialog;

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
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading, please wait...");
        btnGoBack = findViewById(R.id.btnGoBack);
        etName = findViewById(R.id.etName);
        etNotes = findViewById(R.id.etNotes);
        etYield = findViewById(R.id.etYield);
        ivStartCalendar = findViewById(R.id.ivStartCalendar);
        ivFinishCalendar = findViewById(R.id.ivFinishCalendar);
        ivCheck = findViewById(R.id.ivCheck);
        ivBox = findViewById(R.id.ivBox);
        ivImage = findViewById(R.id.ivImage);
        etStartingDate = findViewById(R.id.etStartingDate);
        etFinishDate = findViewById(R.id.etFinishDate);
        calendarDialog = new Dialog(AddingActivity.this);
        calendarDialog.setContentView(R.layout.dialog_calendar);
        calendarDialog.setCancelable(true);
        calendarDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        calendarDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogbg));
        calendarView = calendarDialog.findViewById(R.id.cvCalendar);
        ivStartCalendar.setOnClickListener(view -> {
            calendarDialog.show();
            isState = false;
        });
        ivFinishCalendar.setOnClickListener(view -> {
            calendarDialog.show();
            isState = true;
        });
        calendarView.setMinDate(System.currentTimeMillis());
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
                if (isState == false) {
                startYear = year;
                startMonth = month;
                startDay = day;
                calendarDialog.dismiss();
                etStartingDate.setText(startYear + "/"+ startMonth +"/"+startDay);
                } else if (isState == true) {
                    finishYear = year;
                    finishMonth = month;
                    finishDay = day;
                    calendarDialog.dismiss();
                    etFinishDate.setText(finishYear + "/"+ finishMonth +"/"+finishDay);
                }

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
            if(etName.getText().toString().equals("")) {
                etName.setError("Must not be Empty");
            } else if (etStartingDate.getText().toString().equals("")) {
                etStartingDate.setError("Must not be Empty");
            } else if (etFinishDate.getText().toString().equals("")) {
                etFinishDate.setError("Must not be Empty");
            } else if (etYield.getText().toString().equals("")) {
                etYield.setError("Must not be Empty");
            } else if (etNotes.getText().toString().equals("")) {
                etNotes.setError("Must not be Empty");
            } else {
                if(isSelected == true) {
                    addActivity(name,etYield.getText().toString(),String.valueOf(startYear),String.valueOf(startMonth), String.valueOf(startDay),String.valueOf(finishYear),String.valueOf(finishMonth), String.valueOf(finishDay), type, descrip, notes, stringUrl);
                } else {
                    addActivity(etName.getText().toString(), etYield.getText().toString(),String.valueOf(startYear),String.valueOf(startMonth), String.valueOf(startDay),String.valueOf(finishYear),String.valueOf(finishMonth), String.valueOf(finishDay), "", "", etNotes.getText().toString(), "");
                }
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
    private void addActivity(String name, String yield,String mstartYear, String mstartMonth, String mstartDay, String mfinishYear, String mfinishMonth, String mfinishDay, String type, String descrip, String notes, String urlImage) {
        progressDialog.show();
        DatabaseReference activityRef = FirebaseDatabase.getInstance().getReference("Activity");
        String key = activityRef.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(isSelected == true) {
            activityUploads activityUploads = new activityUploads(user.getEmail(),key,name, type, descrip, mstartYear, mstartMonth, mstartDay, mfinishYear, mfinishMonth, mfinishDay, yield, notes, urlImage);
            activityRef.child(key).setValue(activityUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    progressDialog.dismiss();
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
                            activityUploads activityUploads = new activityUploads(user.getEmail(),key,name, type, descrip, mstartYear, mstartMonth, mstartDay, mfinishYear, mfinishMonth, mfinishDay, yield, notes, uri.toString());
                            activityRef.child(key).setValue(activityUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    progressDialog.dismiss();
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