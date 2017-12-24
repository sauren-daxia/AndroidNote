package rxbus_base;


import com.rxretrofit_build.model.RxBusBean;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by Jason Chen on 2017/2/17.
 */

public class RxBus {
    public static RxBus rxBus;
    private Subject<Object, Object> mRxbusObserverable = new SerializedSubject<>(PublishSubject.create());

    public static RxBus getInstance() {
        if (rxBus == null) {
            synchronized (RxBus.class) {
                if (rxBus == null) {
                    rxBus = new RxBus();
                }
            }
        }
        return rxBus;
    }

    /**
     * 发送数据
     *
     * @param requestMark 标识码
     * @param o           数据
     */
    public void sendData(String requestMark, Object o) {
        mRxbusObserverable.onNext(new RxBusBean(requestMark, o));
    }

    public Observable<Object> toObserverable() {
        return mRxbusObserverable;
    }
}
