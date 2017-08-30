package com.sogeti.columbus.sogetinotifier;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SettingsPage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        TextView pageTitle = (TextView)findViewById(R.id.SettingsTitle);
        pageTitle.setText("Settings");
        pageTitle.setTextSize(28f);


        String fileContents = StorageManager.readExistingNotifications(getApplicationContext());
        ArrayList<String> channelList = NotificationParser.getChannelList(fileContents);

        Log.i("Info", "Jason" + channelList.get(0));



        //ListAdapter settingsAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,channelList);
        ListAdapter settingsAdapter = new ArrayAdapter<String>(this, R.layout.jason_layout_2, R.id.listItem, channelList);
        ListView settingsPageListView = (ListView) findViewById(R.id.settingsPageList);
        settingsPageListView.setAdapter(settingsAdapter);



        /*
        settingsPageListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        String f = String.valueOf(adapterView.getItemAtPosition(i));
                        Toast.makeText(SettingsPage.this, f, Toast.LENGTH_LONG).show();
                    }

                }


        );

        */





    }
}
