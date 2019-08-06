package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elbaz.eliran.mynewsapp.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TextView mStartDate;
    private TextView mEndDate;
    private EditText mSearchQuery;
    private Button mSearchButton;
    private CheckBox artsCheckbox, businessCheckbox, entrepreneursCheckbox, politicsCheckbox, sportsCheckbox, travelCheckbox;
    private String mDate;
    private String BeginDateStringForURL;
    private String EndDateStringForURL;
    private int buttonSelectorFlag=0;
    private List<String> filtersQueryString = new ArrayList<>(6);
    private String finalFilterString;
    String mQueryValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        this.configureToolbar();

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


        // OnDateSet listener (actions to be taken after selecting the date on the dialog and clicking "OK")
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
                SearchActivity.this,
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
     * onClick method for the checkboxes - to create a string for results filtering
     */
    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();
        // set the variable empty

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.checkbox_arts:
                if (checked){
                    filtersQueryString.add(getString(R.string.category_arts_filter));
                }
            else filtersQueryString.remove(getString(R.string.category_arts_filter));
                break;
            case R.id.checkbox_business:
                if (checked){
                    filtersQueryString.add(getString(R.string.category_business_filter));
                }
            else filtersQueryString.remove(getString(R.string.category_business_filter));
                break;
            case R.id.checkbox_entrepreneurs:
                if (checked){
                    filtersQueryString.add(getString(R.string.category_entrepreneurs_filter));
                }
                else filtersQueryString.remove(getString(R.string.category_entrepreneurs_filter));
                    break;
            case R.id.checkbox_politics:
                if (checked){
                    filtersQueryString.add(getString(R.string.category_politics_filter));
                }
                else filtersQueryString.remove(getString(R.string.category_politics_filter));
                    break;
            case R.id.checkbox_sports:
                if (checked){
                    filtersQueryString.add(getString(R.string.category_sports_filter));
                }
                else filtersQueryString.remove(getString(R.string.category_sports_filter));
                    break;
            case R.id.checkbox_travel:
                if (checked){
                    filtersQueryString.add(getString(R.string.category_travel_filter));
                }
                else filtersQueryString.remove(getString(R.string.category_travel_filter));
                    break;
        }
        // Join all checked filters into one string
        finalFilterString = (String) TextUtils.join( " " , filtersQueryString );
        Log.d(TAG, "onCheckboxClicked result: " + finalFilterString);

    }

    /**
     * Search button action - to invoke the search API with all filtered data
     */
    public void searchButtonOnClickAction (View view){
        if (finalFilterString != null){

            String filters = "news_desk:(" + finalFilterString + ")";
            Log.d(TAG, "onSearchClicked result: " + filters);
            // Sort the results (newest, oldest, relevance)
            String sort = getString(R.string.sort_value);

            mQueryValue = mSearchQuery.getText().toString();
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra(getString(R.string.begin_date), BeginDateStringForURL);
            intent.putExtra(getString(R.string.end_date), EndDateStringForURL );
            intent.putExtra(getString(R.string.filter_query), filters );
            intent.putExtra(getString(R.string.search_query), mQueryValue );
            intent.putExtra(getString(R.string.sort), sort );
            startActivityForResult(intent,100);

        }else{
            Toast.makeText(this, getString(R.string.Your_filter_field_is_empty), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Check if failed to find results, and show a SnackBar pop-up
        if (resultCode == Activity.RESULT_CANCELED) {
            Snackbar.make(getCurrentFocus(), R.string.no_results,
                    Snackbar.LENGTH_LONG)
                    .show();
        }
    }

}
