package com.nanbo.vocationalschools.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.View;

import com.nanbo.vocationalschools.R;


/**
 * Created by Jason Chen on 2017/12/7.
 */

public class ToTopBehavior extends CoordinatorLayout.Behavior<View> {

    NestedScrollView nScrollView;

    public ToTopBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View directTargetChild, @NonNull View target, int axes) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull View child, @NonNull View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed, int type) {

        if (dyConsumed > 0 && dyUnconsumed == 0) {
            //上滑中。。。
        }
        if (dyConsumed == 0 && dyUnconsumed > 0) {
            //到边界了还在上滑。。。
        }
        if (dyConsumed < 0 && dyUnconsumed == 0) {
            //下滑中。。。
        }
        if (dyConsumed == 0 && dyUnconsumed < 0) {
            //到边界了，还在下滑。。。
        }

        NestedScrollView nScrollView = coordinatorLayout.findViewById(R.id.nscroll);
        if (this.nScrollView == null) {
            this.nScrollView = nScrollView;
            this.nScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
                if (oldScrollY < scrollY && child.getVisibility() != View.VISIBLE) {

                    scaleShow(child);
                } else if (oldScrollY > scrollY && child.getVisibility() == View.VISIBLE) {
                    scaleHide(child);
                }
            });
        }
    }

    public static void scaleShow(View view) {
        ScaleAnimUtils.scaleShow(view);
    }

    public static void scaleHide(final View view) {
        ScaleAnimUtils.scaleHide(view);
    }
}
