package com.example.farmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

public class displaying extends AppCompatActivity {
    private ImageView ivQr, ivImage, ivQrcode, ivVoiceDescrip, ivVoiceNotes, ivExports;
  //  private EditText etName,etType,etDescrip,etNote;
    private Dialog qrDialog, dialog_exports;
    TextToSpeech t1;
    final static int RequestCode = 1232;
    private Button btnExportPdf,btnExportCsv, btnExportExcel;
    private FloatingActionButton btnGoBack;
    private Boolean editState = false;
    private String fileName;
    private viewHolderDisplay viewHolderDisplay;
    private TabLayout tabLayout;
    private ViewPager viewPager;
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


        ivImage = findViewById(R.id.ivImage);
        btnGoBack = findViewById(R.id.btnGoBack);
        String name = getIntent().getStringExtra("Name");
        String descrip = getIntent().getStringExtra("Descrip");
        String type = getIntent().getStringExtra("Type");
        String notes = getIntent().getStringExtra("Notes");
        String stringUrl = getIntent().getStringExtra("Image");
        String stringQr = getIntent().getStringExtra("qrCode");
        String key = getIntent().getStringExtra("Key");
        String csKey = getIntent().getStringExtra("csKey");
        viewHolderDisplay.setName(name);
        viewHolderDisplay.setDescrip(descrip);
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


        btnGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(displaying.this, DashBoard.class);
            startActivity(intent);
            finish();
        });
    }



    private void askPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode);
        } else {
        }

    }
}