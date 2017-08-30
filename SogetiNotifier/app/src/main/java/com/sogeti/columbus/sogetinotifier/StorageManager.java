package com.sogeti.columbus.sogetinotifier;


import android.content.Context;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessaging;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class StorageManager {


    public static void setUpFilesIfNecessary(Context context){



        try{
            if(!fileExists(context, "HistoryLog.json")){
                FirebaseMessaging.getInstance().subscribeToTopic("SogetiNotifier");

                String emptyJSON = "{\"Notifications\":[" +
                        "{\"ChannelName\":\"Test Button\",\"ChannelHistory\":[{\"datetime\":\"1497813211228\",\"sequenceNumber\":\"0\",\"title\":\"Notification\",\"text\":\"YourVeryFirstNotification!\"}]}," +
                        "{\"ChannelName\":\"Sales Webpage\",\"ChannelHistory\":[]}," +
                        "{\"ChannelName\":\"All\",\"ChannelHistory\":[{\"ChannelName\":\"Test Button\",\"datetime\":\"1497813211228\",\"sequenceNumber\":\"0\",\"title\":\"Notification\",\"text\":\"YourVeryFirstNotification!\"}]}]}";
                FileOutputStream fos = context.openFileOutput("HistoryLog.json", context.MODE_PRIVATE);
                fos.write(emptyJSON.getBytes());
                fos.close();
                Log.i("Info", "wrote the new file jason");
            }
        }
        catch (IOException ioe){
            Log.i("Info", "Got here, IO Exception!");
        }
    }


    public static boolean writeNotifications(Context context, String fileContents){

        String filename = "HistoryLog.json";

        FileOutputStream fos;

        try {
            fos = context.openFileOutput(filename, Context.MODE_PRIVATE);
            fos.write(fileContents.getBytes());
            fos.close();
        }
        catch (FileNotFoundException fne) {
            return false;
        }
        catch (IOException ioe){
            return false;
        }
        return true;
    }









    public static String readExistingNotifications(Context context){

        String fileContents = "";
        try {
            InputStream inputStream = context.openFileInput("HistoryLog.json");
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            if (inputStream != null) {
                String receivedLine = "";
                StringBuilder fileContentsBuilder = new StringBuilder();

                while ((receivedLine = bufferedReader.readLine()) != null) {
                    fileContentsBuilder.append(receivedLine);
                }
                inputStream.close();
                fileContents = fileContentsBuilder.toString();
            }

        }
        catch (FileNotFoundException fnfe){
            Log.i("Info", "Got here, filenotfound Exception!");
        }
        catch (IOException ioe) {
            Log.i("Info", "Got here, IO Exception!");
        }
        return fileContents;
    }



    public static boolean fileExists(Context context, String fname){
        File file = context.getFileStreamPath(fname);
        return file.exists();
    }


}


