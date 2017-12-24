package com.chenzj.baselibrary.base.rxnet.interceptor;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.chenzj.baselibrary.base.bean.BaseBean;
import com.chenzj.baselibrary.base.rxnet.base.BaseRetrofit;
import com.chenzj.baselibrary.base.rxnet.client.Rx;
import com.chenzj.baselibrary.project.url.RxRequest;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by hp on 2017/5/22.
 */
public class RequestInterceptor<T extends Map<String, Object> ,M extends List<MultipartBody.Part>>  implements Interceptor {
    private T t;
    private M m;

    public RequestInterceptor(T t) {
        this.t = t;
    }

    public RequestInterceptor(T t, M m) {
        this.t = t;
        this.m = m;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        return chain.proceed(getBuilder(chain).build());
    }

    protected Request.Builder getBuilder(Chain chain) throws IOException {
        Request request = chain.request();
        String s = request.url().toString();    //获取URL
        if (null != t && t.size() > 0) {
            request = initBody(request);        //添加参数
        }



        Request.Builder builder = setHeader(request.newBuilder());  //设置头部



        if(m!=null && m.size()>0){
            MultipartBody.Builder multiBuilder = new MultipartBody.Builder().setType(MultipartBody.FORM);
            for (int i = 0; i < m.size(); i++) {
                multiBuilder.addPart(m.get(i));
            }
            builder.post(multiBuilder.build());
        }




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
            if(Rx.DEBUG &&this.t.get(body)!=null){
                Log.d("OkHttp", "---------------------------body name = "+body+", body value = "+this.t.get(body));
            }
        }

        request = request.newBuilder().post(builder.build()).build();
        return request;
    }

    /**
     * 设置头部
     * @param header
     * @return
     */
    protected  Request.Builder setHeader(Request.Builder header) throws IOException {
        //TODO 缺少一个判断TOKEN是否存在，不存在则同步请求TOKEN的方法

        //TOKEN不存在，同步请求
        if(judgeToken()){
            //获取成功
        }else{
            //获取不成功
        }


        header.addHeader("apptype",5+"");//app类型
        header.addHeader("clienttype",1+"");//终端类型
        header.addHeader("ua", Build.BRAND+Build.MODEL);//终端型号
        header.addHeader("Connection","close");


        return header;
    }

    /**
     * 这里同步获取TOKEN操作，要改就改这里
     * @return
     * @throws IOException
     */
    private boolean judgeToken() throws IOException {
        //判断TOKEN是否为空
        if(TextUtils.isEmpty("1")){

            //再次同步请求
            BaseBean<String> body = BaseRetrofit
                    .getRetrofit()
                    .create(RxRequest.class)
                    .systemTime()
                    .execute()
                    .body();

            //如果访问成功
            if(body.getCode() == 0){



                return true;
            }
        }
        return false;
    }
}
