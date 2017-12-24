package com.chenzj.baselibrary.base.views;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.chenzj.baselibrary.base.manager.ToastManager;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by Jason Chen on 2017/7/29.
 */

public abstract class BaseDialog extends DialogFragment {
    protected int gravity = Gravity.CENTER;
    protected int width = WindowManager.LayoutParams.WRAP_CONTENT, height = WindowManager.LayoutParams.WRAP_CONTENT;
    protected View rootView;
    protected boolean isEnableCancel = true;
    protected CompositeDisposable mDisposables=new CompositeDisposable();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rootView = LayoutInflater.from(context).inflate(getRootView(), null);
        width = getWidth();
        height = getHeight();
        gravity = getGravity();
        initView();

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        setAnim();
        return rootView;
    }

    protected abstract int getRootView();           //提供布局

    protected   int getWidth(){                     //设置宽
        return width;
    }

    protected   int getHeight(){                    //设置高
        return height;
    }

    protected  int getGravity(){            //设置对齐方式
        return gravity;
    }

    protected void setAnim(){}             //设置出场动画

    protected abstract void initView();             //初始化控件



    /**
     * 设置宽高，对齐方式，居中，上下左右，Dialog默认居中
     */
    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = gravity;
        params.width = width;
        params.height = height;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.setCancelable(isEnableCancel);
    }

    protected void showToast(String text){
        ToastManager.getInstance().showToast(text);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mDisposables.clear();
    }


}
