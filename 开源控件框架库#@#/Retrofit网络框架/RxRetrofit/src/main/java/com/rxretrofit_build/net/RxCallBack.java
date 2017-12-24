package com.rxretrofit_build.net;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/28  17:26
 *
 * @author cree
 * @version 1.0
 */
public interface RxCallBack<T> {
    void onSuccess(T t);

    void onFailure(Throwable e);
}
