package com.android.viewtest;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;

/**
 * Created by 陈志坚 on 2016/8/25.
 */
public class SurfaceView extends ScrollView {
    private LinearLayout mLinear;
    private TopView topView;
    private  BottomView bottomView;
    public SurfaceView(Context context) {
        super(context,null);
    }

    public SurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context,null);
    }

    public SurfaceView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context,null);
    }
	/*
	*	禁止子View内容过长时自动滑动中间
	*/
	@Override
	protected boolean onRequestFocusInDescendants(int direction, Rect previouslyFocusedRect) {
		return true;
	}

	/**
	*	调用这个方法是将两个布局都放在Activity中，代码可能会比较冗余
	*/
    private void init() {
        //去掉滚动条
        this.setVerticalScrollBarEnabled(false);
        //因为ScrollView只能放一个控件，所以在内部创建一个布局容器
        mLinear = new LinearLayout(getContext());
        //设置方向
        mLinear.setOrientation(LinearLayout.VERTICAL);
        //添加到将容器添加scrollView中
        this.addView(mLinear);
        //这两个容器是重点，整个页面主要的布局方式就是一个ScrollView中套一个LinearLayout，
        // LinearLayout中套两个自定义的ScrollView
        topView = new TopView(getContext());
        bottomView = new BottomView(getContext());
        mLinear.addView(topView);
        mLinear.addView(bottomView);
    }

	/**
	*	调用这个方法就是将每个Fragment作为一页，代码清晰
	*/
	public void init(FragmentManager fragmentManager,Fragment fragmentTop, Fragment fragmentBottom){
		FragmentTransaction transaction = fragmentManager.beginTransaction();
		transaction.add(R.id.container_top,fragmentTop,fragmentTop.getClass().getName());
		transaction.add(R.id.container_bottom,fragmentBottom,fragmentBottom.getClass().getName());
		transaction.commit();
	}

    /**
     * @param view1
     * @param view2
     * 因为整个页面设计成两页，每页都装不同的布局，所以需要两个布局文件添加到两个自定义的ScrollView中
     */
    public void init(int view1,int view2){
       View top = inflate(getContext(),view1,null);
       View bottom = inflate(getContext(),view2,null);
        topView.addView(top);
        bottomView.addView(bottom);

    }

    /**
     * onLayout是设置子View的位置的方法
     * 因为两个子ScrollView嵌套在ScrollView里面，肯定显示不全，所以先要设置子ScrollView的高度
     * @param changed
     * @param l
     * @param t
     * @param r
     * @param b
     */
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        topView.getLayoutParams().height = getMeasuredHeight();
        bottomView.getLayoutParams().height = getMeasuredHeight();
    }

    //这个布尔值用于标识第一页还是第二页
    private boolean isPage = true;

    /**
     * 这个touch事件，重点在于点击后放手时判断，
     * @param ev
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()){
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                //处于第一页的时候
                    if(isPage){
                        /**
                         * 处于第一页的时候还继续往下拉滑动，就放手的时候就返回原处
                         * 因为默认布尔标识第一页，所以不需要修改状态
                         */
                        if(getScrollY() <= dip2px(getContext(),50)){
                            this.smoothScrollTo(0,0);
                        }
                        /**
                         * 当上拉滑动超过指定的范围的时候就让它滑到第二页，并且修改状态值
                         */
                        else{
                            this.smoothScrollTo(0,getHeight());
                            isPage = false;
							callBackListener(1);		//切换Fragment
							
                        }
                    }
                    //处于第二页的时候
                    else{
                        /**
                         * 需要判断滑动的距离是否超过this的高度或者少于this的高度
                         * 因为在76行onlayout中的时候已经将this的高度赋值给了里面的两个ScrollView
                         * 处于第一页的时候滑动，其实是滑动第一页ScrollView的内容，而非this的滑动，
                         * 当滑动到第二页的时候才滑动了一个this的高度而已，所以要根据this的高度去判断
                         *
                         */
                        if(getScrollY() >= getHeight() - dip2px(getContext(),50)){
                            this.smoothScrollTo(0,getHeight());
                        }else{
                            this.smoothScrollTo(0,0);
                            isPage = true;
							callBackListener(0);	//切换Fragment
                        }
                    }
                //如果不返回true，smoothScrollTo会失效，可能是因为会交给子View消费了，所以会失效，有待考证
                return true;
        }
        return super.onTouchEvent(ev);
    }


	/**
	*	用于切换Fragment的回调，用于告诉外面当前是第几页，
	*	但是貌似用途并不是很大，如果以后也没用，请取消这个方法
	*/
	private void callBackListener(int index){
		if(listener != null){
			listener.onPageChange(index);
		}
	}
	private OnPageChangeListener listener;
	public void setOnPageChangeListener(OnPageChangeListener listener){
		this.listener = listener;
	}
	public interface OnPageChangeListener{
		void onPageChange(int index);
	}


    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public  int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
