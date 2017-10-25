package com.example.vlad.commitsupervisor;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
//    private Button searchButton;

    private EditText searchEdit;
    private ImageView backButtonImage;
    private View searchField;

    private TextView fakeSearchField;
    private View dimmer;
    private TextView titleText;

    private boolean isSearchActivated; //2 states of a UI: default and with active searchEdit

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

    BroadcastReceiver AutocompleteReceive = new

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
        InputMethodManager imm = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = this.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private boolean isUserInputEmpty(CharSequence username) {
        return TextUtils.isEmpty(username);
    }

    private void initViews() {
//        searchButton = (Button) findViewById(R.id.search_button);

        searchEdit = (EditText) findViewById(R.id.search_edit);
        backButtonImage = (ImageView) findViewById(R.id.back_button_image);
        searchField = findViewById(R.id.search_field);


        fakeSearchField = (TextView) findViewById(R.id.fake_search_field);
        dimmer = findViewById(R.id.dimmer);
        titleText = (TextView) findViewById(R.id.text_title);



        Views.setInvisible(dimmer, searchEdit, searchField, backButtonImage);
        fakeSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchActivated = true;
//
//                titleText.setVisibility(View.INVISIBLE);
//                fakeSearchField.setVisibility(View.INVISIBLE);
//                dimmer.setVisibility(View.VISIBLE);
//                searchEdit.setVisibility(View.VISIBLE);

                Views.setInvisible(fakeSearchField, titleText);
                Views.setVisible(dimmer, searchField, backButtonImage, searchEdit);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        searchEdit.requestFocusFromTouch();
                    }
                }, 500);
                //showKeyboard();
            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //isSearchActivated = true;
                    final CharSequence username = searchEdit.getText();
                    if (isUserInputEmpty(username)) {
                        Toast.makeText(WelcomeActivity.this, "Please, enter an username", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    hideKeyboard();
                    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);
                    Date date = new Date();
                    getCommitSupervisorApp().getSearchService().fetchUserActivity(username.toString(), date); //TODO: redo username type and date filtration

                    changeScreenState();
                    return true;
                }
                return false;
            }
        });

        backButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchActivated = false;

                changeScreenState();
            }
        });

//        searchButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                isSearchActivated = true;
//                final CharSequence username = searchEdit.getText();
//                if (isUserInputEmpty(username)) {
//                    Toast.makeText(WelcomeActivity.this, "Please, enter an username", Toast.LENGTH_SHORT).show();
//                    return;
//                }
//
//                hideKeyboard();
//                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'", Locale.US);  //if date will change during processing, can it cause the problems?
//                Date date = new Date();
//                getCommitSupervisorApp().getSearchService().fetchUserActivity(username.toString(), date); //TODO: redo username type and date filtration
//
//                changeScreenState();
//
//
//
//            }
//        });
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

        if (isSearchActivated) {
            Views.setInvisible(titleText, fakeSearchField);
            Views.setVisible(dimmer, searchField, backButtonImage, searchEdit);
            //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE); //TODO: ?
        }
        else {
            Views.setInvisible(dimmer, searchField, backButtonImage, searchEdit);
            Views.setVisible(titleText, fakeSearchField);
            hideKeyboard();
        }

        if(clearText) {
            searchEdit.setText("");
        }
    }

    private void changeScreenState() {
        changeScreenState(false);
    }

    @Override
    public void onBackPressed() {
        if (isSearchActivated) {
            Views.setInvisible(dimmer, searchField, backButtonImage, searchEdit);
            Views.setVisible(titleText, fakeSearchField);
            isSearchActivated = false;
        }
        else {
            super.onBackPressed();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        searchBroadcastReceiver.onUnregister(this);
    }
}
