package com.example.cc.mvvmtest.net

import com.example.cc.mvvmtest.BuildConfig
import com.example.cc.mvvmtest.net.intercept.HeaderIntercept
import okhttp3.Interceptor

/**
 * 结合这个库再加强封装：https://github.com/c297131019/JNetTest
 * 计划1 ：支持配置Retrofit的Interface
 * 计划2 ：支持配置BASEURL
 * 计划3 ：支持配置HTTP
 * 计划4 ：支持Cookie
 * 计划5 ：封装Retrofit文件上传
 * 计划6 ：封装Retrofit文件下载
 * 计划7 ：封装OKHttp文件上传
 * 计划8 ：封装OKHttp文件下载
 * 后续计划1：实现上传下载进度监听
 * 后续计划1：支持缓存
 * 后续计划1：
 * 后续计划1：
 * 后续计划1：
 * 后续计划1：
 * 后续计划1：
 */
class Net constructor(action: Net.() -> Unit) {
  init {
    action()
  }

  /**
   * TODO 技术问题暂无法实现
   * 添加Retrofit请求的Interface
   * 已标为private
   */
  private fun <T> retrofitAPI(action: () -> Class<T>) {
    val clazz = action()
    Client.create(clazz)
  }

  /**
   * TODO 技术问题暂无法实现
   * 配置BASE_URL
   */
  private fun baseUrl(action: () -> String) {
    action()
  }

  /**
   * 添加网络请求头部
   */
  fun headers(action: HashMap<String, Any>.() -> Unit) {
    val map = HashMap<String, Any>()
    action(map)
    okHttp.headers(map)
  }

  /**
   * 添加拦截器
   */
  fun intercepts(action: ArrayList<Interceptor>.() -> Unit) {
    val list = arrayListOf<Interceptor>()
    action(list)
    okHttp.interceptors().addAll(list)
  }
}

fun main(args: Array<String>) {
  Net {
    headers {
      this["version"] = BuildConfig.VERSION_CODE
      put("alias", "superMan")
    }
    intercepts {
      add(HeaderIntercept())
    }
  }
}