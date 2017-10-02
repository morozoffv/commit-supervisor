package com.example.vlad.commitsupervisor;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;


public class WelcomeActivity extends AppCompatActivity implements AsyncResponse {

    private JSONArray json;
    private String strJson;
    ProgressBar progressBar;
    Button searchButton;
    EditText searchText;
    boolean progressBarState;
    //static private JSONArray events;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        searchButton = (Button) findViewById(R.id.search_button);
        searchText = (EditText) findViewById(R.id.searchText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        progressBar.setVisibility(View.GONE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = searchText.getText().toString();
                if (username.equals("")) {
                    Toast.makeText(WelcomeActivity.this, "Please, enter an username", Toast.LENGTH_SHORT).show();
                    return;
                }

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);          //keyboard
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   //hiding

                new JSONAsyncTask(WelcomeActivity.this).execute(username);

                searchButton.setEnabled(false);
                searchButton.setText(R.string.searching_button);
                searchText.setVisibility(View.INVISIBLE);
                progressBar.setVisibility(View.VISIBLE);




            }
        });

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

    }

    @Override
    protected void onResume() {
        super.onResume();

        retrieveElementState();

    }

    private void retrieveElementState() {
        searchButton = (Button) findViewById(R.id.search_button);
        searchText = (EditText) findViewById(R.id.searchText);
        searchButton.setEnabled(true);
        searchButton.setText(R.string.search_button);
        searchText.setVisibility(View.VISIBLE);
        searchText.setText("");
        progressBar.setVisibility(View.INVISIBLE);
    }



    @Override
    public void processFinish(@NonNull SearchResult searchResult) {

        if(!searchResult.isSuccessful()) {
            Toast.makeText(this, "Error: " + searchResult.getResponseCode(), Toast.LENGTH_SHORT).show();
            retrieveElementState();
            return;
        }

        Toast.makeText(this, "Success: " + searchResult.getResponseCode(), Toast.LENGTH_SHORT).show();
        LogActivity.setEvents(searchResult.getEvents()); //decided to use static variable, because data is too large for putExtra()
        Intent intent = new Intent(this, LogActivity.class);
        startActivity(intent);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
    }

//    @Override
//    public void cancelAsyncTask() {     //if user is not found, hide progressbar and send toast
//        progressBar.setVisibility(View.GONE);
//        Toast.makeText(this, "Not found!", Toast.LENGTH_SHORT).show();
//    }
}
