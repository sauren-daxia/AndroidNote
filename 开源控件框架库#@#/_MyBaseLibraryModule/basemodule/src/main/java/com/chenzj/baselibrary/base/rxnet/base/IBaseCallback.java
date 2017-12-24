package com.chenzj.baselibrary.base.rxnet.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by hp on 2017/5/23.
 */
public interface IBaseCallback<T> {
      void onDisposable(Disposable d);
      void onFailure(Throwable e);
      void onSuccess(T t);
}
