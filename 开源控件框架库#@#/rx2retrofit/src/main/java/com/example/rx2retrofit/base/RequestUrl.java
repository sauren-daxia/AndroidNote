package com.example.rx2retrofit.base;

import io.reactivex.Observable;
import retrofit2.http.POST;

/**
 * Created by hp on 2017/5/22.
 */
public interface RequestUrl {
    @POST("5185415ba171ea3a00704eed")
    Observable<BaseBean<Object>> Login();

}
