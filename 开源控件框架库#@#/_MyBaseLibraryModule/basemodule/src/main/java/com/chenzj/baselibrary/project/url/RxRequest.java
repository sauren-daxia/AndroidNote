package com.chenzj.baselibrary.project.url;

import com.chenzj.baselibrary.base.bean.BaseBean;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * Created by Jason Chen on 2017/8/29.
 */

public interface RxRequest {
    /**
     * Retrofit同步获取系统时间
     *
     * @return
     */
    @POST(RxUrl.SYSTEM_TIME)
    Call<BaseBean> systemTime();

    @POST(RxUrl.SYSTEM_TIME)
    Observable<BaseBean> getTime();

    /**
     * 上传文件
     *
     * @param parts
     * @return
     */
    @Multipart
    @POST("")
    Observable<BaseBean<String>> uploadFile(@Part() List<MultipartBody.Part> parts);
}
