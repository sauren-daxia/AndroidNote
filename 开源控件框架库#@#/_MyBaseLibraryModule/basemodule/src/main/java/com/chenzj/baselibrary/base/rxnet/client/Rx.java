package com.chenzj.baselibrary.base.rxnet.client;

import android.content.Context;

/**
 * Created by hp on 2017/5/22.
 */
public class Rx {

    public static Context con;
    public static boolean DEBUG = false;
    public static String BASE_URL = "";

    public static void init(Context con, boolean DEBUG, String BASE_URL) {
        Rx.con = con;
        Rx.DEBUG = DEBUG;
        Rx.BASE_URL = BASE_URL;
    }

}
