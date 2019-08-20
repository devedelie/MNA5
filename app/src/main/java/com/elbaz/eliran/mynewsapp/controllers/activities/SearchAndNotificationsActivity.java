package com.elbaz.eliran.mynewsapp.controllers.activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.utils.NotificationWorker;
import com.elbaz.eliran.mynewsapp.utils.SnackbarMessagesAndVibrations;
import com.google.common.util.concurrent.ListenableFuture;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import static android.content.ContentValues.TAG;

public class SearchAndNotificationsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TextView mStartDate, mEndDate, mStartDateText, mEndDateText;
    private EditText mSearchQueryEditText, mNotificationsQueryEditText;
    private Button mSearchButton;
    private SwitchCompat mNotificationsSwitch;
    private CheckBox artsCheckbox, businessCheckbox, entrepreneursCheckbox, politicsCheckbox, sportsCheckbox, travelCheckbox;
    private String mDate, BeginDateStringForURL, EndDateStringForURL, joinedFilterString, mQueryValue, finalFilterString;
    private int buttonSelectorFlag=0;
    private List<String> filtersQueryString = new ArrayList<>(6);
    SharedPreferences mSharedPreferences;
    private Boolean searchActivity, notificationActivity, isStartDateBigger=false;
    public static String[] intentBooleanID;
    View rootView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_notification);
        // Get RootView for snackBarMessage
        rootView = getWindow().getDecorView().getRootView();

        // Get boolean of which kind of activity will be shown to the user
        Resources resources = getResources();
        intentBooleanID = resources.getStringArray(R.array.intentSearchOrNotificationsActivity);
        Intent intent = getIntent();
        searchActivity = intent.getBooleanExtra(intentBooleanID[0], false);
        notificationActivity = intent.getBooleanExtra(intentBooleanID[1], false);

        mSearchQueryEditText = findViewById(R.id.SearchField);
        mNotificationsQueryEditText = findViewById(R.id.NotificationsSearchField);
        mStartDate = findViewById(R.id.search_startDate);
        mEndDate = findViewById(R.id.search_endDate);
        mSearchButton = findViewById(R.id.searchButton);
        artsCheckbox = findViewById(R.id.checkbox_arts);
        businessCheckbox = findViewById(R.id.checkbox_business);
        entrepreneursCheckbox = findViewById(R.id.checkbox_entrepreneurs);
        politicsCheckbox = findViewById(R.id.checkbox_politics);
        sportsCheckbox = findViewById(R.id.checkbox_sports);
        travelCheckbox = findViewById(R.id.checkbox_travel);
        mStartDateText = findViewById(R.id.StartDateText);
        mEndDateText = findViewById(R.id.EndDateText);
        mNotificationsSwitch = findViewById(R.id.notifications_switchButton);

        this.configureToolbar();
        this.searchDateListener();
        this.configureLayoutVisibility();
        this.checkBoxesConfiguration();
    }

    /**
     * 1 - Toolbar execution
     */
    private void configureToolbar(){
        //Get the toolbar (Serialise)
        Toolbar toolbar = findViewById(R.id.toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the upper button (back button)
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureLayoutVisibility (){
        // Check and set switch state
        mSharedPreferences = getSharedPreferences(getString(R.string.switch_SP_file), MODE_PRIVATE);
        mNotificationsSwitch.setChecked(mSharedPreferences.getBoolean(getString(R.string.current_switch_state), false));

        // Set view elements visibility to "Gone" - depends on the selected operation of the activity (search OR notification)
        if (searchActivity){
            mNotificationsSwitch.setVisibility(View.GONE);
            mNotificationsQueryEditText.setVisibility(View.GONE);
            // Set ActionBar title
            getSupportActionBar().setTitle(getString(R.string.search_actionBar_title));
        }else if (notificationActivity){
            mSearchQueryEditText.setVisibility(View.GONE);
            mStartDate.setVisibility(View.GONE);
            mEndDate.setVisibility(View.GONE);
            mSearchButton.setVisibility(View.GONE);
            mStartDateText.setVisibility(View.GONE);
            mEndDateText.setVisibility(View.GONE);
            // Set switch listener
            mNotificationsSwitch.setOnCheckedChangeListener(this);
            // Set ActionBar title
            getSupportActionBar().setTitle(getString(R.string.notifications_actionBar_title));
        }
        // Check if the switch is ON, and set the EditText field enabled/disabled
        if(mNotificationsSwitch.isChecked()){
            // Set new hint text
            mNotificationsQueryEditText.setText(mSharedPreferences.getString(getString(R.string.query_string), ""));
            mNotificationsQueryEditText.setEnabled(false);
        }
    }

    /**
     * Configure & Launch the DatePicker dialog  (for search filter)
     */
    public void searchDatePicker (View view){
        // Crete an instance of Calendar and setup date variables
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePicker dialog object with the design and the variables
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SearchAndNotificationsActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mOnDateSetListener,
                year,month,day);
        // set the background to transparent
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
        datePickerDialog.show();
        if(view.equals(mStartDate)){
            buttonSelectorFlag=1;
        }else if(view.equals(mEndDate)){
            buttonSelectorFlag=2;
        }
    }

    /**
     * OnDateSet Listener (actions to be taken after selecting the date on the dialog and clicking "OK")
     */
    public void searchDateListener(){
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Increase month variable +1, as count starts from 0 (ex: April = 3)
                month +=1;
                mDate = dayOfMonth + "/" + month + "/" + year;

                // Set the selected date in a string and add zero(0) to numbers under 10
                String setDate = year + "" + (month < 10 ? ("0" + (month)) : (month)) + "" + (dayOfMonth < 10 ? ("0" + dayOfMonth) : (dayOfMonth));
                // condition for checking if startDate or endDate button is selected
                if (buttonSelectorFlag == 1){
                    // Set the text on the layout
                    mStartDate.setText(mDate);
                    // create a start_date string for search api
                    BeginDateStringForURL = setDate;
                    Log.d(TAG, "onDateSet: URL BeginDate filter is: " + BeginDateStringForURL);
                }else if(buttonSelectorFlag == 2){
                    // Set the text on the layout
                    mEndDate.setText(mDate);
                    // create an end_date string for search api
                    EndDateStringForURL = setDate;
                    Log.d(TAG, "onDateSet: URL BeginDate filter is: " + EndDateStringForURL);
                }
                // set flag back to zero
                buttonSelectorFlag = 0;
            }
        };
        // end of dialog listener
    }

    // Detect the click on toolbar's "back" button and finish the current activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if ( id == android.R.id.home ) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * CheckBoxes configuration (Search OR Notification)
     */
    private void checkBoxesConfiguration (){
        filtersQueryString.clear();
        mSharedPreferences = getSharedPreferences(getString(R.string.checkbox_state), MODE_PRIVATE);
        // Check the type of activity (search OR notification) and load data properly
        if (searchActivity){
            setCheckBoxes(artsCheckbox, getString(R.string.category_arts_filter) );
            setCheckBoxes(businessCheckbox, getString(R.string.category_business_filter) );
            setCheckBoxes(entrepreneursCheckbox, getString(R.string.category_entrepreneurs_filter) );
            setCheckBoxes(politicsCheckbox, getString(R.string.category_politics_filter) );
            setCheckBoxes(sportsCheckbox, getString(R.string.category_sports_filter) );
            setCheckBoxes(travelCheckbox, getString(R.string.category_travel_filter) );
        }else if (notificationActivity){
            setCheckBoxes(artsCheckbox, getString(R.string.category_arts_filter) );
            artsCheckbox.setChecked(mSharedPreferences.getBoolean(getString(R.string.category_arts_filter), false));
            setCheckBoxes(businessCheckbox, getString(R.string.category_business_filter) );
            businessCheckbox.setChecked(mSharedPreferences.getBoolean(getString(R.string.category_business_filter), false));
            setCheckBoxes(entrepreneursCheckbox, getString(R.string.category_entrepreneurs_filter) );
            entrepreneursCheckbox.setChecked(mSharedPreferences.getBoolean(getString(R.string.category_entrepreneurs_filter), false));
            setCheckBoxes(politicsCheckbox, getString(R.string.category_politics_filter) );
            politicsCheckbox.setChecked(mSharedPreferences.getBoolean(getString(R.string.category_politics_filter), false));
            setCheckBoxes(sportsCheckbox, getString(R.string.category_sports_filter) );
            sportsCheckbox.setChecked(mSharedPreferences.getBoolean(getString(R.string.category_sports_filter), false));
            setCheckBoxes(travelCheckbox, getString(R.string.category_travel_filter) );
            travelCheckbox.setChecked(mSharedPreferences.getBoolean(getString(R.string.category_travel_filter), false));
        }
    }

    /**
     * Set checkBoxes state in sharedPreferences + build the filters String
     */
    private void setCheckBoxes(final CheckBox checkBox, final String category){
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.checkbox_state), MODE_PRIVATE).edit();
                if(isChecked){
                    filtersQueryString.add(category);
                    if (notificationActivity)
                    editor.putBoolean(category, true);
                }else{
                    // If all the categories were unchecked (only on Notifications) while the switch was live, block the action and indicate the user
                    if (notificationActivity && filtersQueryString.size() == 1 && mNotificationsSwitch.isChecked()){
                        buttonView.setChecked(true); // set back the checkbox
                        SnackbarMessagesAndVibrations.Vibration(getApplicationContext());
                        SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.GlobalSearchLayout),getString(R.string.category_checkbox_limit));
                    }else{
                        filtersQueryString.remove(category);
                    }
                    if(notificationActivity)
                    editor.putBoolean(category, false);
                }
                editor.commit();
                Log.d(TAG, "onCheckedChanged: "+ filtersQueryString);

                // Join all filters
                joinedFilterString = (String) TextUtils.join( " " , filtersQueryString );
                Log.d(TAG, "onCheckboxClicked result: " + joinedFilterString);
                finalFilterString = "news_desk:(" + joinedFilterString + ")";
                Log.d(TAG, "onCheckedChanged final string: " + finalFilterString);

                // update finalFilterString in sharedPreferences, in order to enable changes without stopping the worker
                SharedPreferences.Editor editor2 = getSharedPreferences(getString(R.string.switch_SP_file), MODE_PRIVATE).edit();
                editor.putString("checkboxes_filter_string", finalFilterString);
                editor2.commit();
            }
        });
    }

    /**
     * Search button action - to invoke the search API with all filtered data
     */
    public void searchButtonOnClickAction (View view){
        // Check if at least one category is selected
        if (joinedFilterString == null || joinedFilterString.isEmpty()){
            SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.GlobalSearchLayout),getString(R.string.Your_filter_field_is_empty));
            SnackbarMessagesAndVibrations.Vibration(getApplicationContext());
        }else if(checkDateValidity()){
            SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.GlobalSearchLayout),getString(R.string.startDate_is_bigger_then_endDate));
            SnackbarMessagesAndVibrations.Vibration(getApplicationContext());
            isStartDateBigger = false;
        }else{
            Log.d(TAG, "onSearchClicked result: " + finalFilterString);
            // Set and transfer data into the invoked activity
            String sort = getString(R.string.sort_value);
            mQueryValue = mSearchQueryEditText.getText().toString();
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra(getString(R.string.begin_date), BeginDateStringForURL);
            intent.putExtra(getString(R.string.end_date), EndDateStringForURL );
            intent.putExtra(getString(R.string.filter_query), finalFilterString );
            intent.putExtra(getString(R.string.search_query), mQueryValue );
            intent.putExtra(getString(R.string.sort), sort );
            startActivityForResult(intent,100);
        }
    }

    // Verify if search start-date is bigger then end-date
    private boolean checkDateValidity(){
        // Check if the user filled both fields
        if(BeginDateStringForURL != null && EndDateStringForURL != null){
            // check if start date is bigger then end date
            isStartDateBigger = (Integer.parseInt(BeginDateStringForURL) > Integer.parseInt(EndDateStringForURL));
        }
        return isStartDateBigger;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check if failed to find results, and show a SnackBar pop-up
        if (resultCode == Activity.RESULT_CANCELED) {
            SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.GlobalSearchLayout),getString(R.string.no_results));
        }
    }

    /**
     * A method to detect the state changes of Android SwitchCompat button
     * to Start/Stop the Worker class
     */
    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        // Check if at least one category is selected
        if (joinedFilterString == null || joinedFilterString.isEmpty()){
            SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.GlobalSearchLayout),getString(R.string.Your_filter_field_is_empty));
            SnackbarMessagesAndVibrations.Vibration(getApplicationContext());
            // set the switch back to off mode
            mNotificationsSwitch.setChecked(false);
        }else{
        SharedPreferences.Editor editor = getSharedPreferences(getString(R.string.switch_SP_file), MODE_PRIVATE).edit();
        ///// Start of isChecked condition
        if(isChecked){
            Log.i("switch_is_checked", isChecked + "");
            // Disable the EditText field
            mNotificationsQueryEditText.setEnabled(false);
            // Get today's date when switch was launched
            String switchStartDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
            Log.d(TAG, "onCheckedChanged: stored date is "+ switchStartDate);
            // Save all information into sharedPreferences
            mQueryValue = mNotificationsQueryEditText.getText().toString();
            editor.putString(getString(R.string.search_start_date), switchStartDate);
            editor.putString(getString(R.string.checkbox_filter_string), finalFilterString);
            editor.putString(getString(R.string.query_string), mQueryValue);
            editor.putBoolean(getString(R.string.current_switch_state), true);
            editor.commit();
            // Show a message
            SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.GlobalSearchLayout),getString(R.string.notifications_on));
            // Initiate the Worker class
            workerSetup();
        }else{
            Log.i("switch_is_checked", isChecked + "");
            // Save switch state
            editor.putBoolean(getString(R.string.current_switch_state), false);
            editor.commit();
            // Enable EditText field
            mNotificationsQueryEditText.setEnabled(true);
            // Show a message
            SnackbarMessagesAndVibrations.showSnakbarMessage(rootView.findViewById(R.id.GlobalSearchLayout),getString(R.string.notifications_off));
            // Stops the activity of the worker (by Tag)
            WorkManager.getInstance(this).cancelAllWorkByTag(getString(R.string.WM_periodic_notifications_tag));
           }  //// end of isChecked condition
        } //// end of entire condition
    }

    // Daily Worker Setup
    public void workerSetup(){
        // Initiate the Worker class to work daily
        Constraints constraints = new Constraints.Builder()
                .setRequiresBatteryNotLow(true)
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build();

        PeriodicWorkRequest saveRequest =
                new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
                        .addTag(getString(R.string.WM_periodic_notifications_tag))
                        .setInitialDelay(1, TimeUnit.DAYS)
                        .setConstraints(constraints)
                        .build();

        WorkManager.getInstance(this).enqueue(saveRequest);
    }

    /////////////////////////////////////////
    // Testing ONLY - A method to know if WorkManager has a running scheduled work (by TAG)
    public void check(View view){
        Boolean x = isWorkScheduled(getString(R.string.WM_periodic_notifications_tag));
        Log.d(TAG, "Scheduled work, with tag: "+getString(R.string.WM_periodic_notifications_tag) + "is ON? "+ x);
    }
    public boolean isWorkScheduled( String tag) {
        WorkManager instance = WorkManager.getInstance();
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING | state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }
    ///////////////////////////////////////////////////////
    // End of Test
}
