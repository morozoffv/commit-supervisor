package com.example.vlad.commitsupervisor;

import android.app.ListActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class LogActivity extends ListActivity {

    String string;

    private static JSONArray events = null;
    String[] stringArray;

    public static JSONArray getEvents() {
        return events;
    }

    public static void setEvents(JSONArray events) {
        LogActivity.events = events;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        stringArray = new String[events.length()];

        for (int i = 0; i < events.length(); i++) {
            try {
                stringArray[i] = events.getJSONObject(i).toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringArray);
        setListAdapter(arrayAdapter);
        //ListView listView = (ListView) findViewById(R.id.listView);

    }


}
