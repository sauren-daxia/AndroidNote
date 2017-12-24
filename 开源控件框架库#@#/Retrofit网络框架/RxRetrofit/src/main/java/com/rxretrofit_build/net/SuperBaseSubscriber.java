package com.rxretrofit_build.net;

import rx.Subscriber;

/**
 * Title:
 * Description:用来给倒计时类处理回调用的
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2016/12/15  14:51
 *
 * @author cree
 * @version 1.0
 */
public abstract class SuperBaseSubscriber<T> extends Subscriber<T> {
    /**
     * 假如是倒计时,回传的时间是毫秒值
     * @param t
     */
    @Override
    public abstract void onNext(T t);
}
