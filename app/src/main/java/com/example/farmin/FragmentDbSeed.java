package com.example.farmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class FragmentDbSeed extends Fragment implements clickInterface{
    private RecyclerView recyclerView;
    private List<addingUploads> uploads;
    private AllAdapter adapter;
    private FirebaseUser user;
    private DatabaseReference seedRef;
    private ImageView ivAddSeed, ivChecking, ivExport;
    private ImageView ivEdit;
    private View checkView, exportView;
    private TextView tvExport;
    private Boolean isPick = false;
    private String name, descrip, type, notes, stringUrl, stringQr, key, csKey;
    private boolean isClicked = false;
    private EditText etSearch;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_db_seed, container, false);
        ivAddSeed = rootView.findViewById(R.id.ivAddSeed);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int screenHeight = displayMetrics.heightPixels;
        int itemHeight = (int) (screenHeight * 0.2);
        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        uploads = new ArrayList<>();
        adapter = new AllAdapter(getActivity(),2, uploads, this, itemHeight);
        user = FirebaseAuth.getInstance().getCurrentUser();
        seedRef = FirebaseDatabase.getInstance().getReference("Seeds");
        seedRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                uploads.clear();
                recyclerView.setAdapter(adapter);
                for( DataSnapshot postSnap : snapshot.getChildren()) {
                    addingUploads addingUploads = postSnap.getValue(com.example.farmin.addingUploads.class);
                    if(user.getEmail().equals(addingUploads.getUserEmail())) {
                        uploads.add(addingUploads);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        ivAddSeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddingSeed.class);
                startActivity(intent);
            }
        });
        ivExport = rootView.findViewById(R.id.ivExport);
        ivChecking = rootView.findViewById(R.id.ivChecking);
        ivEdit = rootView.findViewById(R.id.ivEdit);
        exportView = rootView.findViewById(R.id.exportView);
        checkView = rootView.findViewById(R.id.checkView);
        tvExport = rootView.findViewById(R.id.tvExport);
        ivEdit.setOnClickListener(view -> {
            if(isPick == false ) {
                checkView.setVisibility(View.VISIBLE);
                ivChecking.setVisibility(View.VISIBLE);
                ivExport.setVisibility(View.VISIBLE);
                exportView.setVisibility(View.VISIBLE);
                tvExport.setVisibility(View.VISIBLE);
                isPick = true;
            } else {
                checkView.setVisibility(View.INVISIBLE);
                ivChecking.setVisibility(View.INVISIBLE);
                ivExport.setVisibility(View.INVISIBLE);
                exportView.setVisibility(View.INVISIBLE);
                tvExport.setVisibility(View.INVISIBLE);
                isPick = false;
            }
        });
        etSearch = rootView.findViewById(R.id.etSearch);
        etSearch.addTextChangedListener(new MyEditTextWatcher());
        recyclerView = rootView.findViewById(R.id.recyclerView);
        List<Integer> checkedPosition = adapter.getCheckedPosition();
        ivExport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "Please", Toast.LENGTH_SHORT).show();
                String fileName = "BulkProducts";
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
                for(Integer positions : checkedPosition){
                    if(positions != null) {
                        Log.d("psition", String.valueOf(positions));
                        addingUploads uploadCurrent = uploads.get(positions);
                        name = uploadCurrent.getName();
                        descrip = uploadCurrent.getCount();
                        type = uploadCurrent.getType();
                        notes = uploadCurrent.getNotes();
                        stringUrl = uploadCurrent.getImageurl();
                        stringQr = uploadCurrent.getQrcode();
                        key = uploadCurrent.getKey();
                        csKey = uploadCurrent.getCsType();
                        StringBuilder csvDataBuilder = new StringBuilder();
                        csvDataBuilder.append("Name: "+name +",Type: "+ type +",Description: "+ descrip +",Notes: "+ notes+",key: "+ key+",Image Url: "+ stringUrl+",Qr code Url: "+ stringQr+",Cs Key: "+ csKey);
                        String csvData = csvDataBuilder.toString();
                        createCsv(fileName, csvData);
                        Toast.makeText(getActivity(), "File Created, Please check your downloads", Toast.LENGTH_SHORT).show();

                    }  else {
                        Toast.makeText(getActivity(), "Please Select Something", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ivChecking.setOnClickListener(view -> {
            if (isClicked){
                adapter.checkAll();
            } else {
                adapter.checkAll();
            }

        });
        return rootView;
    }
    private void createCsv(String fileName, String csvData) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), Environment.DIRECTORY_DOWNLOADS);
            if(!root.exists()){
                root.mkdirs();
            }
            File csvFile = new File(root, fileName +".csv");
//            int suffix = 1;
//            while (csvFile.exists()){
//                csvFile = new File(root, fileName + "(" + suffix+ ")"+".csv");
//                suffix++;
//            }
            if(!csvFile.exists()){
                FileWriter writer = new FileWriter(csvFile);
                writer.append(csvData);
                writer.append("\n");
                writer.flush();
                writer.close();
            } else {
                FileWriter writer = new FileWriter(csvFile, true);
                writer.append(csvData);
                writer.append("\n");
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public void setItemClick(int position) {

    }
    public class MyEditTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            filter(s.toString());
        }
        @Override
        public void afterTextChanged(Editable s) {

        }
    }
    private void filter(String newText) {
        List<addingUploads> filteredList = new ArrayList<>();
        for(addingUploads item: uploads) {
            if(item.getName().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(item);
            }
        }
        adapter.filterList(filteredList);
    }
}