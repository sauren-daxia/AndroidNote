package com.chenzj.baselibrary.base.rxnet.client;

import android.content.Context;
import android.util.Log;

import com.chenzj.baselibrary.base.bean.BaseBean;
import com.chenzj.baselibrary.base.manager.ToastManager;
import com.chenzj.baselibrary.base.rxnet.base.BaseObserver;

import io.reactivex.disposables.Disposable;

/**
 * Created by hp on 2017/5/22.
 * Observer实现类，实际数据处理在这里进行
 */
public abstract class RxObserver<T> extends BaseObserver<T> {

    int codes[] = {};       //需要弹Toast的code码总汇

    public RxObserver() {
    }

    public RxObserver(Context dialogContext, boolean isShowDialog) {
        super(dialogContext, isShowDialog);
    }

    @Override
    public void onNext(T t) {
        super.onNext(t);
        if (t instanceof BaseBean) {

            if (((BaseBean) t).getCode()!=200) {
                ToastManager.getInstance().showToast(((BaseBean) t).getMessage());
            }
            //在这里可以存储用户信息,Token,等数据

        }
        onSuccess(t);
    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
        Log.e("okhttp----error:", "onError:"+t.getMessage());
        onFailure(t);
        t.printStackTrace();
    }

    /**
     * 判断是否需要弹Toast
     *
     * @param code
     * @return
     */
    private boolean isShowToast(int code) {
        for (int i = 0; i < codes.length; i++) {
            if (codes[i] == code) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onSubscribe(Disposable d) {
        super.onSubscribe(d);
        onDisposable(d);
    }

    public abstract void onDisposable(Disposable d);

    public abstract void onSuccess(T t);

    public abstract void onFailure(Throwable e);

}
