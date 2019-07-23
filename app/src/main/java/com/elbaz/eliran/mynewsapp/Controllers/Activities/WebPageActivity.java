package com.elbaz.eliran.mynewsapp.Controllers.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.elbaz.eliran.mynewsapp.R;

public class WebPageActivity extends AppCompatActivity {
    WebView mWebView;

    private class WebViewClient extends android.webkit.WebViewClient{
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);

        mWebView= (WebView) findViewById(R.id.webView);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.getSettings().setLoadWithOverviewMode(true);
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.getSettings().setPluginState(WebSettings.PluginState.ON);
        mWebView.setWebViewClient(new WebViewClient());
        mWebView.loadUrl("https://www.google.com");

    }
}
