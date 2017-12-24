package com.chenzj.baselibrary.base.utils;

import android.content.Context;

import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Jason Chen on 2017/9/1.
 */

public class ImgLoadUtils {
    //加载标准全路径图片链接
    public static void frescoLoad(SimpleDraweeView img, String url) {
        img.setImageURI(url);
    }

    //加载本地资源图片
    public static void frescoLoadRes(Context con, SimpleDraweeView img, int res) {
        img.setImageURI("res://" + con.getPackageName() + "/" + res);
    }

    //加载本地路径图片
    public static void frescoLoadLocal(SimpleDraweeView img, String localUrl) {
        img.setImageURI("file://" + localUrl);
    }
}
