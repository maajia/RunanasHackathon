package com.hh.hackathon.runanashackathon.services;

import android.preference.PreferenceActivity;
import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.hackathon.runanashackathon.model.User;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

/**
 * Created by majajaensch on 23.05.15.
 */
public class RunanasClientApi {

    public void getUsers()  {


            RunanansRestClient.get("users", null, new JsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    // Pull out the first event on the public timeline
                    JSONObject firstEvent = null;
                    try {
                        firstEvent = timeline.getJSONObject(0);
                        JsonParser parser = new JsonFactory().createParser(timeline.toString());
                        List<User> users = Arrays.asList(new ObjectMapper().readValue(parser, User[].class));
                        //TODO(Do something with the user)
                    } catch (Exception e) {
                        e.printStackTrace();
                        Log.e("parsing response failed", e.toString());
                    }

                }

                @Override
                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    Log.e("user request failed", responseString);
                }
            });

    }
}
