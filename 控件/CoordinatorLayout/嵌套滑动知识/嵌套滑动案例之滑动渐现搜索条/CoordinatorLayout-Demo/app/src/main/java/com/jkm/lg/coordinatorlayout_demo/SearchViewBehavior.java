package com.jkm.lg.coordinatorlayout_demo;

import android.content.Context;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Ligang on 2017/8/21.
 */

public class SearchViewBehavior extends CoordinatorLayout.Behavior<View>{

    public SearchViewBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, View child, View dependency) {
        Log.d("SearchViewBehavior", "dependency instanceof AppBarLayout:" + (dependency instanceof AppBarLayout));
        return  dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, View child, View dependency) {
        Log.d("SearchViewBehavior", "onDependentViewChanged");
        return super.onDependentViewChanged(parent, child, dependency);
    }
}
