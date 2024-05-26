package com.example.farmin;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fragmentsetting extends Fragment {
    private TextView tvShare;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragmentsetting, container, false);
        tvShare = rootView.findViewById(R.id.tvShare);
        tvShare.setOnClickListener(view -> {
            Intent intent = new Intent(getActivity(), ShareUs.class);
            startActivity(intent);
        });
        return rootView;
    }
}