package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import com.elbaz.eliran.mynewsapp.R;

import java.util.Calendar;

public class SearchAndNotificationsActivity extends AppCompatActivity {
    private DatePickerDialog.OnDateSetListener mOnDateSetListener;
    private TextView mStartDate;
    private TextView mEndDate;
    private TextView mSearchQuery;
    private Button mSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_notifications);

        this.configureToolbar();

        mStartDate = (TextView) findViewById(R.id.search_startDate);

        // DatePicker listener
        mOnDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                // Increase month variable +1, as count starts from 0 (ex: April = 3)
                month +=1;
                String date = dayOfMonth + "/" + month + "/" + year;
                SpannableString content = new SpannableString("Content");
                content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
                mStartDate.setText(date);
            }
        };



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
     * Date picker for search filter
     */
    public void searchDatePicker (View view){
        // Crete an instance of Calendar and get date into variables
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePicker dialog object
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                SearchAndNotificationsActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                mOnDateSetListener,
                year,month,day);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        datePickerDialog.show();



    }


}
