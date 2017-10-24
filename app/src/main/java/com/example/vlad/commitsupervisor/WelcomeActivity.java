package com.example.vlad.commitsupervisor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlad.commitsupervisor.uiutils.Views;

import java.util.Date;

import java.util.Locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class WelcomeActivity extends AppCompatActivity {

//    private JSONArray json;
//    private String strJson;


    //private ProgressBar progressBar;
    private Button searchButton;
    private EditText searchField;
    private TextView fakeSearchField;
    private View dimmer;
    private TextView titleText;

    private boolean isSearchActivated; //2 states of a UI: default and searching

    final private SearchBroadcastReceiver searchBroadcastReceiver = new SearchBroadcastReceiver() {
        @Override
        public void onCompleted(Bundle bundle) {
            isSearchActivated = false;
            changeScreenState(true);
            Toast.makeText(WelcomeActivity.this, "Success, loaded " + bundle.get("eventsCount") + " events", Toast.LENGTH_SHORT).show();
            Intent intentActivity = new Intent(WelcomeActivity.this, LogActivity.class);
            startActivity(intentActivity);
        }

        @Override
        public void onError() {
            isSearchActivated = false;
            Toast.makeText(WelcomeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            changeScreenState();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchBroadcastReceiver.onRegister(this);

        if (savedInstanceState != null) {
            isSearchActivated = savedInstanceState.getBoolean("isSearchActivated");
        }
        setContentView(R.layout.activity_welcome);
        initViews();

        changeScreenState();
    }

    private CommitSupervisorApp getCommitSupervisorApp() {
        return (CommitSupervisorApp) getApplication();
    }

    private void showKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.showSoftInput(getCurrentFocus(), InputMethodManager.SHOW_IMPLICIT);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean isUserInputValid(CharSequence username) {
        return TextUtils.isEmpty(username);
    }

    private void initViews() { //TODO: onBackPressed
        searchButton = (Button) findViewById(R.id.search_button);
        searchField = (EditText) findViewById(R.id.searchField);
        fakeSearchField = (TextView) findViewById(R.id.fakeSearchField);
        dimmer = findViewById(R.id.dimmer);
        titleText = (TextView) findViewById(R.id.textTitle);
        fakeSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleText.setVisibility(View.INVISIBLE);
                fakeSearchField.setVisibility(View.INVISIBLE);
                dimmer.setVisibility(View.VISIBLE);
                searchField.setVisibility(View.VISIBLE);
                searchField.requestFocus();
                showKeyboard();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchActivated = true;
                final CharSequence username = searchField.getText();
                if (isUserInputValid(username)) {
                    Toast.makeText(WelcomeActivity.this, "Please, enter an username", Toast.LENGTH_SHORT).show();
                    return;
                }

                hideKeyboard();
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);  //if date will change during processing, can it cause the problems?
                Date date = new Date();
                getCommitSupervisorApp().getSearchService().fetchUserActivity(username.toString(), date); //TODO: redo username type and date filtration

                changeScreenState();

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isSearchActivated", isSearchActivated);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        changeScreenState();

    }

    private void changeScreenState(boolean clearText) {
        searchButton.setEnabled(!isSearchActivated);
        searchButton.setText(isSearchActivated ? R.string.searching_button : R.string.search_button);
        //searchField.setVisibility(isSearchActivated ? View.INVISIBLE : View.VISIBLE);
        //progressBar.setVisibility(isSearchActivated ? View.VISIBLE : View.GONE);
        dimmer.setVisibility(isSearchActivated ? View.VISIBLE : View.GONE);
        searchField.setVisibility(isSearchActivated ? View.VISIBLE : View.GONE);
        titleText.setVisibility(isSearchActivated ? View.GONE : View.VISIBLE);
        fakeSearchField.setVisibility(isSearchActivated ? View.GONE : View.VISIBLE);


        if (isSearchActivated)
        Views.setInvisible();
        Views.setVisible();

        

        if(clearText) {
            searchField.setText("");
        }
    }

    private void changeScreenState() {
        changeScreenState(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchBroadcastReceiver.onUnregister(this);
    }
}
