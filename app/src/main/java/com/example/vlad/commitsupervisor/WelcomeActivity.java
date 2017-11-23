package com.example.vlad.commitsupervisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vlad.commitsupervisor.uiutils.Views;

import java.io.InputStream;
import java.net.URL;
import java.util.Collections;
import java.util.Date;

import java.util.List;
import java.util.Locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;


public class WelcomeActivity extends AppCompatActivity {

//    private JSONArray json;
//    private String strJson;


    //private ProgressBar progressBar;
//    private Button searchButton;

    private static final String TAG = "WelcomeActivity";

    private boolean isSearchActivated; //2 states of a UI: default and with active searchEdit

    private EditText searchEdit;
    private ImageView backButtonImage;
    private ImageView searchCloseButtonImage;
    //private View searchField;
    //private View searchDivider;
    private ConstraintLayout searchLayout;

    private TextView fakeSearchField;
    private View dimmer;
    private TextView titleText;



    private List<User> autocompleteUserList;



    private RecyclerView autocompleteRecyclerView;
    private AutoCompleteAdapter autocompleteAdapter;

    private RecyclerView searchHistoryRecyclerView;
    private SearchHistoryAdapter searchHistoryAdapter;

    final private BroadcastReceiver autocompleteBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(CommitSupervisorApp.ACTION_USERS_RECEIVED)) { //TODO: if i receive broadcast with this action, it doesn't mean that it already equals to that action?
                return;
            }
            Bundle bundle = intent.getExtras();

            //noinspection unchecked
            autocompleteUserList = (List<User>) bundle.getSerializable("users");

            String currentInput = bundle.getString("input");

            if (!(searchEdit.getText().toString().trim().equals(currentInput)) || autocompleteUserList == null) { //if current input is the same, that upload right now, then upload it until input wasn't change
                return;
            }

            autocompleteAdapter = new AutoCompleteAdapter(autocompleteUserList);
            autocompleteRecyclerView.setAdapter(autocompleteAdapter);
            Log.i(TAG, "onReceive: " + currentInput);
            autocompleteAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {

                    toEmptySearchHistoryAdapter();

                    isSearchActivated = false;
                    hideKeyboard();
                    changeScreenState(true);

                    Intent intentActivity = new Intent(WelcomeActivity.this, LogActivity.class);

                    intentActivity.putExtra("user", autocompleteUserList.get(position));
                    startActivity(intentActivity);

                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IntentFilter autocompleteFilter = new IntentFilter();
        autocompleteFilter.addAction(CommitSupervisorApp.ACTION_USERS_RECEIVED);
        this.registerReceiver(autocompleteBroadcastReceiver, autocompleteFilter);

        IntentFilter blankFieldFilter = new IntentFilter();
        blankFieldFilter.addAction(CommitSupervisorApp.ACTION_BLANK_SEARCH);

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



    private void showKeyboard(View v) {
        v.requestFocus();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager) this.getSystemService(INPUT_METHOD_SERVICE);
        inputManager.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }

    private boolean isUserInputEmpty(CharSequence username) {
        return TextUtils.isEmpty(username);
    }

    private void initViews() {

        searchEdit = (EditText) findViewById(R.id.search_edit);
        backButtonImage = (ImageView) findViewById(R.id.back_button_image);
        searchCloseButtonImage = (ImageView) findViewById(R.id.search_close_button_image);
        //searchField = findViewById(R.id.search_field);
        //searchDivider = findViewById(R.id.divider);
        searchLayout = (ConstraintLayout) findViewById(R.id.search_layout);


        fakeSearchField = (TextView) findViewById(R.id.fake_search_field);
        dimmer = findViewById(R.id.dimmer);
        titleText = (TextView) findViewById(R.id.text_title);

        autocompleteRecyclerView = (RecyclerView) findViewById(R.id.autocomplete_recycler_view); //TODO: refactor
        RecyclerView.LayoutManager layoutManagerAutocomplete = new LinearLayoutManager(this);
        autocompleteRecyclerView.setLayoutManager(layoutManagerAutocomplete);
        //DividerItemDecoration dividerAutocomplete = new DividerItemDecoration(autocompleteRecyclerView.getContext(), LinearLayout.VERTICAL);
        //dividerAutocomplete.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_divider, null));
        //autocompleteRecyclerView.addItemDecoration(dividerAutocomplete);

        searchHistoryRecyclerView = (RecyclerView) findViewById(R.id.search_history);
        RecyclerView.LayoutManager layoutManagerSearchHistory = new LinearLayoutManager(this);
        searchHistoryRecyclerView.setLayoutManager(layoutManagerSearchHistory);
//        DividerItemDecoration dividerSearchHistory = new DividerItemDecoration(autocompleteRecyclerView.getContext(), LinearLayout.VERTICAL);
//        dividerSearchHistory.setDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.custom_divider, null));
//        searchHistoryRecyclerView.addItemDecoration(dividerSearchHistory);

        searchHistoryAdapter = new SearchHistoryAdapter(getCommitSupervisorApp().getStorageService().getStoredUsers());
        searchHistoryRecyclerView.setAdapter(searchHistoryAdapter);

        Views.setInvisible(dimmer, searchLayout/*searchEdit, searchField, backButtonImage*/, autocompleteRecyclerView, searchHistoryRecyclerView);

        fakeSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO: remove listeners to the other method
                isSearchActivated = true;

                showKeyboard(searchEdit);
                changeScreenState();
            }
        });

        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            Timer timer = new Timer();
            @Override
            public void afterTextChanged(Editable s)
            {
                if(searchEdit.getText().toString().trim().equals("")) {
                    autocompleteAdapter = new AutoCompleteAdapter(Collections.<User>emptyList());
                    autocompleteRecyclerView.setAdapter(autocompleteAdapter);
                    Views.setVisible(searchHistoryRecyclerView);
                    Views.setInvisible(searchCloseButtonImage);
                }
                else {
                    Views.setInvisible(searchHistoryRecyclerView);
                    Views.setVisible(searchCloseButtonImage, autocompleteRecyclerView);
                }

                timer.cancel();
                timer = new Timer();
                timer.schedule(new TimerTask() {
                    @Override
                    public void run() {
                        String username = searchEdit.getText().toString().trim();
                        if (!(!username.equals("") && username.length() >= 3)) { return; } //if username is empty and user writes less than 3 symbols
                        getCommitSupervisorApp().getSearchService().loadAutoCompletionsForUsername(username);
                    }
                },
                500);
            }
        });

        backButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchActivated = false;
                changeScreenState();
                hideKeyboard();
            }
        });

        searchCloseButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeScreenState(true);
                Views.setInvisible(searchCloseButtonImage, autocompleteRecyclerView);
                Views.setVisible(searchHistoryRecyclerView);
                hideKeyboard();

            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {                     //after pressing on search button on keyboard
                    String username = searchEdit.getText().toString().trim();
                    if (isUserInputEmpty(username)) {
                        Toast.makeText(WelcomeActivity.this, "Please, enter an username", Toast.LENGTH_SHORT).show();
                        return true;
                    }

                    toEmptySearchHistoryAdapter();

                    isSearchActivated = false;
                    hideKeyboard();
                    changeScreenState(true);
                    Intent intentActivity = new Intent(WelcomeActivity.this, LogActivity.class);

                    User user = new User();     //user that
                    user.setLogin(username);

                    intentActivity.putExtra("user", user);
                    startActivity(intentActivity);
                    return true;
                }
                return false;
            }
        });

        onItemClickListenerForSearchHistoryAdapter();

    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putBoolean("isSearchActivated", isSearchActivated);
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshSearchHistoryAdapter();
        changeScreenState();
    }

    private void refreshSearchHistoryAdapter() { //for invisibility of searchHistoryRecyclerView
        if (searchHistoryAdapter != null) {
            searchHistoryAdapter.setStoredUsers(getCommitSupervisorApp().getStorageService().getStoredUsers());
            onItemClickListenerForSearchHistoryAdapter();
            searchHistoryRecyclerView.setAdapter(searchHistoryAdapter);
        }
    }

    private void toEmptySearchHistoryAdapter() {
        searchHistoryAdapter.setStoredUsers(Collections.<User>emptyList());
        searchHistoryAdapter.notifyDataSetChanged();
        searchHistoryRecyclerView.setAdapter(searchHistoryAdapter);
    }

    private void onItemClickListenerForSearchHistoryAdapter() { //TODO: long func name
        if (searchHistoryAdapter != null) {
            searchHistoryAdapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {

                    toEmptySearchHistoryAdapter();

                    isSearchActivated = false;
                    hideKeyboard();
                    changeScreenState(true);

                    Intent intentActivity = new Intent(WelcomeActivity.this, MainActivity.class);
                    intentActivity.putExtra("user", getCommitSupervisorApp().getStorageService().getStoredUsers().get(position)); //TODO: redo
                    startActivity(intentActivity);
                }
            });
        }
    }

    private void changeScreenState(boolean clearText) {

        if (isSearchActivated) {

            Views.setVisible(dimmer, searchLayout, autocompleteRecyclerView);
            Views.setInvisible(titleText, fakeSearchField);

            if(!searchEdit.getText().toString().trim().equals("")) {
                Views.setInvisible(searchHistoryRecyclerView);
                Views.setVisible(searchCloseButtonImage); //is not necessary because searchCloseButtonImage is already in searchLayout
            }
            else {
                Views.setVisible(searchHistoryRecyclerView);
                Views.setInvisible(searchCloseButtonImage);
            }

            hideKeyboard();
        }
        else {
            Views.setInvisible(dimmer, searchLayout, autocompleteRecyclerView, searchHistoryRecyclerView);
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
            Views.setInvisible(dimmer, searchLayout, autocompleteRecyclerView, searchHistoryRecyclerView);
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
        this.unregisterReceiver(autocompleteBroadcastReceiver);
    }
}
