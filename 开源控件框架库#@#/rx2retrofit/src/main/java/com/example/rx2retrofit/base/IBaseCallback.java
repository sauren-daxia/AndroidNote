package com.example.rx2retrofit.base;

import io.reactivex.disposables.Disposable;

/**
 * Created by hp on 2017/5/23.
 */
public interface IBaseCallback {
      void onDisposable(Disposable d);
      void onFailure(Throwable e);
}
