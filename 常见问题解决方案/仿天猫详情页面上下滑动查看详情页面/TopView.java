package com.android.viewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by 陈志坚 on 2016/8/25.
 */
public class TopView extends ScrollView {
    public TopView(Context context) {
        super(context);
    }

    public TopView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TopView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public TopView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        //事件传递到事件拦截这里，需要阻止外层的ScrollView获取touch事件
       switch (ev.getAction()){
           case MotionEvent.ACTION_DOWN:
               getParent().getParent().requestDisallowInterceptTouchEvent(true);
               break;
       }
        lastY = ev.getY();
        return super.onInterceptTouchEvent(ev);
    }
    private float lastY;

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float offY = ev.getY() - lastY;
                //当上拉滑动的值小于0，并且将滑动的距离加上控件的高度跟Top的布局View的高度进行比较，如果为true，
                // 说明this已经将内容滑动到底，这时候就要释放外层ScrollView的touch事件，让外层滑动到第二页。
                /**
                 * 因为在外层的时候已经将Top的布局转换为View添加到this里面了，所以getChildAt()获取的就是那个View的控件
                 */
                if(offY < 0 && getScrollY()+getHeight() == getChildAt(0).getHeight()){
                    //释放外层ScrollView的touch事件
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }

                break;
        }
        return super.onTouchEvent(ev);
    }
}
