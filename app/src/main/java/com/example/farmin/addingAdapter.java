package com.example.farmin;

import static androidx.core.app.ActivityCompat.startActivityForResult;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import android.net.Uri;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class addingAdapter extends RecyclerView.Adapter<addingAdapter.viewHolder> {
    private List<addingUploads> uploads;
    private Context context;
    private addingInterface addingInterface;
    String key, qrCodeUrl;
    String name, type, descrip, notes;
    private String ref;
    private int selectedItem = -1;

    public addingAdapter(Context context, String ref,List<addingUploads> uploads, addingInterface addingInterface) {
        if (uploads == null || uploads.isEmpty()) {
            addingUploads addingUploads = new addingUploads("","","","","","","","","");
            uploads.add(addingUploads);
        }
        this.uploads = uploads;
        this.ref = ref;
        this.context = context;
        this.addingInterface = addingInterface;
    }
    @NonNull
    @Override
    public addingAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adding_item, parent, false);
        return new viewHolder(v , addingInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull addingAdapter.viewHolder holder, int position) {
        addingUploads currentUploads = uploads.get(position);
        String[] items = context.getResources().getStringArray(R.array.typeCrop);
        holder.etName.setText(currentUploads.getName());
        int index = Arrays.asList(items).indexOf(currentUploads.getType());
        holder.spinType.setSelection(index);
        holder.etDescription.setText(currentUploads.getDescrip());
        holder.etNotes.setText(currentUploads.getNotes());
        String imageUrl = currentUploads.getImageurl();
        String modified = imageUrl.replace("Image Url: ", "");
        if (modified != null && !modified.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(modified)
                    .fitCenter()
                    .into(holder.ivImage);
            Log.d("mic check", modified);
        } else {
            //Toast.makeText(context, "Image URL is empty", Toast.LENGTH_SHORT).show();
        }
        if(selectedItem == position){
            holder.itemView.setBackgroundColor(Color.WHITE);
        }else {
            holder.itemView.setBackgroundColor(Color.TRANSPARENT);
        }

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextInputEditText etNotes, etDescription, etName;
        private Spinner spinType;
        private ImageView ivImage;
        private Button btnAdd;
        private ConstraintLayout mainBackground;
        public viewHolder(@NonNull View itemView, addingInterface addingInterface) {
            super(itemView);
            mainBackground = itemView.findViewById(R.id.mainBackground);
            etNotes = itemView.findViewById(R.id.etNotes);
            etDescription = itemView.findViewById(R.id.etDescription);
            spinType = itemView.findViewById(R.id.spinType);
            etName = itemView.findViewById(R.id.etName);
            ivImage = itemView.findViewById(R.id.ivImage);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            ivImage.setOnClickListener(view -> {
                addingInterface.setItemClick(getAdapterPosition(), "Image");
            });
            itemView.setOnClickListener(view -> {
                addingInterface.setItemClick(getAdapterPosition(), "Background");
                selectedItem = getAdapterPosition();
                notifyDataSetChanged();
            });
            btnAdd.setOnClickListener(view -> {
                addingUploads currentUploads = uploads.get(getAdapterPosition());
                String imageUrl = currentUploads.getImageurl();
                String name = etName.getText().toString();
                String type = spinType.getSelectedItem().toString();
                String descrip = etDescription.getText().toString();
                String notes = etNotes.getText().toString();
                String modified = imageUrl.replace("Image Url: ", "");
                if(name.isEmpty() ||descrip.isEmpty()|| notes.isEmpty() || type.isEmpty() ||modified.isEmpty()){
                    Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show();
                } else{
                    addProduct(name, type,descrip,notes,modified);
                    uploads.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    if (uploads == null || uploads.isEmpty()) {
                        Glide.with(context).clear(ivImage);
                        addingUploads addingUploads = new addingUploads("","","","","","","","","");
                        uploads.add(addingUploads);
                    } else {

                    }
                }
            });
        }
    }
    public void setImageForItem(int position, String imageUrl){
        uploads.get(position).setImageurl(imageUrl);
        notifyItemChanged(position);
    }
    public void setAllforItemQr(int position, String name, String type, String descrip,String notes, String imageUrl){
        uploads.get(position).setImageurl(imageUrl);
        uploads.get(position).setName(name);
        uploads.get(position).setType(type);
        uploads.get(position).setDescrip(descrip);
        uploads.get(position).setNotes(notes);
        notifyItemChanged(position);
    }

    private void addProduct(String name, String type, String descrip, String notes, String imageUrl) {
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference(ref);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MultiFormatWriter writer = new MultiFormatWriter();
        key = productRef.push().getKey();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Qr Code");
        try {
            BitMatrix matrix = writer.encode(key, BarcodeFormat.QR_CODE, 800, 800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference qrRef = storageRef.child(System.currentTimeMillis() + "." + ".jpg");
            qrRef.putBytes(data).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                    double progress = (100.0 * snapshot.getBytesTransferred()/ snapshot.getTotalByteCount());
//                    progressBar.setProgress((int) progress);
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                    currentSteps++;
//                    progressBar.setProgress((currentSteps * 100) / totalSteps);
                    taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            qrCodeUrl = uri.toString();
                            addingUploads addingUploads = new addingUploads("Seeds",key, user.getEmail(), name, type, descrip, notes,imageUrl,qrCodeUrl);
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
        } catch (WriterException e) {

        }

    }
}
