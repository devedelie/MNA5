package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.elbaz.eliran.mynewsapp.R;

public class SearchAndNotificationsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_and_notifications);

        this.configureToolbar();
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
}
