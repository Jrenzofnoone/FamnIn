package com.example.farmin;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

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


public class fragmentExports extends Fragment {
    private Button btnExportPdf, btnExportCsv, btnExportExcel;
    private viewHolderDisplay viewHolderDisplay;
    private String fileName;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exports, container, false);
        viewHolderDisplay = new ViewModelProvider(requireActivity()).get(viewHolderDisplay.class);
        btnExportPdf = rootView.findViewById(R.id.btnExportPdf);
        btnExportCsv = rootView.findViewById(R.id.btnExportCsv);
        btnExportExcel = rootView.findViewById(R.id.btnExportExcel);
        String name = viewHolderDisplay.getName();
        String descrip = viewHolderDisplay.getDescrip();
        String type = viewHolderDisplay.getType();
        String notes = viewHolderDisplay.getNotes();
        String stringUrl = viewHolderDisplay.getQrcode();
        String stringQr = viewHolderDisplay.getImageurl();
        String key = viewHolderDisplay.getKey();
        String csKey = viewHolderDisplay.getCsType();
        btnExportPdf.setOnClickListener(view -> {
            createPdf(name, type, descrip, notes);
        });
        btnExportCsv.setOnClickListener(view -> {
            StringBuilder csvDataBuilder = new StringBuilder();
            csvDataBuilder.append("Name: "+name +",Type: "+ type +",Description: "+ descrip +",Notes: "+ notes);
            String csvData = csvDataBuilder.toString();
            if(name.equals("")){
                fileName = "name";
            } else {
                fileName = name.trim();
            }
            createCsv(fileName+".csv", csvData);
        });
        btnExportExcel.setOnClickListener(view -> {
            createExcel(name,type,descrip,notes);
        });
        return rootView;
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
        String fileName = name.trim() +".pdf";
        File file = new File(downloads, fileName);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT).show();
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
            writer.append("\n");
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
}