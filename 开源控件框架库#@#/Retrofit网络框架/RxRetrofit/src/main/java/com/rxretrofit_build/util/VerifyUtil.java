package com.rxretrofit_build.util;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Suzy on 2016/6/13.
 * <p/>
 * 字符串验证
 */
public class VerifyUtil {

    public static boolean isNullOrEmptyString(String inputString) {
        if (TextUtils.isEmpty(inputString) || "null".equals(inputString.trim()))
            return true;
        else
            return false;
    }

    /**
     * 判读字符串是否为空
     *
     * @param inputString
     * @return
     */
    public static boolean isNullOrEmpty(String inputString) {
        if (null == inputString) {
            return true;
        } else if (inputString.trim().equals("")) {
            return true;
        }

        return false;
    }

    /**
     * 比较两个字符串是否相等
     *
     * @param arg1
     * @param arg2
     * @return
     */
    public static boolean compareEquals(String arg1, String arg2) {
        if (VerifyUtil.isNullOrEmpty(arg1)) {
            if (VerifyUtil.isNullOrEmpty(arg2)) {
                return true;
            } else {
                return false;
            }
        } else {
            return arg1.equals(arg2);
        }
    }

    /**
     * 复制字符串
     *
     * @param inputString
     * @return
     */
    public static String copy(String inputString) {
        if (inputString == null) {
            return null;
        }
        return new String(inputString);
    }

    /**
     * 时间格式转换HH:mm:ss
     *
     * @param hour
     * @param min
     * @param sec
     * @return
     */
    public static String formatDuration(int hour, int min, int sec) {

        StringBuilder strBuider = new StringBuilder();
        if (hour < 10) {
            strBuider.append(0);
        }
        strBuider.append(hour).append(":");
        if (min < 10) {
            strBuider.append(0);
        }
        strBuider.append(min).append(":");
        if (sec < 10) {
            strBuider.append(0);
        }
        strBuider.append(sec);

        return strBuider.toString();
    }

    /**
     * 是否为电话号码13X、15X、18X、14X、17X
     *
     * @param phoneNumber
     * @return
     */
    public static boolean isTelPhoneNumber(String phoneNumber) {
        Pattern p = Pattern
                .compile("^13[0-9]{1}[0-9]{8}$|15[012356789]{1}[0-9]{8}$|18[0-9]{1}[0-9]{8}$|14[57]{1}[0-9]{8}$|17[0678]{1}[0-9]{8}$");
        //^(13\\d|18\\d|14[57]|15[012356789]|17[0678])\\d{8}$
        //^13[0-9]{1}[0-9]{8}$|15[0-9]{1}[0-9]{8}$|18[0-3,5-9]{1}[0-9]{8}$|14[0-3,5-9]{1}[0-9]{8}$|17[0-3,5-9]{1}[0-9]{8}$
        Matcher m = p.matcher(phoneNumber);
        return m.matches();
    }

    /**
     * 是否为邮箱
     *
     * @param email
     * @return
     */
    public static boolean isEmailNumber(String email) {
        Pattern p = Pattern
                .compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 身份证验证
     *
     * @param id
     * @return
     */
    public static boolean isCardID(String id) {
        Pattern p = Pattern
                .compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$|^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X)$");
        Matcher m = p.matcher(id);
        return m.matches();
    }

    /**
     * 密码验证（字母、数字、特殊符号）
     *
     * @param id
     * @return
     */
    public static boolean isPassWord(String id) {
        Pattern p = Pattern
                .compile("^[a-zA-Z]\\\\w{5,17}$");//^(?![a-zA-Z0-9]+$)(?![^a-zA-Z/D]+$)(?![^0-9/D]+$).{6,20}$
        Matcher m = p.matcher(id);
        return m.matches();
    }

    /**
     * 字母
     *
     * @param id
     * @return
     */
    public static boolean isLetter(String id) {
        Pattern p = Pattern.compile("^[A-Za-z]+");
        Matcher m = p.matcher(id);
        return m.matches();
    }

    /**
     * 数字
     *
     * @param id
     * @return
     */
    public static boolean isNumber(String id) {
        Pattern p = Pattern.compile("^[0-9]\\d*|0");
        Matcher m = p.matcher(id);
        return m.matches();
    }

    /**
     * @param date
     * @param format yy-hh-mm
     * @return
     */
    public static String dateFormat(Date date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static String dateFormat(String date, String format) {
        SimpleDateFormat formatter = new SimpleDateFormat(format);
        return formatter.format(date);
    }

    public static long dateFormat(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return formatter.parse(date).getTime();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 字符串转为经纬度
     *
     * @param str
     * @return
     */
    public static int stringToGeopoint(String str) {
        // int) (39.918* 1E6)
        int i = 0;
        try {
            float temp = Float.parseFloat(str);
            i = (int) (temp * 1E6);
        } catch (Exception ex) {
            i = 0;
        }
        return i;
    }

    public static String dateFormat(long ms) {
        long temp = System.currentTimeMillis() - ms;
        Date date = new Date(ms);
        if (temp < 86400000) {
            return dateFormat(date, "HH:mm");
        } else {
            return dateFormat(date, "MM-dd");
        }
    }

}
