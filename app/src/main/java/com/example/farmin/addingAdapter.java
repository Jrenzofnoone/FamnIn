package com.example.farmin;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.List;

public class addingAdapter extends RecyclerView.Adapter<addingAdapter.viewHolder> {
    private List<addingUploads> uploads;
    private Context context;
    private addingInterface addingInterface;
    private String ref;
    private int selectedItem = -1;
    private int itemHeight;
    private ProgressDialog progressDialog;

    public addingAdapter(Context context, String ref, List<addingUploads> uploads, addingInterface addingInterface, int itemHeight) {
        if (uploads == null || uploads.isEmpty()) {
            addingUploads addingUploads = new addingUploads("","", "", "", "", "", "", "", "", "");
            uploads.add(addingUploads);
        }
        this.uploads = uploads;
        this.ref = ref;
        this.context = context;
        this.addingInterface = addingInterface;
        this.itemHeight = itemHeight;
    }

    @NonNull
    @Override
    public addingAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.adding_item, parent, false);
        return new viewHolder(v, addingInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull addingAdapter.viewHolder holder, int position) {
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if (layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        } else {
            layoutParams.height = itemHeight;
        }

        addingUploads currentUploads = uploads.get(position);
        String imageUrl = currentUploads.getImageurl();
        String name = currentUploads.getName();
        String count = currentUploads.getCount();
        String type = currentUploads.getType();
        String status = currentUploads.getType();
        String note = currentUploads.getNotes();
        String modified = imageUrl.replace("Image Url: ", "");
        String namemodified = name.replace("Name: ", "");
        String countmodified = count.replace("Count: ", "");
        String typemodified = type.replace("Type: ", "");
        String statusmodified = status.replace("Status: ", "");
        String notesmodified = note.replace("Notes: ", "");

        String[] statusitems = context.getResources().getStringArray(R.array.status);
        ArrayAdapter<String> adapterstats = new ArrayAdapter<>(context, R.layout.custom_spinner_text_two, statusitems);
        holder.spinStatus.setAdapter(adapterstats);
        String[] items;
        if(ref.equals("Seeds")) {
            items = context.getResources().getStringArray(R.array.typeSeed);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_spinner_text_two, items);
            holder.spinType.setAdapter(adapter);
        } else {
            items = context.getResources().getStringArray(R.array.typeCrop);
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.custom_spinner_text_two, items);
            holder.spinType.setAdapter(adapter);
        }

        holder.etName.setText(namemodified);
        int index = Arrays.asList(items).indexOf(typemodified);
        int statusindex = Arrays.asList(statusitems).indexOf(statusmodified);
        holder.spinType.setSelection(index);
        holder.spinStatus.setSelection(statusindex);
        holder.etCount.setText(countmodified);
        holder.etNotes.setText(notesmodified);
        if (modified != null && !modified.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(modified)
                    .fitCenter()
                    .into(holder.ivImage);
            Log.d("mic check", modified);
        } else {
        }

        if (selectedItem == position) {
            holder.second_main.setBackgroundResource(R.drawable.custom_item_two);
            Toast.makeText(context, "Item Selected", Toast.LENGTH_SHORT).show();
        } else {
            holder.second_main.setBackgroundResource(0);
        }

        holder.etName.addTextChangedListener(new GenericTextWatcher(currentUploads, "name"));
        holder.etCount.addTextChangedListener(new GenericTextWatcher(currentUploads, "descrip"));
        holder.etNotes.addTextChangedListener(new GenericTextWatcher(currentUploads, "notes"));
        holder.spinType.setOnItemSelectedListener(new GenericItemSelectedListener(currentUploads));
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextInputEditText etNotes, etCount, etName;
        private Spinner spinType, spinStatus;
        private ImageView ivImage;
        private Button btnAdd;
        private ConstraintLayout second_main;

        public viewHolder(@NonNull View itemView, addingInterface addingInterface) {
            super(itemView);
            second_main = itemView.findViewById(R.id.second_main);
            etNotes = itemView.findViewById(R.id.etNotes);
            etCount = itemView.findViewById(R.id.etCount);
            spinStatus = itemView.findViewById(R.id.spinStatus);
            spinType = itemView.findViewById(R.id.spinType);
            etName = itemView.findViewById(R.id.etName);
            ivImage = itemView.findViewById(R.id.ivImage);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage("Uploading, please wait...");


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
                String csKey = ref;
                String name = etName.getText().toString();
                String type = spinType.getSelectedItem().toString();
                String status = spinStatus.getSelectedItem().toString();
                String count = etCount.getText().toString();
                String notes = etNotes.getText().toString();
                String modified = imageUrl.replace("Image Url: ", "");
                if (name.isEmpty() || count.isEmpty() || notes.isEmpty() || type.isEmpty() || modified.isEmpty()) {
                    Toast.makeText(context, "empty", Toast.LENGTH_SHORT).show();
                } else {
                    addProduct(name, type,status, count, notes, modified, csKey);
                    uploads.remove(getAdapterPosition());
                    notifyDataSetChanged();
                    if (uploads == null || uploads.isEmpty()) {
                        Glide.with(context).clear(ivImage);
                        addingUploads addingUploads = new addingUploads("", "", "", "", "", "", "", "", "", "");
                        uploads.add(addingUploads);
                    }
                }
            });
        }
    }

    public void setImageForItem(int position, String imageUrl) {
        uploads.get(position).setImageurl(imageUrl);
        notifyItemChanged(position);
    }

    public void setAllforItemQr(int position, String name, String type, String count, String notes, String imageUrl) {
        uploads.get(position).setImageurl(imageUrl);
        uploads.get(position).setName(name);
        uploads.get(position).setType(type);
        uploads.get(position).setCount(count);
        uploads.get(position).setNotes(notes);
        notifyItemChanged(position);
    }

    private void addProduct(String name, String type, String status, String count, String notes, String imageUrl, String csKey) {
        progressDialog.show();
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference(ref);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        MultiFormatWriter writer = new MultiFormatWriter();
        String key = productRef.push().getKey();
        StorageReference storageRef = FirebaseStorage.getInstance().getReference("Qr Code");

        try {
            BitMatrix matrix = writer.encode(key, BarcodeFormat.QR_CODE, 800, 800);
            BarcodeEncoder encoder = new BarcodeEncoder();
            Bitmap bitmap = encoder.createBitmap(matrix);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] data = baos.toByteArray();

            StorageReference qrRef = storageRef.child(System.currentTimeMillis() + "." + ".jpg");
            qrRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri -> {
                    String qrCodeUrl = uri.toString();
                    addingUploads addingUploads = new addingUploads(csKey, key, user.getEmail(), name, type,status, count, notes, imageUrl, qrCodeUrl);
                    productRef.child(key).setValue(addingUploads).addOnSuccessListener(unused -> {
                        progressDialog.dismiss();
                        Log.d("please", "wirk");
                    });
                });
            });
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private addingUploads currentUploads;
        private String field;

        public GenericTextWatcher(addingUploads currentUploads, String field) {
            this.currentUploads = currentUploads;
            this.field = field;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            switch (field) {
                case "name":
                    currentUploads.setName(s.toString());
                    break;
                case "descrip":
                    currentUploads.setCount(s.toString());
                    break;
                case "notes":
                    currentUploads.setNotes(s.toString());
                    break;
            }
        }

        @Override
        public void afterTextChanged(Editable s) {}
    }
}
class GenericItemSelectedListener implements android.widget.AdapterView.OnItemSelectedListener {
    private addingUploads currentUploads;

    public GenericItemSelectedListener(addingUploads currentUploads) {
        this.currentUploads = currentUploads;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}