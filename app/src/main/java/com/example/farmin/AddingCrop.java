package com.example.farmin;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddingCrop extends AppCompatActivity implements addingInterface {
//    private EditText etName,etDescription, etNotes;
//    private Button btnAdd;
    private FloatingActionButton btnGoBack;
    private ImageView ivscan;
//    private Spinner spinType;
    private final static int pickImageRequest = 1;
    private final static int pickFileRequest = 123;
    private Uri imageUri;
    private String key;
//    String key, stringUri, qrCodeUrl;
    private String haveScan = "none";
//    private ProgressBar progressBar;
    private RecyclerView recyclerView;
    private addingAdapter adapter;
    private List<addingUploads> uploads;
    private ImageView ivImport;
    private int pos;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding_crop);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int itemHeight = (int) (screenHeight * 0.2);
        ivImport = findViewById(R.id.ivImport);
        recyclerView = findViewById(R.id.recyclerView);
        uploads = new ArrayList<>();
        addingUploads addingUploads = new addingUploads("","","","","","","","","");
        uploads.add(addingUploads);
        adapter = new addingAdapter(AddingCrop.this, "Crops", uploads, this, itemHeight);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        ivImport.setOnClickListener(view -> {
            openFilePicker();
        });
//        btnAdd = findViewById(R.id.btnAdd);
        btnGoBack = findViewById(R.id.btnGoBack);
//        spinType = findViewById(R.id.spinType);
//        etName = findViewById(R.id.etName);
//        etDescription = findViewById(R.id.etDescription);
//        etNotes = findViewById(R.id.etNotes);
//        ivImage = findViewById(R.id.ivImage);
        ivscan = findViewById(R.id.ivscan);
//        progressBar = findViewById(R.id.progressBar);

        ivscan.setOnClickListener(view -> {
            scanCode();
            haveScan = "true";
        });
//        if(haveScan == true) {
//
//        } else {
//
//        }
//        btnAdd.setOnClickListener(view -> {
//            if(etName.getText().toString().isEmpty()) {
//                etName.setError("Must not be empty");
//            } else if (etNotes.getText().toString().isEmpty()) {
//                etNotes.setError("Must not be empty");
//            } else if (etDescription.getText().toString().isEmpty()) {
//                etDescription.setError("Must not be empty");
//            } else {
//                progressBar.setVisibility(View.VISIBLE);
//                addProduct(etName.getText().toString().trim(),spinType.getSelectedItem().toString(),etNotes.getText().toString().trim(),etDescription.getText().toString().trim());
//            }
//        });
        btnGoBack.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DashBoard.class);
            startActivity(intent);
            finish();
        });
//        ivImage.setOnClickListener(view -> {
//            openFileChooser();
//        });
    }
    @Override
    public void setItemClick(int position, String itemClick) {
        pos = position;
        if(itemClick.equals("Image")) {
            openImagePicker();
            haveScan = "false";
        } else if(itemClick.equals("Background")){

        } else {
            scanCode();
        }
    }


    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/*");
        startActivityForResult(intent, pickFileRequest);
    }

    private void openImagePicker() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, pickImageRequest);
    }

    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            key = result.getContents();
            if(key.isEmpty()) {
                Toast.makeText(this, "Does not exist", Toast.LENGTH_SHORT).show();
            }
            DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference("Crops");
            cropRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnap : snapshot.getChildren()) {
                        addingUploads addingUploads = postSnap.getValue(addingUploads.class);
                        if (addingUploads.getKey().equals(key)) {
                           adapter.setAllforItemQr(pos,addingUploads.getName(), addingUploads.getType(),addingUploads.getDescrip(),
                                   addingUploads.getNotes(),addingUploads.getImageurl());
                            haveScan = "none";
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    });


//
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("error", String.valueOf(requestCode));
        if(requestCode == pickImageRequest && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imageUri = data.getData();
            uploadImageToFirebase(imageUri);
            Toast.makeText(this, "is this working", Toast.LENGTH_SHORT).show();
        } else if (requestCode == pickFileRequest && resultCode == RESULT_OK) {
            Toast.makeText(this, "mane i hate life", Toast.LENGTH_SHORT).show();
            if(data != null) {
                Uri uri = data.getData();
                uploads.clear();
                List<addingUploads> newUploads = readCSV(uri);
                if(newUploads != null && !newUploads.isEmpty()){
                    uploads.addAll(newUploads);
                } else {

                }
                adapter.notifyDataSetChanged();
            }
        }
    }
    private void uploadImageToFirebase(Uri imageUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Product");
        StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        fileReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                adapter.setImageForItem(pos, imageUrl);
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
    private List<addingUploads> readCSV(Uri uri) {
        List<addingUploads> muploads = new ArrayList<>();
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while((line = reader.readLine()) != null){
                String[] string = line.split(",");
                addingUploads addingUploads = new addingUploads();
                addingUploads.setName(string[0]);
                addingUploads.setType(string[1]);
                addingUploads.setDescrip(string[2]);
                addingUploads.setNotes(string[3]);
                addingUploads.setKey(string[4]);
                addingUploads.setImageurl(string[5]);
                addingUploads.setQrcode(string[6]);
                addingUploads.setCsType(string[7]);
                muploads.add(addingUploads);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return muploads;
    }
    private static final int PermissionCode = 1001;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PermissionCode);
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "hell", Toast.LENGTH_SHORT).show();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == PermissionCode) {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "hello", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "hell", Toast.LENGTH_SHORT).show();
            }
        }
    }
}