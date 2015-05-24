package com.hh.hackathon.runanashackathon;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Notification;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;

import com.hh.hackathon.runanashackathon.services.CookieManager;
import com.hh.hackathon.runanashackathon.services.MatchActivity;

public class MainActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final String NOTIFICATION_ID = "notification_id";
        int eventId = 002;
// Build intent for notification content
        Intent viewIntent = new Intent(this, MatchActivity.class);
        viewIntent.putExtra(NOTIFICATION_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Run?")
                        .setContentText("Run?")
                        .setContentIntent(viewPendingIntent)
                        .addAction(R.mipmap.ic_launcher,NOTIFICATION_ID, viewPendingIntent);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(eventId, notificationBuilder.build());
    }
    public PendingIntent getPendingIntent(){
        // Build an intent for an action to open a url
        Intent urlIntent = new Intent(Intent.ACTION_VIEW);
        Uri uri = Uri.parse("http://www.google.com");
        mapIntent.setData(uri);
        PendingIntent urlPendingIntent =
                PendingIntent.getActivity(this, 0, urlIntent, 0);

        return urlPendingIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
        if(id == R.id.action_logout) {
            CookieManager.get().logOut();
            startActivity(new Intent(MainActivity.this, LandingActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
