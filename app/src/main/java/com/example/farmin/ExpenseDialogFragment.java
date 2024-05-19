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

public class ExpenseDialogFragment extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_expense, null);

        builder.setView(dialogView);
        builder.setTitle("Add Expense");
        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                EditText etAmount = dialogView.findViewById(R.id.etAmount);
                EditText etNote_expensive = dialogView.findViewById(R.id.etNote);  // Assuming you have a note field

                String amount = etAmount.getText().toString();
                String note = etNote_expensive.getText().toString();

                // Save to Firebase
                saveExpenseToFirebase(amount, note);

                Toast.makeText(getContext(), "Expense saved: Amount - " + amount + ", Note - " + note, Toast.LENGTH_SHORT).show();
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

    private void saveExpenseToFirebase(String amount, String note) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference expensesRef = database.getReference("expenses");

        String expenseId = expensesRef.push().getKey();
        if (expenseId != null) {
            Expense expense = new Expense(amount, note);
            expensesRef.child(expenseId).setValue(expense);
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

    public static class Expense {
        public String amount;
        public String note;

        public Expense() {
            // Default constructor required for calls to DataSnapshot.getValue(Expense.class)
        }

        public Expense(String amount, String note) {
            this.amount = amount;
            this.note = note;
        }
    }
}
