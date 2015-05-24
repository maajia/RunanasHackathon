package com.hh.hackathon.runanashackathon;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;


public class LifeLoginWebPage extends Activity {


    private static final String URL_TO_LOAD = "https://platform.lifelog.sonymobile.com/oauth/2/authorize?client_id=805oGS4lLGQZpXLfXw1RXwFbV7D3z9TL&scope=lifelog.activities.read";
    Dialog auth_dialog;
    WebView web;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_login_web_page);
        auth_dialog = new Dialog(LifeLoginWebPage.this);
        auth_dialog.setContentView(R.layout.lifelog_dialog);
        web = (WebView)auth_dialog.findViewById(R.id.webdiv);
        web.getSettings().setJavaScriptEnabled(true);
        web.setWebViewClient(new Callback());
        web.loadUrl(URL_TO_LOAD);
        auth_dialog.show();
        auth_dialog.setTitle("Authorize Runanas");
        auth_dialog.setCancelable(true);
    }

    public void parseLoginResponse() {
        startActivity(new Intent(LifeLoginWebPage.this, MainActivity.class));
    }
    private class Callback extends WebViewClient {  //HERE IS THE MAIN CHANGE.

        boolean authComplete = false;
        Intent resultIntent = new Intent();

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon){
            super.onPageStarted(view, url, favicon);

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.v("pageFinished", url );
            if (url.contains("cconnection.de") && authComplete != true) {
                Uri uri = Uri.parse(url);
                Log.i("", "Say hello to my little friend!");
                authComplete = true;
                resultIntent.putExtra("code", "woop");
                LifeLoginWebPage.this.setResult(Activity.RESULT_OK, resultIntent);
                setResult(Activity.RESULT_CANCELED, resultIntent);

                auth_dialog.dismiss();
                Toast.makeText(getApplicationContext(), "Authorization Code is: " + "HACKATHON", Toast.LENGTH_SHORT).show();
                LifeLoginWebPage.this.parseLoginResponse();
            }
        }
    }
}
