package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
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

import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.elbaz.eliran.mynewsapp.R;
import com.elbaz.eliran.mynewsapp.Utils.NotificationWorker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.content.ContentValues.TAG;

public class SearchAndNotificationsActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TextView mStartDate, mEndDate, mStartDateText, mEndDateText;
    private EditText mSearchQuery;
    private Button mSearchButton;
    private SwitchCompat mSwitchForNotifications;
    private CheckBox artsCheckbox, businessCheckbox, entrepreneursCheckbox, politicsCheckbox, sportsCheckbox, travelCheckbox;
    private String mDate, BeginDateStringForURL, EndDateStringForURL, joinedFilterString, mQueryValue, finalFilterString;
    private int buttonSelectorFlag=0;
    private List<String> filtersQueryString = new ArrayList<>(6);
    SharedPreferences mSharedPreferences;
    private Boolean searchActivuty, notificationActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_notification);

        // Get boolean of which kind of activity will be shown to the user
        Intent intent = getIntent();
        searchActivuty = intent.getBooleanExtra("search_activity", false);
        notificationActivity = intent.getBooleanExtra("notification_activity", false);

        mSearchQuery = (EditText) findViewById(R.id.SearchField);
        mStartDate = (TextView) findViewById(R.id.search_startDate);
        mEndDate = (TextView) findViewById(R.id.search_endDate);
        mSearchButton = (Button) findViewById(R.id.searchButton);
        artsCheckbox = (CheckBox) findViewById(R.id.checkbox_arts);
        businessCheckbox = (CheckBox) findViewById(R.id.checkbox_business);
        entrepreneursCheckbox = (CheckBox) findViewById(R.id.checkbox_entrepreneurs);
        politicsCheckbox = (CheckBox) findViewById(R.id.checkbox_politics);
        sportsCheckbox = (CheckBox) findViewById(R.id.checkbox_sports);
        travelCheckbox = (CheckBox) findViewById(R.id.checkbox_travel);
        mStartDateText = (TextView) findViewById(R.id.StartDateText);
        mEndDateText = (TextView) findViewById(R.id.EndDateText);
        mSwitchForNotifications = (SwitchCompat) findViewById(R.id.notifications_switchButton);

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the upper button (back button)
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureLayoutVisibility (){
        // Check and set switch state
        mSharedPreferences = getSharedPreferences("save_switch_state", MODE_PRIVATE);
        mSwitchForNotifications.setChecked(mSharedPreferences.getBoolean("current_switch_state", false));

        // Set view elements visibility to "Gone" - depends on the selected operation of the activity (search OR notification)
        if (searchActivuty){
            mSwitchForNotifications.setVisibility(View.GONE);
        }else if (notificationActivity){
            mStartDate.setVisibility(View.GONE);
            mEndDate.setVisibility(View.GONE);
            mSearchButton.setVisibility(View.GONE);
            mStartDateText.setVisibility(View.GONE);
            mEndDateText.setVisibility(View.GONE);
            // Set switch listener
            mSwitchForNotifications.setOnCheckedChangeListener(this);
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
        mSharedPreferences = getSharedPreferences("checkbox_state", MODE_PRIVATE);
        // Check the type of activity (search OR notification) and load data properly
        if (searchActivuty){
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
                SharedPreferences.Editor editor = getSharedPreferences("checkbox_state", MODE_PRIVATE).edit();
                if(isChecked){
                    filtersQueryString.add(category);
                    if (notificationActivity)
                    editor.putBoolean(category, true);
                }else{
                    // If all the categories were unchecked while the switch was live, block the action and indicate the user
                    if (filtersQueryString.size() == 1 && mSwitchForNotifications.isChecked()){
                        buttonView.setChecked(true); // set back the checkbox
                        Vibration();
                        SnackBarMessages(getString(R.string.category_checkbox_limit));
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
                SharedPreferences.Editor editor2 = getSharedPreferences("save_switch_state", MODE_PRIVATE).edit();
                editor.putString("checkboxes_filter_string", finalFilterString);
                editor.commit();
            }
        });
    }

    /**
     * Search button action - to invoke the search API with all filtered data
     */
    public void searchButtonOnClickAction (View view){
        // Check if at least one category is selected
        if (joinedFilterString == null || joinedFilterString.isEmpty()){
            SnackBarMessages(getString(R.string.Your_filter_field_is_empty));
            Vibration();
        }else{
            Log.d(TAG, "onSearchClicked result: " + finalFilterString);
            // Set and transfer data into the invoked activity
            String sort = getString(R.string.sort_value);
            mQueryValue = mSearchQuery.getText().toString();
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra(getString(R.string.begin_date), BeginDateStringForURL);
            intent.putExtra(getString(R.string.end_date), EndDateStringForURL );
            intent.putExtra(getString(R.string.filter_query), finalFilterString );
            intent.putExtra(getString(R.string.search_query), mQueryValue );
            intent.putExtra(getString(R.string.sort), sort );
            startActivityForResult(intent,100);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check if failed to find results, and show a SnackBar pop-up
        if (resultCode == Activity.RESULT_CANCELED) {
            SnackBarMessages(getString(R.string.no_results));
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
            SnackBarMessages(getString(R.string.Your_filter_field_is_empty));
            Vibration();
            // set the switch back to off mode
            mSwitchForNotifications.setChecked(false);
        }else{
        SharedPreferences.Editor editor = getSharedPreferences("save_switch_state", MODE_PRIVATE).edit();
        if(isChecked){
            Log.i("switch_is_checked", isChecked + "");
            // Get today's date when switch was launched
            String switchStartDate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
            Log.d(TAG, "onCheckedChanged: stored date is "+ switchStartDate);
            // Save all information into sharedPreferences
            editor.putString("search_start_date", switchStartDate);
            editor.putString("checkboxes_filter_string", finalFilterString);
            editor.putString("query_string", mQueryValue);
            editor.putBoolean("current_switch_state", true);
            editor.commit();
            // Show a message
            SnackBarMessages(getString(R.string.notifications_on));
            // Test
            OneTimeWorkRequest request = new OneTimeWorkRequest.Builder(NotificationWorker.class).build();
            WorkManager.getInstance().enqueue(request);
            // end Test
            //////// Worker Setup ///////////
//            // Initiate the Worker class to work daily
//            Constraints constraints = new Constraints.Builder()
//                    .setRequiresBatteryNotLow(true)
//                    .setRequiredNetworkType(NetworkType.CONNECTED)
//                    .build();
//
//            PeriodicWorkRequest saveRequest =
//                    new PeriodicWorkRequest.Builder(NotificationWorker.class, 1, TimeUnit.DAYS)
//                            .addTag("periodic_notifications")
//                            .setConstraints(constraints)
//                            .build();
//
//            WorkManager.getInstance()
//                    .enqueue(saveRequest);
            ////////// End of Worker Setup /////////
        }else{
            Log.i("switch_is_checked", isChecked + "");
            // Save switch state
            editor.putBoolean("current_switch_state", false);
            editor.commit();
            // Show a message
            SnackBarMessages(getString(R.string.notifications_off));
            // Stops the activity of the worker (by Tag)
            WorkManager.getInstance().cancelAllWorkByTag("periodic_notifications");
        }  /// end of isChecked condition

        } // end of entire condition
    }

    // A method to show popup SnackBar messages
    protected void SnackBarMessages (String string){
        Snackbar.make(getCurrentFocus(), string,
                Snackbar.LENGTH_LONG)
                .show();
    }

    // Vibration method
    protected void Vibration (){
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(50);
    }


}
