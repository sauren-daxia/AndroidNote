package com.android.viewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;

/**
 * Created by 陈志坚 on 2016/8/25.
 */
public class BottomView extends ScrollView{
    public BottomView(Context context) {
        super(context);
    }

    public BottomView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public BottomView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                //当处于第二页的时候拦截外层ScrollView的touch事件
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
            case MotionEvent.ACTION_MOVE:
                float offY = ev.getY()- lastY;
                //处于第二页的时候，如果想滑到第一页的话，getScrollY()一直都是0的，向上滑动ScrollY才会变化
                //判断点击的位置减去滑动的位置，是向上还是向下，小于零下拉滑动，大于零上拉滑动。
                if(offY > 0  && getScrollY() == 0){
                    getParent().getParent().requestDisallowInterceptTouchEvent(false);
                }
                break;
        }
        return super.onTouchEvent(ev);
    }
}
