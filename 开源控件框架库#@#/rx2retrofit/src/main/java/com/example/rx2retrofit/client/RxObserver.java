package com.example.rx2retrofit.client;

import android.widget.Toast;

import com.example.rx2retrofit.base.BaseBean;
import com.example.rx2retrofit.base.BaseObserver;

import io.reactivex.disposables.Disposable;

/**
 * Created by hp on 2017/5/22.
 * Observer实现类，实际数据处理在这里进行
 */
public abstract class RxObserver<T> extends BaseObserver<T> {

    int codes[] = {};       //需要弹Toast的code码总汇

    @Override
    public void onNext(T t) {
        super.onNext(t);
        if (t instanceof BaseBean) {

            if (isShowToast(((BaseBean) t).getCode())) {
                Toast.makeText(con, ((BaseBean) t).getMessage(), Toast.LENGTH_SHORT).show();
            }

            //在这里可以存储用户信息,Token,等数据

        }
        onSuccess(t);
    }

    @Override
    public void onError(Throwable t) {
        super.onError(t);
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
