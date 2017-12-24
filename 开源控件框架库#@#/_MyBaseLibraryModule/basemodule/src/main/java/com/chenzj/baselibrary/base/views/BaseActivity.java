package com.chenzj.baselibrary.base.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.alibaba.android.arouter.launcher.ARouter;
import com.chenzj.baselibrary.base.manager.ActivityManager;
import com.chenzj.baselibrary.base.manager.ToastManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Administrator on 2017/7/20 0020.
 * activity 基类
 */

public abstract class BaseActivity extends AppCompatActivity {
    //Rx发射器管理
    protected CompositeDisposable mDisposables=new CompositeDisposable();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        //阿里路由
        ARouter.getInstance().inject(this);
        //取消头部
        if (getActionBar() != null) {
            getActionBar().hide();
            requestWindowFeature(Window.FEATURE_NO_TITLE);
        }
        //添加activity到管理
        ActivityManager.getInstance().addActivity(this);
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDisposables.clear();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //极光推送统计
//        JPushInterface.onResume(this);
        //友盟统计
//        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面(仅有Activity的应用中SDK自动调用，不需要单独写。"SplashScreen"为页面名称，可自定义)
//        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //极光推送统计
//        JPushInterface.onPause(this);
        //友盟统计
//        MobclickAgent.onPageEnd(this.getClass().getName()); // （仅有Activity的应用中SDK自动调用，不需要单独写）保证 onPageEnd 在onPause 之前调用,因为 onPause 中会保存信息。"SplashScreen"为页面名称，可自定义
//        MobclickAgent.onPause(this);
    }


    /**
     * 弹提示
     * @param text
     */
    protected void showToast(String text) {
        ToastManager.getInstance().showToast(text);
    }

}
