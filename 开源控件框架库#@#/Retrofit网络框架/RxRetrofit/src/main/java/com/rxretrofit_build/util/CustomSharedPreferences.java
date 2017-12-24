package com.rxretrofit_build.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.Map;
import java.util.Set;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/12/5  15:52
 *
 * @author cree
 * @version 1.0
 */
public class CustomSharedPreferences {
    public static void saveString(Context context, Map<String, Object> map, String fileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            edit.putString(key, map.get(key) + "");
        }
        edit.commit();
    }

    public static void saveBoolean(Context context, Map<String, Object> map, String fileName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(fileName, Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        Set<String> keySet = map.keySet();
        for (String key : keySet) {
            edit.putBoolean(key, (Boolean) map.get(key));
        }
        edit.commit();
    }

    public static String getString(Context context, String key, String fileName) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getString(key, null);
    }

    public static boolean getBoolean(Context context, String key, String fileName) {
        return context.getSharedPreferences(fileName, Context.MODE_PRIVATE).getBoolean(key, false);
    }
}
