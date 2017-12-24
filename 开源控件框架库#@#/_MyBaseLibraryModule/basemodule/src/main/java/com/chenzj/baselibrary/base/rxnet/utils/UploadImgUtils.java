package com.chenzj.baselibrary.base.rxnet.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.chenzj.baselibrary.base.bean.BaseBean;
import com.chenzj.baselibrary.base.bean.UploadPhotoBean;
import com.chenzj.baselibrary.base.manager.ToastManager;
import com.chenzj.baselibrary.base.rxnet.client.RxObserver;
import com.chenzj.baselibrary.base.rxnet.client.RxRetrofit;
import com.chenzj.baselibrary.project.url.RxRequest;
import com.nanchen.compresshelper.CompressHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Jason Chen on 2017/9/1.
 */

public class UploadImgUtils {

    /**
     * 创建一个文件对象
     *
     * @param name
     * @param type
     * @param path
     * @return
     */
    public static UploadPhotoBean createUploadPhotoBean(String name, UploadPhotoBean.MediaType type, String path) {
        UploadPhotoBean bean = new UploadPhotoBean();
        bean.setMediaType(type);
        bean.setFileName(name);
        bean.setPath(path);
        return bean;
    }

    /**
     * 获取回调图片
     *
     * @param
     */
    public static void getPhoto(final Context con, final OnPhotoResultListener listener, UploadPhotoBean... beans) {
        updateImg(con, listener, beans);
    }

    /**
     * 上传图片
     */
    private static void updateImg(final Context con, final OnPhotoResultListener listener, final
    UploadPhotoBean... beans) {
        if (beans == null || beans.length < 1) {
            ToastManager.getInstance().showToast("请选择图片");
            return;
        }
        Observable.create(new ObservableOnSubscribe<UploadPhotoBean>() {
            @Override
            public void subscribe(ObservableEmitter<UploadPhotoBean> e) throws Exception {

                /**
                 * 判断是否图片类型，如果是就压缩，不是则直接传递
                 */
                for (UploadPhotoBean bean : beans) {
                    if (UploadPhotoBean.MediaType.JPG.getType().equals(bean.getMediaType())
                            || UploadPhotoBean.MediaType.PNG.getType().equals(bean.getMediaType())) {
                        bean.setFile(photoCompress(con, bean.getPath()));
                        e.onNext(bean);
                        continue;
                    }
                    e.onNext(bean);
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .subscribe(new Observer<UploadPhotoBean>() {
                    private List<UploadPhotoBean> list = new ArrayList<>();

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UploadPhotoBean bean) {
                        list.add(bean);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        updataToService(list, listener);
                    }
                });

    }

    /**
     * 压缩图片
     *
     * @param con
     * @param path
     * @return
     */
    public static File photoCompress(Context con, String path) {
        File png = new CompressHelper.Builder(con)
                .setQuality(80)    // 默认压缩质量为80
                .setCompressFormat(Bitmap.CompressFormat.PNG) // 设置默认压缩为jpg格式
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(new File(path));
        return png;
    }

    /**
     * 上传图片给PHP
     *
     * @param files
     */
    private static void updataToService(final List<UploadPhotoBean> files, final OnPhotoResultListener listener) {
        List<MultipartBody.Part> parts = toBody(files);

        RxRetrofit.request(RxRetrofit.create(new HashMap<String, Object>(), RxRequest.class).uploadFile(parts), new RxObserver<BaseBean>() {
            @Override
            public void onDisposable(Disposable d) {

            }

            @Override
            public void onSuccess(BaseBean o) {
                if (o.getCode() == 0) {

                    //把本地图片和网络图片一起发过去，但只加载本地图片
                    if (listener != null) {
                        listener.onPhoto(o.getData());
                    }
                }
                deleteAllCacheFile(files); //如果返回来的链接不为空则删除压缩缓存路径
                ToastManager.getInstance().showToast(o.getMessage());
            }

            @Override
            public void onFailure(Throwable e) {
                deleteAllCacheFile(files);
                ToastManager.getInstance().showToast(e.getMessage());
            }

        });
    }



    /**
     * 删除所有缓存文件
     */
    private static void deleteAllCacheFile(List<UploadPhotoBean> files) {
        for (UploadPhotoBean bean : files) {
            if (bean.getFile() != null && bean.getFile().exists()) {
                bean.getFile().delete();
            }
        }
    }

    /**
     * file转MultipartBody
     *
     * @param list
     * @return
     */
    private static List<MultipartBody.Part> toBody(List<UploadPhotoBean> list) {
        List<MultipartBody.Part> bodys = new ArrayList<>();
        for (UploadPhotoBean bean : list) {
            RequestBody requestBody = RequestBody.create(MediaType.parse(bean.getMediaType()), bean.getFile());
            MultipartBody.Part part = MultipartBody.Part.createFormData(bean.getFileName(), bean.getFile().getName(), requestBody);
            bodys.add(part);
        }
        return bodys;
    }

    public interface OnPhotoResultListener<T> {
        void onPhoto(T photo);
    }
}
