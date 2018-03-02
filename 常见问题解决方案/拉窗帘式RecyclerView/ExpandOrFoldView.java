package com.dsfa.reimbursement.ui.widget;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dsfa.common.utils.util.DisplayUtil;
import com.dsfa.reimbursement.R;


/**
 * Created by hp on 2017/4/13.
 */
public class ExpandOrFoldView extends RelativeLayout implements View.OnClickListener {
    private Context con;
    private TextView controlView;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RelativeLayout root;


    private CustomLinearLayoutManager layoutManager;        //自定义的Manager，为了可以禁止滑动
    private float recyclerHeight;
    boolean isShow;                 //判断上展开还是折叠，true = 折叠 false = 展开
    private int topArrowID;         //箭头向上的资源ID
    private int bottomArrowID;      //箭头向下的资源ID；
    private int dismissTime;        //隐藏的动画时间

    public ExpandOrFoldView(Context context) {
        super(context);
        this.con = context;
        init();
    }

    public ExpandOrFoldView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.con = context;
        init();
    }

    public ExpandOrFoldView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.con = context;
        init();
    }


    /**
     * 初始化控件
     */
    private void init() {

        View.inflate(con, R.layout.view_expandorfold, this);
        controlView = (TextView) findViewById(R.id.view_control);
        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        root = (RelativeLayout) findViewById(R.id.root);
    }

    /**
     * 必须调用的方法
     *
     * @param adapter
     * @param layoutManager
     * @param topArrowID    向上的箭头图片
     * @param bottomArrowID 向下的箭头图片
     */
    public void initView(RecyclerView.Adapter adapter, CustomLinearLayoutManager layoutManager, int topArrowID, int bottomArrowID) {
        this.adapter = adapter;
        this.layoutManager = layoutManager;
        this.topArrowID = topArrowID;
        this.bottomArrowID = bottomArrowID;
        if (layoutManager == null || adapter == null) {
            return;
        }

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        controlView.setOnClickListener(this);


        layoutManager.setScrollEnabled(false);  //收缩禁止滑动
        layoutManager.scrollToPosition(layoutManager.getItemCount() - 1);
    }


    @Override
    public void onClick(View v) {
        if (!isShow) {
            int densityHheight = DisplayUtil.getDensity_Height(getContext());
            int height = densityHheight - root.getHeight();
            int count = adapter.getItemCount();
            final View itemView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
            int totalHeight = itemView.getHeight() * count;
            if(totalHeight < height){
                recyclerHeight = totalHeight;
            }else{
                recyclerHeight = recyclerView.getHeight() + height;
            }
            show();
            changeArrowBottom();
        } else {
            //隐藏
            recyclerHeight = recyclerView.getHeight();
            changeArrowTop();
            dismiss();
        }
    }


    /**
     * 改变箭头方向为上
     */
    private void changeArrowTop() {
        controlView.setText("点击展开");
        if (topArrowID == 0) {
            return;
        }
        Drawable drawable = getResources().getDrawable(topArrowID);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        controlView.setCompoundDrawables(null, drawable, null, null);
    }

    /**
     * 改变箭头方向为下
     */
    private void changeArrowBottom() {
        controlView.setText("点击折叠");
        if (bottomArrowID == 0) {
            return;
        }
        Drawable drawable = getResources().getDrawable(bottomArrowID);
        /// 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        controlView.setCompoundDrawables(null, drawable, null, null);
    }


    /**
     * 显示
     */
    public void show() {
        isShow = true;
        final View itemView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
        if (itemView != null) {
            //不知道怎么说原理，反正就是获取到一个Item以外的RecyclerView的高度
            int translationY = (int) recyclerHeight - itemView.getHeight();
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, translationY);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //使用一个Item固定的高度加上以前被剪掉的高度
                    float v = itemView.getHeight() + (float) animation.getAnimatedValue();


                    ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                    layoutParams.height = (int) v;
                    //最后再设置到recyclerview，就会慢慢的减少高度了
                    recyclerView.setLayoutParams(layoutParams);


                    //最后不断的将Item滑动到第一个
                    layoutManager.setScrollEnabled(true);
                }
            });
            valueAnimator.addListener(listener);
            valueAnimator.setDuration(500).start();
        }
    }

    /**
     * 隐藏
     */
    private void dismiss() {
        isShow = false;
        //获取recyclerView其中一个Item即可
        View itemView = layoutManager.findViewByPosition(layoutManager.findFirstVisibleItemPosition());
        if (itemView != null) {
            //不知道怎么说原理，反正就是获取到一个Item以外的RecyclerView的高度
            int translationY = (int) recyclerHeight - itemView.getHeight();
            //设置动画移动的距离
            ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, translationY);
            //监听动画滚动
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    //使用recyclerView固定的高度减去容器顶部到最后一个Item的间距
                    float v = recyclerHeight - (float) animation.getAnimatedValue();


                    ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                    layoutParams.height = (int) v;
                    //最后再设置到recyclerview，就会慢慢的减少高度了
                    recyclerView.setLayoutParams(layoutParams);


                    layoutManager.setScrollEnabled(false);  //收缩禁止滑动
                }
            });
            valueAnimator.addListener(listener);
            valueAnimator.setDuration(500).start();

        }
    }

    private static final String TAG = "ExpandOrFoldView";
    /**
     * 控制是否可以点击展开收缩按钮
     */
    private Animator.AnimatorListener listener = new Animator.AnimatorListener() {
        @Override
        public void onAnimationStart(Animator animation) {
            controlView.setClickable(false);
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            controlView.setClickable(true);
            if (!isShow) {
                layoutManager.scrollToPosition(layoutManager.getItemCount() - 1);
            }
        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    };



    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    public TextView getControlView() {
        return controlView;
    }

    public void setControlView(TextView controlView) {
        this.controlView = controlView;
    }

    /**
     * 需要gridLayoutManager的话自己再添加一个即可，内容如下
     */
    public static class CustomLinearLayoutManager extends LinearLayoutManager {
        private boolean isScrollEnabled = true;

        public CustomLinearLayoutManager(Context context) {
            super(context);
        }

        public CustomLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
            super(context, orientation, reverseLayout);
        }

        public CustomLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
            super(context, attrs, defStyleAttr, defStyleRes);
        }


        public void setScrollEnabled(boolean flag) {
            this.isScrollEnabled = flag;
        }

        @Override
        public boolean canScrollVertically() {
            return isScrollEnabled && super.canScrollVertically();
        }
    }

}
