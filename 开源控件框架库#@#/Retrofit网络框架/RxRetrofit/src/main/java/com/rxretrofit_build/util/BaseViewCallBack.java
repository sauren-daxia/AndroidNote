package com.rxretrofit_build.util;

import rx.functions.Action1;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2016/12/16  14:31
 *
 * @author cree
 * @version 1.0
 */
public abstract class BaseViewCallBack<T> implements Action1<T> {

    public abstract void onCallBack(T t);

    @Override
    public void call(T t) {
        onCallBack(t);
    }
}
