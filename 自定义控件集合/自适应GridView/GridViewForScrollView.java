package com.nanbo.vocationalschools.ui.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.GridView;
import android.widget.ListAdapter;

import com.nanbo.vocationalschools.R;

/**
 * Created by LuYu on 2016-10-26.9:46.
 */

public class GridViewForScrollView extends GridView {
    private int height;
    public GridViewForScrollView(Context context) {
        super(context);
    }

    public GridViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GridViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(
                Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
        height = expandSpec;
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
    public int getRealHeight(){
        return height;
    }
}
