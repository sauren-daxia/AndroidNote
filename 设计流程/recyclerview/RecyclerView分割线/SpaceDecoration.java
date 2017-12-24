package com.nanbo.vocationalschools.utils;

import android.graphics.Rect;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;

import static android.widget.LinearLayout.VERTICAL;

public class SpaceDecoration extends RecyclerView.ItemDecoration {

    private int widthSpace;
    private int heightSpace;
    private boolean mPaddingEdgeSide = true;
    private boolean mPaddingStart = true;
    private boolean notHead,notFoot;

    public SpaceDecoration(int space) {
        this(space,space);
    }


    public SpaceDecoration(int widthSpace, int heightSpace) {
        this.widthSpace = widthSpace;
        this.heightSpace = heightSpace;
    }

    public SpaceDecoration(int widthSpace, int heightSpace,boolean notHead,boolean notFoot) {
        this.widthSpace = widthSpace;
        this.heightSpace = heightSpace;
        this.notHead = notHead;
        this.notFoot = notFoot;
    }

    public void setPaddingEdgeSide(boolean mPaddingEdgeSide) {
        this.mPaddingEdgeSide = mPaddingEdgeSide;
    }

    public void setPaddingStart(boolean mPaddingStart) {
        this.mPaddingStart = mPaddingStart;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int spanCount = 0;
        int orientation = 0;
        int spanIndex = 0;

        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof StaggeredGridLayoutManager) {
            orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof GridLayoutManager) {
            orientation = ((GridLayoutManager) layoutManager).getOrientation();
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
            spanIndex = ((GridLayoutManager.LayoutParams) view.getLayoutParams()).getSpanIndex();
        } else if (layoutManager instanceof LinearLayoutManager) {
            orientation = ((LinearLayoutManager) layoutManager).getOrientation();
            spanCount = 1;
            spanIndex = 0;
        }

        if((position == 0 && notHead) ){
            return;
        }
        if((position == layoutManager.getItemCount()-1 && notFoot)){
            return;
        }

        if (orientation == VERTICAL) {
            float expectedWidth = (float) (parent.getWidth() - widthSpace * (spanCount + (mPaddingEdgeSide ? 1 : -1))) / spanCount;
            float originWidth = (float) parent.getWidth() / spanCount;
            float expectedX = (mPaddingEdgeSide ? widthSpace : 0) + (expectedWidth + widthSpace) * spanIndex;
            float originX = originWidth * spanIndex;
            outRect.left = (int) (expectedX - originX);
            outRect.right = (int) (originWidth - outRect.left - expectedWidth);

            if (position < spanCount && mPaddingStart) {
                outRect.top = heightSpace;
            }
            outRect.bottom = heightSpace;
            return;
        } else {
            float expectedHeight = (float) (parent.getHeight() - heightSpace * (spanCount + (mPaddingEdgeSide ? 1 : -1))) / spanCount;
            float originHeight = (float) parent.getHeight() / spanCount;
            float expectedY = (mPaddingEdgeSide ? heightSpace : 0) + (expectedHeight + heightSpace) * spanIndex;
            float originY = originHeight * spanIndex;
            outRect.bottom = (int) (expectedY - originY);
            outRect.top = (int) (originHeight - outRect.bottom - expectedHeight);
            if (position < spanCount && mPaddingStart) {
                outRect.left = widthSpace;
            }
            outRect.right = widthSpace;
            return;
        }
    }
}