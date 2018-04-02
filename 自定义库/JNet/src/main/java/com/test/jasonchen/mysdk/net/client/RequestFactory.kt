package com.test.jasonchen.mysdk.net.client

import okhttp3.FormBody
import okhttp3.Request

/**
 * Created by marti on 2018/3/3.
 */
object RequestFactory {

    /**
     * POST方法
     */
    fun createPostRequest(url: String, params: MutableMap<String,Any>): Request {
        return FormBody.Builder().let { build ->
            params.forEach {
                build.add(it.key, it.value.toString())
            }
            Request.Builder().url(url).post(build.build()).build()
        }
    }

    /**
     * Get方法
     */
    fun createGetRequest(url: String, params: MutableMap<String,Any>): Request {
        return StringBuilder().let { sb ->
            sb.append(url)
            params.forEach {
                sb.append(it.key).append("=").append(it.value.toString()).append("&")
            }
            Request.Builder().url(sb.substring(0, sb.length - 1)).get().build()
        }
    }
}