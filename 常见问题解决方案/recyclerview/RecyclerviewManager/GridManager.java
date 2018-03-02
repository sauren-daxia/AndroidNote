package com.nanbo.vocationalschools.utils;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by CHEN on 2017/12/28.
 */

public class GridManager extends GridLayoutManager {
    private boolean notHead;
    private boolean notFoot;
    private int height;

    // RecyclerView高度随Item自适应
    public GridManager(Context context, int spanCount, @RecyclerView.Orientation int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    /**
     * 如果有，必须要穿，用来计算高度的，用在decoration
     *
     * @param notHead
     * @param notFoot
     * @param height
     */
    public void decoration(boolean notHead, boolean notFoot, int height) {
        this.notFoot = notFoot;
        this.notHead = notHead;
        this.height = height;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, final int widthSpec, final int heightSpec) {
        int height = 0;
        int childCount = getItemCount();


        height += this.height;
        height += childCount / getSpanCount() * this.height;
        for (int i = 0; i < childCount; i++) {
            View child = recycler.getViewForPosition(i);

            // measureChild(child, widthSpec, heightSpec);
            RecyclerView.MarginLayoutParams lp = (RecyclerView.MarginLayoutParams) child.getLayoutParams();
            // 奇怪,最近测试发现,上面的measureChild方法好像不太管用,换成下面
            int childHeightSpec = ViewGroup.getChildMeasureSpec(heightSpec, getPaddingTop() + getPaddingBottom(), lp.height);
            child.measure(widthSpec, childHeightSpec);

            if (i % getSpanCount() == 0) {

                int measuredHeight = child.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
                height += measuredHeight;
            }
        }

        if (notHead) {
            height -= this.height;
        }
        if (notFoot) {
            height -= this.height;
        }

        setMeasuredDimension(View.MeasureSpec.getSize(widthSpec), height);
    }
}
