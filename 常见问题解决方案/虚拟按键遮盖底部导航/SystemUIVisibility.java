package com.nanbo.vocationalschools.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.lang.reflect.Method;

/**
 * Created by LuYu on 2017-01-04.17:10.
 */

public class SystemUIVisibility {

    private static final String TAG = "SystemUIVisibility";


    /**
     * 判断是否存在NavigationBar
     *
     * @param context
     * @return
     */
    public static boolean checkDeviceHasNavigationBar(Context context) {

        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            Log.w(TAG, e);
        }


        return hasNavigationBar;
    }

    /**
     * 获取NavigationBar的高度
     *
     * @param context
     * @return
     */
    public static int getNavigationBarHeight(Context context) {
        int navigationBarHeight = 0;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("navigation_bar_height", "dimen", "android");
        if (id > 0 && checkDeviceHasNavigationBar(context)) {
            navigationBarHeight = rs.getDimensionPixelSize(id);
        }
        return navigationBarHeight;
    }

    /**
     * 获取StatusBar的高度
     *
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 如果有虚拟按键则增加bootomMargin
     *
     * @param con
     * @param views
     */
    public static void setMarginNavigationBar(Context con, View... views) {
        if (checkDeviceHasNavigationBar(con) && views != null) {
            for (int i = 0; i < views.length; i++) {
                ViewGroup.LayoutParams params = views[i].getLayoutParams();
                try {
                    if (params instanceof LinearLayout.LayoutParams) {

                        ((LinearLayout.LayoutParams) params).bottomMargin = getNavigationBarHeight(con) + ((LinearLayout.LayoutParams) params).bottomMargin;
                        views[i].setLayoutParams(params);
                    } else if (params instanceof RelativeLayout.LayoutParams) {
                        ((RelativeLayout.LayoutParams) params).bottomMargin = getNavigationBarHeight(con) + ((RelativeLayout.LayoutParams) params).bottomMargin;
                        views[i].setLayoutParams(params);
                    } else if (params instanceof CoordinatorLayout.LayoutParams) {
                        ((CoordinatorLayout.LayoutParams) params).bottomMargin = getNavigationBarHeight(con) + ((CoordinatorLayout.LayoutParams) params).bottomMargin;
                        views[i].setLayoutParams(params);
                    } else if (params instanceof ConstraintLayout.LayoutParams) {
                        ((ConstraintLayout.LayoutParams) params).bottomMargin = getNavigationBarHeight(con) + ((ConstraintLayout.LayoutParams) params).bottomMargin;
                        views[i].setLayoutParams(params);
                    }
                } catch (ClassCastException e) {
                    Log.d("CHEN---------- : ", "setMarginNavigationBar: 类型转换失败");
                }
            }
        }
    }
}
