package com.example.farmin;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;


public class fragmentQr extends Fragment {
    private ImageView ivQr;
    private viewHolderDisplay viewHolderDisplay;
    private String stringQr;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_qr, container, false);
        viewHolderDisplay = new ViewModelProvider(requireActivity()).get(viewHolderDisplay.class);
        stringQr = viewHolderDisplay.getQrcode();
        ivQr = rootView.findViewById(R.id.ivQr);
            Glide.with(getActivity())
                    .load(stringQr)
                    .fitCenter()
                    .into(ivQr);

        return rootView;
    }
}