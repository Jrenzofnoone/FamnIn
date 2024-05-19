package com.example.farmin;

import android.content.Context;
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

public class fragmentAdapter extends RecyclerView.Adapter<fragmentAdapter.viewHolder> {
    private List<addingUploads> uploads ;
    private Context context;
    private int depends, itemHeight;
    private clickInterface clickInterface;
    List<Integer> checkedPosition;

    public fragmentAdapter(List<addingUploads> uploads, Context context, int depends, int itemHeight, com.example.farmin.clickInterface clickInterface) {
        this.uploads = uploads;
        this.context = context;
        this.depends = depends;
        this.itemHeight = itemHeight;
        this.clickInterface = clickInterface;
        this.checkedPosition = new ArrayList<>();
    }

    @NonNull
    @Override
    public fragmentAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.all_item, parent, false);
        return new viewHolder(v , clickInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull fragmentAdapter.viewHolder holder, int position) {
        addingUploads currentUploads = uploads.get(position);
        holder.tvName.setText(currentUploads.getName());
        holder.tvType.setText("Type: " + currentUploads.getType());
        holder.tvDescrip.setText("Description: " + currentUploads.getDescrip());
        holder.tvNote.setText("Notes: " + currentUploads.getNotes());
        String imageUrl = currentUploads.getImageurl();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(holder.itemView.getContext())
                    .load(imageUrl)
                    .fitCenter()
                    .into(holder.ivImage);
        } else {
            Toast.makeText(context, "Image URL is empty", Toast.LENGTH_SHORT).show();
        }
        if (areCheckBoxVisible) {
            holder.checkBox.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return uploads.size();
    }
    private boolean areCheckBoxVisible = false;
    public void setCheckBoxVisible() {
        areCheckBoxVisible = !areCheckBoxVisible;
        notifyDataSetChanged();

    }
    public List<Integer> getCheckedPosition() {
        return checkedPosition;
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        private TextView tvNote, tvDescrip, tvType, tvName;
        private ImageView ivImage;
        private CheckBox checkBox;
        public viewHolder(@NonNull View itemView, com.example.farmin.clickInterface clickInterface) {
            super(itemView);
            tvNote = itemView.findViewById(R.id.tvNote);
            tvDescrip = itemView.findViewById(R.id.tvDescrip);
            tvType = itemView.findViewById(R.id.tvType);
            tvName = itemView.findViewById(R.id.tvName);
            ivImage = itemView.findViewById(R.id.ivImage);
            checkBox = itemView.findViewById(R.id.checkBox);
        }
    }
}
