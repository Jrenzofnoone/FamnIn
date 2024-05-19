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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class IncomeDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_income, null);

        builder.setView(dialogView);
        builder.setTitle("Add Income");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText etAmount = dialogView.findViewById(R.id.etAmount);
                EditText etNoteIncome = dialogView.findViewById(R.id.etNote);  // Assuming you have a note field

                String amount = etAmount.getText().toString();
                String note = etNoteIncome.getText().toString();

                // Save to Firebase
                saveIncomeToFirebase(amount, note);

                Toast.makeText(getContext(), "Income saved: Amount - " + amount + ", Note - " + note, Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    private void saveIncomeToFirebase(String amount, String note) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference incomeRef = database.getReference("income");
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String incomeId = incomeRef.push().getKey();
        if (incomeId != null) {
            HashMap<String, String> list = new HashMap<>();
            list.put("amount",amount);
            list.put("note",note);
            list.put("user", user.getEmail());

            incomeRef.child(incomeId).setValue(list);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
}
