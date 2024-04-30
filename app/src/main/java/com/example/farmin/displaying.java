package com.example.farmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.os.Environment;
import android.speech.tts.TextToSpeech;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;

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

public class displaying extends AppCompatActivity {
    private ImageView ivQr, ivImage, ivQrcode, ivVoiceDescrip, ivVoiceNotes, ivExports;
    private TextView tvName, tvType, tvDescrip, tvNote;
    private Dialog qrDialog, dialog_exports;
    TextToSpeech t1;
    final static int RequestCode = 1232;
    private Button btnExportPdf,btnExportCsv, btnExportExcel;
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
        t1 = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR) {
                    t1.setLanguage(Locale.ENGLISH);
                }
            }
        });
        ivVoiceNotes = findViewById(R.id.ivVoiceNotes);
        ivVoiceDescrip = findViewById(R.id.ivVoiceDescrip);
        ivImage = findViewById(R.id.ivImage);
        ivQr = findViewById(R.id.ivQr);
        ivExports = findViewById(R.id.ivExports);
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
        dialog_exports = new Dialog(displaying.this);
        dialog_exports.setContentView(R.layout.dialog_exports);
        dialog_exports.setCancelable(true);
        dialog_exports.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog_exports.getWindow().setBackgroundDrawable(getDrawable(R.drawable.custom_dialogbg));
        btnExportPdf = dialog_exports.findViewById(R.id.btnExportPdf);
        btnExportCsv = dialog_exports.findViewById(R.id.btnExportCsv);
        btnExportExcel = dialog_exports.findViewById(R.id.btnExportExcel);
        ivVoiceNotes.setOnClickListener(view -> {
            String voice = tvNote.getText().toString();
            t1.speak(voice, TextToSpeech.QUEUE_FLUSH, null);
        });
        ivVoiceDescrip.setOnClickListener(view -> {
            String voice = tvDescrip.getText().toString();
            t1.speak(voice, TextToSpeech.QUEUE_FLUSH, null);
        });
        ivQr.setOnClickListener(view -> {
            qrDialog.show();
        });
        ivExports.setOnClickListener(view -> {
            dialog_exports.show();
        });
        btnExportPdf.setOnClickListener(view -> {
            createPdf(name, type, descrip, notes);
            dialog_exports.dismiss();
        });
        btnExportCsv.setOnClickListener(view -> {
            StringBuilder csvDataBuilder = new StringBuilder();
            csvDataBuilder.append("Name: "+name +",Type: "+ type +",Description: "+ descrip +",Notes: "+ notes);
            String csvData = csvDataBuilder.toString();
            String fileName = name.trim();
            createCsv(fileName+".csv", csvData);
            dialog_exports.dismiss();
        });
        btnExportExcel.setOnClickListener(view -> {
            createExcel(name,type,descrip,notes);
            dialog_exports.dismiss();
        });
    }

    private void createPdf(String name, String type, String descrip, String notes) {
        PdfDocument document = new PdfDocument();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1080, 1920, 1).create();
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(72);
        Paint paint2 = new Paint();
        paint.setColor(Color.GREEN);
        paint.setTextSize(48);
        canvas.drawText(name , 300, 500, paint);
        canvas.drawText(type , 300, 600, paint2);
        canvas.drawText(descrip , 300, 700, paint2);
        canvas.drawText(notes , 300, 800, paint2);
        document.finishPage(page);
        File downloads = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = name +".pdf";
        File file = new File(downloads, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void createCsv(String fileName, String csvData) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            if(!root.exists()){
                root.mkdirs();
            }
            File csvFile = new File(root, fileName);
            FileWriter writer = new FileWriter(csvFile);
            writer.append(csvData);
            writer.flush();
            writer.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void createExcel(String name, String type, String descrip, String notes) {
        String fileName = name+".xlsx";
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Name", "Type", "Description", "Notes"};
        for(int i = 0; i<headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }
        Row dataRow = sheet.createRow(1);
        dataRow.createCell(0).setCellValue(name);
        dataRow.createCell(1).setCellValue(type);
        dataRow.createCell(2).setCellValue(descrip);
        dataRow.createCell(3).setCellValue(notes);
        File downloadDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String filePath = downloadDir.getAbsolutePath() + File.separator + fileName;
        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            workbook.write(fos);
            fos.close();
            workbook.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, RequestCode);
    }
}