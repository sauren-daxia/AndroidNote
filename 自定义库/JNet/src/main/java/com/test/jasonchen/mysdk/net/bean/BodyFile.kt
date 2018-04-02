package com.test.jasonchen.mysdk.net.bean

import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File

/**
 * Created by marti on 2018/3/4.
 * 文件实体类
 */

/**
 * @param key 键值对的key
 * @param filePath 文件路径
 * @param type 文件类型
 */
class BodyFile(val key: String, filePath: String, type: Types = Types.FORM_DATA) {
    var part: MultipartBody.Part

    init {
        var file = File(filePath)
        var body = RequestBody.create(MediaType.parse(type.type), file)
        part = MultipartBody.Part.createFormData(key, file.getName(), body)
    }
}

