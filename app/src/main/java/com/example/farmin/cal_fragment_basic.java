package com.example.farmin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class cal_fragment_basic extends Fragment {

    private EditText etIbs;
    private EditText etIbsUnit;
    private EditText etAreas;
    private EditText etTotalResult;
    private Button calculateButton;
    private Button resetButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cal_basic, container, false);

        // Initialize views
        etIbs = view.findViewById(R.id.et_ibs);
        etIbsUnit = view.findViewById(R.id.et_ibs_unit);
        etAreas = view.findViewById(R.id.et_areas);
        etTotalResult = view.findViewById(R.id.et_total_result);
        calculateButton = view.findViewById(R.id.calculate_button);
        resetButton = view.findViewById(R.id.button3); // Assigning the reset button

        // Set onClickListener for the calculateButton
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calculateResult();
            }
        });

        // Set onClickListener for the resetButton
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetFields();
            }
        });

        return view;
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

    private void resetFields() {
        // Clear all EditText fields
        etIbs.getText().clear();
        etIbsUnit.getText().clear();
        etAreas.getText().clear();
        etTotalResult.getText().clear();
    }
}
