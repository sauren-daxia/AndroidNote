package com.nanbo.vocationalschools.utils;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by LG on 2016/10/21.
 * Tips:控制FloatingActionButton的显示和隐藏
 */
public class ToTopOnRecyclerView implements View.OnClickListener {
    private int showForPosition = 8;
    private int maxSmoothScrollPosition = 12;
    private View floatView;
    private RecyclerView recyclerView;
    private View.OnClickListener clickListener;

    public ToTopOnRecyclerView(View floatView, RecyclerView recyclerView, View.OnClickListener clickListener) {
        this.floatView = floatView;
        this.floatView.setOnClickListener(this);
        this.recyclerView = recyclerView;
        this.clickListener = clickListener;
        init();
    }

    public void init() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int oldX, oldY, mScrollX, mScrollY;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (mScrollX != dx || mScrollY != dy) {
                    int oldX = mScrollX;
                    int oldY = mScrollY;
                    mScrollX = dx;
                    mScrollY = dy;
                }
                if (oldY < mScrollY && floatView.getVisibility() != View.VISIBLE) {

                    ScaleAnimUtils.scaleShow(floatView);
                } else if (oldY > mScrollY && floatView.getVisibility() == View.VISIBLE) {
                    ScaleAnimUtils.scaleHide(floatView);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < 5; i++) {
            recyclerView.getLayoutManager().scrollToPosition(0);
        }
        ToTopBehavior.scaleHide(floatView);
        if (clickListener != null) {
            clickListener.onClick(v);
        }
    }
}
