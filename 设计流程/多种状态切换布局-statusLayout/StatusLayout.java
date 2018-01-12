package com.nanbo.vocationalschools.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
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
import android.support.v4.view.animation.FastOutLinearInInterpolator;
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
    private View requestErrorView;
    private boolean isEnabledShow = true;  //是否启用
    private boolean isShow;             //是否显示错误布局

    private Context con;
    private Build build;

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
        return build = new Build();
    }


    /**
     * 将状态布局添加到容器里面
     *
     * @param view
     */
    private void show(View view) {
        if (view == null) {
            return;
        }
        if (isEnabledShow == false) {
            return;
        }
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
        isShow = true;
        showAnim();
    }

    private void showAnim() {
        if (build != null) {
            //播放loadingAnim
            if (loadingView != null && statusView.getId() == loadingView.getId()) {
                if (build.getLoadingAnimIv() != null && build.getLoadingAnimIv().getBackground() != null) {
                    ((AnimationDrawable) build.getLoadingAnimIv().getBackground()).start();
                }
            }
            else if (netErrorView != null && statusView.getId() == netErrorView.getId()) {
                if (build.getLoadingAnimIv() != null && build.getNetErrorAnimIv().getBackground() != null) {
                    ((AnimationDrawable) build.getNetErrorAnimIv().getBackground()).start();
                }
            }

        }
    }

    private void cancalAnim() {
        if (build != null) {
            //播放loadingAnim
            if (loadingView != null && statusView.getId() == loadingView.getId()) {
                if (build.getLoadingAnimIv() != null && build.getLoadingAnimIv().getBackground() != null) {
                    if (((AnimationDrawable) build.getLoadingAnimIv().getBackground()).isRunning()) {
                        ((AnimationDrawable) build.getLoadingAnimIv().getBackground()).stop();
                    }
                }
            }
            else if (netErrorView != null && statusView.getId() == netErrorView.getId()) {
                if (build.getLoadingAnimIv() != null && build.getNetErrorAnimIv().getBackground() != null) {
                    if (((AnimationDrawable) build.getNetErrorAnimIv().getBackground()).isRunning()) {
                        ((AnimationDrawable) build.getNetErrorAnimIv().getBackground()).stop();
                    }
                }
            }
        }
    }

    /**
     * 隐藏布局，如果当前子view是状态布局则隐藏并且移除，否则显示其他子view
     */
    public void hide() {
        if (isEnabledShow == false) {
            return;
        }
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
        cancalAnim();
        removeView(statusView);
        statusView = null;
        isShow = false;

    }

    public void hideAnim() {
        if (isEnabledShow == false) {
            return;
        }
        if (statusView == null) {
            return;
        }
        if (statusView.getAnimation() != null) {
            statusView.getAnimation().cancel();
        }
        statusView
                .animate()
                .alpha(0f)
                .setDuration(200)
                .setInterpolator(new FastOutLinearInInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        hide();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {
                        super.onAnimationCancel(animation);
                        hide();
                    }
                });
    }


    /**
     * 找控件
     *
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
     * 自动判断显示网络错误布局
     */
    public void showNetErrorLayout(Exception e) {
        boolean isNetError = false;
        if (e.getMessage().contains("SocketException")) {
            isNetError = true;
        } else if (e.getMessage().contains("ConnectException")) {
            isNetError = true;
        } else if (e.getMessage().contains("EOFException")) {
            isNetError = true;
        } else if (e.getMessage().contains("analysisExcetpion")) {
            isNetError = true;
        } else if (e.getMessage().contains("UnknownHostException")) {
            isNetError = true;
        } else {
            isNetError = false;
        }
        if (isNetError) {
            show(netErrorView);
        } else {
            //TODO 此处需要显示一个未知错误
        }
    }

    /**
     * 显示加载布局
     */
    public void showLoadingLayout() {
        show(loadingView);
    }


    /**
     * 显示请求错误页面
     */
    public void showRequestErrorLayout() {
        show(requestErrorView);
    }

    /**
     * 显示其他布局
     *
     * @param view 需要显示的其他布局
     */
    public void showOtherLayout(View view) {
        show(view);
    }

    /**
     * 获取是否被禁用了
     *
     * @return
     */
    public boolean isEnabledShow() {
        return isEnabledShow;
    }

    /**
     * 设置是否禁用
     *
     * @param enabledShow
     */
    public void setEnabledShow(boolean enabledShow) {
        isEnabledShow = enabledShow;
    }

    /**
     * 获取是否显示
     *
     * @return
     */
    public boolean isShow() {
        return isShow;
    }

    public class Build {
        private int emptyLayoutId;      //空视图布局
        private int loadingLayoutId;    //加载中布局
        private int netErrorLayoutId;   //网络异常布局
        private int requestErrorLayoutId;//请求错误布局

        private int emptyTipsId;        //空视图提示语
        private int emptyImageId;       //空视图图片
        private String emptyTips;       //提示语
        private int emptiImage;      //图片

        private int netErrorTipsId;     //网络状态错误提示语
        private int netErrorLoadAgainId;//网络状态错误重新加载
        private String netErrorTips;    //提示语
        private int netErrorImageId;    //网络错误图片控件
        private int netErrorAnimRes;    //网络错误图片资源


        private int requestErrorTipsId;    //请求错误提示控件
        private String requestErrorTips;    //请求错误提示语

        private int loadingImageId;          //显示loading的控件
        private int loadingAnimRes;         //帧动画
        private int loadingTipsId;          //loading提示控件
        private String loadingTips;         //loading提示语

        private ImageView loadingAnimIv;    //loading控件
        private ImageView netErrorAnimIv;   //网络错误控件


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
        public Build setEmptyLayoutId(@LayoutRes int layoutID) {
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
         * 设置请求错误布局
         *
         * @param layoutId 需要显示请求错误布局的ID
         * @return
         */
        public Build setRequestError(@LayoutRes int layoutId) {
            this.requestErrorLayoutId = layoutId;
            return this;
        }

        /**
         * 设置网络错误提示语
         *
         * @param requestErrorTipsId 需要显示提示语的控件ID
         * @param text               提示语
         * @return
         */
        public Build setRequestErrorTipsId(@IdRes int requestErrorTipsId, String text) {
            this.requestErrorTipsId = requestErrorTipsId;
            this.requestErrorTips = text;
            return this;
        }


        /**
         * 设置加载中的控件
         *
         * @param loadingAnimId  需要显示加载中动画的控件
         * @param loadingAnimRes 加载中动画资源
         * @return
         */
        public Build setLoadingAnimId(@IdRes int loadingAnimId, @DrawableRes int loadingAnimRes) {
            this.loadingImageId = loadingAnimId;
            this.loadingAnimRes = loadingAnimRes;
            return this;
        }


        /**
         * 设置加载中提示语
         *
         * @param loadingTipsId 需要显示加载中提示语的控件ID
         * @param text          提示语
         * @return
         */
        public Build setLoadingTipsId(@IdRes int loadingTipsId, String text) {
            this.loadingTipsId = requestErrorTipsId;
            this.loadingTips = text;
            return this;
        }

        /**
         * 设置网络错误图片的控件
         *
         * @param netErrorImageId  需要显示加载中动画的控件
         * @param netErrorAnimRes 加载中动画资源
         * @return
         */
        public Build setNetErrorAnimId(@IdRes int netErrorImageId, @DrawableRes int netErrorAnimRes) {
            this.netErrorImageId = netErrorImageId;
            this.netErrorAnimRes = netErrorAnimRes;
            return this;
        }


        /**
         * 设置点击事件
         *
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
            initRequestErrorLayout();
        }

        /**
         * 初始化请求错误视图
         */
        private void initRequestErrorLayout() {
            if (requestErrorLayoutId != 0) {
                requestErrorView = LayoutInflater.from(getContext()).inflate(requestErrorLayoutId, null);
                //获取网络错误提示语控件并设置提示语
                if (requestErrorView != null && requestErrorTipsId != 0) {
                    View tipsView = requestErrorView.findViewById(requestErrorTipsId);
                    if (tipsView != null && tipsView instanceof TextView) {
                        ((TextView) tipsView).setText(requestErrorTips);
                    }
                }
            }
        }


        /**
         * 初始化加载中视图
         */
        private void initLoadingLayout() {
            if (loadingLayoutId != 0) {
                loadingView = LayoutInflater.from(getContext()).inflate(loadingLayoutId, null);

                //获取网络错误提示语控件并设置提示语
                if (loadingView != null && loadingTipsId != 0) {
                    View tipsView = loadingView.findViewById(loadingTipsId);
                    if (tipsView != null && tipsView instanceof TextView) {
                        ((TextView) tipsView).setText(loadingTips);
                    }
                }

                //获取空视图图片控件并设置图片
                if (loadingView != null && loadingImageId != 0) {
                    loadingAnimIv = loadingView.findViewById(loadingImageId);
                    if (loadingAnimIv != null && loadingAnimIv instanceof ImageView) {
                        ((ImageView) loadingAnimIv).setBackgroundResource(loadingAnimRes);
                    }
                }
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

                //获取空视图图片控件并设置图片
                if (netErrorView != null && netErrorImageId != 0) {
                    netErrorAnimIv = netErrorView.findViewById(netErrorImageId);
                    if (netErrorAnimIv != null && netErrorAnimIv instanceof ImageView) {
                        ((ImageView) netErrorAnimIv).setBackgroundResource(netErrorAnimRes);
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
                        ((ImageView) imgView).setImageResource(emptiImage);
                    }
                }
            }
        }

        /**
         * 获取加载中动画控件
         *
         * @return
         */
        public ImageView getLoadingAnimIv() {
            return loadingAnimIv;
        }

        /**
         * 获取网络错误动画控件
         *
         * @return
         */
        public ImageView getNetErrorAnimIv() {
            return netErrorAnimIv;
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
