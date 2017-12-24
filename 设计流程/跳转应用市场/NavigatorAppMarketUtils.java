package com.weiyin.card_gobook.util;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Jason Chen on 2017/10/25.
 */

public class NavigatorAppMarketUtils {
    public static void start(Context con) {
        if (!TextUtils.isEmpty(android.os.Build.BRAND)) {
            if (android.os.Build.BRAND.contains("xiaomi")) {
                xiaomiStart(con);
            } else if (android.os.Build.BRAND.contains("samsung")) {
                samsungStart(con);
            } else {
                defaultStart(con);
            }
        }
    }


    /**
     * 三星跳转
     *
     * @param con
     */
    private static void samsungStart(Context con) {
        Uri uri = Uri.parse("http://www.samsungapps.com/appquery/appDetail.as?appId=" + con.getPackageName());
        Intent goToMarket = new Intent();
        goToMarket.setClassName("com.sec.android.app.samsungapps", "com.sec.android.app.samsungapps.Main");
        goToMarket.setData(uri);
        try {
            con.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 小米跳转
     *
     * @param con
     */
    private static void xiaomiStart(Context con) {
        Uri uri = Uri.parse("market://details?id=" + con.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            goToMarket.setClassName("com.tencent.android.qqdownloader", "com.tencent.pangu.link.LinkProxyActivity");
            con.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 默认跳转
     *
     * @param con
     */
    private static void defaultStart(Context con) {
        Uri uri = Uri.parse("market://details?id=" + con.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            con.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
}
