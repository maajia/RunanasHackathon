package com.hh.hackathon.runanashackathon;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;


public class LifeLoginWebPage extends Activity {


    private static final String URL_TO_LOAD = "http://google.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        WebView wv = (WebView) findViewById(R.id.browser_demo);
        loadResource(wv, URL_TO_LOAD);
    }

    private void loadResource(WebView wv, String resource) {
        wv.loadUrl(resource);
        wv.getSettings().setJavaScriptEnabled(true);
    }

}
