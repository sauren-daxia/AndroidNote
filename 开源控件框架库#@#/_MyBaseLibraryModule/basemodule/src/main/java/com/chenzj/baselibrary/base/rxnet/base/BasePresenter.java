package com.chenzj.baselibrary.base.rxnet.base;

import io.reactivex.disposables.CompositeDisposable;

/**
 * Created by hp on 2017/5/23.
 */
public abstract class BasePresenter<V extends IBaseView,M extends IBaseModel> {
    protected V view;
    protected M model;
    protected CompositeDisposable mDisposable;


    public void cancelSubscribe() {
        if(mDisposable!=null){
            mDisposable.clear();
        }
    }

    public BasePresenter(V v){

    }

    public BasePresenter(V v,M m) {
        this.view = v;
        this.model = m;
        mDisposable = new CompositeDisposable();
    }
}
