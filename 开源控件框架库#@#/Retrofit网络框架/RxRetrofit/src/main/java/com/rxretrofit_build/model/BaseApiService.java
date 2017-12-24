package com.rxretrofit_build.model;

/**
 * Title:
 * Description:对Retrofit的Api进行封装
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/28  09:48
 *
 * @author cree
 * @version 1.0
 */
public interface BaseApiService {
    String baseUrl = "http://ip.taobao.com/";
    //测试
//    public static final String URL = "http://192.168.0.240/v2/api/";
    //线上
    String URL= "http://api.v3.kexiansen.com/";
    //图片接口
    String IMGURL = "";


//    @POST("{url}")
//    Observable<ResponseBody> executePost(@Path("url") String url);
//
//
//    @POST("{url}")
//    Observable<ResponseBody> postMap(@Path("url") String url, @QueryMap Map<String, String> maps);
//    @GET("{url}")
//    <T> Observable<BaseResponse<T>> executeGet(
//            @Path("url") String url,
//            @QueryMap Map<String, String> maps
//    );
//
//
//    @POST("{url}")
//    Observable<ResponseBody> executePost(
//            @Path("url") String url,
//            //  @Header("") String authorization,
//            @QueryMap Map<String, String> maps);
//
//    @POST("{url}")
//    Observable<ResponseBody> json(
//            @Path("url") String url,
//            @Body RequestBody jsonStr);
//
//    @Multipart
//    @POST("{url}")
//    Observable<ResponseBody> upLoadFile(
//            @Path("url") String url,
//            @Part("image\"; filename=\"image.jpg") RequestBody requestBody);
//
//    @POST("{url}")
//    Call<ResponseBody> uploadFiles(
//            @Path("url") String url,
//            @Path("headers") Map<String, String> headers,
//            @Part("filename") String description,
//            @PartMap() Map<String, RequestBody> maps);
//
//    @Streaming
//    @GET
//    Observable<ResponseBody> downloadFile(@Url String fileUrl);
//
//
//    @GET("{url}")
//    <T>Observable<T> test(
//            @Path("url") String url
//    );

}
