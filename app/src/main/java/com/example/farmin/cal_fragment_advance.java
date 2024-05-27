package com.example.farmin;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

public class cal_fragment_advance extends Fragment {

    private EditText etLbs;
    private EditText etLbsUnit;
    private EditText etAcres;
    private EditText etPercent;
    private EditText etTotalResult;
    private Button calculateButton;
    private Button resetButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_cal_advance, container, false);

        // Initialize views
        etLbs = view.findViewById(R.id.etlbs);
        etLbsUnit = view.findViewById(R.id.etlbsunit);
        etAcres = view.findViewById(R.id.etacres);
        etPercent = view.findViewById(R.id.etpercent);
        etTotalResult = view.findViewById(R.id.ettotalresult);
        calculateButton = view.findViewById(R.id.calculate_button);
        resetButton = view.findViewById(R.id.button3); // Assigning the reset button

        // Set onClickListener for the calculateButton
        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etLbs.getText().toString().equals("")){
                    etLbs.setError("Must not be Empty");
                } else if (etLbsUnit.getText().toString().equals("")) {
                    etLbsUnit.setError("Must not be Empty");
                } else if (etAcres.getText().toString().equals("")) {
                    etAcres.setError("Must not be Empty");
                } else if (etPercent.getText().toString().equals("")) {
                    etPercent.setError("Must not be Empty");
                } else {
                    calculateResult();
                }

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
        double totalHarvestWeight = Double.parseDouble(etLbs.getText().toString());
        double weightPerUnit = Double.parseDouble(etLbsUnit.getText().toString());
        double totalArea = Double.parseDouble(etAcres.getText().toString());
        double efficiencyFactor = Double.parseDouble(etPercent.getText().toString());

        // Calculate the adjusted harvest yield
        double harvestYield = (totalHarvestWeight / weightPerUnit) / totalArea;
        double adjustedHarvestYield = harvestYield * (efficiencyFactor / 100);

        // Display the result in the etTotalResult EditText
        etTotalResult.setText(String.valueOf(adjustedHarvestYield));
    }

    private void resetFields() {
        // Clear all EditText fields
        etLbs.getText().clear();
        etLbsUnit.getText().clear();
        etAcres.getText().clear();
        etPercent.getText().clear();
        etTotalResult.getText().clear();
    }
}