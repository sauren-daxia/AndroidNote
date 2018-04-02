package com.test.jasonchen.mysdk.net.client

import com.test.jasonchen.mysdk.NetSDK
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by marti on 2018/3/4.
 */
object RetrofitClient {
    /*用来储存不同baseUrl的Retrofit实例*/
    private var retrofitMap: MutableMap<String, Retrofit> = mutableMapOf()
    /* baseUrlType 其实也就是 baseUrl*/
    var baseUrlType = NetSDK.baseUrl

    var retrofit: Retrofit = Retrofit
            .Builder()
            .client(OkClient.ok)                //设置Okhttp
            .baseUrl(baseUrlType)                          //设置baseUrl
            .addConverterFactory(GsonConverterFactory.create())     //支持GSON
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create()).build() //支持Rxjava
            /*
                每次获取的时候先判断之前是否保存过当前设置的baseUrlType的实例
                如果保存过就直接获取，没有保存过先重新设置baseUrlType，再保存
                最后把BaseUrlType依然改修为默认的，避免下次请求错用其他BaseURL
            */
        get() {
            if (retrofitMap[baseUrlType] == null) {
                field = field.newBuilder().baseUrl(baseUrlType).build()
                retrofitMap[baseUrlType] = field
            } else {
                field = retrofitMap[baseUrlType]!!
            }

            baseUrlType = NetSDK.baseUrl
            return field
        }
        private set


}