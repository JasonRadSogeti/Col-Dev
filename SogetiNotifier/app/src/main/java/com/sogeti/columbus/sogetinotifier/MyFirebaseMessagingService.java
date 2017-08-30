package com.sogeti.columbus.sogetinotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Calendar;
import java.util.Map;
import java.util.TimeZone;

import static android.content.ContentValues.TAG;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    public MyFirebaseMessagingService() {
    }

/*
    @Override
    public void handleIntent(Intent intent){
        Log.i("Info","Jason - INTENT received!" + intent.getDataString());
    }*/

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...
        Log.i("Info","Jason - Message received!");



        Map<String, String> notificationData = remoteMessage.getData();


        if(notificationData.get("ChannelName") != null  && notificationData.get("ChannelName").equals("Sales Webpage")){
            Log.i("Info","Jason - website notification received!");

            final MediaPlayer mp = MediaPlayer.create(this, R.raw.bloop11);
            mp.start();

            TimeZone tz = TimeZone.getTimeZone("America/New_York");
            Calendar calendarNow = Calendar.getInstance(tz);

            String channelName = notificationData.get("ChannelName");
            Context context = getApplicationContext();
            long timeNow = calendarNow.getTimeInMillis();
            String notificationTitle = notificationData.get("title");
            String notificationText = notificationData.get("text");


            boolean notificationWriteSuccess = NotificationParser.recordNotification(
                    context,
                    channelName,
                    timeNow,
                    notificationTitle,
                    notificationText);

            Log.i("Info","Jason"+ notificationWriteSuccess);



            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 323, intent, 0);
            long[] pattern = new long[6];
            for (int x = 1; x < 6; x++){
                pattern[x] = 400;
            }

            Notification notification = new NotificationCompat.Builder(getApplicationContext())
                    .setContentTitle(notificationTitle)
                    .setContentText(notificationText)
                    .setSmallIcon(R.drawable.spade_small_icon)
                    .setContentIntent(pendingIntent)
                    .setVibrate(pattern)
                    .build();


            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(6, notification);

            //String token = FirebaseService.getToken();
            //Log.i("Info","Jason"+ token);


        }
    }



}
