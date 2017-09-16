package com.example.vlad.commitsupervisor;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class LogActivity extends AppCompatActivity {

    String string;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(getIntent().getStringExtra("LogActivity"));
    }
}
