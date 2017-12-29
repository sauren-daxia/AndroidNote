package com.nanbo.vocationalschools.ui.views;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import java.util.HashMap;
import java.util.Map;


public class WrapContentHeightViewPager extends ViewPager {
    private Map<Integer, Integer> maps = new HashMap<Integer, Integer>();
    private int current;
    public WrapContentHeightViewPager(Context context) {
        super(context);
    }

    public WrapContentHeightViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = 0;
        if(maps.size() > current){
            height = maps.get(current + 1);
        }
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            if (h > height) height = h;
        }
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
    public void resetHeight(int current){
        this.current = current;
        if(maps.size() > current){
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) getLayoutParams();
            if(layoutParams == null){
                layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, maps.get(current + 1));
            }else{
                layoutParams.height = maps.get(current + 1);
            }
            setLayoutParams(layoutParams);
        }
    }
    public void calculate(int type, int height){
        maps.put(type, height);
    }
}