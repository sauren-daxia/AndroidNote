package rxretrofit_base.net;

import android.app.Activity;
import android.content.Context;

import com.example.codeutils.utils.FileUtils;
import com.rxretrofit_build.net.LifeCallBack;
import com.rxretrofit_build.net.RetrofitClient;
import com.rxretrofit_build.net.RxSubscriber;
import com.rxretrofit_build.net.UpLoadRequestBuild;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Title:
 * Description:用于构建请求
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/29  12:19
 *
 * @author cree
 * @version 1.0
 */
public class RxRequest {

    //--------------------------请求数据--------------------------------

    public static <T> void request(Boolean isShow, Observable<T> observable, RxSubscriber<T> subscriber) {
        subscriber.setShowProgress(isShow);
        if (subscriber.getContext() instanceof LifeCallBack) {
            LifeCallBack lifeCallBack = (LifeCallBack) subscriber.getContext();
            lifeCallBack.setObserveble(observable, subscriber);
        }
//        Observable<Object> objectObservable = observable.flatMap()
        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }

    public static RetrofitClient getClient(Context context, HashMap<String, Object> mapData) {
        return RetrofitClient.getInstance(context).setHashMap(mapData);
    }


    //---------------------------图片上传相关---------------------------------

    private final static OkHttpClient client = new OkHttpClient();

    public static void upLoadImage(boolean isShow, final Context context, List<String> imgList, final RxSubscriber subscriber) {
        //手动调用
        subscriber.setShowProgress(isShow);
        subscriber.onStart();
        //发起请求
        client.newCall(UpLoadRequestBuild.getupLoadRequest(context,(ArrayList<String>) imgList)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onCompleted();
                        try {
                            subscriber.onNext(response.body().string());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });
    }

    //----------------------------下载文件-------------------------
    public void downLoadFile(boolean isShow, final Context context, final File dstfile, String url, final RxSubscriber subscriber) {
        //手动调用
        subscriber.setShowProgress(isShow);
        subscriber.onStart();

        client.newCall(UpLoadRequestBuild.getDownLoadRequest(url)).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onError(e);
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                //将输入流写入文件，如果没有这个类，请自行写
                final boolean isSuccess = FileUtils.writeFileFromIS(dstfile, response.body().byteStream(), false);
                ((Activity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        subscriber.onCompleted();
                        subscriber.onNext(isSuccess);//将是否写入成功传递出去。或者将原数据传递出去。有待需求
                    }
                });
            }
        });
    }
}
