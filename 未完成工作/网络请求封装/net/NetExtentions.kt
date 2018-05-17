package com.example.cc.mvvmtest.net

import com.example.cc.mvvmtest.net.intercept.HeaderIntercept
import okhttp3.OkHttpClient

val http by lazy { Client.create(API::class.java) }

val okHttp by lazy { Client.createOkHttp() }

val retrofit by lazy { Client.createRetrofit() }

/**
 * 获取OKHttp头部拦截
 */
fun OkHttpClient.getHeadersIntercept(): HeaderIntercept? {
  this.interceptors().forEach {
    if (it::class.java == HeaderIntercept::class.java) {
      return it as HeaderIntercept
    }
  }
  return null
}

/**
 * 获取OKHttp头部
 */
fun OkHttpClient.getHeaders(): HashMap<String, Any> {
  return getHeadersIntercept()!!.getHeaders()
}

/**
 * 为OKHttp添加头部
 */
fun OkHttpClient.headers(action: HashMap<String, Any>) {
  getHeaders().putAll(action)
}
