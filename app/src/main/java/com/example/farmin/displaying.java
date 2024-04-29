package com.example.farmin;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

public class displaying extends AppCompatActivity {
    private ImageView ivQr, ivImage, ivQrcode;
    private TextView tvName, tvType, tvDescrip, tvNote;
    private Dialog qrDialog;

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
        ivImage = findViewById(R.id.ivImage);
        ivQr = findViewById(R.id.ivQr);
        tvName = findViewById(R.id.tvName);
        tvType = findViewById(R.id.tvType);
        tvDescrip = findViewById(R.id.tvDescrip);
        tvNote = findViewById(R.id.tvNote);
        String name = getIntent().getStringExtra("Name");
        String descrip = getIntent().getStringExtra("Descrip");
        String type = getIntent().getStringExtra("Type");
        String notes = getIntent().getStringExtra("Notes");
        String stringUrl = getIntent().getStringExtra("Image");
        String stringQr = getIntent().getStringExtra("qrCode");
        tvName.setText(name);
        tvDescrip.setText(descrip);
        tvType.setText(type);
        tvNote.setText(notes);
        if(!isFinishing() && !isDestroyed()) {
            Glide.with(getApplicationContext())
                    .load(stringUrl)
                    .fitCenter()
                    .into(ivImage);
            Glide.with(getApplicationContext())
                    .load(stringQr)
                    .fitCenter()
                    .into(ivQr);
        }
        qrDialog = new Dialog(displaying.this);
        qrDialog.setContentView(R.layout.dialog_qr);
        qrDialog.setCancelable(true);
        qrDialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        qrDialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogbg));
        ivQrcode = qrDialog.findViewById(R.id.ivQrcode);
        if(!isFinishing() && !isDestroyed()) {
            Glide.with(getApplicationContext())
                    .load(stringQr)
                    .fitCenter()
                    .into(ivQrcode);
        }
        ivQr.setOnClickListener(view -> {
            qrDialog.show();
        });
    }
}