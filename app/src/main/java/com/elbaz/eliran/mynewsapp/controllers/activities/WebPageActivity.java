package com.elbaz.eliran.mynewsapp.controllers.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.elbaz.eliran.mynewsapp.controllers.fragments.TabFragment1;
import com.elbaz.eliran.mynewsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebPageActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        ButterKnife.bind(this);

        // Call the toolbar
        this.configureToolbar();
        // Configure the webView
        this.configureWebView();
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

    private void configureWebView(){
        // Get the data from the Fragment (URL String)
        // the Bundle is located in the first Fragment "TabFragment1.java" and is in use by all other Fragments
        String url = getIntent().getStringExtra(TabFragment1.BUNDLE_URL);

        // Page tools and configurations
//      mWebView.getSettings().setJavaScriptEnabled(true);  // Disabled to avoid blocking pop-ups
        mWebView.getSettings().setLoadWithOverviewMode(true); //Loads the WebView with the attributes and zooms out the content to fit on screen by width
        mWebView.getSettings().setUseWideViewPort(false); // When the value is false, the layout width is always set to the width of the WebView control in device-independent (CSS) pixels.
        mWebView.getSettings().setBuiltInZoomControls(true);
        // Load page
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl(url);
    }

    // Detect the click on "back" button and finish the current activity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if ( id == android.R.id.home ) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}