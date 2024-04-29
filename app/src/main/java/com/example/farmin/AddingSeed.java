package com.example.farmin;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;

public class AddingSeed extends AppCompatActivity {
    private EditText etName,etDescription, etNotes;
    private Button btnAdd;
    private FloatingActionButton btnGoBack;
    private ImageView ivImage, ivscan;
    private Spinner spinType;
    private final static int pickImageRequest = 1;
    private Uri imageUri;
    private Boolean isSelected , haveScan = false;
    String key, stringUri, qrCodeUrl;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_adding_seed);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnAdd = findViewById(R.id.btnAdd);
        btnGoBack = findViewById(R.id.btnGoBack);
        spinType = findViewById(R.id.spinType);
        etName = findViewById(R.id.etName);
        etDescription = findViewById(R.id.etDescription);
        etNotes = findViewById(R.id.etNotes);
        ivImage = findViewById(R.id.ivImage);
        ivscan = findViewById(R.id.ivscan);
        String[] items = getResources().getStringArray(R.array.typeCrop);
        ivscan.setOnClickListener(view -> {
            scanCode();
        });
        btnAdd.setOnClickListener(view -> {
            if(etName.getText().toString().isEmpty()) {
                etName.setError("Must not be empty");
            } else if (etNotes.getText().toString().isEmpty()) {
                etNotes.setError("Must not be empty");
            } else if (etDescription.getText().toString().isEmpty()) {
                etDescription.setError("Must not be empty");
            } else {
                addProduct(etName.getText().toString().trim(),spinType.getSelectedItem().toString(),etNotes.getText().toString().trim(),etDescription.getText().toString().trim());
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
    }
    private void scanCode() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
        haveScan = true;
    }
    ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() != null) {
            key = result.getContents();
            if(key.isEmpty()) {
                Toast.makeText(this, "Does not exist", Toast.LENGTH_SHORT).show();
            }
            String[] items = getResources().getStringArray(R.array.typeCrop);
            DatabaseReference cropRef = FirebaseDatabase.getInstance().getReference("Seeds");
            cropRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot postSnap : snapshot.getChildren()) {
                        addingUploads addingUploads = postSnap.getValue(com.example.farmin.addingUploads.class);
                        if (addingUploads.getKey().equals(key)) {
                            etName.setText(addingUploads.getName());
                            int index = Arrays.asList(items).indexOf(addingUploads.getType());
                            spinType.setSelection(index);
                            Glide.with(getApplicationContext())
                                    .load(addingUploads.getImageurl())
                                    .fitCenter()
                                    .into(ivImage);
                            etDescription.setText(addingUploads.getDescrip());
                            etNotes.setText(addingUploads.getNotes());
                            stringUri = addingUploads.getImageurl();
                            isSelected = true;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    });
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
            ivImage.setImageURI(imageUri);
            isSelected = false;
        }
    }
    private  String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void addProduct(String name,String type, String descrip, String notes) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference("Seeds");
        String key = productRef.push().getKey();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Seeds");
        MultiFormatWriter writer = new MultiFormatWriter();
        try {
            BitMatrix matrix = writer.encode(key, BarcodeFormat.QR_CODE, 800, 800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();
            StorageReference qrRef = storageRef.child(System.currentTimeMillis() + "." + ".jpg");
            qrRef.putBytes(data).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            qrCodeUrl = uri.toString();
                        }
                    });
                }
            });
        } catch (WriterException e) {
            throw new RuntimeException(e);
        }
        if(isSelected == false) {
            StorageReference fileReference = storageRef.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            fileReference.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            stringUri = uri.toString();
                            addingUploads addingUploads = new addingUploads(key, user.getEmail(), name, type, descrip, notes,stringUri,qrCodeUrl);
                            productRef.child(key).setValue(addingUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Log.d("please", "wirk");
                                }
                            });
                        }
                    });
                }
            });
        } else {
            addingUploads addingUploads = new addingUploads(key, user.getEmail(), name, type, descrip, notes,stringUri,qrCodeUrl);
            productRef.child(key).setValue(addingUploads).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d("please", "wirk");
                }
            });
        }


    }
}