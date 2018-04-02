package com.test.jasonchen.mysdk.net.interceptor

import com.test.jasonchen.mysdk.NetSDK
import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Created by marti on 2018/3/4.
 */
object InterceptorFactory {
    //日志拦截
    fun loogerInterceptor() = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    //添加头部信息
    fun headersInterceptor() = Interceptor { chain ->
        chain.let {
            val request = chain.request().newBuilder()
            request.apply {
                NetSDK
                        .headers
                        .forEach {
                            var aFun = it.value as? () -> Any
                            if (aFun == null) {
                                addHeader(it.key, it.value?.toString())
                            } else {
                                addHeader(it.key, (it.value as? () -> Any)?.invoke()?.toString())
                            }
                        }
            }
            chain.proceed(request.build())
        }
    }
}

