package com.weiyin.card_gobook.base;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.weiyin.card_gobook.R;

/**
 * Created by 陈志坚 on 2016/11/7.
 */

public abstract class BasePopupWindow extends PopupWindow {
    protected View rootView;
    protected Context con;
    protected boolean isExtends =true;  //判断是否继承的

    public BasePopupWindow(Context con) {
        this.con = con;
        rootView = LayoutInflater.from(con).inflate(getView(), null);
        Log.d("CHEN", "BasePopupWindow: rootView == null = "+(rootView == null));
        // 设置SelectPicPopupWindow的View
        this.setContentView(rootView);
        if(isExtends){
            init();
            initView();
        }
    }

    /**
     * 加载布局
     *
     * @return
     */
    protected abstract   int getView();
    protected abstract   int getAnimStyle();
    protected abstract  void initView();

    /**
     * 直接继承的会走这里初始化
     */
    protected void init() {
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体的高
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置点击屏幕外消失
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        if(getAnimStyle()!=0){
            this.setAnimationStyle(getAnimStyle());
        }
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.parseColor("#00000000"));
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
    }

    /**
     * 获取控件
     * @param id 控件ID
     * @return
     */
    public View findView(int id){
        return  rootView.findViewById(id);
    }


    @Override
    public void dismiss() {
        super.dismiss();
        WindowManager.LayoutParams lp = ((Activity) con).getWindow().getAttributes();
        lp.alpha = 1f;
        ((Activity) con).getWindow().setAttributes(lp);

    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBrightness(con, this);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBrightness(con, this);
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBrightness(con, this);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        // 设置SelectPicPopupWindow弹出窗体的背景
        setBrightness(con, this);
    }

    public  void setBrightness(Context mContext ,PopupWindow pop){
        ColorDrawable dw = new ColorDrawable(mContext.getResources().getColor(
                R.color.transparent));
        WindowManager.LayoutParams lp = ((Activity)mContext).getWindow().getAttributes();
        lp.alpha = 0.7f;
        ((Activity)mContext).getWindow().setAttributes(lp);
        // 设置SelectPicPopupWindow弹出窗体的背景
        pop.setBackgroundDrawable(dw);

    }


    public static class Builder {
        private int rootView, style, width, height;
        private BasePopupWindow window;
        private Context con;
        private boolean Focuse, OutSideCancel;

        public Builder() {
        }

        public Builder( Context con) {
            this.con = con;
        }

        public Builder( int rootView,  Context con) {
            this.rootView = rootView;
            this.con = con;
            if (rootView == 0) {
                throw new NullPointerException("not set Builder.setView() plaese set Layout，没有设置Builder布局，请设置一个布局！");
            }
        }

        /**
         * 设置布局
         *
         * @param rootView 布局文件
         * @return
         */
        public Builder setView( int rootView) {
            if (rootView == 0) {
                throw new NullPointerException("not set Builder.setView() plaese set Layout，没有设置Builder布局，请设置一个布局！");
            }
            this.rootView = rootView;
            return this;
        }

        /**
         * 设置动画
         *
         * @param style
         * @return
         */
        public Builder setAnimStyle(int style) {
            this.style = style;
            return this;
        }

        /**
         * 设置宽高
         *
         * @param width  宽
         * @param height 高
         * @return
         */
        public Builder setWidthAndHeight(int width, int height) {
            this.width = width;
            this.height = height;
            if (width == 0 || height == 0) {
                throw new NullPointerException("not set Builder.setWidthAndHeight() plaese set width and height，没有设置Builder宽高，请设置一个宽高！");
            }
            return this;
        }

        /**
         * 是否可点击popupWindow
         *
         * @param isFocuse
         * @return
         */
        public Builder setFocuse(boolean isFocuse) {
            this.Focuse = isFocuse;
            return this;
        }

        /**
         * 是否点击屏幕外消失
         *
         * @param isOutSideCancel
         * @return
         */
        public Builder setOutSideCancel(boolean isOutSideCancel) {
            this.OutSideCancel = isOutSideCancel;
            return this;
        }

        public BasePopupWindow builder() {
            window = new BasePopupWindow(con) {
                @Override
                protected int getView() {
                    if (Builder.this.rootView == 0) {
                        throw new NullPointerException("not set Builder.setView() plaese set Layout，没有设置Builder布局，请设置一个布局！");
                    }
                    this.isExtends = false;
                    return Builder.this.rootView;
                }

                @Override
                protected int getAnimStyle() {
                    return 0;
                }

                @Override
                protected void initView() {

                }
            };
            window.setOutsideTouchable(OutSideCancel);    //是否点击屏幕外消失
            window.setFocusable(Focuse);                  //是否可以点击popupwindow
            if (style != 0) {
                window.setAnimationStyle(style);            //设置动画
            }
            window.setWidth(width);                         //设置宽
            window.setHeight(height);                       //设置高
            return window;
        }


    }
}
