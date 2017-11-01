package com.example.vlad.commitsupervisor;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.CountDownTimer;
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

import java.util.Date;

import java.util.List;
import java.util.Locale;

import java.text.DateFormat;
import java.text.SimpleDateFormat;


public class WelcomeActivity extends AppCompatActivity {

//    private JSONArray json;
//    private String strJson;


    //private ProgressBar progressBar;
//    private Button searchButton;

    private static final String TAG = "WelcomeActivity";

    private EditText searchEdit;
    private ImageView backButtonImage;
    private View searchField;

    private TextView fakeSearchField;
    private View dimmer;
    private TextView titleText;

    private boolean isSearchActivated; //2 states of a UI: default and with active searchEdit

    private List<User> autocompleteUserList;

    private RecyclerView autocompleteRecyclerView;
    private AutoCompleteAdapter autocompleteAdapter;
    private RecyclerView.LayoutManager layoutManager;

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

    final private BroadcastReceiver autocompleteBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (!intent.getAction().equals(CommitSupervisorApp.ACTION_USERS_RECEIVED)) {
                return;
            }
            Bundle bundle = intent.getExtras();
            //noinspection unchecked
            autocompleteUserList = (List<User>) bundle.getSerializable("users");
            String currentInput = bundle.getString("input");

            if (!(searchEdit.getText().toString().equals(currentInput)) || autocompleteUserList == null) { //if current input is the same, that upload right now, then upload it until input wasn't change
                return;
            }
            autocompleteAdapter = new AutoCompleteAdapter(autocompleteUserList);
            autocompleteRecyclerView.setAdapter(autocompleteAdapter);
            Log.i(TAG, "onReceive: " + currentInput);
            autocompleteAdapter.setOnItemClickListener(new AutoCompleteAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View v, int position) {
                    Toast.makeText(WelcomeActivity.this, position + "", Toast.LENGTH_SHORT).show();
                }
            });
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchBroadcastReceiver.onRegister(this);

        IntentFilter autocompleteFilter = new IntentFilter();
        autocompleteFilter.addAction(CommitSupervisorApp.ACTION_USERS_RECEIVED);
        this.registerReceiver(autocompleteBroadcastReceiver, autocompleteFilter);

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
        searchField = findViewById(R.id.search_field);

        fakeSearchField = (TextView) findViewById(R.id.fake_search_field);
        dimmer = findViewById(R.id.dimmer);
        titleText = (TextView) findViewById(R.id.text_title);

        autocompleteRecyclerView = (RecyclerView) findViewById(R.id.autocomplete_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        autocompleteRecyclerView.setLayoutManager(layoutManager);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(autocompleteRecyclerView.getContext(), LinearLayout.VERTICAL); //need getOrientation()
        autocompleteRecyclerView.addItemDecoration(dividerItemDecoration);

        Views.setInvisible(dimmer, searchEdit, searchField, backButtonImage, autocompleteRecyclerView);


        fakeSearchField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //TODO: remove listeners to the other method
                isSearchActivated = true;
                showKeyboard(searchEdit);

                Views.setInvisible(fakeSearchField, titleText);
                Views.setVisible(dimmer, searchField, backButtonImage, searchEdit, autocompleteRecyclerView);
            }
        });

        searchEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() { //after pressing on search button on keyboard
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    return preFetchUserActivity();
                }
                return false;
            }
        });


        searchEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final CharSequence username = searchEdit.getText();

                if (!searchEdit.getText().toString().trim().equals("")) {
                    getCommitSupervisorApp().getSearchService().loadAutoCompletionsForUsername(username.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        backButtonImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSearchActivated = false;
                changeScreenState();
                hideKeyboard();
            }
        });




    }

    private boolean preFetchUserActivity() {
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
            Views.setVisible(dimmer, searchField, backButtonImage, searchEdit, autocompleteRecyclerView);
            hideKeyboard();
        }
        else {
            Views.setInvisible(dimmer, searchField, backButtonImage, searchEdit, autocompleteRecyclerView);
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
            Views.setInvisible(dimmer, searchField, backButtonImage, searchEdit, autocompleteRecyclerView);
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
        this.unregisterReceiver(autocompleteBroadcastReceiver);
    }
}
