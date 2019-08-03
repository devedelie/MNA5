package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.elbaz.eliran.mynewsapp.R;

import java.util.Calendar;

import static android.content.ContentValues.TAG;

public class SearchActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TextView mStartDate;
    private TextView mEndDate;
    private EditText mSearchQuery;
    private Button mSearchButton;
    private String mDate;
    private String BeginDateStringForURL="";
    private String EndDateStringForURL="";
    private int buttonSelectorFlag=0;
//    private String finalSearchQueryString;
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

//                // Update filter with default date whenever the user left it empty
//                if (BeginDateStringForURL == null || BeginDateStringForURL.equals("")){
//                    BeginDateStringForURL = "";
//                }
//                if(EndDateStringForURL == null || EndDateStringForURL.equals("")){
//                    EndDateStringForURL = "";
//                }
//
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

    public void searchButtonOnClickAction (View view){
        // for test
        String filter = "politics";
        String sort = "newest";
        /////////
        mQueryValue = mSearchQuery.getText().toString();
        if(mQueryValue==null || mQueryValue.equals("")){
            Toast.makeText(this, "Your search field is empty", Toast.LENGTH_LONG).show();
        }else{
            Intent intent = new Intent(this, SearchResultsActivity.class);
            intent.putExtra("begin_date", BeginDateStringForURL);
            intent.putExtra("end_date", EndDateStringForURL );
            intent.putExtra("filter_query", filter );
            intent.putExtra("search_query", mQueryValue );
            intent.putExtra("sort", sort );
            startActivity(intent);
        }
    }

}
