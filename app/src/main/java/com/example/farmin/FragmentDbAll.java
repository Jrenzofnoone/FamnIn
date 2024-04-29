package com.example.farmin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


public class FragmentDbAll extends Fragment {
    private ImageView ivAddCrop, ivAddSeed, ivAddActivity;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_db_all, container, false);
        ivAddCrop = rootView.findViewById(R.id.ivAddCrop);
        ivAddSeed = rootView.findViewById(R.id.ivAddSeed);
        ivAddActivity = rootView.findViewById(R.id.ivAddActivity);
        ivAddCrop.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddingCrop.class);
            startActivity(intent);
        });
        ivAddSeed.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddingSeed.class);
            startActivity(intent);
        });
        ivAddActivity.setOnClickListener(view -> {
            Intent intent = new Intent(getContext(), AddingActivity.class);
            intent.putExtra("depends", 1);
            startActivity(intent);
        });
        return rootView;
    }
}