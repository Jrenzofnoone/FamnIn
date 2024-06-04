package com.example.farmin;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    private List<objectIncome> itemList;
    private Context context;

    public MyAdapter(List<objectIncome> itemList, Context context) {
        this.itemList = itemList;
        this.context = context;
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
        objectIncome objectIncome = itemList.get(position);
        holder.amountTextView.setText(String.valueOf(objectIncome.getAmountAsDouble()));
        holder.noteTextView.setText(objectIncome.getNote());
        holder.tvIncomeOrExpense.setText(objectIncome.getType());
        holder.tv_date.setText(objectIncome.getDate());
        holder.setTextColor(objectIncome.getType()); // Set the text color based on the type
        holder.ivTrash.setOnClickListener(view -> {
            DatabaseReference ref = FirebaseDatabase.getInstance().getReference(objectIncome.getType());
            Log.d("checking delete", objectIncome.getKey());
            Log.d("checking delete", objectIncome.getType());
            ref.child(objectIncome.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    Toast.makeText(context, "Successfully Deleted", Toast.LENGTH_SHORT).show();
                }
            });
            itemList.remove(position);
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public void updateData(List<objectIncome> newItemList) {
        this.itemList = newItemList;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amountTextView;
        public TextView noteTextView;
        public TextView tvIncomeOrExpense;
        public TextView tv_date;
        private ImageView ivTrash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.text_amount);
            noteTextView = itemView.findViewById(R.id.text_note);
            tvIncomeOrExpense = itemView.findViewById(R.id.tvIncomeOrExpense);
            tv_date = itemView.findViewById(R.id.tv_date);
            ivTrash = itemView.findViewById(R.id.ivTrash);
        }

        public void setTextColor(String type) {
            if ("INCOME".equals(type)) {
                tvIncomeOrExpense.setTextColor(Color.parseColor("#49B3E5")); // Set to #49B3E5 for income
            } else if ("EXPENSES".equals(type)) {
                tvIncomeOrExpense.setTextColor(Color.parseColor("#FE4444")); // Set to #FE4444 for expense
            }
        }
    }
}
