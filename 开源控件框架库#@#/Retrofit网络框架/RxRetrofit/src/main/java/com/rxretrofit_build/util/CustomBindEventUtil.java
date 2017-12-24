package com.rxretrofit_build.util;

import android.support.annotation.NonNull;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.jakewharton.rxbinding.view.RxView;
import com.jakewharton.rxbinding.widget.RxCompoundButton;
import com.jakewharton.rxbinding.widget.RxTextView;
import com.jakewharton.rxbinding.widget.TextViewTextChangeEvent;

import java.util.concurrent.TimeUnit;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2016/12/16  13:31
 *
 * @author cree
 * @version 1.0
 */
public class CustomBindEventUtil {
    /**
     * 点击
     * @param view
     * @param viewCallBack
     */
    public static void click(@NonNull View view, BaseViewCallBack<Void> viewCallBack) {
        RxView.clicks(view)
                .throttleFirst(2, TimeUnit.SECONDS)
                .subscribe(viewCallBack);
    }

    /**
     * 长按
     * @param view
     * @param viewCallBack
     */
    public static void longClick(@NonNull View view, BaseViewCallBack<Void> viewCallBack) {
        RxView.longClicks(view)
                .subscribe(viewCallBack);
    }

    /**
     * checkbox改变事件绑定
     * @param view
     * @param viewCallBack
     */
    public static void checkedChanges(@NonNull CompoundButton view, BaseViewCallBack<Boolean> viewCallBack) {
        RxCompoundButton.checkedChanges(view)
                .subscribe(viewCallBack);
    }

    /**
     * 文本输入框改变监听事件
     * @param view
     * @param viewCallBack
     */
    public static void textChangeEvents(@NonNull TextView view, BaseViewCallBack<String> viewCallBack) {
        RxTextView.textChangeEvents(view)
                .debounce(300, TimeUnit.MILLISECONDS)  //debounce:每次文本更改后有300毫秒的缓冲时间，默认在computation调度器
                .observeOn(AndroidSchedulers.mainThread())  //触发后回到Android主线程调度器
//                .filter(new Func1<TextViewTextChangeEvent, Boolean>() {
//                    @Override
//                    public Boolean call(TextViewTextChangeEvent textViewTextChangeEvent) {
//                        String key = textViewTextChangeEvent.text().toString().trim();
//                        return !TextUtils.isEmpty(key);
//                    }
//                })
                .map(new Func1<TextViewTextChangeEvent, String>() {
                    @Override
                    public String call(TextViewTextChangeEvent textViewTextChangeEvent) {
                        String key = textViewTextChangeEvent.text().toString().trim();
                        return key;
                    }
                })
                .subscribe(viewCallBack);
    }
}
