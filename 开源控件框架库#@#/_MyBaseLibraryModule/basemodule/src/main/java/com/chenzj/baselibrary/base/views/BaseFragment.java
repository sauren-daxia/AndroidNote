package com.chenzj.baselibrary.base.views;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;

import com.chenzj.baselibrary.base.manager.ToastManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Administrator on 2017/7/20 0020.
 * 碎片基类
 */

public abstract class BaseFragment extends Fragment {
    protected View rootView;
    protected CompositeDisposable mDisposables = new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //友盟统计
//        MobclickAgent.onPageStart(this.getClass().getName()); //统计页面，"MainScreen"为页面名称，可自定义
    }

    @Override
    public void onPause() {
        super.onPause();
        //友盟统计
//        MobclickAgent.onPageEnd(this.getClass().getName());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }

    protected void showToast(String text) {
        ToastManager.getInstance().showToast(text);
    }



}
