package com.example.vlad.commitsupervisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


public class WelcomeActivity extends AppCompatActivity {

//    private JSONArray json;
//    private String strJson;

    private ProgressBar progressBar;
    private Button searchButton;
    private EditText searchText;

    private CommitSupervisorApp app;
    private BroadcastReceiver broadcastReceiver;


    private boolean isSearching; //2 states of a UI: default and searching

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        app = getCommitSupervisorApp();
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //SearchResult searchResult = app.getResult();
                isSearching = false;
                changeScreenState(intent.getAction().equals(CommitSupervisorApp.ACTION_SEARCH_COMPLETED));

                if(intent.getAction().equals(CommitSupervisorApp.ACTION_SEARCH_ERROR)) {
                    Toast.makeText(WelcomeActivity.this, "Error: " /*+ searchResult.getResponseCode()*/, Toast.LENGTH_SHORT).show();
                    return;
                }


                Toast.makeText(WelcomeActivity.this, "Success: " + intent.getExtras(), Toast.LENGTH_SHORT).show();
                Intent intentActivity = new Intent(WelcomeActivity.this, LogActivity.class);
                startActivity(intentActivity);
            }
        };


        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CommitSupervisorApp.ACTION_SEARCH_COMPLETED);
        intentFilter.addAction(CommitSupervisorApp.ACTION_SEARCH_ERROR);
        registerReceiver(broadcastReceiver, intentFilter);

        if (savedInstanceState != null) {
            isSearching = savedInstanceState.getBoolean("isSearching");
        }
        setContentView(R.layout.activity_welcome);
        initViews();

        changeScreenState();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearching = true;
                final CharSequence username = searchText.getText();
                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(WelcomeActivity.this, "Please, enter an username", Toast.LENGTH_SHORT).show();
                    return;
                }

                InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);          //keyboard
                inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);   //hiding

                app.search(username);

                changeScreenState();

            }
        });

    }

    private CommitSupervisorApp getCommitSupervisorApp() {
        return (CommitSupervisorApp) getApplication();
    }

    private void initViews() {
        searchButton = (Button) findViewById(R.id.search_button);
        searchText = (EditText) findViewById(R.id.searchText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isSearching", isSearching);
        super.onSaveInstanceState(savedInstanceState);
        
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeScreenState();

    }

    private void changeScreenState(boolean clearText) {
        searchButton.setEnabled(!isSearching);
        searchButton.setText(isSearching ? R.string.searching_button : R.string.search_button);
        searchText.setVisibility(isSearching ? View.INVISIBLE : View.VISIBLE);
        progressBar.setVisibility(isSearching ? View.VISIBLE : View.GONE);

        if(clearText) {
            searchText.setText("");
        }
    }

    private void changeScreenState() {
        changeScreenState(false);
    }

//    @Override
//    public void processFinish(@NonNull SearchResult searchResult) {
//
//        isSearching = false;
//        changeScreenState(searchResult.isSuccessful());
//
//        if(!searchResult.isSuccessful()) {
//            Toast.makeText(this, "Error: " + searchResult.getResponseCode(), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//
//        Toast.makeText(this, "Success: " + searchResult.getResponseCode(), Toast.LENGTH_SHORT).show();
//        LogActivity.setEvents(searchResult.getEvents()); //decided to use static variable, because data is too large for putExtra()
//        Intent intent = new Intent(this, LogActivity.class);
//        startActivity(intent);
//
//
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
}
