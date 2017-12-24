package com.rxretrofit_build.util;

import android.app.Activity;

import com.rxretrofit_build.net.RxCallBack;
import com.tbruyelle.rxpermissions.RxPermissions;

import rx.functions.Action1;

/**
 * Title:
 * Description:用来处理android6.0以上权限的工具类
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2016/12/16  13:53
 *
 * @author cree
 * @version 1.0
 */
public class RxPermissUtil {
    /**
     * @param activity
     * @param booleanRxCallBack 用来回调的接口
     * @param permissions       需要被处理的权限
     */
    public static void getPermission(Activity activity, final RxCallBack<Boolean> booleanRxCallBack, final String... permissions) {
        new RxPermissions(activity).request(permissions)
                .subscribe(new Action1<Boolean>() {
                    @Override
                    public void call(Boolean aBoolean) {
                        if (aBoolean) {
                            booleanRxCallBack.onSuccess(true);
                        } else {
                            booleanRxCallBack.onFailure(new Throwable("授权失败"));
                        }
                    }
                });
    }
}
