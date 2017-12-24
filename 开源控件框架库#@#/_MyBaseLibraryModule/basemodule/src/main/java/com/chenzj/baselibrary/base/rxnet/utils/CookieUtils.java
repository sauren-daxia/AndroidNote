package com.chenzj.baselibrary.base.rxnet.utils;

import android.util.Log;

import com.chenzj.baselibrary.base.rxnet.client.Rx;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by Jason Chen on 2017/11/29.
 */

public class CookieUtils {
    public static final PersistentCookieStoreUtils cookieUtils = new PersistentCookieStoreUtils(Rx.con);
    public static CookieJar jar = new CookieJar() {

        @Override
        public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
            if (cookies != null && cookies.size() > 0) {
                for (Cookie item : cookies) {
                    cookieUtils.add(url, item);
                }
            }
        }

        @Override
        public List<Cookie> loadForRequest(HttpUrl url) {
            List<Cookie> cookies = cookieUtils.get(url);
            if(cookies!=null){
                Log.d("CHEN", "--------------get: cookies = "+cookies.toString());
            }
            return cookies != null ? cookies : new ArrayList<Cookie>();
        }
    };
}
