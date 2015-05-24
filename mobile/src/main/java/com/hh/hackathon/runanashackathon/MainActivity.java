package com.hh.hackathon.runanashackathon;

import android.app.AlarmManager;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.app.Notification;
import android.app.PendingIntent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.NotificationCompat.WearableExtender;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.hackathon.runanashackathon.model.UserMatch;
import com.hh.hackathon.runanashackathon.services.CookieManager;
import com.hh.hackathon.runanashackathon.services.MatchActivity;
import com.hh.hackathon.runanashackathon.services.RunanasClientApi;
import com.hh.hackathon.runanashackathon.services.RunanasRestClient;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class MainActivity extends ActionBarActivity {

    SimpleAdapter adpt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Similar Runners near by");


        final String NOTIFICATION_ID = "notification_id";
        int eventId = 002;

       startAsynchUpdate();


// Build intent for notification content
        Intent viewIntent = new Intent(this, MatchActivity.class);

        viewIntent.putExtra(NOTIFICATION_ID, eventId);
        PendingIntent viewPendingIntent =
                PendingIntent.getActivity(this, 0, viewIntent, 0);

        NotificationCompat.Action action = new NotificationCompat.Action.Builder(R.mipmap.ic_launcher, "Ok", viewPendingIntent).build();

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setContentTitle("Got a Match!")
                        .setContentText("Wanna Run?")
                        .setContentIntent(viewPendingIntent)
                        .addAction(action);

// Get an instance of the NotificationManager service
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

// Build the notification and issues it with notification manager.
        notificationManager.notify(eventId, mBuilder.build());
    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }




    private void startAsynchUpdate() {
        Log.e("startSynch", "yes");
        adpt  = new SimpleAdapter(new ArrayList<UserMatch>(), this);
        ListView lView = (ListView) findViewById(R.id.listView1);
        lView.setAdapter(adpt);
        new AsyncListViewLoader().execute();

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


    public class SimpleAdapter extends ArrayAdapter<UserMatch> {

        private List<UserMatch> itemList;
        private Context context;

        public SimpleAdapter(List<UserMatch> itemList, Context ctx) {
            super(ctx, android.R.layout.simple_list_item_1 , itemList);
            this.itemList = itemList;
            this.context = ctx;
        }

        public int getCount() {
            if (itemList != null)
                return itemList.size();
            return 0;
        }

        public UserMatch getItem(int position) {
            if (itemList != null)
                return itemList.get(position);
            return null;
        }

        public long getItemId(int position) {
            if (itemList != null)
                return itemList.get(position).hashCode();
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.e("printView", "print");
            View v = convertView;
            if (v == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = inflater.inflate(R.layout.match_item, null);
            }

            UserMatch c = itemList.get(position);
            TextView text = (TextView) v.findViewById(R.id.list_item_username);
            text.setText(c.getUsername());

            TextView distance = (TextView) v.findViewById(R.id.list_item_distance);
            distance.setText("Distance: " + c.getDistance() + "m");

            return v;
        }

        public List<UserMatch> getItemList() {
            return itemList;
        }

        public void setItemList(List<UserMatch> itemList) {
            this.itemList = itemList;
        }


    }


    private class AsyncListViewLoader extends AsyncTask<String, Void, List<UserMatch>> {
        private final ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPostExecute(List<UserMatch> result) {
            super.onPostExecute(result);
            dialog.dismiss();
            adpt.setItemList(result);
            adpt.notifyDataSetChanged();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setMessage("Search for Matches...");
            dialog.show();
        }

        @Override
        protected List<UserMatch> doInBackground(String... params) {
            List<UserMatch> result = new ArrayList<>();

            try {
                URL u = new URL("http://cconnection.de/lifelog/search_mate/?userId=" + CookieManager.get().getUserId());

                HttpURLConnection conn = (HttpURLConnection) u.openConnection();
                conn.setRequestMethod("GET");

                conn.connect();
                InputStream is = conn.getInputStream();

                // Read the stream
                byte[] b = new byte[1024];
                ByteArrayOutputStream baos = new ByteArrayOutputStream();

                while ( is.read(b) != -1)
                    baos.write(b);

                String JSONResp = new String(baos.toByteArray());
                JSONObject singleResult = new JSONObject(JSONResp);
                Log.w("found matches", " " + singleResult.toString());
                UserMatch match = new UserMatch();
                match.setUsername(singleResult.getString("username"));
                match.setDistance(singleResult.getString("distance"));
                match.setLatitude(singleResult.getString("latitude"));
                match.setLongitude(singleResult.getString("longitude"));

                List<UserMatch> users = Collections.singletonList(match);
                Log.w("found matches", " " + users.size());

                return users;
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            return null;
        }


    }

}
