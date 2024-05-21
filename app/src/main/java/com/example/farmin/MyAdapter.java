package com.example.farmin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
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

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.text_amount);
            noteTextView = itemView.findViewById(R.id.text_note);
            tvIncomeOrExpense = itemView.findViewById(R.id.tvIncomeOrExpense);
            tv_date = itemView.findViewById(R.id.tv_date);
        }
    }
}
