package com.test.jasonchen.mysdk

import android.content.Context
import okhttp3.Interceptor

/**
 * Created by marti on 2018/3/3.
 * 初始化类
 */

object NetSDK {
    internal lateinit var con: Context
    internal val headers: MutableMap<String, Any> = mutableMapOf()
    internal var baseUrl: String = ""
    internal val baseUrls: MutableList<String> = mutableListOf()
    internal val intercepts: MutableList<Interceptor> = mutableListOf()
    var DEBUG = false

    internal fun initialize(con: Context) {
        this.con = con
    }

    fun addHeaders(vararg pairs: Pair<String, Any>) {
        pairs.forEach { headers[it.first] = it.second }
    }

    fun baseUrl(url: String) {
        this.baseUrl = url
    }

    /**
     * 能力不够，做不到
     */
    private fun mutilBaseUrl(vararg urls: String) {
        urls.filter { !baseUrls.contains(it) }.forEach { baseUrls.add(it) }
    }

    fun addIntercept(vararg interceptor: Interceptor) {
        intercepts.filter { !intercepts.contains(it) }.forEach { intercepts.add(it) }
    }
}

fun NetSDK(con: Context, action: NetSDK.() -> Unit = {}) {
    NetSDK.initialize(con)
    action.invoke(NetSDK)
}

