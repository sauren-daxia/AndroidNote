package com.rxretrofit_build.util;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by tzm529 on 2016/8/3.
 * <p/>
 * 获取数据校验
 */
public class MD5Result {
    public static String sign(HashMap<String, Object> system, HashMap<String, Object> post, HashMap<String, Object> get) {
        String skipKey = "VECODE";

        system.putAll(post);
        system.putAll(get);

        if (system.containsKey(skipKey)) {
            system.remove(skipKey);
        }

        Object[] keys = system.keySet().toArray();
        Arrays.sort(keys);

        String result = "";
        int length = keys.length;
        String value;
        String key;

        for (int i = 0; i < length; i++) {
            key = keys[i].toString();
            value = system.get(key).toString();
            Log.v("test", "key = " + key + ", value = " + value);
            result = i + result + i + key + i + (value.length() > 60 ? md5(value) : value);
        }
        return md5(result);

    }

    private static String md5(String value) {
        try {
            MessageDigest md = MessageDigest.getInstance("md5");
            byte[] e = md.digest(value.getBytes());
            return toHexString(e);
        } catch (NoSuchAlgorithmException e) {
            Log.v("test", e.getMessage());
            return value;
        }
    }

    private static String toHexString(byte bytes[]) {
        StringBuilder hs = new StringBuilder();
        String stmp = "";
        for (int n = 0; n < bytes.length; n++) {
            stmp = Integer.toHexString(bytes[n] & 0xff);
            if (stmp.length() == 1)
                hs.append("0").append(stmp);
            else
                hs.append(stmp);
        }

        return hs.toString();
    }

}
