package com.example.farmin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

public class AllAdapter extends RecyclerView.Adapter<AllAdapter.viewHolder> {
    private List<addingUploads> uploads ;
    private Context context;
    private int depends, itemHeight;
    private clickInterface clickInterface;
    List<Integer> checkedPosition;

    public AllAdapter(Context context, int depends, List<addingUploads> uploads, clickInterface clickInterface, int itemHeight) {
        this.uploads = uploads;
        this.context = context;
        this.depends = depends;
        this.checkedPosition = new ArrayList<>();
        this.clickInterface = clickInterface;
        this.itemHeight = itemHeight;
    }

    @NonNull
    @Override
    public AllAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.all_item, parent, false);
        return new viewHolder(v , clickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull AllAdapter.viewHolder holder, int position) {
        Log.d("AllAdapter", "onBindViewHolder: position - " + position);
        Log.d("AllAdapter", "Uploads list size: " + uploads.size());
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        if(layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        } else {
            layoutParams.height = itemHeight;
        }
        addingUploads currentUploads = uploads.get(position);
        holder.tvName.setText(currentUploads.getName());
        holder.tvType.setText(currentUploads.getType());
        holder.tvDescrip.setText(currentUploads.getDescrip());
        holder.tvNote.setText(currentUploads.getNotes());
        String imageUrl = currentUploads.getImageurl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .fitCenter()
                    .into(holder.ivImage);
        } else {
            Toast.makeText(context, "Image URL is empty", Toast.LENGTH_SHORT).show();
        }
        holder.checkBox.setChecked(areCheckBoxVisible);
    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }
    public List<Integer> getCheckedPosition() {
        return checkedPosition;
    }
    private boolean areCheckBoxVisible = false;
    public void setCheckBoxVisible() {
        areCheckBoxVisible = !areCheckBoxVisible;
        notifyDataSetChanged();
    }
    public void checkAll() {
        areCheckBoxVisible = !areCheckBoxVisible;
        if(areCheckBoxVisible) {
            checkedPosition.clear();
            for(int i = 0; i<uploads.size(); i++) {
                checkedPosition.add(i);
            }
         Toast.makeText(context, "select everything", Toast.LENGTH_SHORT).show();
        } else {
            checkedPosition.clear();
        }
        notifyDataSetChanged();

    }
    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvNote, tvDescrip, tvType, tvName;
        private ImageView ivImage;
        private CheckBox checkBox;
        public viewHolder(@NonNull View itemView, clickInterface clickInterface) {
            super(itemView);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvDescrip = itemView.findViewById(R.id.tvDescrip);
            tvType = itemView.findViewById(R.id.tvType);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            checkBox = itemView.findViewById(R.id.checkBox);
            checkBox.setOnClickListener(view -> {
                int position =getAdapterPosition();
                if(position != RecyclerView.NO_POSITION) {
                    if(checkBox.isChecked()) {
                        checkedPosition.add(position);
                    } else {
                        checkedPosition.remove(Integer.valueOf(position));
                    }
                }
            });
            itemView.setOnClickListener(view -> {
                int pos = getAdapterPosition();
                if(depends == 1) {
                    addingUploads addingUploads = uploads.get(pos);
                    Intent intent = new Intent(context, displaying.class);
                    intent.putExtra("Name",addingUploads.getName());
                    intent.putExtra("Descrip",addingUploads.getDescrip());
                    intent.putExtra("Type",addingUploads.getType());
                    intent.putExtra("Notes",addingUploads.getNotes());
                    intent.putExtra("Image",addingUploads.getImageurl());
                    intent.putExtra("key", addingUploads.getKey());
                    intent.putExtra("csKey", addingUploads.getCsType());
                    intent.putExtra("depends", 2);
                    context.startActivity(intent);
                } else if (depends == 2) {
                    addingUploads addingUploads = uploads.get(pos);
                    Intent intent = new Intent(context, displaying.class);
                    intent.putExtra("Name",addingUploads.getName());
                    intent.putExtra("Descrip",addingUploads.getDescrip());
                    intent.putExtra("Type",addingUploads.getType());
                    intent.putExtra("Notes",addingUploads.getNotes());
                    intent.putExtra("Image",addingUploads.getImageurl());
                    intent.putExtra("qrCode", addingUploads.getQrcode());
                    intent.putExtra("Key", addingUploads.getKey());
                    intent.putExtra("csKey", addingUploads.getCsType());
                    context.startActivity(intent);
                }
            });
        }

    }
}