### BaseFragment设计
- 增加处理按键监听
```Kotlin
/**
* Fragment中
* 返回true表示需要处理时间，false不处理
*/
fun onKeyDown(keyCode :Int, event:KeyEvent):Boolean{

}

/**
* Activity中
* 返回true表示需要处理时间，false不处理
*/
override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
    if ((keyCode == KeyEvent.KEYCODE_BACK)) {
      if(mianFragment.onKeyDown(keyCode,event)){
        //Fragment需要处理
        return true
      } 
    }
    return super.onKeyDown(keyCode, event)
  }
```