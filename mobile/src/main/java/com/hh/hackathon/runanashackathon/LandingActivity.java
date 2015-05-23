package com.hh.hackathon.runanashackathon;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hh.hackathon.runanashackathon.services.CookieManager;
import com.hh.hackathon.runanashackathon.services.RunanasClientApi;
import com.hh.hackathon.runanashackathon.services.RunanasRestClient;
import com.loopj.android.http.PersistentCookieStore;


public class LandingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        hideSystemUI();
        CookieManager.init(this);
        //new RunanasClientApi().getUsers();
        findViewById(R.id.leaveLandingPage).setOnClickListener(sendNotificationClickListener);
    }

    View.OnClickListener sendNotificationClickListener = new View.OnClickListener(){
        public void onClick(View view) {
           if(!CookieManager.get().isLoggedIn()) {
               startActivity(new Intent(LandingActivity.this, LoginActivity.class));
           } else {
               startActivity(new Intent(LandingActivity.this, MainActivity.class));
           }
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_landing, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    // This snippet hides the system bars.
    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

}
