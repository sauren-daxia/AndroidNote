package com.chenzj.baselibrary.base.rxnet.base;

import android.content.Context;

import com.chenzj.baselibrary.base.rxnet.client.Rx;
import com.chenzj.baselibrary.base.rxnet.interceptor.RequestInterceptor;
import com.chenzj.baselibrary.base.rxnet.interceptor.ResponseInterceptor;
import com.chenzj.baselibrary.base.rxnet.utils.CookieUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by hp on 2017/5/20.
 * OKHttp构造类
 */
public class BaseOKHttp {

    /**
     * 参数和文件
     * @param context
     * @param map
     * @param list
     * @return
     */
    public static OkHttpClient getOk(Context context, HashMap<String, Object> map,List<MultipartBody.Part> list) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)    //读取超时
                .writeTimeout(10, TimeUnit.SECONDS)    //写入超时
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                .pingInterval(10, TimeUnit.SECONDS)    //websocket 轮训间隔
                .cookieJar(CookieUtils.jar)
                //--------------------请求拦截------！！！！！！！！！！！！1
                .addInterceptor(new RequestInterceptor<Map<String, Object>,List<MultipartBody.Part>>(map, list))
                .addInterceptor(new ResponseInterceptor());
                //--------------------缓存拦截------！！！！！！！！！！！！1
//                .addNetworkInterceptor(new CaCheInterceptor())
//                .addInterceptor(new CaCheInterceptor())
                //--------------------缓存路径------！！！！！！！！！！！！1
//                .cache(new Cache(context.getExternalCacheDir(), 20 * 1024));    //设置缓存文件以及缓存大小，单位bytes
        if (Rx.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }

    /**
     * 只包含参数
     * @param context
     * @param map
     * @return
     */
    public static OkHttpClient getOk(Context context, HashMap<String, Object> map) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)    //读取超时
                .writeTimeout(10, TimeUnit.SECONDS)    //写入超时
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                .pingInterval(10, TimeUnit.SECONDS)    //websocket 轮训间隔
                .cookieJar(CookieUtils.jar)
                //--------------------请求拦截------！！！！！！！！！！！！1
                .addInterceptor(new RequestInterceptor<Map<String, Object>,List<MultipartBody.Part>>(map))
                .addInterceptor(new ResponseInterceptor());
        //--------------------缓存拦截------！！！！！！！！！！！！1
//                .addNetworkInterceptor(new CaCheInterceptor())
//                .addInterceptor(new CaCheInterceptor())
        //--------------------缓存路径------！！！！！！！！！！！！1
//                .cache(new Cache(context.getExternalCacheDir(), 20 * 1024));    //设置缓存文件以及缓存大小，单位bytes

        if (Rx.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }

    /**
     * 单独使用
     * @return
     */
    public static OkHttpClient getOk(){
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(10, TimeUnit.SECONDS)    //读取超时
                .writeTimeout(10, TimeUnit.SECONDS)    //写入超时
                .connectTimeout(10, TimeUnit.SECONDS)//连接超时
                .pingInterval(10, TimeUnit.SECONDS);    //websocket 轮训间隔
        if (Rx.DEBUG) {
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        return builder.build();
    }
}
