package com.test.jasonchen.mysdk.net.rx

import io.reactivex.Observer
import io.reactivex.disposables.Disposable


/**
 * Created by marti on 2018/3/4.
 * Observer 抽象类 主要处理一些基础数据
 */
abstract class BaseRx<T> : Observer<T> {


    override fun onComplete() {

    }

    override fun onSubscribe(d: Disposable) {

    }

    override fun onNext(t: T) {
        try {
            onSuccess(t)
        } catch (e: Exception) {
            onFailure(e)
        }

    }

    override fun onError(e: Throwable) {
        onFailure(e)
    }


    abstract fun onSuccess(data: T)
    abstract fun onFailure(e: Throwable)
    abstract fun onDisposable(d: Disposable)
}
