package com.rxretrofit_build.util;
import com.rxretrofit_build.net.SuperBaseSubscriber;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Title:
 * Description:倒计时类
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:2016/12/15  14:47
 *
 * @author cree
 * @version 1.0
 */
public class RxCountDown {
    private Subscription subscription;
    private SuperBaseSubscriber subscriber;
    private long time = 0;

    public RxCountDown(long time, SuperBaseSubscriber<Long> superBaseSubscriber) {
        this.time = time;
        this.subscriber = superBaseSubscriber;
    }

    public void startTime() {
        int time = (int) (this.time / 1000L);
        subscription = countdown(time)
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onStart() {
                        if (subscriber != null)
                            subscriber.onStart();
                    }

                    @Override
                    public void onCompleted() {
                        if (subscriber != null)
                            subscriber.onCompleted();
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (subscriber != null)
                            subscriber.onError(e);
                    }

                    @Override
                    public void onNext(Long integer) {
                        if (subscriber == null)
                            subscriber.onNext(integer * 1000);
                    }


                });
    }

    public void destroyTime() {
        if (subscription != null && !subscription.isUnsubscribed())
            subscription.unsubscribe();

    }

    /**
     * 传入的是秒  响应回传的单位也是秒
     *
     * @param time
     * @return
     */
    private Observable<Long> countdown(int time) {
        if (time < 0) time = 0;
        final long countTime = time;
        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long increaseTime) {
                        return countTime - increaseTime;
                    }
                })
                .take(time);

    }

}
