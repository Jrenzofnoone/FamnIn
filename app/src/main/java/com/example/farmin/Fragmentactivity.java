package com.example.farmin;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class Fragmentactivity extends Fragment implements activityInterface{

    private ImageView imageView;
    private TextView tvName,tvTime,tvBeforeTime, tvNote, tvStart, tvFinish, to;
    private GridView gridView;
    private GridAdapter adapter;
    private DatabaseReference databaseRef;
    private List<activityUploads> uploads;
    private CountDownTimer countDownFinish;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_fragmentactivity, container, false);
        imageView = rootView.findViewById(R.id.imageView);
        tvName = rootView.findViewById(R.id.tvName);
        to = rootView.findViewById(R.id.to);
        tvFinish = rootView.findViewById(R.id.tvFinish);
        tvStart = rootView.findViewById(R.id.tvStart);
        tvTime = rootView.findViewById(R.id.tvTime);
        tvBeforeTime = rootView.findViewById(R.id.tvBeforeTime);
        tvNote = rootView.findViewById(R.id.tvNote);
        gridView = rootView.findViewById(R.id.gvSelling);
        uploads = new ArrayList<>();
        adapter = new GridAdapter(getActivity(), uploads, this);
        databaseRef = FirebaseDatabase.getInstance().getReference("Activity");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                gridView.setAdapter(adapter);
                for(DataSnapshot postSnopshot : snapshot.getChildren()) {
                    activityUploads activityUploads = postSnopshot.getValue(activityUploads.class);
                    int mfinishYear = Integer.parseInt(activityUploads.finishYear) ;
                    int mfinishMonth = Integer.parseInt(activityUploads.finishMonth) + 1;
                    int mfinishDay = Integer.parseInt(activityUploads.finishDay);
                    try {
                        Date finishDate = format.parse(mfinishYear+"-"+ mfinishMonth +"-"+ mfinishDay);
                        Date currentDate = new Date();
                        if(currentDate.after(finishDate)){

                        } else {
                            uploads.add(activityUploads);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        return rootView;
    }
    private long timeDifference;
    @Override
    public void setOnClick(String name, String type, String descrip, String startYear, String startMonth, String startDay, String finishYear, String finishMonth, String finishDay, String yield, String notes, String image) {
        tvName.setText(name);
        Glide.with(this)
                .load(image)
                .fitCenter()
                .into(imageView);
        tvNote.setText(notes);
        try{
            int mstartYear = Integer.parseInt(startYear) ;
            int mstartMonth = Integer.parseInt(startMonth) + 1;
            int mfinishYear = Integer.parseInt(finishYear) ;
            int mfinishMonth = Integer.parseInt(finishMonth) + 1;

            Date startDate = format.parse(mstartYear+"-"+ mstartMonth +"-"+ startDay);
            Date finishDate = format.parse(mfinishYear+"-"+ mfinishMonth +"-"+ finishDay);
            Date currentDate = new Date();
//            tvStart.setText(startDate.toString());
//            tvFinish.setText(finishDate.toString());
            to.setVisibility(View.VISIBLE);
            Log.d("start", startDate.toString());
            Log.d("1", startYear);
            Log.d("2", startMonth);
            Log.d("3", startDay);
            Log.d("current", currentDate.toString());
            Log.d("finish", finishDate.toString());
            long timeDifference = 0;
            if(currentDate.before(startDate)) {
                tvBeforeTime.setText("Time before Start");
                timeDifference = startDate.getTime() - currentDate.getTime();
            } else if(currentDate.after(startDate) && currentDate.before(finishDate)){
                tvBeforeTime.setText("Time before Finish");
                timeDifference = finishDate.getTime() - currentDate.getTime();
            } else {
                Log.d("check", "check");
            }
            if(countDownFinish != null) {
                countDownFinish.cancel();
            }
            countDownFinish = new CountDownTimer(timeDifference, 1000) {
                @Override
                public void onTick(long l) {
                    long timeDif = finishDate.getTime() -(System.currentTimeMillis() - currentDate.getTime());
                    long seconds = l / 1000;
                    long minutes = seconds/ 60;
                    long hours = minutes / 60;
                    long days = hours / 24;
                    long weeks = days / 7;
                    long mdays = days % 7;
                    long mhours = hours %24;

                    long secs = seconds % 60;
                    String remainingTime ;
                    if(mhours >99) {
                        remainingTime = "99...:" + String.format("%02d:%02d", mdays, mhours);
                    } else {
                        remainingTime = String.format("%02d:%02d:%02d", weeks, mdays, mhours);
                    }
                    tvTime.setText(remainingTime);
                }

                @Override
                public void onFinish() {

                }
            }.start();
        } catch (Exception e) {
            Log.d("error", e.toString());
        }
    }
    }