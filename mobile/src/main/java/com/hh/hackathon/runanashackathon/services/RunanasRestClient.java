package com.hh.hackathon.runanashackathon.services;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.PersistentCookieStore;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;

/**
 * Created by majajaensch on 23.05.15.
 */
public class RunanasRestClient {

    private static final String BASE_URL = "https://cconnection.de:8000/";

    private static PersistentCookieStore _cookieStore;

    public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().get(getAbsoluteUrl(url), params, responseHandler);
    }

    private static AsyncHttpClient getClient(){
        AsyncHttpClient client = new AsyncHttpClient();
        CookieManager manager =  CookieManager.get();
        if(manager.isLoggedIn()) {
            client.setBasicAuth(manager.getUsername(), manager.getPassword());
        }
        return client;
    }

    public static void post(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        getClient().post(getAbsoluteUrl(url), params, responseHandler);
    }

    private static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }



}
