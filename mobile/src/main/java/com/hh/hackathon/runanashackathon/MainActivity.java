package com.hh.hackathon.runanashackathon;

import android.app.AlarmManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Notification;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.widget.Toast;

import com.hh.hackathon.runanashackathon.model.UserMatch;
import com.hh.hackathon.runanashackathon.services.CookieManager;
import com.hh.hackathon.runanashackathon.services.MatchActivity;
import com.hh.hackathon.runanashackathon.services.RunanasClientApi;
import com.hh.hackathon.runanashackathon.services.RunanasRestClient;

import java.util.Calendar;
import java.util.List;
import java.util.Objects;

public class MainActivity extends ActionBarActivity {

    public class UpdateService extends Service {

        List<UserMatch> matchingUser;
        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onCreate() {

            super.onCreate();
        }

        @Override
        public void onDestroy() {
            // TODO Auto-generated method stub
          super.onDestroy();



        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            RunanasClientApi.getMatchingUsers(new RunanasClientApi.MatchinUserCallback() {
                public void execute(List<UserMatch> newMatches) {
                    List<UserMatch> matchingUser = newMatches;
                    Toast.makeText(getApplicationContext(), "found Matching Users " + matchingUser.size(), Toast.LENGTH_LONG ).show();
                }
            });
            return super.onStartCommand(intent, flags, startId);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("find your running mate");


        final String NOTIFICATION_ID = "notification_id";
        int eventId = 002;

       startAsynchUpdate();


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
                        .addAction(R.mipmap.ic_launcher, NOTIFICATION_ID, viewPendingIntent);

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
       // mapIntent.setData(uri);
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


    private void startAsynchUpdate() {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 10);

        Intent intent = new Intent(this, UpdateService.class);

        PendingIntent pintent = PendingIntent.getService(this, 0, intent, 0);

        AlarmManager alarm = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        //for 30 mint 60*60*1000
        alarm.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(),
                60*60*1000, pintent);
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
