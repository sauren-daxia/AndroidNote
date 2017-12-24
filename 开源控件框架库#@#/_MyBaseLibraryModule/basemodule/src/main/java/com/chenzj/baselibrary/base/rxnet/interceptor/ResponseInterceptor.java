package com.chenzj.baselibrary.base.rxnet.interceptor;

import android.util.Log;

import com.chenzj.baselibrary.base.bean.BaseBean;
import com.chenzj.baselibrary.base.rxnet.base.BaseRetrofit;
import com.chenzj.baselibrary.project.url.RxRequest;
import com.google.gson.Gson;

import org.json.JSONException;

import java.io.EOFException;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Jason Chen on 2017/9/19.
 */

public class ResponseInterceptor implements Interceptor {
    //用来计算次数，如果连续重新请求3次都错误就不再执行了
    private int count = 0;

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response proceed = chain.proceed(request);
        MediaType mediaType = proceed.body().contentType();
        ResponseBody responseBody = proceed.body();
        long contentLength = responseBody.contentLength();
        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();
        if (!isPlaintext(buffer)) {
            return proceed;
        }

        /**
         * 这里是主要代码，先判断是否请求到了内容，有内容contentLength肯定不为0，然后解析出body，根据自己业务判断状态
         * TOKEN是否过期，如果过期会执行newToken请求一次最新的token，然后再把上一次的请求参数链接等再次请求一次
         * 就完成了无感的TOKEN失效刷新，并继续上次的请求，有待使用设计模式将代码规范化
         * PS：
         * 1、上面那堆代码我也不知道干嘛，可以不用管
         * 2、responseBody.string()执行了这句话之后，就不会返回数据到页面，因为流只能用一次，需要重新生成流
         *                  proceed.newBuilder().body(ResponseBody.create(mediaType, body)).build()
         */
        String body = responseBody.string();
        if (contentLength != 0) {
            int code;
            try {
                code = new Gson().fromJson(body, BaseBean.class).getCode();
            } catch (Exception e) {
                return proceed.newBuilder().body(ResponseBody.create(mediaType, body)).build();
            }
            //========================================重新获取token并重试
            if (code == 100) {
                try {
                    newToken();//同步获取token
                } catch (Exception e) {
                    e.printStackTrace();
                    return proceed.newBuilder().body(ResponseBody.create(mediaType, body)).build();
                }
                Request.Builder post = new Request.Builder()
                        .url(request.url().url())
                        .post(request.body());
                for (String name : request.headers().names()) {
                    //判断如果参数是token就用获取到的token赋值
                    if (name.equals("token")) {
                        post.addHeader(name, "TOKEN");
                    } else {
                        post.addHeader(name, request.header(name));
                    }

                    Log.d("OkHttp", "---------------------------response name = " + name + " value = " + request.header(name));
                }
                count++;
                if (count < 3) {
                    return chain.proceed(post.build());   //重试
                } else {
                    count = 0;
                    return proceed.newBuilder().body(ResponseBody.create(mediaType, body)).build();
                }
            }
        }
        return proceed.newBuilder().body(ResponseBody.create(mediaType, body)).build();
    }

    /**
     * 不知道判断啥，也不知道有啥用
     *
     * @param buffer
     * @return
     * @throws EOFException
     */
    private boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }

    /**
     * 同步获取token.登录时刷新状态
     *
     * @return
     * @throws IOException
     * @throws JSONException
     */
    private void newToken() throws IOException {
        //同步请求
        BaseBean<String> body = BaseRetrofit
                .getRetrofit()
                .create(RxRequest.class)
                .systemTime()
                .execute()
                .body();
        if (body.getCode() == 0) {
           //保存TOKEN
        }
    }
}
