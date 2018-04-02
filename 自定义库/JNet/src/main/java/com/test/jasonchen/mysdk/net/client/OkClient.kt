package com.test.jasonchen.mysdk.net.client

import com.test.jasonchen.mysdk.NetSDK
import com.test.jasonchen.mysdk.net.cookie.PersistentCookieJar
import com.test.jasonchen.mysdk.net.cookie.cache.SetCookieCache
import com.test.jasonchen.mysdk.net.cookie.persistence.SharedPrefsCookiePersistor
import com.test.jasonchen.mysdk.net.interceptor.InterceptorFactory
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

/**
 * Created by marti on 2018/3/3.
 */
object OkClient {
    val ok: OkHttpClient by lazy {
        var ok = OkHttpClient
                .Builder()
                //--------链接超时
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                //--------Https
                .followRedirects(true)  //重新定向，不知道什么
                .hostnameVerifier { hostname, session -> true }    //支持Https,支持任何类型所以返回true
                //--------常规设置
                .retryOnConnectionFailure(true)         //错误尝试重新连接,默认为true
                .cookieJar(PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(NetSDK.con)))
        if (NetSDK.DEBUG) {    //判断是Debug模式才添加
            ok.addInterceptor(InterceptorFactory.loogerInterceptor())
        }
        ok.addInterceptor(InterceptorFactory.headersInterceptor())
        NetSDK.intercepts.forEach {
            ok.addInterceptor(it)
        }
        ok.build()
    }
}