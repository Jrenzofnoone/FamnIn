package com.example.farmin;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
        holder.amountTextView.setText(objectIncome.getAmount());
        holder.noteTextView.setText(objectIncome.getNote());
        holder.text_date.setText(objectIncome.getDate());
        holder.text_type.setText(objectIncome.getType());
        holder.ivTrash.setOnClickListener(view -> {
            if(objectIncome.getType().equals("income")){
                objectIncome objectIncome2 = itemList.get(position);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("income");
                ref.child(objectIncome2.getKey()).removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        itemList.remove(position);
                        notifyDataSetChanged();
                    }
                });
                Log.d("mic check mic check",objectIncome2.getKey());
            } else if (objectIncome.getType().equals("expenses")) {
                objectIncome objectIncome2 = itemList.get(position);
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference("expenses");
                itemList.remove(position);
                notifyDataSetChanged();
                ref.child(objectIncome2.getKey()).removeValue();
            }
        });

    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView amountTextView;
        public TextView noteTextView;
        public TextView text_date;
        public TextView text_type;
        public ImageView ivTrash;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            amountTextView = itemView.findViewById(R.id.text_amount);
            noteTextView = itemView.findViewById(R.id.text_note);
            text_date = itemView.findViewById(R.id.text_date);
            ivTrash = itemView.findViewById(R.id.ivTrash);
            text_type = itemView.findViewById(R.id.text_type);

        }
    }
}
