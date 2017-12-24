package com.example.rx2retrofit.base;

import android.content.Context;

import com.example.rx2retrofit.BuildConfig;
import com.example.rx2retrofit.interceptor.CaCheInterceptor;
import com.example.rx2retrofit.interceptor.RquestInterceptor;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hp on 2017/5/20.
 * OKHttp构造类
 */
public class BaseOKHttp {
    public static OkHttpClient getOk(Context context, HashMap<String, Object> map) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)    //读取超时
                .writeTimeout(10, TimeUnit.SECONDS)    //写入超时
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                .pingInterval(10, TimeUnit.SECONDS)    //websocket 轮训间隔
                //--------------------请求拦截------！！！！！！！！！！！！1
                .addInterceptor(new RquestInterceptor<Map<String, Object>>(map))
                //--------------------缓存拦截------！！！！！！！！！！！！1
                .addNetworkInterceptor(new CaCheInterceptor())
  //              .addInterceptor(new CaCheInterceptor()) //存在504请求异常，不知道怎么使用最好别用
                //--------------------缓存路径------！！！！！！！！！！！！1
                .cache(new Cache(context.getExternalCacheDir(), 20 * 1024));    //设置缓存文件以及缓存大小，单位bytes

        if (BuildConfig.LOG_DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }
}
