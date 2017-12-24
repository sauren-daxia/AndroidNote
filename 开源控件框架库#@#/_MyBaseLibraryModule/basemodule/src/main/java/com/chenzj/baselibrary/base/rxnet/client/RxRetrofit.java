package com.chenzj.baselibrary.base.rxnet.client;


import com.chenzj.baselibrary.base.rxnet.base.BaseRetrofit;

import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

/**
 * Created by hp on 2017/5/22.
 * 网络访问请求类
 */
public class RxRetrofit extends Rx {

    public static <T> T create(HashMap<String, Object> map, List<MultipartBody.Part> list, Class<T> reqeustClass) {
        return BaseRetrofit.getRetrofit(con, map, list).create(reqeustClass);
    }

    public static <T> T create(HashMap<String, Object> map, Class<T> reqeustClass) {
        return BaseRetrofit.getRetrofit(con, map).create(reqeustClass);
    }

    public static void request(final Observable observable, RxObserver subscriber) {
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
