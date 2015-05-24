package com.hh.hackathon.runanashackathon.services;

import android.util.Log;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hh.hackathon.runanashackathon.model.User;
import com.hh.hackathon.runanashackathon.model.UserMatch;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;

import java.util.Arrays;
import java.util.List;

/**
 * Created by majajaensch on 23.05.15.
 */
public class RunanasClientApi {

    public interface MatchinUserCallback {
        void execute(List<UserMatch> users);
    }

    private RunanasClientApi(){

    }


    public static void getMatchingUsers(final MatchinUserCallback callback)  {

        RunanasRestClient.get("search_mate/?userId=" + CookieManager.get().getUserId(), null, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                try {
                    JsonParser parser = new JsonFactory().createParser(timeline.toString());
                    List<UserMatch> users = Arrays.asList(new ObjectMapper().readValue(parser, UserMatch[].class));
                    callback.execute(users);
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
