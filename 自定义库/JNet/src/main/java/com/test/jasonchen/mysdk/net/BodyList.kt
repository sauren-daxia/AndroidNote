package com.test.jasonchen.mysdk.net

import com.test.jasonchen.mysdk.net.bean.BodyFile
import okhttp3.MultipartBody
import okhttp3.RequestBody

/**
 * Created by marti on 2018/3/4.
 */
class BodyList : ArrayList<BodyFile>()

/**
 * 返回参数map，用于上传文件的同时上传参数
 */
fun BodyList.toBodyMap(): MutableMap<String, RequestBody> =
        let {
            val map = mutableMapOf<String, RequestBody>()
            this
                    .filter { !(it.part == null || it.part.headers() == null || it.part.body() == null) }
                    .forEach {
                        map[it.key] = it.part.body()
                    }
            map
        }

/**
 * 返回文件集合
 */
fun BodyList.toPartList(): MutableList<MultipartBody.Part> =
        let {
            var list = mutableListOf<MultipartBody.Part>()
            this
                    .filter { it.part != null }
                    .forEach {
                        list.add(it.part)
                    }
            list
        }
