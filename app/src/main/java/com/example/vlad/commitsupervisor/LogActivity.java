package com.example.vlad.commitsupervisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

public class LogActivity extends AppCompatActivity {

    String string;
    JSONArray events = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        TextView textView = (TextView) findViewById(R.id.textView);
        try {
            events = new JSONArray(getIntent().getStringExtra("LogActivity"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        textView.setText(getIntent().getStringExtra("LogActivity"));

    }
}
