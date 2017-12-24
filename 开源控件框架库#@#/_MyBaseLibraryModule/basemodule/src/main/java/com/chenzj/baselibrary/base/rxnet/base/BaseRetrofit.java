package com.chenzj.baselibrary.base.rxnet.base;

import android.content.Context;

import com.chenzj.baselibrary.base.rxnet.client.Rx;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp on 2017/5/20.
 */
public class BaseRetrofit {
    /**
     * 有参有拦截请求
     * @param con
     * @param map
     * @return
     */
    public static Retrofit getRetrofit(Context con, HashMap<String, Object> map,List<MultipartBody.Part> list) {
        return new Retrofit.Builder()
                .baseUrl(Rx.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(BaseOKHttp.getOk(con, map,list))
                .build();
    }

    public static Retrofit getRetrofit(Context con, HashMap<String, Object> map) {
        return new Retrofit.Builder()
                .baseUrl(Rx.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(BaseOKHttp.getOk(con, map))
                .build();
    }

    /**
     * 无参无拦截请求
     * @return
     */
    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(Rx.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(BaseOKHttp.getOk())
                .build();
    }
}
