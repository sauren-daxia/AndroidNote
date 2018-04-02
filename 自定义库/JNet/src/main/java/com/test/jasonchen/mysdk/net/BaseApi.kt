package com.test.jasonchen.mysdk.net

import com.test.jasonchen.mysdk.NetSDK
import com.test.jasonchen.mysdk.net.client.RetrofitClient
import com.test.jasonchen.mysdk.net.rx.RxAdapter
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

/**
 * Created by marti on 2018/3/4.
 */
abstract class BaseApi {
    /*每次子类实现会第一时间获取retrofit实例，这个retrofit实例的baseurl是默认的*/
    protected var retrofit = RetrofitClient.retrofit
        private set
    /*参数集合 只传参数不传文件的时候使用*/
    var paramsMap: MutableMap<String, Any> = mutableMapOf()
    /*文件集*/
    var fileList = BodyList()
    /*参数集，传了文件的时候使用*/
    var bodyMap = BodyList()
    /*
        BaseUrl类型，设计存在多BaseURL的情况
        默认BaseURL，如果使用者修改了baseURL
        则在修改的同时去修改Retrofit的BaseUrl
        然后重新获取一次修改后的retrofit变量
        因为Retrofit的BaseUrl是通过集合获取
        所以只会修改某个BaseApi实现类的请求，
        当新的实现类再请求时会用默认的BaseURL
    */
    var baseUrlType = NetSDK.baseUrl
        set(value) {
            if (value != field) {
                RetrofitClient.baseUrlType = value
                retrofit = RetrofitClient.retrofit
            }
        }

    /*
        implement like this:
                return retrofit.create(Request::class.java).home()

        explain:
                retrofit : This is the property of the class.
                Request  : This is the Retrofit required interface.
                home()   : This is request methos

        use    :
                post<Any>().toRx(RxAdapter(
                {
                    it ->
                        tips: it is Type Any or Other
                }
                ))

        PS :    more use please see see RxAdapter Constructor

     */
    abstract fun post(): Observable<*>
}

fun <T : Any> Observable<T>.toRx(rx: RxAdapter<T>) {
    this
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(rx)
}
