package com.test.jasonchen.mysdk.net.rx

import io.reactivex.disposables.Disposable

/**
 * Created by marti on 2018/3/4.
 * Observer 适配类，主要简化构造
 */

class RxAdapter<T> : BaseRx<T> {
    lateinit private var onSuccess: (T) -> Unit
    lateinit private var onSubscribe: (Disposable) -> Unit
    lateinit private var onError: (Throwable) -> Unit

    private constructor():super()

    constructor(onSuccessBuild: (T) -> Unit) : this() {
        this.onSuccess = onSuccessBuild
    }

    constructor(onSuccessBuild: (T) -> Unit, onErrorBuild: (Throwable) -> Unit) : this() {
        this.onSuccess = onSuccessBuild
        this.onError = onErrorBuild
    }

    constructor(onSuccessBuild: (T) -> Unit, onErrorBuild: (Throwable) -> Unit, onSubscribeBuild: (Disposable) -> Unit) : this() {
        this.onSuccess = onSuccessBuild
        this.onError = onErrorBuild
        this.onSubscribe = onSubscribeBuild
    }

    override fun onSuccess(data: T) {
        this?.onSuccess?.invoke(data)
    }

    override fun onFailure(e: Throwable) {
        this?.onError?.invoke(e)
    }

    override fun onDisposable(d: Disposable) {
        this?.onSubscribe?.invoke(d)
    }
}