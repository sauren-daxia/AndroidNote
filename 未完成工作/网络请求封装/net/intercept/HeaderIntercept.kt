package com.example.cc.mvvmtest.net.intercept

import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class HeaderIntercept : Interceptor {
  private val map: HashMap<String, Any> = HashMap()

  override fun intercept(chain: Interceptor.Chain): Response {
    return chain.proceed(initHeader(chain))
  }

  /**
   * 每个请求都添加头部
   */
  fun initHeader(chain: Interceptor.Chain): Request {
    val newBuilder = chain.request().newBuilder()

    for (it in map) {
      newBuilder.addHeader(it.key, it.value.toString())
    }

    return newBuilder.build()
  }

  /**
   * 添加一组头部
   */
  fun addHeaders(vararg header: Pair<String, Any>) {
    header.forEach {
      map[it.first] = it.second
    }
  }

  /**
   * 添加一组头部
   */
  fun addHeaders(map: Map<String, Any>) {
    this.map.putAll(map)
  }

  /**
   * 添加一个头部
   */
  fun addHeader(key: String, value: Any) {
    map.put(key, value.toString())
  }

  /**
   * 清除所有头部
   */
  fun clearHeaders() {
    map.clear()
  }

  /**
   * 移除一个头部
   */
  fun removeHeader(key: String) {
    map.remove(key)
  }

  /**
   * 获取存储头部的MAP
   */
  fun getHeaders() = map

}