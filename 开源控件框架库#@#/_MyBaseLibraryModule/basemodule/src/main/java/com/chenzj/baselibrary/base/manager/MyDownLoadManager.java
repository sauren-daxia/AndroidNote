package com.chenzj.baselibrary.base.manager;

import android.content.Context;
import android.text.TextUtils;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import zlc.season.rxdownload2.RxDownload;
import zlc.season.rxdownload2.entity.DownloadEvent;
import zlc.season.rxdownload2.entity.DownloadFlag;

/**
 * Created by Jason Chen on 2017/9/1.
 */

public class MyDownLoadManager {
    private static MyDownLoadManager manager;
    private Context con;

    public static MyDownLoadManager getInstance(Context con) {
        if (manager == null) {
            synchronized (MyDownLoadManager.class) {
                if (manager == null) {
                    manager = new MyDownLoadManager();
                    manager.con = con;
                }
            }
        }
        return manager;
    }

    /**
     * 设置路径
     *
     * @param url  链接
     * @param name 重命名
     * @param path 存储路径
     */
    public void download(final String url, final String name, final String path,final OnDownloadCompleted onDownloadCompleted) {
        RxDownload.getInstance(con).
                receiveDownloadStatus(url)
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<DownloadEvent>() {
                    @Override
                    public void accept(@NonNull DownloadEvent event) throws Exception {
                        switch (event.getFlag()) {
                            case DownloadFlag.NORMAL:
                            case DownloadFlag.FAILED:
                            case DownloadFlag.DELETED:
                                //未下载
                                if (!TextUtils.isEmpty(path)) {
                                    downloadFile(url, name, path);
                                } else {
                                    downloadFile(url, name);
                                }
                                break;
                            case DownloadFlag.COMPLETED:
                                //下载完安装
                                if(onDownloadCompleted!=null){
                                    onDownloadCompleted.onCompleted();
                                }
                                break;
                        }
                    }
                });
    }


    /**
     * 不设置路径
     *
     * @param url  链接
     * @param name 重命名
     */
    private void downloadFile(String url, String name) {
        RxDownload.getInstance(con)
                .serviceDownload(url, name)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }

    /**
     * 设置路径
     *
     * @param url  链接
     * @param name 重命名
     * @param path 存储路径
     */
    private void downloadFile(String url, String name, String path) {
        RxDownload.getInstance(con)
                .serviceDownload(url, name, path)
                .observeOn(Schedulers.io())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {

                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(@NonNull Throwable throwable) throws Exception {

                    }
                });
    }


    public interface OnDownloadCompleted{
        void onCompleted();
    }
}
