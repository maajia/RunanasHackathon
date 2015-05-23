package com.hh.hackathon.runanashackathon;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class LifeLoginWebPage extends Activity {


    private static final String URL_TO_LOAD = "http://google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_login_web_page);
        WebView wv = (WebView) findViewById(R.id.browser_demo);
        wv.setWebViewClient(new Callback());
        wv.loadUrl(URL_TO_LOAD);
    }

    private void loadResource(WebView wv, String resource) {
        wv.loadUrl(resource);
        wv.getSettings().setJavaScriptEnabled(true);
    }

    private class Callback extends WebViewClient{  //HERE IS THE MAIN CHANGE.

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return (false);
        }

    }
}