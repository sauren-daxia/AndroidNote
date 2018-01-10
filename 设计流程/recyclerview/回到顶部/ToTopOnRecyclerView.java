package com.nanbo.vocationalschools.utils;

import android.support.v7.widget.LinearLayoutManager;
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
    private LinearLayoutManager layoutManager;
    public ToTopOnRecyclerView(View floatView, RecyclerView recyclerView) {
        this.floatView = floatView;
        this.floatView.setOnClickListener(this);
        this.recyclerView = recyclerView;
        layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
    }
    public void init(){
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            private int oldX,oldY,mScrollX,mScrollY;
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
        layoutManager.scrollToPosition(0);
        ToTopBehavior.scaleHide(floatView);
    }
}
