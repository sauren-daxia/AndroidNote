package com.chenzj.baselibrary.base.rxnet.utils;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Jason Chen on 2017/11/29.
 */

public class CookieUtils {
    public static final HashMap<String, List<Cookie>> cookieStore = new HashMap<String, List<Cookie>>();
    public static CookieJar jar = new CookieJar() {

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            cookieStore.put(url.host(), cookies);
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieStore.get(url.host());
            Log.d("CHEN", "loadForRequest: " + cookieStore.size());
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    };
}
