package com.test.jasonchen.mysdk.net.bean

/**
 * Created by marti on 2018/3/10.
 */
data class BaseBean<T>(
        var code: Int = 0,
        var data: T? = null,
        var message: String = ""
)