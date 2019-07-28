package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.elbaz.eliran.mynewsapp.Controllers.Fragments.TabFragment1;
import com.elbaz.eliran.mynewsapp.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class WebPageActivity extends AppCompatActivity {
    @BindView(R.id.webView)
    WebView mWebView;
    String mUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        ButterKnife.bind(this);

        // Call the toolbar
        this.configureToolbar();
        // Configure the webView
        this.configureWebView();



//        // Create the webView
//        mWebView= (WebView) findViewById(R.id.webView);
//        mWebView.getSettings().setJavaScriptEnabled(true);
//        mWebView.getSettings().setLoadWithOverviewMode(true);
//        mWebView.getSettings().setUseWideViewPort(true);
//        mWebView.getSettings().setBuiltInZoomControls(true);
//        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
//        mWebView.setWebViewClient(new WebViewClient());
//        mWebView.loadUrl("https://www.google.com");
    }

    /**
     * 1 - Toolbar execution
     */
    private void configureToolbar(){
        //Get the toolbar (Serialise)
        Toolbar toolbar = (Toolbar)  findViewById(R.id.toolbar);
        // Sets the Toolbar
        setSupportActionBar(toolbar);
        //Get a support ActionBar corresponding to this toolbar
        ActionBar actionBar = getSupportActionBar();
        // Enable the upper button (back button)
        actionBar.setDisplayHomeAsUpEnabled(true);
    }

    private void configureWebView(){
        String url = getIntent().getStringExtra(TabFragment1.BUNDLE_URL);
        mWebView.loadUrl(url);
        mWebView.setWebViewClient(new WebViewClient());
    }
}