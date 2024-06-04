package com.example.farmin;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

public class cal_dash extends Fragment {
    private EditText etIbs;
    private EditText etIbsUnit;
    private EditText etAreas;
    private EditText etTotalResult;
    private Button calculateButton;
    private Button resetButton;

    public cal_dash() {
        // Required empty public constructor
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cal_basic, container, false);
        etIbs = view.findViewById(R.id.et_ibs);
        etIbsUnit = view.findViewById(R.id.et_ibs_unit);
        etAreas = view.findViewById(R.id.et_areas);
        etTotalResult = view.findViewById(R.id.et_total_result);
        calculateButton = view.findViewById(R.id.calculate_button);
        resetButton = view.findViewById(R.id.button3); // Assigning the reset button

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etIbs.getText().toString().equals("")){
                    etIbs.setError("Must not be Empty");
                } else if (etIbsUnit.getText().toString().equals("")){
                    etIbsUnit.setError("Must not be Empty");
                } else if (etAreas.getText().toString().equals("")){
                    etAreas.setError("Must not be Empty");
                } else {
                    calculateResult();
                }

            }
        });
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        return view;
    }

    private void resetFields() {
        // Clear all EditText fields
        etIbs.getText().clear();
        etIbsUnit.getText().clear();
        etAreas.getText().clear();
        etTotalResult.getText().clear();
    }

    private void calculateResult() {
        // Your calculation logic goes here...
        double harvestWeight = Double.parseDouble(etIbs.getText().toString());
        double weightPerUnit = Double.parseDouble(etIbsUnit.getText().toString());
        double totalArea = Double.parseDouble(etAreas.getText().toString());

        // Calculate result
        double harvestYield = (harvestWeight / weightPerUnit) / totalArea;

        // Display result
        etTotalResult.setText(String.valueOf(harvestYield));
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // You can initialize UI elements and set up calculation logic here
    }
}
