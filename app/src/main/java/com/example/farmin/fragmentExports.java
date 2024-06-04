package com.example.farmin;

import android.app.ProgressDialog;
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
    private ProgressDialog progressDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exports, container, false);
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Uploading, please wait...");
        viewHolderDisplay = new ViewModelProvider(requireActivity()).get(viewHolderDisplay.class);
        btnExportPdf = rootView.findViewById(R.id.btnExportPdf);
        btnExportCsv = rootView.findViewById(R.id.btnExportCsv);
        btnExportExcel = rootView.findViewById(R.id.btnExportExcel);
        String name = viewHolderDisplay.getName();
        String count = viewHolderDisplay.getCount();
        String type = viewHolderDisplay.getType();
        String notes = viewHolderDisplay.getNotes();
        String stringUrl = viewHolderDisplay.getImageurl();
        String stringQr = viewHolderDisplay.getQrcode();
        String key = viewHolderDisplay.getKey();
        String csKey = viewHolderDisplay.getCsType();
        btnExportPdf.setOnClickListener(view -> {
            String fileName = "FarmInExportPdf";
            File root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            if(!root.exists()){
                root.mkdirs();
            }
            File rawr = new File(root, fileName +".csv");
            int suffix = 1;
            while (rawr.exists()){
                fileName = fileName + "(" + suffix+ ")";
                suffix++;
                rawr = new File(root, fileName +".csv");
            }
            createPdf(fileName, name, type, count, notes);
        });
        btnExportCsv.setOnClickListener(view -> {
            String fileName = "FarmInExportCsv";
            File root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            if(!root.exists()){
                root.mkdirs();
            }
            File rawr = new File(root, fileName +".csv");
            int suffix = 1;
            while (rawr.exists()){
                fileName = fileName + "(" + suffix+ ")";
                suffix++;
                rawr = new File(root, fileName +".csv");
            }
            StringBuilder csvDataBuilder = new StringBuilder();
            csvDataBuilder.append("Name: "+name +",Type: "+ type +",Description: "+ count +",Notes: "+ notes+",key: "+ key+",Image Url: "+ stringUrl+",Qr code Url: "+ stringQr+",Cs Key: "+ csKey);
            String csvData = csvDataBuilder.toString();
            createCsv(fileName, csvData);
            Toast.makeText(getActivity(), "File Created, Please check your downloads", Toast.LENGTH_SHORT).show();

            createCsv(fileName, csvData);
        });
        btnExportExcel.setOnClickListener(view -> {
            String fileName = "FarmInExportExcel";
            File root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            if(!root.exists()){
                root.mkdirs();
            }
            File rawr = new File(root, fileName +".csv");
            int suffix = 1;
            while (rawr.exists()){
                fileName = fileName + "(" + suffix+ ")";
                suffix++;
                rawr = new File(root, fileName +".csv");
            }
            createExcel(fileName,name,type,count,notes);
        });
        return rootView;
    }
    private void createPdf(String realfilename,String name, String type, String descrip, String notes) {
        progressDialog.show();
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
        int suffix = 1;
        String mrealfilename = realfilename;
        File file;
        do {
            file = new File(downloads, mrealfilename + "(" + suffix + ").pdf");
            suffix++;
        } while (file.exists());
        try {
            FileOutputStream fos = new FileOutputStream(file);
            document.writeTo(fos);
            document.close();
            fos.close();
            Toast.makeText(getActivity(), "File Created, Please check your downloads", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private void createCsv(String fileName, String csvData) {
        progressDialog.show();
        try {
            File root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            if(!root.exists()){
                root.mkdirs();
            }
            int suffix = 1;
            File csvFile;
            do {
                csvFile = new File(root, fileName + "(" + suffix + ").csv");
                suffix++;
            } while (csvFile.exists());
            FileWriter writer = new FileWriter(csvFile);
            writer.append(csvData);
            writer.append("\n");
            writer.flush();
            writer.close();
            Toast.makeText(getActivity(), "File Created, Please check your downloads", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void createExcel(String realfileName,String name, String type, String descrip, String notes) {
        progressDialog.show();
        String mfileName = realfileName;
        int suffix = 1;
        File excelFile;
        do {
            excelFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                    mfileName + "(" + suffix + ").xlsx");
            suffix++;
        } while (excelFile.exists());
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

        try {
            FileOutputStream fos = new FileOutputStream(excelFile);
            workbook.write(fos);
            fos.close();
            workbook.close();
            Toast.makeText(getActivity(), "File Created, Please check your downloads", Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}