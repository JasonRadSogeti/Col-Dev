package com.sogeti.columbus.sogetinotifier;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.TimeZone;


public class HistoryPage extends Activity {







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.history);

        TextView pageTitle = (TextView)findViewById(R.id.HistoryLabel);
        pageTitle.setText("Notification History");
        pageTitle.setTextSize(28f);

        String fileContents = StorageManager.readExistingNotifications(getApplicationContext());
        JSONArray allHistory = NotificationParser.getChannelHistory(fileContents, "All");
        ArrayList<String> allHistoryList = new ArrayList<String>();

        try{
            for(int index=0; index < allHistory.length(); index++){
                JSONObject currentNotification = (JSONObject) allHistory.get(index);

                long timestamp = Long.parseLong(currentNotification.get("datetime").toString());

                String currentChannelName = currentNotification.get("ChannelName").toString();
                String currentTimeParsed;
                String currentDateParsed;


                TimeZone tz = TimeZone.getTimeZone("America/New_York");
                Calendar timeThen = Calendar.getInstance(tz);
                timeThen.setTimeInMillis(timestamp);
                Date timeThenDate = timeThen.getTime();
                SimpleDateFormat messageTimeFormat = new SimpleDateFormat("h:mm a");
                SimpleDateFormat messageDateFormat = new SimpleDateFormat("MMMM d");


                String currentMessage = currentChannelName + " notification from: " + messageTimeFormat.format(timeThenDate) + " on " + messageDateFormat.format(timeThenDate);
                allHistoryList.add(index, currentMessage);
            }

        }
        catch(JSONException jse){

        }

        Collections.reverse(allHistoryList);





        //ArrayList<String> channelList = NotificationParser.getChannelList(fileContents);




        ListAdapter historyAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,allHistoryList);
        ListView historyPageListView = (ListView) findViewById(R.id.historyPage);
        historyPageListView.setAdapter(historyAdapter);

        historyPageListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String f = String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(HistoryPage.this, f, Toast.LENGTH_LONG).show();
                    }

                }


        );

    }
}

