package com.example.cc.mvvmtest.net

import com.example.cc.mvvmtest.BuildConfig
import com.example.cc.mvvmtest.net.intercept.HeaderIntercept
import com.facebook.stetho.okhttp3.StethoInterceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object Client {

  fun <T> create(clazz: Class<T>) = retrofit.create(clazz)

  fun createOkHttp(): OkHttpClient {
    val builder = OkHttpClient.Builder()
    builder
        .writeTimeout(3000, TimeUnit.MILLISECONDS)
        .readTimeout(3000, TimeUnit.MILLISECONDS)
        .connectTimeout(3000, TimeUnit.MILLISECONDS)
//      .cookieJar()
        .addInterceptor(HeaderIntercept())

    /**
     * 判断是否开启了Debug模式
     */
    if (true) {
      builder.addInterceptor(StethoInterceptor())
      builder.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
    }
    return builder.build()
  }


  fun createRetrofit() =
      Retrofit
          .Builder()
          .baseUrl(BuildConfig.BASE_URL)
          .addConverterFactory(GsonConverterFactory.create())
          .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
          .client(okHttp)
          .build()
}