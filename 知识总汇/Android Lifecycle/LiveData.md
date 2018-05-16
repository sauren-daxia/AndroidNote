### LiveData
- 能感知组件的生命周期
- 能被观察的数据存储结构
- 只在`START`、`RESAUME`时才通知组件更新数据
- 不用担心内存泄漏，参考上一条特点，并且会在组件`onDestroy`移除观察者
- 将LiveData声明到ViewModel里，不用担心组件`Recreated`后数据被销毁

### 用法
- 使用子类MutableLiveData
- 继承子类MutableLiveData

### 使用子类MutableLiveData案例
```java
class MyViewModel{
    //如果需要观察数据则在泛型写上类型
    val name = MutableLiveData<String>()
    //不需要观察数据，只做一个回调，则写Void
    val login = MutableLiveData<Void>()

    /**
    *一般会在ViewModel中配合MVVM写点击事件的
    *这里模拟点击登录好了
    */
    fun login(view: View){
        login.call()
    }

    /**
    *MVVM虽然需要写点击事件，但也可以直接调用某个方法
    *这里
    */
    fun rename(rename: String){
        //直接setValue是在主线程进行的，所以也只能在主线程setValue
        name.setValue(rename)
        //POST则可以在子线程，异步的话需要改变数据可以用POST，主线程就用setValue
        //name.postValue(rename)
    }
}

class MyActivity :  AppcompatActivity{
    override fun onCreate(savedInstanceState: Bundle?) {
        val viewModel = ViewModelProviders.of(this).get(MyViewModel::class.java)

        viewModel.login.observer(this,Observer{
            //当点击登录的时候会调用login.call()，这是通知观察者的意思
            //所以在这可以收到点击通知，并且只能在START、RESUME的时候收到
        })

        viewModel.name.observer(this,Observer{ it->rename
            //当name.setValue的时候，观察者也能收到通知
            //并且将最新设置的数据返回过来
        })
    }
}
```

### 使用继承子类MutableLiveData案例
- 暂无

### 特点分析
* 能感知组件的生命周期：
在`LiveData.observer(this,Observer{})`的时候，就已经持有`Activity`的引用，并且`Activity`或`Fragment`是直接或间接实现了`LifecycleOwner`接口的，所以可被观察生命周期。
在`observer()`内部，传入的`this`和`Observer`会被封装成`LifecycleBoundObserver`(`GenericLifecycleObserver`的子类，`GenericLifecycleObserver`则是`LifecycleObserver`的子类，`LifecycleObserver`可观察`LifecycleOwner`)，被封装后的`LifecycleBoundObserver`会被添加到传入的`this.addObserver()`中，所以当组件有生命周期的变化的时候`LiveData`能够感知生命周期
* 能被观察的数据存储结构
`LiveData`是观察者模式的结构，可以被观察变化，当数据发生变化的时候回通知观察者，并且一般是在主线程上通知的
* 只在`START`、`RESAUME`时才通知组件更新数据
`LiveData`的数据被更新的时候，还会判断当前组件的生命周期是否处于`START`、`RESAUME`中，否则暂缓更新，为了避免内存泄漏造成的组件被销毁后还更新数据
* 不用担心内存泄漏，参考上一条特点，并且会在组件`onDestroy`移除观察者
所有的`LifecycleObserver`的子类其实存放于组件变量的`Lifecycle`子类`LifecycleRegister`中，每当生命周期发生变化的时候就会调用`mLifecycleRegistry.handleLifecycleEvent()`更新当前状态并通知观察者当前状态被改变了，当组件`onDestroy`的时候，`LifecycleRegister`会
==..................暂时理解不了==

