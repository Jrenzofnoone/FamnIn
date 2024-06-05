package com.example.farmin;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private  static final String channelId = "my_channel_id";
    private DatabaseReference databaseRef;
    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    List<activityUploads> finishList, startList;

    @Override
    public void onReceive(Context context, Intent intent) {
        finishList = new ArrayList<>();
        startList = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference("Activity");
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot postSnopshot : snapshot.getChildren()) {
                    activityUploads activityUploads = postSnopshot.getValue(activityUploads.class);
                    int mfinishYear = Integer.parseInt(activityUploads.finishYear) ;
                    int mfinishMonth = Integer.parseInt(activityUploads.finishMonth) + 1;
                    int mfinishDay = Integer.parseInt(activityUploads.finishDay);
                    int mstartYear = Integer.parseInt(activityUploads.startYear) ;
                    int mstartMonth = Integer.parseInt(activityUploads.startMonth) + 1;
                    int mstartDay = Integer.parseInt(activityUploads.startDay);
                    try {
                        Date finishDate = format.parse(mfinishYear+"-"+ mfinishMonth +"-"+ mfinishDay);
                        Date startDate = format.parse(mstartYear+"-"+ mstartMonth +"-"+ mstartDay);
                        Log.d("finishate", finishDate.toString());
                        Date currentDate = new Date();
                        String current = String.valueOf(currentDate.getYear() + currentDate.getMonth() + currentDate.getDay());
                        String finish = String.valueOf(finishDate.getYear() + finishDate.getMonth() + finishDate.getDay());
                        String start = String.valueOf(startDate.getYear() + startDate.getMonth() + startDate.getDay());

                        if(current.equals(finish)){
                            finishList.add(activityUploads);
                            Log.d("broom broom", finishDate.toString());
                        } else if (current.equals(start)){
                            startList.add(activityUploads);
                        }
                    } catch (ParseException e) {
                        throw new RuntimeException(e);
                    }
                }
                for(int i = 0; i<finishList.size(); i++) {
                    String title = "Activity";
                    String message = finishList.get(i).getName() + " activity finish";
                    sendNotification(context, title, message);
                    Log.d("mic check", "trust check");
                }
                for(int i = 0; i<startList.size(); i++) {
                    String title = "Activity";
                    String message = startList.get(i).getName() + " activity started";
                    sendNotification(context, title, message);
                    Log.d("mic check", "trust check");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    private void sendNotification(Context context, String title, String message) {
        Intent intent = new Intent(context, SignIn.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setContentIntent(pendingIntent);
        int notificationId = (int) System.currentTimeMillis();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(notificationId,notificationBuilder.build());
    }

}
