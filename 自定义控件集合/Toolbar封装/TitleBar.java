package com.dinghong.studyviewdemo1.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dinghong.studyviewdemo1.R;

/**
 * Created by CHEN ZHI JIAN on 2017/12/23.
 * QQ群：347615100
 */

public class TitleBar extends Toolbar {
    private View rootView;
    private View leftOneView;
    private ImageView leftIv;
    private TextView leftTv;
    private View leftTwoView;
    private View rightOneView;
    private ImageView rightOneIv;
    private TextView rightOneTv;
    private View rightTwoView;
    private ImageView rightTwoIv;
    private TextView rightTwoTv;
    private TextView titleTv;

    private Context con;

    public TitleBar(Context context) {
        super(context);
        this.con = context;
        init();
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.con = context;
        init();
    }

    public TitleBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.con = context;
        init();
    }

    private void init() {

        rootView = inflate(con, R.layout.layout_titlebar, this);
        initViews();

        /* 去除默认边距 */
        setContentInsetsRelative(0, 0);
        setContentInsetsAbsolute(0, 0);
    }

    /**
     * 初始化控件
     */
    private void initViews() {
        if (rootView == null) {
            return;
        }
        leftOneView = rootView.findViewById(R.id.rlty_left);
        leftIv = rootView.findViewById(R.id.iv_left);
        leftTv = rootView.findViewById(R.id.tv_left);

        leftTwoView = rootView.findViewById(R.id.rlty_left_two);

        rightOneView = rootView.findViewById(R.id.rlty_right_one);
        rightOneIv = rootView.findViewById(R.id.iv_right_one);
        rightOneTv = rootView.findViewById(R.id.tv_right_one);

        rightTwoView = rootView.findViewById(R.id.rlty_right_two);
        rightTwoIv = rootView.findViewById(R.id.iv_right_two);
        rightTwoTv = rootView.findViewById(R.id.tv_right_two);

        titleTv = rootView.findViewById(R.id.tv_title);
    }

    /**
     * 创建构建类
     *
     * @return
     */
    public Build Build() {
        return new Build();
    }

    /**
     * 获取根布局
     *
     * @return
     */
    @Override
    public View getRootView() {
        return rootView;
    }

    /**
     * 获取左边ImageView控件
     *
     * @return
     */
    public ImageView getLeftImageView() {
        return leftIv;
    }

    /**
     * 获取左边TextView控件
     *
     * @return
     */
    public TextView getLeftTextView() {
        return leftTv;
    }

    /**
     * 获取右边第一个ImageView控件,从右往左数
     *
     * @return
     */
    public ImageView getRightOneImageView() {
        return rightOneIv;
    }

    /**
     * 获取右边第一个TextView控件,从右往左数
     *
     * @return
     */
    public TextView getRightOneTextView() {
        return rightOneTv;
    }

    /**
     * 获取右边第二个ImageView控件,从右往左数
     *
     * @return
     */
    public ImageView getRightTwoIv() {
        return rightTwoIv;
    }

    /**
     * 获取右边第二个TextView控件,从右往左数
     *
     * @return
     */
    public TextView getRightTwoTv() {
        return rightTwoTv;
    }

    /**
     * 隐藏左边
     *
     * @param visiable
     */
    public void setVisibilityLeft(int visiable) {
        leftOneView.setVisibility(visiable);
    }

    /**
     * 隐藏右边第一个，从右往左数
     *
     * @param visiable
     */
    public void setVisibilityRightOne(int visiable) {
        rightOneView.setVisibility(visiable);
    }

    /**
     * 隐藏右边第二个，从右往左数
     *
     * @param visiable
     */
    public void setVisibilityRightTwo(int visiable) {
        rightTwoView.setVisibility(visiable);
    }

    /**
     * 获取标题控件
     *
     * @return
     */
    public TextView getTitleTextView() {
        return titleTv;
    }

    public class Build {
        private int leftImg = -1;           //左边的控件图标
        private int leftImgWidth = -1;      //左边的控件图标宽
        private int leftImgHeight = -1;     //左边的控件图标高
        private String leftText;            //左边的文本
        private int leftTextSize = -1;      //左边的文本文字大小
        private int leftTextColor = -1;     //左边的文本文字颜色
        private Attr leftAttr;              //图文组合属性


        private int rightOneImg = -1;         //右边第一个控件图标，从右往左数
        private int rightOneImgWidth = -1;  //右边第一个控件图标宽，从右往左数
        private int rightOneImgHeight = -1; //右边第一个控件图标高，从右往左数
        private String rightOneText;        //右边第一个控件文本，从右往左数
        private int rightOneTextSize = -1;  //右边第一个控件文本文字大小，从右往左数
        private int rightOneTextColor = -1; //右边第一个控件文本文字颜色，从右往左数
        private Attr rightOneAttr;          //右边第一个图文组合属性，从右往左数

        private int rightTwoImg = -1;         //右边第二个控件图标，从右往左数
        private int rightTwoImgWidth = -1;  //右边第二个控件图标宽，从右往左数
        private int rightTwoImgHeight = -1; //右边第二个控件图标高，从右往左数
        private String rightTwoText;        //右边第二个控件文本，从右往左数
        private int rightTwoTextSize = -1;  //右边第二个控件文本文字大小，从右往左数
        private int rightTwoTextColor = -1; //右边第二个控件文本文字颜色，从右往左数
        private Attr rightTwoAttr;          //右边第二个图文组合属性，从右往左数

        private String titleText;           //标题文本
        private int titleTextSize = -1;     //标题文本文字大小
        private int titleTextColor = -1;    //标题文本文字颜色

        /**
         * 左边的控件图标
         *
         * @param leftImg 图标资源
         */
        public Build setLeftImg(@DrawableRes int leftImg) {
            this.leftImg = leftImg;
            this.leftImgWidth = -1;
            this.leftImgHeight = -1;
            return this;
        }

        /**
         * 左边的控件图标
         *
         * @param leftImg  图标资源
         * @param widthDp  宽，单位是DP，最好是文字大小的2倍或3倍
         * @param heightDp 高，单位是DP，最好是文字大小的2倍或3倍
         */
        public Build setLeftImg(@DrawableRes int leftImg, int widthDp, int heightDp) {
            this.leftImg = leftImg;
            this.leftImgWidth = widthDp;
            this.leftImgHeight = heightDp;
            return this;
        }

        /**
         * 左边的文本
         *
         * @param leftText
         */
        public Build setLeftText(@Nullable String leftText) {
            this.leftText = leftText;
            this.leftTextSize = -1;
            this.leftTextColor = -1;
            return this;
        }

        /**
         * 左边的文本
         *
         * @param leftText   文本
         * @param textSizeSp 文字大小，单位是SP
         * @param textColor  文字颜色
         */
        public Build setLeftText(@Nullable String leftText, int textSizeSp, int textColor) {
            this.leftText = leftText;
            this.leftTextSize = textSizeSp;
            this.leftTextColor = textColor;
            return this;
        }

        /**
         * 图文结合
         *
         * @param attr 属性类，不可空
         */
        public Build setLeft(@Nullable Attr attr) {
            this.leftAttr = attr;
            return this;
        }

        /**
         * 图文结合
         *
         * @param leftText
         * @param leftImg
         */
        public Build setLeft(@Nullable String leftText, @DrawableRes int leftImg) {
            Attr attr = new Attr(con);
            attr.text = leftText;
            attr.img = leftImg;
            this.leftAttr = attr;
            return this;
        }

        /**
         * 右边第一个控件图标，从右往左数
         *
         * @param rightOneImg
         */
        public Build setRightOneImg(@DrawableRes int rightOneImg) {
            this.rightOneImg = rightOneImg;
            this.rightOneImgWidth = -1;
            this.rightOneImgHeight = -1;
            return this;
        }

        /**
         * 右边第一个控件图标，从右往左数
         *
         * @param rightOneImg 图标资源
         * @param widthDp     宽，单位是DP，最好是文字大小的2倍或3倍
         * @param heightDp    高，单位是DP，最好是文字大小的2倍或3倍
         */
        public Build setRightOneImg(@DrawableRes int rightOneImg, int widthDp, int heightDp) {
            this.rightOneImg = rightOneImg;
            this.rightOneImgWidth = widthDp;
            this.rightOneImgHeight = heightDp;
            return this;
        }

        /**
         * 右边第一个控件文本，从右往左数
         *
         * @param rightOneText
         */
        public Build setRightOneText(@Nullable String rightOneText) {
            this.rightOneText = rightOneText;
            this.rightOneTextSize = -1;
            this.rightOneTextColor = -1;
            return this;
        }

        /**
         * 右边第一个控件文本，从右往左数
         *
         * @param rightOneText 文本
         * @param textSizeSp   文字大小，单位是SP
         * @param textColor    文字颜色
         */
        public Build setRightOneText(@Nullable String rightOneText, int textSizeSp, int textColor) {
            this.rightOneText = rightOneText;
            this.rightOneTextSize = textSizeSp;
            this.rightOneTextColor = textColor;
            return this;
        }

        /**
         * 右边第一个图文结合，从右往左数
         *
         * @param attr 属性类，不可空
         */
        public Build setRinghtOne(@Nullable Attr attr) {
            this.leftAttr = attr;
            return this;
        }

        /**
         * 右边第一个图文结合，从右往左数
         *
         * @param rightOneText
         * @param rightOneImg
         */
        public Build setRinghtOne(@Nullable String rightOneText, @DrawableRes int rightOneImg) {
            Attr attr = new Attr(con);
            attr.text = rightOneText;
            attr.img = rightOneImg;
            this.rightOneAttr = attr;
            return this;
        }

        /**
         * 右边第二个控件图标，从右往左数
         *
         * @param rightTwoImg
         */
        public Build setRightTwoImg(@DrawableRes int rightTwoImg) {
            this.rightTwoImg = rightTwoImg;
            this.rightTwoImgWidth = -1;
            this.rightTwoImgHeight = -1;
            return this;
        }

        /**
         * 右边第二个控件图标，从右往左数
         *
         * @param rightTwoImg 图标资源
         * @param widthDp     宽，单位是DP，最好是文字大小的2倍或3倍
         * @param heightDp    高，单位是DP，最好是文字大小的2倍或3倍
         */
        public Build setRightTwoImg(@DrawableRes int rightTwoImg, int widthDp, int heightDp) {
            this.rightTwoImg = rightTwoImg;
            this.rightTwoImgWidth = widthDp;
            this.rightTwoImgHeight = heightDp;
            return this;
        }

        /**
         * 右边第二个控件文本，从右往左数
         *
         * @param rightTwoText
         */
        public Build setRightTwoText(@Nullable String rightTwoText) {
            this.rightTwoText = rightTwoText;
            this.rightTwoTextSize = -1;
            this.rightTwoTextColor = -1;
            return this;
        }

        /**
         * 右边第二个控件文本，从右往左数
         *
         * @param rightTwoText 文本
         * @param textSizeSp   文字大小，单位是SP
         * @param textColor    文字颜色
         */
        public Build setRightTwoText(@Nullable String rightTwoText, int textSizeSp, int textColor) {
            this.rightTwoText = rightTwoText;
            this.rightTwoTextSize = textSizeSp;
            this.rightTwoTextColor = textColor;
            return this;
        }

        /**
         * 右边第二个图文结合，从右往左数
         *
         * @param attr 属性类，不可空
         */
        public Build setRinghtTwo(@Nullable Attr attr) {
            this.rightTwoAttr = attr;
            return this;
        }

        /**
         * 右边第二个图文结合，从右往左数
         *
         * @param rightTwoText
         * @param rightTwoImg
         */
        public Build setRinghtTwo(@Nullable String rightTwoText, @DrawableRes int rightTwoImg) {
            Attr attr = new Attr(con);
            attr.text = rightTwoText;
            attr.img = rightTwoImg;
            this.rightTwoAttr = attr;
            return this;
        }

        /**
         * 标题文本
         *
         * @param titleText
         */
        public Build setTitleText(@Nullable String titleText) {
            this.titleText = titleText;
            this.titleTextSize = -1;
            this.titleTextColor = -1;
            return this;
        }

        /**
         * @param titleText       标题
         * @param titleTextSizeSp 标题文字大小，单位sp
         * @param titleTextColor  标题文字颜色
         */
        public Build setTitleText(@Nullable String titleText, int titleTextSizeSp, int titleTextColor) {
            this.titleText = titleText;
            this.titleTextSize = titleTextSizeSp;
            this.titleTextColor = titleTextColor;
            return this;
        }

        /**
         * 设置标题行数
         *
         * @param lines
         * @return
         */
        public Build setTitleLine(int lines) {
            titleTv.setMaxLines(lines);
            return this;
        }

        /**
         * 设置左边点击事件
         *
         * @param listener
         */
        public Build setLeftClickListener(View.OnClickListener listener) {
            leftOneView.setOnClickListener(listener);
            return this;
        }

        /**
         * 设置右边第一个点击事件，从右往左数
         *
         * @param listener
         */
        public Build setRightOneClickListener(View.OnClickListener listener) {
            rightOneView.setOnClickListener(listener);
            return this;
        }

        /**
         * 设置右边第二个点击事件，从右往左数
         *
         * @param listener
         */
        public Build setRightTwoClickListener(View.OnClickListener listener) {
            rightTwoView.setOnClickListener(listener);
            return this;
        }

        /**
         * 设置标题点击事件
         *
         * @param listener
         * @return
         */
        public Build setTitleClickListener(View.OnClickListener listener) {
            titleTv.setOnClickListener(listener);
            return this;
        }

        public void build() {
            initLeft();
            initRightOne();
            initRightTwo();
            initTitle();
        }

        /**
         * 初始化标题
         */
        private void initTitle() {
            titleTv.setText(titleText);
            if (titleTextColor != -1) {
                titleTv.setTextColor(titleTextColor);
            }
            if (titleTextSize != -1) {
                titleTv.setTextSize(titleTextSize);
            }
        }

        /**
         * 初始化右边第二个，从右往左数
         */
        private void initRightTwo() {
            if (rightTwoAttr != null) {
                initTextAndPhoto(rightTwoTv, rightTwoIv, rightTwoView, rightTwoAttr);
                leftTwoView.setVisibility(VISIBLE);
            } else {
                if (!TextUtils.isEmpty(rightTwoText)) {
                    rightTwoTv.setText(rightTwoText);
                    setTextSize(rightTwoTv, rightTwoTextColor, rightTwoTextSize);
                    showTextView(rightTwoView, rightTwoTv, rightTwoIv);
                    leftTwoView.setVisibility(VISIBLE);
                } else if (rightTwoImg != -1 && rightTwoImg > 0) {
                    rightTwoIv.setBackgroundResource(rightTwoImg);
                    setImgSize(rightTwoIv, rightTwoImgWidth, rightTwoImgHeight);
                    showImageView(rightTwoView, rightTwoTv, rightTwoIv);
                    leftTwoView.setVisibility(VISIBLE);
                }
            }
        }

        /**
         * 初始化右边第一个，从右往左数
         */
        private void initRightOne() {
            if (rightOneAttr != null) {
                initTextAndPhoto(rightOneTv, rightOneIv, rightOneView, rightOneAttr);
                leftOneView.setVisibility(VISIBLE);
            } else {
                if (!TextUtils.isEmpty(rightOneText)) {
                    rightOneTv.setText(rightOneText);
                    setTextSize(rightOneTv, rightOneTextColor, rightOneTextSize);
                    showTextView(rightOneView, rightOneTv, rightOneIv);
                    leftOneView.setVisibility(VISIBLE);
                } else if (rightOneImg != -1 && rightOneImg > 0) {
                    rightOneIv.setBackgroundResource(rightOneImg);
                    setImgSize(rightOneIv, rightOneImgWidth, rightOneImgHeight);
                    showImageView(rightOneView, rightOneTv, rightOneIv);
                    leftOneView.setVisibility(VISIBLE);
                }
            }

        }

        /**
         * 初始化左边
         */

        private void initLeft() {
            if (leftAttr != null) {
                initTextAndPhoto(leftTv, leftIv, leftOneView, leftAttr);
                rightOneView.setVisibility(VISIBLE);
            } else {
                if (!TextUtils.isEmpty(leftText)) {
                    leftTv.setText(leftText);
                    setTextSize(leftTv, leftTextColor, leftTextSize);
                    showTextView(leftOneView, leftTv, leftIv);
                    rightOneView.setVisibility(VISIBLE);
                } else if (leftImg != -1 && leftImg > 0) {
                    leftIv.setBackgroundResource(leftImg);
                    setImgSize(leftIv, leftImgWidth, leftImgHeight);
                    showImageView(leftOneView, leftTv, leftIv);
                    rightOneView.setVisibility(VISIBLE);
                }
            }
        }

        /**
         * 初始化图文组合
         *
         * @param tv
         * @param iv
         * @param layout
         * @param attr
         */
        private void initTextAndPhoto(TextView tv, ImageView iv, View layout, Attr attr) {
            iv.setVisibility(GONE);
            tv.setVisibility(VISIBLE);
            layout.setVisibility(VISIBLE);

            tv.setText(attr.text);
            tv.setTextSize(attr.textSize);
            tv.setTextColor(attr.textColor);

            if (attr.img == -1) {
                return;
            }
            tv.setCompoundDrawablePadding(dip2px(con, attr.drawablePadding));
            Drawable drawable = getResources().getDrawable(attr.img);
            drawable.setBounds(0, 0, dip2px(con, attr.imgWidth), dip2px(con, attr.imgHeight));

            switch (attr.gravity) {
                case Gravity.LEFT:
                    tv.setCompoundDrawables(drawable, null, null, null);
                    break;
                case Gravity.RIGHT:
                    tv.setCompoundDrawables(null, null, drawable, null);
                    break;
                case Gravity.TOP:
                    tv.setCompoundDrawables(null, drawable, null, null);
                    break;
                case Gravity.BOTTOM:
                    tv.setCompoundDrawables(null, null, null, drawable);
                    break;
            }
        }

        /**
         * 显示图标
         *
         * @param layout
         * @param tv
         * @param iv
         */
        private void showImageView(View layout, View tv, View iv) {
            layout.setVisibility(VISIBLE);
            iv.setVisibility(VISIBLE);
            tv.setVisibility(GONE);
        }

        /**
         * 显示文本
         *
         * @param layout
         * @param tv
         * @param iv
         */
        private void showTextView(View layout, View tv, View iv) {
            layout.setVisibility(VISIBLE);
            tv.setVisibility(VISIBLE);
            iv.setVisibility(GONE);
        }

        /**
         * 设置控件大小
         *
         * @param iv
         * @param width
         * @param height
         */
        private void setImgSize(ImageView iv, int width, int height) {
            if (width <= 0 || height <= 0) {
                return;
            }
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv.getLayoutParams();
            layoutParams.width = dip2px(con, width);
            layoutParams.height = dip2px(con, height);
            iv.setLayoutParams(layoutParams);
        }

        /**
         * 设置文本大小和颜色
         *
         * @param tv
         * @param color
         * @param size
         */
        private void setTextSize(TextView tv, int color, int size) {
            if (color != -1) {
                tv.setTextColor(color);
            }
            if (size != -1) {
                tv.setTextSize(size);
            }
        }
    }


    /**
     * 将dp转换为px
     *
     * @param dipValue
     * @return
     */
    private static int dip2px(Context con, float dipValue) {
        final float scale = con.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    /**
     * 可以对控件设置的属性
     */
    public static class Attr {
        private int defualtSize;
        public int textSize;
        public int textColor;
        public int gravity;
        public int imgWidth;
        public int imgHeight;
        public int drawablePadding;

        public String text;
        public int img = -1;

        public Attr(Context con) {
            defualtSize = 10;
            textSize = defualtSize;
            textColor = Color.parseColor("#666666");
            gravity = Gravity.TOP;
            imgWidth = defualtSize * 3;
            imgHeight = defualtSize * 3;
            drawablePadding = defualtSize / 6;
        }

        /**
         * @param text            文本
         * @param img             图标
         * @param textSize        文本字体大小，单位SP
         * @param textColor       文本字体颜色
         * @param imgGravity      图标位置，上下左右可选
         * @param imgWidth        图标宽，最好是文字大小的2倍或3倍
         * @param imgHeight       图标搞，最好是文字大小的2倍或3倍
         * @param drawablePadding 图标与文本间距
         */
        public Attr(Context con, String text, int img, int textSize, int textColor, int imgGravity, int imgWidth, int imgHeight, int drawablePadding) {
            this(con);
            this.text = text;
            this.img = img;
            if (textSize != 1 && textSize > 0)
                this.textSize = textSize;
            if (textColor != 1 && textColor > 0)
                this.textColor = textColor;
            if ((gravity == Gravity.TOP || gravity == Gravity.BOTTOM) || (gravity == Gravity.LEFT || gravity == Gravity.RIGHT))
                this.gravity = imgGravity;
            if (imgWidth != 1 && imgWidth > 0)
                this.imgWidth = imgWidth;
            if (imgHeight != 1 && imgHeight > 0)
                this.imgHeight = imgHeight;
            if (drawablePadding != 1 && drawablePadding > 0)
                this.drawablePadding = drawablePadding;
        }
    }
}
