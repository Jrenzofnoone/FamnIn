package com.example.farmin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class IncomeDialogFragment extends DialogFragment {
    private EditText etAmount;
    private EditText etNote;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_income, container, false);
        etAmount = view.findViewById(R.id.etAmount);
        etNote = view.findViewById(R.id.etNote);

        Button addButton = view.findViewById(R.id.addButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addIncome();
            }
        });

        return view;
    }

    private void addIncome() {
        String amount = etAmount.getText().toString();
        String note = etNote.getText().toString();
        Income income = new Income(amount, note);
        ((Fragment_dashboard_expensive_income) getParentFragment()).addIncomeToFirebase(income);
        dismiss();
    }
}
