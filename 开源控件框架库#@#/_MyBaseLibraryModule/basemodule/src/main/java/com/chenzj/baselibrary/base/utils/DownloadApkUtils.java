package com.chenzj.baselibrary.base.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.chenzj.baselibrary.base.manager.MyDownLoadManager;
import com.chenzj.baselibrary.base.views.dialog.ReusableDoubleSelectDialog;

import java.io.File;

import zlc.season.rxdownload2.RxDownload;

/**
 * Created by Jason Chen on 2017/9/12.
 */

public class DownloadApkUtils {
    public static void download(final Context con, final String url,final String content) {
        MyDownLoadManager.getInstance(con).download(url, "xiaofeixia.apk", null, new MyDownLoadManager.OnDownloadCompleted() {
            @Override
            public void onCompleted() {
                final ReusableDoubleSelectDialog dialog = new ReusableDoubleSelectDialog();
                dialog.show(((AppCompatActivity) con).getSupportFragmentManager(),
                        content,
                        "暂不安装",
                        "去安装",
                        null, new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                dialog.dismiss();
                                installApk(con, url);
                            }
                        }, true);
            }
        });
    }

    private static void installApk(Context con, String url) {
        File[] files = RxDownload.getInstance(con).getRealFiles(url);
        File file = null;
        if (files != null) {
            file = files[0];
        } else {
            //这个是错误的，99.999999%不会走这里
            file = new File(url);
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Uri contentUri = FileProvider.getUriForFile(con, "com.weiyin.flyman.fileprovider",
                    file);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        if (con.getPackageManager().queryIntentActivities(intent, 0).size() > 0) {
            con.startActivity(intent);
        }
    }
}
