## LifecycleObserver
订阅生命周期，不知道何时销毁

## LiveData 			
只有在`START`、`RESUME`的时候回通知`Activity`、`Fragment`更新数据，并且会在`onDestroy`的时候销毁数据,当`LiveData`添加`Observer`监听的时候也会封装成`LifecycleBoundObserver`的继承类，和`LifecycleObserver`一样被`LifecycleOwner.addObserver`

## AndroidViewModel 		
当`Activity`、`Fragment`销毁后也会销毁，但是如果是被重构的话，`ViewModel`不会销毁，还是能拿到之前的数据的
如果`Fragment`通过`getActivity`获取`Activity`的ViewModel的话，就可以互相通讯，不会新建一个`ViewModel`，而使用`Fragment`获取得话则会新建一个`ViewMode`


## LifecycleOwner、LifecycleObserver & LiveData

`LifecycleRegister.add` `LifecycleObserver`的时候会将`LifecycleObserver`封装成`ObserverWithState`

`LiveData.observer` `Observer`的时候会将`Observer`和`LifecycleOwner` 封装成`LifecycleBoundObserver`，而`LifecycleOwner`可以获取`LifecycleRegister`，
`LifecycleBoundObserver`又是继承`LifecycleObserver`，然后`LifecycleOwner.add` `LifecycleBoundObserver`，然后又被封装成`ObserverWithState`

`Activity`或`Fragment`会在每个生命周期都调用`LifecycleRegister`的`handleLifecycleEvent`，`handleLifecycleEvent`又会调用`moveToState`改变状态，
当`moveToState`判断是最后一个生命周期的时候就会执行`sync`，`sync`则会循环所有被封装成`ObserverWithState`的`dispatchEvent`，`dispatchEvent`则
调用`GenericLifecycleObserver`(继承`LifecycleObserver`)的`onStateChange`
- `LiveData`的`LifecycleBoundObserver`(继承`GenericLifecycleObserver`),则会通过持有的`LifecycleOwner`移除`Observer`
- `LifeObserver`会通过`Lifecycling.getCallback`转换成子类，不然没有`onStateChange`方法

## ViewModel
`ViewModelProviders.of`会构造全局唯一的`Factory`,并且会让`of`的`activity`或`Fragment`构造一个`ViewModelStore`对象，
接着又会`ViewModelStore.of`，其实就是将刚刚构造的`ViewModelStore`又拿出来构建`ViewProvider`，而`ViewProvider`只是类似装饰者
其内部操作的还是`Activity`或`Fragment`的`ViewModelStore`

`ViewModelProvider.get`的时候会判断`ViewModel`是否存在，不存在则重新构建并添加到`ViewModelStore`中，存在则直接取出来
所以`ViewModel`可以共享的原因是因为已经存在`Activity`或`Fragment`中的`ViewModelStore`集合里，通过`Activity`或者`Fragment`的引用
就可以获取到同一个ViewModel

`Activity`或`Fragment`只会在`onDestroy`里调用`ViewModelStore`的`clear`，`clear`其实就是调用集合中的所有`ViewModel`的`onCleared`，所以`onCleared`可以说对应组件的`onDestroy`生命周期