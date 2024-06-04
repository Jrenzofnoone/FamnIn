package com.example.farmin;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class GridAdapter extends BaseAdapter {
    Context context;
    List<activityUploads> uploads;

    private final activityInterface activityInterface;
    LayoutInflater inflater;
    private int itemHeight;
    public GridAdapter(Context context, List<activityUploads> uploads, activityInterface activityInterface, int itemHeight) {
        this.context = context;
        this.uploads = uploads;
        this.activityInterface = activityInterface;
        this.itemHeight = itemHeight;
    }

    @Override
    public int getCount() {
        return uploads.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    public ImageView ivImage;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if(view == null) {
            view = inflater.inflate(R.layout.griditem, null);
        }
        ivImage = view.findViewById(R.id.ivImage);
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        if(layoutParams == null) {
            layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, itemHeight);
        } else {
            layoutParams.height = itemHeight;
        }
        Log.d("heigh", String.valueOf(itemHeight));
        view.setLayoutParams(layoutParams);
        activityUploads uploadCurrent = uploads.get(i);
        // holder.ivImage.setImageURI(Uri.parse(uploadCurrent.getImageUrl()));
        String imageUrl = uploadCurrent.getimage();
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Glide.with(view.getContext())
                    .load(imageUrl)
                    .fitCenter()
                    .into(ivImage);
            Log.d("checking", "lol");
        } else {
            Log.d("TAG", "imageUrl: " + uploadCurrent.getimage());
            ivImage.setImageResource(R.drawable.ic_launcher_foreground);
        }
        view.setOnClickListener(view1 -> {
            activityUploads currentUploads = uploads.get(i);
            activityInterface.setOnClick(currentUploads.getName(), currentUploads.getType(), currentUploads.descrip, currentUploads.getStartYear(),
                    currentUploads.getStartMonth(), currentUploads.getStartDay(), currentUploads.getFinishYear(), currentUploads.getFinishMonth(),
                    currentUploads.getFinishDay(), currentUploads.notes, currentUploads.getimage());
        });
        return view;
    }
//    public void filterList(List<sellingUploads> filteredList) {
//        uploads = filteredList;
//        notifyDataSetChanged();
//    }
}
