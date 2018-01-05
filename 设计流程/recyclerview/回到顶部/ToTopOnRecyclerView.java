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
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(layoutManager.findFirstVisibleItemPosition() < 1 && floatView.getVisibility() == View.VISIBLE){
                    ToTopBehavior.scaleShow(floatView);
                    return;
                }
                if(dy > 0 && floatView.getVisibility() == View.VISIBLE){
                    ToTopBehavior.scaleShow(floatView);
                }else if(dy < 0 && floatView.getVisibility() != View.VISIBLE){
                    ToTopBehavior.scaleHide(floatView);
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
