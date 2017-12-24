package com.rxretrofit_build.net;

import android.content.Context;

import com.example.codeutils.utils.ImageUtils;
import com.rxretrofit_build.model.BaseApiService;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Jason Chen on 2017/2/18.
 */

public class UpLoadRequestBuild {

    /**
     * 创建上传的Request
     * @param fileList 文件路径集合
     * @return
     */
    public static Request getupLoadRequest(Context context,ArrayList<String> fileList){

        return builderRequest(builderMultipartBody(context,fileList));
    }

    /**
     * 创建下载的Request
     * @param url 链接
     * @return
     */
    public static Request getDownLoadRequest(String url){
        return new Request
                .Builder()
                .url(url)
                .build();
    }

    /**
     * 将图片设置到请求体重
     *
     * @param fileList
     */
    private static MultipartBody builderMultipartBody(Context context,ArrayList<String> fileList) {
        MultipartBody.Builder builder = new MultipartBody.Builder().setType(MultipartBody.FORM);
        for (int i = 0; i < fileList.size(); i++) {
            File imgFile = new File(fileList.get(i));
            if (imgFile.exists()) {
                //这里要判断是图片还是文件
                if(ImageUtils.isImage(imgFile)){
                    //获取缓存地址，压缩后的图片放到缓存地址
                    String dstPath = context.getCacheDir().getAbsoluteFile() + File.separator + imgFile.getName() + ".png";
                    //压缩
                    File newImaFile = new File(ImageUtils.compressImage(imgFile.getAbsolutePath(), dstPath, 30));
                    //判断压缩后文件是否存在
                    if(newImaFile.exists()){
                        //添加压缩文件到请求中
                        builder.addFormDataPart("file["+i+"]", newImaFile.getName(), RequestBody.create(MediaType.parse("image/png"), newImaFile));
                    }
                }else{

                }
            }
        }

        return builder.build();
    }

    /**
     * 配置OKHttp请求对象
     *
     * @param requestBody
     * @return
     */
    public static Request builderRequest(MultipartBody requestBody) {
        return new Request.Builder()
                .url(BaseApiService.IMGURL)//地址
                .addHeader("SOURCE", "android")
                .post(requestBody)//添加请求体
                .build();
    }
}
