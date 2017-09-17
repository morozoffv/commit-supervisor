package com.example.vlad.commitsupervisor;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;





public class WelcomeActivity extends AppCompatActivity implements AsyncResponce {

    private JSONArray json;
    private String strJson;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Button searchButton = (Button) findViewById(R.id.search_button);
        final EditText searchText = (EditText) findViewById(R.id.searchText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = searchText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(WelcomeActivity.this, "Please, enter a username", Toast.LENGTH_SHORT).show();
                    return;
                }
                progressBar.setVisibility(View.VISIBLE);

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);          //keyboard
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   //hiding

                new JSONAsyncTask(WelcomeActivity.this).execute(username);



            }
        });
    }

    @Override
    public void processFinish(JSONArray j) {
        json = j;
        strJson = j.toString();
        Log.i("DEBUG", strJson);
        Intent intent = new Intent(this, LogActivity.class);
        intent.putExtra("LogActivity", strJson);
        startActivity(intent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void cancelAsyncTask() {     //if user is not found, hide progressbar and send toast
        progressBar.setVisibility(View.GONE);
        Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show();
    }
}
