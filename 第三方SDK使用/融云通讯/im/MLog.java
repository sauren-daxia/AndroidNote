package com.bjesc.app.rlimtest.im;

import com.orhanobut.logger.Logger;

/**
 * Created by Jason Chen on 2017/8/25.
 */

public class MLog {
    public static String TAG = "APPLOG";

    public static void d(String text) {
        Logger.d(TAG, text);
    }

    public static void e(String text) {
        Logger.d(TAG, text);
    }

    public static void i(String text) {
        Logger.d(TAG, text);
    }

    public static void v(String text) {
        Logger.d(TAG, text);
    }

    public static void d(String TAG , String text){Logger.d(TAG,text);}
}
