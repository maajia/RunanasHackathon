package com.hh.hackathon.runanashackathon.services;

import android.content.ContextWrapper;

import com.loopj.android.http.PersistentCookieStore;

import org.apache.http.cookie.Cookie;
import org.apache.http.impl.cookie.BasicClientCookie;

import java.util.Date;
import java.util.List;

/**
 * Created by majajaensch on 24.05.15.
 */
public class CookieManager {

    private final static String USERNAME = "username";
    private final static String PASSWORD = "password";
    private final static String LIFELOG_TOKEN = "lifelog";



    private final PersistentCookieStore _cookieStore;
    private static  CookieManager INSTANCE;

    private  CookieManager(PersistentCookieStore cookieStore){
        _cookieStore = cookieStore;
    }

    public static CookieManager init(ContextWrapper context) {
        if( INSTANCE == null ) {
            INSTANCE = new CookieManager(new PersistentCookieStore(context.getApplicationContext()));
        }
        return INSTANCE;
    }

    public static CookieManager get(){
        if(INSTANCE == null) {
            throw new IllegalStateException("not initialized yet");
        }
        return INSTANCE;
    }

    public void logIn(String username, String passwordHash){
        _cookieStore.clear();
        BasicClientCookie newCookie = new BasicClientCookie("runanans", "cookie");
        newCookie.setVersion(1);
        newCookie.setDomain("cconnection.de");
        newCookie.setPath("/");
        newCookie.setAttribute(USERNAME, username);
        newCookie.setAttribute(PASSWORD, passwordHash);
        _cookieStore.addCookie(newCookie);
    }

    public void logOut(){
        _cookieStore.clear();
    }

    public void storeLifeLogToken(String token){
        getCookie().setAttribute(LIFELOG_TOKEN, token);
    }

    public boolean isLoggedIn(){
        Cookie cookie = getCookie();
        return cookie != null;
    }

    private BasicClientCookie getCookie(){
        List<Cookie> cookies = _cookieStore.getCookies();
        if( cookies==null || cookies.size()==0 ) {
            return null;
        }
        return  (BasicClientCookie) cookies.get(0);
    }




}
