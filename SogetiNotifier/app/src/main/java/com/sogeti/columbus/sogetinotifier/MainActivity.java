package com.sogeti.columbus.sogetinotifier;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;



public class MainActivity extends AppCompatActivity {
    public Button pingbutton;
    public Button historybutton;
    public Button settingsbutton;


    public void sampleNotification(View view){


        TimeZone tz = TimeZone.getTimeZone("America/New_York");
        Calendar calendarNow = Calendar.getInstance(tz);

        String channelName = "Test Button";
        Context context = getApplicationContext();
        long timeNow = calendarNow.getTimeInMillis();
        String notificationTitle = "Test Button Notification";
        String notificationText = "The test button was pressed!";


        boolean notificationWriteSuccess = NotificationParser.recordNotification(
                context,
                channelName,
                timeNow,
                notificationTitle,
                notificationText);



        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 322, intent, 0);
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
        notificationManager.notify(5, notification);

        String token = FirebaseService.getToken();
        Log.i("Info","Jason"+ token);

        refreshLastNotification();
    }



    

    public void hist() {
        historybutton= (Button)findViewById(R.id.historybutton);
        historybutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                Intent toy = new Intent(MainActivity.this,HistoryPage.class);
                startActivity(toy);


            }

        });
    }


    public void ping() {
        pingbutton= (Button)findViewById(R.id.pingbutton);
        pingbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){

                Intent ad = new Intent(MainActivity.this,AddPage.class);
                startActivity(ad);


            }

        });
    }

    public void settings() {
        settingsbutton= (Button)findViewById(R.id.settingsbutton);
        settingsbutton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View V){

                Intent ad = new Intent(MainActivity.this,SettingsPage.class);
                startActivity(ad);


            }

        });
    }


    public void settings(View view) {

    }

    public void refreshLastNotification(){
        String fileContents = StorageManager.readExistingNotifications(getApplicationContext());
        String lastNotificationMessage = new String();
        try{
            JSONObject lastNotification = NotificationParser.getLastNotification(fileContents);


            TimeZone tz = TimeZone.getTimeZone("America/New_York");
            Calendar timeThen = Calendar.getInstance(tz);
            timeThen.setTimeInMillis(Long.valueOf(lastNotification.get("datetime").toString()));
            Date timeThenDate = timeThen.getTime();
            SimpleDateFormat messageTimeFormat = new SimpleDateFormat("h:mm a");
            SimpleDateFormat messageDateFormat = new SimpleDateFormat("MMMM d");

            lastNotificationMessage =  lastNotification.get("ChannelName").toString() + " notification from: "
                    + messageTimeFormat.format(timeThenDate) + " on " + messageDateFormat.format(timeThenDate);

            //Log.i("Info","Jason!!!!!" + lastNotificationMessage);
        }
        catch(JSONException jse){
            Log.i("Info","JSONException happened");
        }

        TextView lastText = (TextView)findViewById(R.id.textView2);
        lastText.setText(lastNotificationMessage);

        TextView lastLabel = (TextView)findViewById(R.id.lastLabel);
        lastLabel.setText("Last Notification:");



    }

    @Override
    public void onResume(){
        super.onResume();

        refreshLastNotification();

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        hist();
        ping();
        settings();

        //Adding the internal storage file if it does not exist
        StorageManager.setUpFilesIfNecessary(getApplicationContext());

        refreshLastNotification();
    }


}