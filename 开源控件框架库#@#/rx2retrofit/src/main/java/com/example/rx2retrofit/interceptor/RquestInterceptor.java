package com.example.rx2retrofit.interceptor;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hp on 2017/5/22.
 */
public class RquestInterceptor<T extends Map<String, Object>> implements Interceptor {
    private T t;

    public RquestInterceptor(T t) {
        this.t = t;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(getBuilder(chain).build());
    }

    protected Request.Builder getBuilder(Chain chain) {
        Request request = chain.request();
        String s = request.url().toString();    //获取URL
        if (null != t && t.size() > 0) {
            request = initBody(request);        //添加参数
        }

        Request.Builder builder = setHeader(request.newBuilder());  //设置头部

        return builder;
    }

    /**
     * 设置参数
     * @param request
     * @return
     */
    protected Request initBody(Request request) {
        FormBody.Builder builder = new FormBody.Builder();
        Set<String> strings = this.t.keySet();
        for (String body : strings) {
            builder.addEncoded(body, this.t.get(body) + "");
        }
        request = request.newBuilder().post(builder.build()).build();
        return request;
    }

    /**
     * 设置头部
     * @param header
     * @return
     */
    protected  Request.Builder setHeader(Request.Builder header) {
//        header.addHeader("","")
        return header;
    }
}
