package com.rxretrofit_build.util;

import java.util.HashMap;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/29  09:03
 *
 * @author cree
 * @version 1.0
 */
public class MapUtil {
    HashMap<String, Object> hashMap;

    public MapUtil() {
        hashMap = new HashMap<>();
    }

    public MapUtil put(String key, Object value) {
        if (VerifyUtil.isNullOrEmptyString(value + "")) {
            hashMap.put(key, "");
            return this;
        }

        hashMap.put(key, value);
        return this;
    }

    public HashMap<String, Object> build() {
        return hashMap;
    }
}
