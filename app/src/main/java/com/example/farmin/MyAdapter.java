package com.example.farmin;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public class Income {
        public String amount;
        public String note;

        public Income() {
            // Default constructor required for calls to DataSnapshot.getValue(Income.class)
        }

        public Income(String amount, String note) {
            this.amount = amount;
            this.note = note;
        }
    }

    public class Expense {
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
    private List<Object> itemList; // Can hold both Income and Expense

    public MyAdapter(List<Object> itemList) {
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_income_expense, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object currentItem = itemList.get(position);

        if (currentItem instanceof Income) {
            Income income = (Income) currentItem;
            holder.amountTextView.setText(income.amount);
            holder.noteTextView.setText(income.note);
        } else if (currentItem instanceof Expense) {
            Expense expense = (Expense) currentItem;
            holder.amountTextView.setText(expense.amount);
            holder.noteTextView.setText(expense.note);
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amountTextView;
        public TextView noteTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.text_amount);
            noteTextView = itemView.findViewById(R.id.text_note);
        }
    }
}
