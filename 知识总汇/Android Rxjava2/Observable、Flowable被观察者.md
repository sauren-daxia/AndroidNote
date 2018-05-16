## Observable简介
就是字面意思，被观察者，在Rxjava中被观察者通常是用来发送事件的，比如老板需要秘书处理一份文件，比如顾客点了一份鸡腿堡

## Observable使用
```java
void testRx(){
	/**
         * 第一种，最基本创建被观察者
         * 在创建的过程中由自己控制事件发送，无限制
         * 可以发送三种类型的事件，
         * onNext        : 发送一个正常工作事件
         * ononComplete  : 发送一个事件发送完毕的通知，观察者收到后则不会再接收任何事件
         * onError       : 发送一个异常事件，通知观察者发送异常，观察者收到后则不会再接收任何事件
         * setDisposable :
         * setCancellable:
         * isDisposed    :
         * serialize     :
         * tryOnError    :
         */
     Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> it) throws Exception {
                it.onNext(1);//发送一个正常工作事假
                it.onComplete();//告诉观察者事件已经发送完，后面有事件也不处理
                it.onError(new Exception());//告诉观察者这里出现了异常，后面有事件也不处理
            }
        });
}
```

## 订阅事件
当我们定义好被观察者，那么总得有个观察者吧，没人观察你，谁知道你干嘛。
通过Observable.subscribe()方法就可以订阅，不懂什么是观察者Observer的先看看Observer的文档简介
```java
void testRx(){
 Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> it) throws Exception {
                it.onNext(1);//发送一个正常工作事假
                it.onComplete();//告诉观察者事件已经发送完，后面有事件也不处理
                it.onError(new Exception());//告诉观察者这里出现了异常，后面有事件也不处理
            }
        }).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
		//当事件观察者主动不想处理事件的时候，可以使用Disposable切断与被观察者的联系
                //但是观察者依然会执行自己的事件发送，只是观察者不再收到事件，常用与避免内存泄漏
            }

            @Override
            public void onNext(Integer integer) {
		//当观察者每发送一个OnNext事件的时候会在这里收到通知
            }

            @Override
            public void onError(Throwable e) {
		//当观察者发送一个OnError事件的时候会在这里收到通知
            }

            @Override
            public void onComplete() {
		//当被观察者发送onComplete的时候会在这里收到通知
            }
        });
}
```

## Actions
标准的订阅的话，会有四个回调事件：`onSubcribe()`、`onNext()``onCompleted()`、`onError()`，但是一般不会在四个事件里面都有操作，可能只在`onNext()`和`onError()`里面操作，那么也得订阅四个事件的话，显得太啰嗦，所以`Rx`也有自己的`Action`事件的适配器
#### Consumer
```java
/**
* subscribe有多个重载方法，常用的有
* subscribe(onNext: Consumer)
* subscribe(onNext: Consumer, onError: Consumer)
* subscribe(onNext: Consumer, onError: Consumer, onCompleted: Consumer)
* subscribe(onNext: Consumer, onError: Consumer, onCompleted: Consumer, onSubscribe: Consumer)
*/
private fun rx(){
    Observable.create<String>({
      it.onNext("hello")
    })
        .subscribe{
            //onNext
        }
    Observable.create<String>({
      it.onNext("hello")
    })
        .subscribe({
            //onNext
        }, {
            //onError
        }) 
    Observable.create<String>({
      it.onNext("hello")
    })
        .subscribe({
            //onNext
        }, {
            //onError
        }, {
            //onCompleted
        })
    
    Observable.create<String>({
        it.onNext("hello")
    })
        .subscribe({
            //onNext
        }, {
            //onError
        }, {
            //onCompleted
        }, {
            //onSubscribe
        })
}
```

## Flowable和Observeable
使用`Observable`和`Observer`进行事件流操作的时候，事件少于1K的话基本不会造成OOM，但是超过后就很容易OOM，比如数据库操作，IO操作等。使用`Flowable`就可以解决这个问题，`Flowable`和`Observable`使用方式一样

## BackPressure(背压)
背压只有使用`Flowable`的时候才用到，因为事件过多的话，发送的频率高于处理的能力的话，就会一直堆积着，`BackpressureStrategy`提供了几种策略来解决上游发送频率的问题
- `BackpressureStrategy.MISSING`
上下游流量不均匀的时候会抛出异常
- `BackpressureStrategy.ERROR`
上下游流量不均匀的时候会抛出异常
- `BackpressureStrategy.BUFFER`
与Observable并没有什么不同，如果不处理的话还是会OOM
- `BackpressureStrategy.DROP`
提供一个128长度的事件池，直到每处理一个才把最新的一个存进来，在事件池满时发送的事件全部会丢失
- `BackpressureStrategy.LATEST`
每进入一个就把第一个丢弃掉，最终只会保存最新最新的128事件，比如1-999，只会保存999之前的事件

