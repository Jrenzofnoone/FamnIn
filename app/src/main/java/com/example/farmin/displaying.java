package com.example.farmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;
import java.util.Map;

public class displaying extends AppCompatActivity implements displayingInterface{
    private ImageView ivQr, ivImage, ivQrcode, ivVoiceDescrip, ivVoiceNotes, ivExports, ivGoBack;
    //  private EditText etName,etType,etDescrip,etNote;
    private Dialog qrDialog, dialog_exports;
    TextToSpeech t1;
    final static int RequestCode = 1232;
    private Button btnExportPdf,btnExportCsv, btnExportExcel;
    private Boolean editState = false;
    private String fileName;
    private viewHolderDisplay viewHolderDisplay;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private final static int pickImageRequest = 1;
    private Uri imageUri;
    private ProgressDialog progressDialog;
    private String depends = "";
    private String name, count, notes,type, stringUrl, stringQr, key, csKey;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_displaying);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        askPermission();
        tabLayout = findViewById(R.id.tabLayout);
        viewPager = findViewById(R.id.viewPager);
        tabLayout.setupWithViewPager(viewPager);
        VPadapter vpAdapter = new VPadapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new fragmentOverview(), "Overview");
        vpAdapter.addFragment(new fragmentExports(), "Exports");
        vpAdapter.addFragment(new fragmentQr(), "Qrcode");
        viewPager.setAdapter(vpAdapter);

        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });
        viewHolderDisplay = new ViewModelProvider(this).get(viewHolderDisplay.class);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading, please wait...");

        ivImage = findViewById(R.id.ivImage);
        ivGoBack = findViewById(R.id.ivGoBack);
        name = getIntent().getStringExtra("Name");
        count = getIntent().getStringExtra("Count");
        type = getIntent().getStringExtra("Type");
        notes = getIntent().getStringExtra("Notes");
        stringUrl = getIntent().getStringExtra("Image");
        stringQr = getIntent().getStringExtra("qrCode");
        key = getIntent().getStringExtra("key");
        csKey = getIntent().getStringExtra("csKey");
        Log.d("qrcode", stringQr);
        viewHolderDisplay.setName(name);
        viewHolderDisplay.setCount(count);
        viewHolderDisplay.setType(type);
        viewHolderDisplay.setNotes(notes);
        viewHolderDisplay.setQrcode(stringQr);
        viewHolderDisplay.setImageurl(stringUrl);
        viewHolderDisplay.setKey(key);
        viewHolderDisplay.setCsType(csKey);
        if(!isFinishing() && !isDestroyed()) {
            Glide.with(getApplicationContext())
                    .load(stringUrl)
                    .fitCenter()
                    .into(ivImage);
        }
        ivGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(displaying.this, DashBoard.class);
            startActivity(intent);
            finish();
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(depends.equals("false")) {
                    openImagePicker();

                } else {

                }
            }
        });
    }



    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode);
        } else {
        }

    }
    private void openImagePicker() {
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
            uploadImageToFirebase(imageUri);
            Toast.makeText(this, "is this working", Toast.LENGTH_SHORT).show();
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        progressDialog.show();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Product");
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                Glide.with(getApplicationContext()).load(imageUrl).fitCenter().into(ivImage);
                progressDialog.dismiss();
                viewHolderDisplay.setImageurl(imageUrl);
            });
        }).addOnFailureListener(e -> {
            Log.d("error", "definitely failed");
        });
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
    @Override
    public void setOnclick(String Bool) {
        if(Bool.equals("false")){
            depends = Bool;
        } else if (Bool.equals("true")){
            depends = Bool;
        } else {
        }
    }
}