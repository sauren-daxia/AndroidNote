package com.bjesc.app.appraiser.utils;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.DeleteObjectRequest;
import com.alibaba.sdk.android.oss.model.DeleteObjectResult;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.bjesc.app.appraiser.AppraiserApplication;
import com.rxretrofit_build.model.BaseApiService;
import com.rxretrofit_build.util.CustomLog;

import rxretrofit_base.model.KeyBean;

/**
 * Created by lenovo on 2017/2/28.
 */

public class AliYunUtils {
    private static final String BUCKETNAME = "bjesc-img-upload";

    public static void aLiYunUpLoadFuncation(Context context,String sourceId,String fileName, String filePath, final OnALiYunUpLoadListener upLoadListener) {
        OSS oss = initALiYunUpLoad(context);

        String ObjectKey ="";           //图片名，由车源ID+图片ID+图片名组成

        //判断是否本地还是向上，本地的话前面加test
        if(BaseApiService.URL.equals("http://192.168.0.13/")){
            ObjectKey = "test/"+ CustomSharedPreferences.getString(context, KeyBean.CARID.valueOf(),KeyBean.FILE_NAME.valueOf())
                    +"/"+sourceId+"/"+fileName;
        }else if(BaseApiService.URL.equals("http://api.appraiser.bjesc.com/")){
            ObjectKey =  CustomSharedPreferences.getString(context, KeyBean.CARID.valueOf(),KeyBean.FILE_NAME.valueOf())
                    +"/"+sourceId+"/"+fileName;
        }
        // 构造上传请求
        PutObjectRequest put = new PutObjectRequest(BUCKETNAME, ObjectKey, filePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {
                if (upLoadListener != null) {
                    upLoadListener.onProgress(request, currentSize, totalSize);
                }
            }
        });

        OSSAsyncTask task = oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                if (upLoadListener != null) {
                    upLoadListener.onSuccess(request, result);
                }
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (serviceException != null) {
                    // 服务异常
                    CustomLog.d("___aliyun_ErrorCode = " + serviceException.getErrorCode());
                    CustomLog.d("___aliyun_RequestId = " + serviceException.getRequestId());
                    CustomLog.d("___aliyun_HostId = " + serviceException.getHostId());
                    CustomLog.d("___aliyun_RawMessage = " + serviceException.getRawMessage());
                }
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    CustomLog.e(clientExcepion);
                }
            }
        });
    }

    public static void deleteFile(Context context,String objectKey){
        OSS oss = initALiYunUpLoad(context);
        DeleteObjectRequest delete = new DeleteObjectRequest(BUCKETNAME, objectKey);
// 异步删除
        OSSAsyncTask deleteTask = oss.asyncDeleteObject(delete, new OSSCompletedCallback<DeleteObjectRequest, DeleteObjectResult>() {
            @Override
            public void onSuccess(DeleteObjectRequest request, DeleteObjectResult result) {
            }

            @Override
            public void onFailure(DeleteObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (serviceException != null) {
                    // 服务异常
                    CustomLog.d("___aliyun_ErrorCode = " + serviceException.getErrorCode());
                    CustomLog.d("___aliyun_RequestId = " + serviceException.getRequestId());
                    CustomLog.d("___aliyun_HostId = " + serviceException.getHostId());
                    CustomLog.d("___aliyun_RawMessage = " + serviceException.getRawMessage());
                }
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    CustomLog.e(clientExcepion);
                }
            }

        });
    }


    private static OSS initALiYunUpLoad(Context context) {
        ClientConfiguration conf = new ClientConfiguration();
        conf.setConnectionTimeout(15 * 1000); // 连接超时，默认15秒
        conf.setSocketTimeout(15 * 1000); // socket超时，默认15秒
        conf.setMaxConcurrentRequest(5); // 最大并发请求书，默认5个
        conf.setMaxErrorRetry(2); // 失败后最大重试次数，默认2次
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI9XJqRGkVmUBI", "htWsxzeDnX1m7KfhTsjaJ1cZQRlZpS");
        return new OSSClient(context, AppraiserApplication.ENDPOINT, credentialProvider, conf);
    }

    public interface OnALiYunUpLoadListener {
        void onProgress(PutObjectRequest request, long currentSize, long totalSize);

        void onSuccess(PutObjectRequest request, PutObjectResult result);


    }
}
