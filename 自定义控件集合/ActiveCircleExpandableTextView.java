package com.nanbo.vocationalschools.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nanbo.vocationalschools.R;

/**
 * Created by CHEN on 2018/2/1.
 */

public class ActiveCircleExpandableTextView extends LinearLayout implements CompoundButton.OnCheckedChangeListener {
    private TextView contentTv;
    private CheckBox expandableCb;
    private View root;

    public ActiveCircleExpandableTextView(Context context) {
        super(context);
        init();
    }

    public ActiveCircleExpandableTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ActiveCircleExpandableTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        root = LayoutInflater.from(getContext()).inflate(R.layout.widget_active_circle_expandable_textview, this);
        contentTv = root.findViewById(R.id.tv_content);
        expandableCb = root.findViewById(R.id.cb_expandable);
        expandableCb.setOnCheckedChangeListener(this);

        root.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if(contentTv.getLineCount()<=3){
                expandableCb.setVisibility(GONE);
            }else{
                expandableCb.setVisibility(VISIBLE);
            }
        });
    }

    public void setText(CharSequence text) {
        contentTv.setText(text);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            expandableCb.setText("收起");
            contentTv.setMaxLines(Integer.MAX_VALUE);
        } else {
            expandableCb.setText("展开");
            contentTv.setMaxLines(3);
        }
        postInvalidate();
    }
}
