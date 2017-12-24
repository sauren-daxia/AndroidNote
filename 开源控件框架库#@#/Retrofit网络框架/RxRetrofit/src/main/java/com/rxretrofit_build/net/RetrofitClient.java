package com.rxretrofit_build.net;

import android.content.Context;

import com.rxretrofit_build.factory.GsonConverterFactory;
import com.rxretrofit_build.model.BaseApiService;
import com.rxretrofit_build.model.DevelopBean;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;


/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/28  09:41
 *
 * @author cree
 * @version 1.0
 */
public class RetrofitClient {

    private static Retrofit retrofit;
    private HashMap<String, Object> mapData;
    private static Context mContext;

    public static RetrofitClient getInstance(Context context) {
        mContext = context;
        return SingletonHolder.INSTANCE;
    }

    private static class SingletonHolder {
        private static RetrofitClient INSTANCE = new RetrofitClient(mContext);
    }


    public RetrofitClient() {
        this(null);
    }

    public RetrofitClient(Context context) {

        mapData = new HashMap<>();

        retrofit = new Retrofit
                .Builder()
                .client(genericClient())
                .baseUrl(BaseApiService.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
    }


    public OkHttpClient genericClient() {
        OkHttpClient.Builder interceptor = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.MILLISECONDS)    //链接超时
                .writeTimeout(5000, TimeUnit.MILLISECONDS)       //写入超时
                .readTimeout(5000, TimeUnit.MILLISECONDS)       //读取超时
                //--------------------去InterceptorManager里面修改------！！！！！！！！！！！！1
                .addInterceptor(new InterceptorManager<HashMap<String, Object>>(mapData) {});
        if (DevelopBean.isDevelop) {
            interceptor.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
        }

        return interceptor.build();
    }

    private BuilderReuqest builderReuqest;

    public void setBuilderReuqest(BuilderReuqest builderReuqest) {
        this.builderReuqest = builderReuqest;
    }

    public <T> T create(final Class<T> service) {
        if (service == null) {
            throw new RuntimeException("Api service is null!");
        }
        return retrofit.create(service);
    }

    public RetrofitClient setHashMap(HashMap<String, Object> hashMap) {
        mapData.clear();
        if (null == hashMap)
            return this;
        this.mapData.putAll(hashMap);
        return this;
    }
}
