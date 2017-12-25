package com.dinghong.studyviewdemo1.view;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.support.v4.view.NestedScrollingParent;
import android.support.v4.view.NestedScrollingParentHelper;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Jason StatuLayout on 2017/10/30.
 */

public class StatusLayout extends RelativeLayout implements NestedScrollingParent, NestedScrollingChild {

    private View statusView;        //各种状态
    private View emptyView;         //空视图布局
    private View loadingView;       //加载中布局
    private View netErrorView;      //网络异常布局

    private Context con;

    //----------------处理嵌套滑动相关，完全不用理会------------------------
    private NestedScrollingParentHelper mParentHelper;  //嵌套滑动父类帮助类
    private NestedScrollingChildHelper mChildHelper;//嵌套滑子类帮助类
    private final int[] mParentScrollConsumed = new int[2];
    private final int[] mParentOffsetInWindow = new int[2];
    private float mTotalUnconsumed;
    //----------------处理嵌套滑动相关，完全不用理会------------------------

    public StatusLayout(@NonNull Context context) {
        super(context);
        this.con = context;
        initHelper();
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.con = context;
        initHelper();
    }

    public StatusLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.con = context;
        initHelper();
    }

    /**
     * 初始化嵌套滑动
     */
    private void initHelper() {
        mParentHelper = new NestedScrollingParentHelper(this);
        mChildHelper = new NestedScrollingChildHelper(this);
        setNestedScrollingEnabled(true);
    }

    /**
     * 获取当前显示的布局，可用来获取控件做其他操作
     *
     * @return
     */
    public View getStatusView() {
        return statusView;
    }

    //------------------------------------------------父------------------------------------------------------------------------------
    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        Log.d("StatuLayout------父", "onStartNestedScroll--target:" + target + ",       nestedScrollAxes:" + nestedScrollAxes + "       child:" + child);
        //判断是横向滑动或者是竖向滑动，nestedscrollAxes值是关键，如果是false就没有嵌套滑动的后续了
        return (nestedScrollAxes & ViewCompat.SCROLL_AXIS_VERTICAL) != 0;//判断是否竖滑动
    }

    @Override
    public void onNestedScrollAccepted(View child, View target, int axes) {
        Log.d("StatuLayout------父", "onNestedScrollAccepted    target:" + target + ",  nestedScrollAxes:" + axes + ",    child:" + child);
        mParentHelper.onNestedScrollAccepted(child, target, axes);
        startNestedScroll(axes & ViewCompat.SCROLL_AXIS_VERTICAL);
        mTotalUnconsumed = 0;
    }

    @Override
    public void onStopNestedScroll(View child) {
        Log.d("StatuLayout------父", "onStopNestedScroll--target:" + child);
        mParentHelper.onStopNestedScroll(child);
        if (mTotalUnconsumed != 0) {
            mTotalUnconsumed = 0;
        }
        stopNestedScroll();
    }

    @Override
    public void onNestedScroll(View target, int dxConsumed, int dyConsumed, int dxUnconsumed, int dyUnconsumed) {
        dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, mParentOffsetInWindow);
        final int dy = dyUnconsumed + mParentOffsetInWindow[1];
        if (dy < 0) {
            mTotalUnconsumed += Math.abs(dy);
        }
    }

    @Override
    public void onNestedPreScroll(View target, int dx, int dy, int[] consumed) {
        //在这里决定是否分发子view的滑动，然后父类会根据子类滑动的距离进行消费，分发多少看计算
//        consumed[1]=dy/2;//完全消费y轴的滑动

        Log.d("StatuLayout------父", "-----------onNestedPreScroll dy =" + dy + ",      mTotalUnconsumed = " + mTotalUnconsumed);
        if (dy != 0 && mTotalUnconsumed != 0) {
            if (dy > mTotalUnconsumed) {
                consumed[1] = dy - (int) mTotalUnconsumed;
                mTotalUnconsumed = 0;
            } else {
                mTotalUnconsumed -= dy;
                consumed[1] = dy;
            }
        }
        Log.d("StatuLayout------父", "onNestedPreScroll--getScrollY():" + getScrollY() + ",     dx:" + dx + ",       dy:" + dy + ",     consumed[0]:" + consumed[0] + ",      consumed[1]" + consumed[1]);

        final int[] parentConsumed = mParentScrollConsumed;
        if (dispatchNestedPreScroll(dx - consumed[0], dy - consumed[1], parentConsumed, null)) {
            consumed[0] += parentConsumed[0];
            consumed[1] += parentConsumed[1];
        }

    }

    @Override
    public boolean onNestedFling(View target, float velocityX, float velocityY, boolean consumed) {
        return dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean onNestedPreFling(View target, float velocityX, float velocityY) {
        return dispatchNestedPreFling(velocityX, velocityY);
    }

    @Override
    public int getNestedScrollAxes() {
        return mParentHelper.getNestedScrollAxes();
    }

    //------------------------------------------------子，不需要任何手动代码------------------------------------------------------------------------------

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        Log.d("StatuLayout------子", "setNestedScrollingEnabled:" + enabled);
        mChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        //决定是否支持嵌套
        Log.d("StatuLayout------子", "isNestedScrollingEnabled");
        return mChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {


        return mChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        Log.d("StatuLayout------子", "stopNestedScroll");
        mChildHelper.stopNestedScroll();

    }

    @Override
    public boolean hasNestedScrollingParent() {
        Log.d("StatuLayout------子", "hasNestedScrollingParent");
        return mChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        Log.d("StatuLayout------子", "dispatchNestedScroll:dxConsumed:" + dxConsumed + "," +
                "dyConsumed:" + dyConsumed + ",dxUnconsumed:" + dxUnconsumed + ",dyUnconsumed:" +
                dyUnconsumed + ",offsetInWindow:" + offsetInWindow);
        return mChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed, dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        Log.d("StatuLayout------子", "dispatchNestedPreScroll:dx" + dx + ",dy:" + dy + ",consumed:" + consumed + ",offsetInWindow:" + offsetInWindow);
        return mChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        Log.d("StatuLayout------子", "dispatchNestedFling:velocityX:" + velocityX + ",velocityY:" + velocityY + ",consumed:" + consumed);
        return mChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        Log.d("StatuLayout------子", "dispatchNestedPreFling:velocityX:" + velocityX + ",velocityY:" + velocityY);
        return mChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }

    public Build Build() {
        return new Build();
    }


    /**
     * 将状态布局添加到容器里面
     *
     * @param view
     */
    private void show(View view) {
        if (statusView != null) {
            removeView(statusView);
        }
        statusView = view;
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(statusView, layoutParams);
        show();
    }

    /**
     * 显示布局,如果当前子view是状态布局就显示，否则隐藏其他子view
     */
    private void show() {
        if (statusView == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == statusView) {
                statusView.setVisibility(VISIBLE);
            } else {
                getChildAt(i).setVisibility(GONE);
            }
        }
    }

    /**
     * 隐藏布局，如果当前子view是状态布局则隐藏并且移除，否则显示其他子view
     */
    public void hide() {
        if (statusView == null) {
            return;
        }
        for (int i = 0; i < getChildCount(); i++) {
            if (getChildAt(i) == statusView) {
                statusView.setVisibility(GONE);
            } else {
                getChildAt(i).setVisibility(VISIBLE);
            }
        }
        removeView(statusView);
        statusView = null;
    }

    /**
     * 找控件
     * @param id
     * @param <T>
     * @return
     */
    public <T extends View> T finvView(@IdRes int id) {
        if (statusView != null) {
            return statusView.findViewById(id);
        }
        return null;
    }

    /**
     * 显示空视图
     */
    public void showEmptyLayout() {
        show(emptyView);
    }

    /**
     * 显示网络错误布局
     */
    public void showNetErrorLayout() {
        show(netErrorView);
    }

    /**
     * 显示加载布局
     */
    public void showLoadingLayout() {
        show(loadingView);
    }

    /**
     * 显示其他布局
     *
     * @param view 需要显示的其他布局
     */
    public void showOtherLayout(View view) {
        show(view);
    }


    public class Build {
        private int emptyLayoutId;      //空视图布局
        private int loadingLayoutId;    //加载中布局
        private int netErrorLayoutId;   //网络异常布局

        private int emptyTipsId;        //空视图提示语
        private int emptyImageId;       //空视图图片
        private String emptyTips;       //提示语
        private int emptiImage;      //图片

        private int netErrorTipsId;     //网络状态错误提示语
        private int netErrorLoadAgainId;//网络状态错误重新加载
        private String netErrorTips;    //提示语

        private ClickAdapter clickAdapter;   //点击事件

        private Build() {
        }

        //TODO 暂时没想到加载中需要控制什么控件

        /**
         * 设置空视图
         *
         * @param layoutID 显示空视图的布局ID
         * @return
         */
        public Build setEmptyLayout(@LayoutRes int layoutID) {
            this.emptyLayoutId = layoutID;
            return this;
        }

        /**
         * @param emptyTipsId 需要显示提示语的控件ID
         * @param text        提示语
         * @return
         */
        public Build setEmptyTipsId(@IdRes int emptyTipsId, String text) {
            this.emptyTipsId = emptyTipsId;
            this.emptyTips = text;
            return this;
        }

        /**
         * @param emptyImageId 空视图图片控件ID
         * @param resId        空视图图片
         * @return
         */
        public Build setEmptyImageId(@IdRes int emptyImageId, @DrawableRes int resId) {
            this.emptyImageId = emptyImageId;
            this.emptiImage = resId;
            return this;
        }

        /**
         * 设置网络错误布局
         *
         * @param layoutId 显示网络错误的布局ID
         * @return
         */
        public Build setNetErrorLayoutId(@LayoutRes int layoutId) {
            this.netErrorLayoutId = layoutId;
            return this;
        }

        /**
         * 设置网络错误提示语
         *
         * @param netErrorTipsId 需要显示提示语的控件ID
         * @param text           提示语
         * @return
         */
        public Build setNetErrorTipsId(@IdRes int netErrorTipsId, String text) {
            this.netErrorTipsId = netErrorTipsId;
            this.netErrorTips = text;
            return this;
        }

        /**
         * 设置点击重新加载的控件ID
         *
         * @return
         */
        public Build setNetErrorLoadAgainId(@IdRes int netErrorLoadAgainId) {
            this.netErrorLoadAgainId = netErrorLoadAgainId;
            return this;
        }

        /**
         * 设置加载布局
         *
         * @param layoutId
         * @return
         */
        public Build setLoadingLayoutId(@LayoutRes int layoutId) {
            this.loadingLayoutId = layoutId;
            return this;
        }

        /**
         * 设置点击事件
         * @param clickAdapter
         * @return
         */
        public Build setClickListener(ClickAdapter clickAdapter) {
            this.clickAdapter = clickAdapter;
            return this;
        }

        public void build() {
            initNetErrorLayout();
            initEmptyLayout();
            initLoadingLayout();
        }


        /**
         * 初始化加载中视图
         */
        private void initLoadingLayout() {
            if (loadingLayoutId != 0) {
                loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, null);

            }
        }

        /**
         * 初始化网络错误视图
         */
        private void initNetErrorLayout() {
            if (netErrorLayoutId != 0) {
                netErrorView = LayoutInflater.from(getContext()).inflate(netErrorLayoutId, null);

                //获取网络错误提示语控件并设置提示语
                if (netErrorView != null && netErrorTipsId != 0) {
                    View tipsView = netErrorView.findViewById(netErrorTipsId);
                    if (tipsView != null && tipsView instanceof TextView) {
                        ((TextView) tipsView).setText(netErrorTips);
                    }
                }

                //获取网络错误重新加载控件并设置点击事件
                if (netErrorView != null && netErrorLoadAgainId != 0) {
                    View loadAgainView = netErrorView.findViewById(netErrorLoadAgainId);
                    if (loadAgainView != null) {
                        loadAgainView.setOnClickListener(v -> {
                            if (clickAdapter != null) {
                                clickAdapter.onLoadAgain(v);
                            }
                        });
                    }
                }

            }
        }

        /**
         * 初始化空视图
         */
        private void initEmptyLayout() {
            if (emptyLayoutId != 0) {
                emptyView = LayoutInflater.from(getContext()).inflate(emptyLayoutId, null);

                //获取空视图提示语控件并设置提示语
                if (emptyView != null && emptyTipsId != 0) {
                    View tipsView = emptyView.findViewById(emptyTipsId);
                    if (tipsView != null && tipsView instanceof TextView) {
                        ((TextView) tipsView).setText(emptyTips);
                    }
                }

                //获取空视图图片控件并设置图片
                if (emptyView != null && emptyImageId != 0) {
                    View imgView = emptyView.findViewById(emptyImageId);
                    if (imgView != null && imgView instanceof ImageView) {
                        imgView.setBackground(con.getDrawable(emptiImage));
                    }
                }
            }
        }
    }

    /**
     * 简化点击事件
     */
    public abstract static class ClickAdapter implements OnStatuClickListener {
        @Override
        public void onLoadAgain(View view) {

        }
    }

    /**
     * 点击事件回调
     */
    interface OnStatuClickListener {
        void onLoadAgain(View view);
    }


}
