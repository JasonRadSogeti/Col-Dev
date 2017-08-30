package com.sogeti.columbus.sogetinotifier;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class NotificationParser  {


    public static JSONObject getLastNotification(String fileContents){

        JSONObject lastNotification = new JSONObject();


        try{
            JSONObject fileContentsJSON = new JSONObject(fileContents);

            JSONArray notificationsArray = (JSONArray)fileContentsJSON.get("Notifications");

            int numberOfChannels = notificationsArray.length();
            for (int index=0; index < numberOfChannels; index++ ){
                JSONObject currentChannel = (JSONObject) notificationsArray.get(index);
                String channelName = currentChannel.getString("ChannelName");
                if (channelName.equals("All")) {

                    JSONArray channelHistory = (JSONArray) currentChannel.get("ChannelHistory");
                    int numberOfNotifications = channelHistory.length();
                    lastNotification = (JSONObject) channelHistory.get(numberOfNotifications-1);
                }
            }
        }
        catch(JSONException jse){
            Log.i("Info", "Got here, JSON Exception!");
        }
        return lastNotification;
    }

    public static boolean recordNotification(Context context, String nChannelName, long nTime, String nTitle, String nText){
        try{
            JSONObject fileContentsJSON = new JSONObject(StorageManager.readExistingNotifications(context));
            JSONArray notificationsArray = (JSONArray)fileContentsJSON.get("Notifications");
            int numberOfChannels = notificationsArray.length();
            for (int index=0; index < numberOfChannels; index++ ){
                JSONObject currentChannel = (JSONObject) notificationsArray.get(index);
                String currentChannelName = currentChannel.getString("ChannelName");
                if (currentChannelName.equals(nChannelName)) {
                    //Add it to the channel
                    JSONArray channelHistory = (JSONArray) currentChannel.get("ChannelHistory");
                    int sequenceNumber = channelHistory.length();
                    JSONObject newNotificationForChannel = new JSONObject();
                    newNotificationForChannel.put("datetime", String.valueOf(nTime));
                    newNotificationForChannel.put("sequenceNumber", String.valueOf(sequenceNumber));
                    newNotificationForChannel.put("title", nTitle);
                    newNotificationForChannel.put("text", nText);
                    channelHistory.put(sequenceNumber, newNotificationForChannel);
                }
                else if(currentChannelName.equals("All")){
                    //also add it to the all channel
                    JSONArray channelHistory = (JSONArray) currentChannel.get("ChannelHistory");
                    int sequenceNumber = channelHistory.length();
                    JSONObject newNotificationForAll = new JSONObject();
                    newNotificationForAll.put("ChannelName", nChannelName);
                    newNotificationForAll.put("datetime", String.valueOf(nTime));
                    newNotificationForAll.put("sequenceNumber", String.valueOf(sequenceNumber));
                    newNotificationForAll.put("title", nTitle);
                    newNotificationForAll.put("text", nText);
                    channelHistory.put(channelHistory.length(), newNotificationForAll);

                }

            }

            StorageManager.writeNotifications(context, fileContentsJSON.toString());


        }
        catch(JSONException jse){

        }
        return true;
    }


    //Returns a JSONArray of the channel History for the given channel name
    public static JSONArray getChannelHistory(String fileContents, String channelName) {

        JSONArray channelHistory = new JSONArray();

        try {
            JSONObject fileContentsJSON = new JSONObject(fileContents);
            JSONArray notificationsArray = (JSONArray) fileContentsJSON.get("Notifications");
            int numberOfChannels = notificationsArray.length();
            for (int index = 0; index < numberOfChannels; index++) {
                JSONObject currentChannel = (JSONObject) notificationsArray.get(index);
                String currentChannelName = currentChannel.getString("ChannelName");
                if (currentChannelName.equals(channelName)) {
                    channelHistory = (JSONArray) currentChannel.get("ChannelHistory");
                }
            }

        } catch (JSONException jse) {
            Log.i("Info", "Got here, JSON Exception!");
        }


        return channelHistory;
    }


    public static ArrayList<String> getChannelList(String fileContents){
        ArrayList<String> channelList = new ArrayList<String>();


        JSONArray notificationsArray = null;
        try {
            JSONObject fileContentsJSON = new JSONObject(fileContents);
            notificationsArray = (JSONArray) fileContentsJSON.get("Notifications");
            int arraySize = notificationsArray.length();
            for(int index=0; index< arraySize; index++){
                JSONObject currentChannel = (JSONObject) notificationsArray.get(index);
                String currentChannelName = currentChannel.getString("ChannelName");
                channelList.add(currentChannelName);
            }

        } catch (JSONException jse) {
            Log.i("Info", "Got here, JSON Exception!");
        }

        return channelList;
    }




}