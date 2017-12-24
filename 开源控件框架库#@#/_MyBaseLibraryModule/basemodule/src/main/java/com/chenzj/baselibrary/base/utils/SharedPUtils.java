package com.dinghong.dinghongcollect.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Jason Chen on 2017/8/31.
 * 本地数据存储工具类
 */

public class SharedPUtils {

    public static Context con;

    public static void init(Context context) {
        con = context;
    }

    public static boolean isFirst() {
        return getBoolean("isfirst", true);
    }




    public static void saveInt(String key, int value) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        sp.edit().putInt(key, value).commit();
    }

    public static long getInt(String key, int defValue) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }


    public static void saveLong(String key, long value) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        sp.edit().putLong(key, value).commit();
    }

    public static long getLong(String key, long defValue) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        return sp.getLong(key, defValue);
    }

    public static void saveString(String key, String value) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        sp.edit().putString(key, value).commit();
    }

    public static String getString(String key, String defValue) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void saveBoolean(String key, boolean value) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sp.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public static boolean getBoolean(String key, boolean defValue) {
        SharedPreferences sp = con.getSharedPreferences(con.getPackageName(), Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }


    public static void cleanSharedPreferences(String preferencesName) {
        SharedPreferences settings = con.getSharedPreferences(preferencesName, 0);
        SharedPreferences.Editor editor = settings.edit();
        settings.edit().clear().commit();
    }
}
