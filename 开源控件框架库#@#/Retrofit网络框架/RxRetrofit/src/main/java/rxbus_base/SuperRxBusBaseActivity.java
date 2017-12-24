package rxbus_base;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;

import com.rxretrofit_build.model.RxBusBean;

import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Jason Chen on 2017/2/18.
 */

public  class SuperRxBusBaseActivity extends Activity {
    private Subscription subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initRxBus(setOnNext());
    }

    private void initRxBus(final Action1<Object> onNext) {
        if (onNext != null) {
            subscription = RxBus
                    .getInstance()
                    .toObserverable()
                    .subscribeOn(Schedulers.io())
                    .unsubscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(onNext);

        }
    }

    private Action1<Object> setOnNext() {
        return new Action1<Object>() {
            @Override
            public void call(Object o) {
                if(o instanceof RxBusBean){
                    RxBusBean rxBusBean = (RxBusBean) o;
                    if(!TextUtils.isEmpty(rxBusBean.getRequestMark())){
                        doOnNext(rxBusBean);
                    }
                }
            }
        };
    }

    protected  void doOnNext(RxBusBean rxBusBean){

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }
}
