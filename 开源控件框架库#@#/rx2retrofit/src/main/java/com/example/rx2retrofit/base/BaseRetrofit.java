package com.example.rx2retrofit.base;

import android.content.Context;

import com.example.rx2retrofit.BuildConfig;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.util.HashMap;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp on 2017/5/20.
 */
public class BaseRetrofit {
    public static Retrofit getRetrofit(Context con, HashMap<String, Object> map) {
        return new Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(BaseOKHttp.getOk(con, map))
                .build();
    }
}
