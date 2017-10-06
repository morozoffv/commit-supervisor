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
import android.widget.ProgressBar;
import android.widget.Toast;


public class WelcomeActivity extends AppCompatActivity {

//    private JSONArray json;
//    private String strJson;

    private ProgressBar progressBar;
    private Button searchButton;
    private EditText searchText;

    private boolean isSearching; //2 states of a UI: default and searching

    final private SearchBroadcastReceiver searchBroadcastReceiver = new SearchBroadcastReceiver() {
        @Override
        public void onCompleted(Bundle bundle) {
            isSearching = false;
            changeScreenState(true);
            Toast.makeText(WelcomeActivity.this, "Success, loaded " + bundle.get("eventsCount") + " events", Toast.LENGTH_SHORT).show();
            Intent intentActivity = new Intent(WelcomeActivity.this, LogActivity.class);
            startActivity(intentActivity);
        }

        @Override
        public void onError() {
            isSearching = false;
            Toast.makeText(WelcomeActivity.this, "Error!", Toast.LENGTH_SHORT).show();
            changeScreenState();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchBroadcastReceiver.onRegister(this);

        if (savedInstanceState != null) {
            isSearching = savedInstanceState.getBoolean("isSearching");
        }
        setContentView(R.layout.activity_welcome);
        initViews();

        changeScreenState();
    }

    private CommitSupervisorApp getCommitSupervisorApp() {
        return (CommitSupervisorApp) getApplication();
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private boolean isUserInputValid(CharSequence username) {
        return TextUtils.isEmpty(username);
    }

    private void initViews() {
        searchButton = (Button) findViewById(R.id.search_button);
        searchText = (EditText) findViewById(R.id.searchText);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearching = true;
                final CharSequence username = searchText.getText();
                if (isUserInputValid(username)) {
                    Toast.makeText(WelcomeActivity.this, "Please, enter an username", Toast.LENGTH_SHORT).show();
                    return;
                }

                hideKeyboard();
                getCommitSupervisorApp().search(username);
                changeScreenState();

            }
        });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchBroadcastReceiver.onUnregister(this);
    }
}
