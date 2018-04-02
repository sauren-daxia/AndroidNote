package com.studio.chen.baselib.widgets

import android.content.Context
import android.graphics.Color
import android.support.annotation.DrawableRes
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toolbar
import com.studio.chen.baselib.R


/**
 * Created by marti on 2018/4/2.
 */
class JTitleBar @JvmOverloads constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int = 0) : Toolbar(context, attrs, defStyleAttr) {
    private var mLeftImg = 0                       //左边的控件图标
    private var mLeftImgWidth = 0                  //左边的控件图标宽
    private var mLeftImgHeight = 0                 //左边的控件图标高
    private var mLeftText: String? = null           //左边的文本
    private var mLeftTextSize = 0                  //左边的文本文字大小
    private var mLeftTextColor = 0                 //左边的文本文字颜色
    private var mLeftAttr: Attr? = null             //图文组合属性

    private var mRightOneImg = 0                   //右边第一个控件图标，从右往左数
    private var mRightOneImgWidth = 0              //右边第一个控件图标宽，从右往左数
    private var mRightOneImgHeight = 0             //右边第一个控件图标高，从右往左数
    private var mRightOneText: String? = null       //右边第一个控件文本，从右往左数
    private var mRightOneTextSize = 0              //右边第一个控件文本文字大小，从右往左数
    private var mRightOneTextColor = 0             //右边第一个控件文本文字颜色，从右往左数
    private var mRightOneAttr: Attr? = null         //右边第一个图文组合属性，从右往左数

    private var mRightTwoImg = 0                  //右边第二个控件图标，从右往左数
    private var mRightTwoImgWidth = -1              //右边第二个控件图标宽，从右往左数
    private var mRightTwoImgHeight = -1             //右边第二个控件图标高，从右往左数
    private var mRightTwoText: String? = null       //右边第二个控件文本，从右往左数
    private var mRightTwoTextSize = 0              //右边第二个控件文本文字大小，从右往左数
    private var mRightTwoTextColor = 0             //右边第二个控件文本文字颜色，从右往左数
    private var mRightTwoAttr: Attr? = null         //右边第二个图文组合属性，从右往左数

    private var mTitleText: String? = null          //标题文本
    private var mTitleTextSize = 0                 //标题文本文字大小
    private var mTitleTextColor = 0                //标题文本文字颜色
    private var mTitleLines = 1                     //标题文本行数

    private var mLeftClick: ((View) -> Unit)? = null        //左边点击事件
    private var mRightOneClick: ((View) -> Unit)? = null    //右边第一个点击事件
    private var mRightTwoClick: ((View) -> Unit)? = null     //右边第二个点击事件
    private var mTitleClick: ((View) -> Unit)? = null       //标题点击事件

    private lateinit var mRootView: View
    private lateinit var leftOneView: View
    private lateinit var leftIv: ImageView
    private lateinit var leftTv: TextView
    private lateinit var leftTwoView: View
    private lateinit var rightOneView: View
    private lateinit var rightOneIv: ImageView
    private lateinit var rightOneTv: TextView
    private lateinit var rightTwoView: View
    private lateinit var rightTwoIv: ImageView
    private lateinit var rightTwoTv: TextView
    private lateinit var titleTv: TextView


    class Attr() {
        private val defualtSize: Int = 12
        var textSize: Int = defualtSize
        var textColor: Int = Color.parseColor("#666666")
        var gravity: Int = Gravity.TOP
        var imgWidth: Int = defualtSize * 2
        var imgHeight: Int = defualtSize * 2
        var drawablePadding: Int = defualtSize / 6

        var text: String? = null
        var img = 0


        /**
         * @param text            文本
         * @param img             图标
         * @param textSize        文本字体大小，单位SP
         * @param textColor       文本字体颜色
         * @param imgGravity      图标位置，上下左右可选
         * @param imgWidth        图标宽，最好是文字大小的2倍或3倍，单位DP
         * @param imgHeight       图标搞，最好是文字大小的2倍或3倍，单位DP
         * @param drawablePadding 图标与文本间距
         */
        constructor(text: String, @DrawableRes img: Int, textSize: Int, textColor: Int, imgGravity: Int, imgWidth: Int, imgHeight: Int, drawablePadding: Int) : this() {
            this.text = text
            this.img = img
            this.textSize = textSize
            this.textColor = textColor
            Log.d("CHEN", "-----------  attr.textColor =  ${textColor}")
            this.gravity = imgGravity
            this.imgWidth = imgWidth
            this.imgHeight = imgHeight
            this.drawablePadding = drawablePadding
        }
    }

    init {
        initAttr(attrs)
        initView()
        /* 去除默认边距 */
        setContentInsetsRelative(0, 0)
        setContentInsetsAbsolute(0, 0)
        build()
    }

    private fun initAttr(attrs: AttributeSet) {
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.JTitleBar)
        //左边第一个相关属性
        mLeftImg = typeArray.getResourceId(R.styleable.JTitleBar_leftImg, 0)
        mLeftImgWidth = typeArray.getDimension(R.styleable.JTitleBar_leftImgWidth, 0f).toInt()
        mLeftImgHeight = typeArray.getDimension(R.styleable.JTitleBar_leftImgHeight, 0f).toInt()
        mLeftText = typeArray.getString(R.styleable.JTitleBar_leftText)
        mLeftTextSize = typeArray.getDimension(R.styleable.JTitleBar_leftTextSize, 0f).toInt()
        mLeftTextColor = typeArray.getColor(R.styleable.JTitleBar_leftTextColor, 0)
        //右边第一个相关属性
        mRightOneImg = typeArray.getResourceId(R.styleable.JTitleBar_rightOneImg, 0)
        mRightOneImgWidth = typeArray.getDimension(R.styleable.JTitleBar_rightOneImgWidth, 0f).toInt()
        mRightOneImgHeight = typeArray.getDimension(R.styleable.JTitleBar_rightOneImgHeight, 0f).toInt()
        mRightOneText = typeArray.getString(R.styleable.JTitleBar_rightOneText)
        mRightOneTextSize = typeArray.getDimension(R.styleable.JTitleBar_rightOneTextSize, 0f).toInt()
        mRightOneTextColor = typeArray.getColor(R.styleable.JTitleBar_rightOneTextColor, 0)
        //右边第二个相关属性
        mRightTwoImg = typeArray.getResourceId(R.styleable.JTitleBar_rightTwoImg, 0)
        mRightTwoImgWidth = typeArray.getDimension(R.styleable.JTitleBar_rightTwoImgWidth, 0f).toInt()
        mRightTwoImgHeight = typeArray.getDimension(R.styleable.JTitleBar_rightTwoImgHeight, 0f).toInt()
        mRightTwoText = typeArray.getString(R.styleable.JTitleBar_rightTwoText)
        mRightTwoTextSize = typeArray.getDimension(R.styleable.JTitleBar_rightTwoTextSize, 0f).toInt()
        mRightTwoTextColor = typeArray.getColor(R.styleable.JTitleBar_rightTwoTextColor, 0)
        //标题相关属性
        mTitleText = typeArray.getString(R.styleable.JTitleBar_titleText)
        mTitleTextSize = typeArray.getDimension(R.styleable.JTitleBar_titleTextSize, 0f).toInt()
        mTitleTextColor = typeArray.getColor(R.styleable.JTitleBar_titleTextColor, 0)
    }


    private fun initView() {
        mRootView = View.inflate(context, R.layout.layout_titlebar, this)
        leftOneView = rootView.findViewById(R.id.rlty_left)
        leftIv = rootView.findViewById(R.id.iv_left)
        leftTv = rootView.findViewById(R.id.tv_left)
        leftTwoView = rootView.findViewById(R.id.rlty_left_two)
        rightOneView = rootView.findViewById(R.id.rlty_right_one)
        rightOneIv = rootView.findViewById(R.id.iv_right_one)
        rightOneTv = rootView.findViewById(R.id.tv_right_one)
        rightTwoView = rootView.findViewById(R.id.rlty_right_two)
        rightTwoIv = rootView.findViewById(R.id.iv_right_two)
        rightTwoTv = rootView.findViewById(R.id.tv_right_two)
        titleTv = rootView.findViewById(R.id.tv_title)
    }

    /**
     * 设置新属性
     */
    fun newBuilder(): Builder {
        return Builder(
                mLeftImg,
                mLeftImgWidth,
                mLeftImgHeight,
                mLeftText,
                mLeftTextSize,
                mLeftTextColor,
                mLeftAttr,
                mRightOneImg,
                mRightOneImgWidth,
                mRightOneImgHeight,
                mRightOneText,
                mRightOneTextSize,
                mRightOneTextColor,
                mRightOneAttr,
                mRightTwoImg,
                mRightTwoImgWidth,
                mRightTwoImgHeight,
                mRightTwoText,
                mRightTwoTextSize,
                mRightTwoTextColor,
                mRightTwoAttr,
                mTitleText,
                mTitleTextSize,
                mTitleTextColor,
                mTitleLines,
                mLeftClick,
                mRightOneClick,
                mRightTwoClick,
                mTitleClick
        )
    }

    /**
     * 初始化
     */
    private fun build() {
        initLeft()
        initRightOne()
        initRightTwo()
        initTitle()
    }

    /**
     * 初始化左边
     */

    private fun initLeft() {
        if (mLeftAttr != null) {
            initTextAndPhoto(leftTv, leftIv, leftOneView, mLeftAttr!!)
            rightOneView.visibility = View.VISIBLE
        } else {
            if (!TextUtils.isEmpty(mLeftText)) {
                leftTv.text = mLeftText
                setTextSize(leftTv, mLeftTextColor, mLeftTextSize)
                showTextView(leftOneView, leftTv, leftIv)
                rightOneView.visibility = View.VISIBLE
            } else if (mLeftImg > 0 && mLeftImg > 0) {
                leftIv.setBackgroundResource(mLeftImg)
                setImgSize(leftIv, mLeftImgWidth, mLeftImgHeight)
                showImageView(leftOneView, leftTv, leftIv)
                rightOneView.visibility = View.VISIBLE
            } else {
                rightOneView.visibility = View.GONE
            }
        }
        setClick(leftOneView, mLeftClick)
    }

    /**
     * 初始化右边第一个，从右往左数
     */
    private fun initRightOne() {
        if (mRightOneAttr != null) {
            initTextAndPhoto(rightOneTv, rightOneIv, rightOneView, mRightOneAttr!!)
            leftOneView.visibility = View.VISIBLE
        } else {
            if (!TextUtils.isEmpty(mRightOneText)) {
                rightOneTv.text = mRightOneText
                setTextSize(rightOneTv, mRightOneTextColor, mRightOneTextSize)
                showTextView(rightOneView, rightOneTv, rightOneIv)
                leftOneView.visibility = View.VISIBLE
            } else if (mRightOneImg > 0 && mRightOneImg > 0) {
                rightOneIv.setBackgroundResource(mRightOneImg)
                setImgSize(rightOneIv, mRightOneImgWidth, mRightOneImgHeight)
                showImageView(rightOneView, rightOneTv, rightOneIv)
                leftOneView.visibility = View.VISIBLE
            } else {
                leftOneView.visibility = View.GONE
            }
        }
        setClick(rightOneView, mRightOneClick)
    }

    /**
     * 初始化右边第二个，从右往左数
     */
    private fun initRightTwo() {
        if (mRightTwoAttr != null) {
            initTextAndPhoto(rightTwoTv, rightTwoIv, rightTwoView, mRightTwoAttr!!)
            leftTwoView.visibility = View.VISIBLE
        } else {
            if (!TextUtils.isEmpty(mRightTwoText)) {
                rightTwoTv.text = mRightTwoText
                setTextSize(rightTwoTv, mRightTwoTextColor, mRightTwoTextSize)
                showTextView(rightTwoView, rightTwoTv, rightTwoIv)
                leftTwoView.visibility = View.VISIBLE
            } else if (mRightTwoImg > 0 && mRightTwoImg > 0) {
                rightTwoIv.setBackgroundResource(mRightTwoImg)
                setImgSize(rightTwoIv, mRightTwoImgWidth, mRightTwoImgHeight)
                showImageView(rightTwoView, rightTwoTv, rightTwoIv)
                leftTwoView.visibility = View.VISIBLE
            } else {
                leftTwoView.visibility = View.GONE
            }
        }

        setClick(rightTwoView, mRightTwoClick)
    }


    /**
     * 初始化标题
     */
    private fun initTitle() {
        titleTv.text = mTitleText
        if (mTitleTextColor > 0) {
            titleTv.setTextColor(mTitleTextColor)
        }
        if (mTitleTextSize > 0) {
            titleTv.textSize = mTitleTextSize.toFloat()
        }
        titleTv.setLines(mTitleLines)
        setClick(titleTv, mTitleClick)
    }


    /**
     * 初始化图文组合
     *
     * @param tv
     * @param iv
     * @param layout
     * @param attr
     */
    private fun initTextAndPhoto(tv: TextView, iv: ImageView, layout: View, attr: Attr) {
        iv.visibility = View.GONE
        tv.visibility = View.VISIBLE
        layout.visibility = View.VISIBLE

        tv.text = attr.text

        if (attr.textSize > 0) {
            tv.textSize = attr.textSize.toFloat()
        }
        Log.d("CHEN", "attr.textColor = ${attr.textColor} color = ${Color.parseColor("#87aa34")}")

        tv.setTextColor(attr.textColor)

        if (attr.img <= 0) {
            return
        }
        tv.compoundDrawablePadding = dip2px(context, attr.drawablePadding.toFloat()).toInt()
        val drawable = resources.getDrawable(attr.img)
        drawable.setBounds(0, 0, dip2px(context, attr.imgWidth.toFloat()).toInt(), dip2px(context, attr.imgHeight.toFloat()).toInt())
        when (attr.gravity) {
            Gravity.LEFT -> tv.setCompoundDrawables(drawable, null, null, null)
            Gravity.RIGHT -> tv.setCompoundDrawables(null, null, drawable, null)
            Gravity.TOP -> tv.setCompoundDrawables(null, drawable, null, null)
            Gravity.BOTTOM -> tv.setCompoundDrawables(null, null, null, drawable)
        }
    }

    /**
     * 设置控件大小
     *
     * @param iv
     * @param width
     * @param height
     */
    private fun setImgSize(iv: ImageView, width: Int, height: Int) {
        if (width <= 0 || height <= 0) {
            return
        }
        val layoutParams = iv.layoutParams as RelativeLayout.LayoutParams
        layoutParams.width = dip2px(context, width.toFloat()).toInt()
        layoutParams.height = dip2px(context, height.toFloat()).toInt()
        iv.layoutParams = layoutParams
    }

    /**
     * 设置文本大小和颜色
     *
     * @param tv
     * @param color
     * @param size
     */
    private fun setTextSize(tv: TextView, color: Int, size: Int) {
        if (color > 0) {
            tv.setTextColor(color)
        }
        if (size > 0) {
            tv.textSize = size.toFloat()
        }
    }

    /**
     * 显示图标
     *
     * @param layout
     * @param tv
     * @param iv
     */
    private fun showImageView(layout: View, tv: View, iv: View) {
        layout.visibility = View.VISIBLE
        iv.visibility = View.VISIBLE
        tv.visibility = View.GONE
    }

    /**
     * 显示文本
     *
     * @param layout
     * @param tv
     * @param iv
     */
    private fun showTextView(layout: View, tv: View, iv: View) {
        layout.visibility = View.VISIBLE
        tv.visibility = View.VISIBLE
        iv.visibility = View.GONE
    }

    /**
     * 设置点击事件
     */
    private fun setClick(view: View, click: ((View) -> Unit)?) {
        view.setOnClickListener(click)
    }

    /**
     * 将dp转换为px
     *
     * @param dipValue
     * @return
     */
    private fun dip2px(con: Context, dipValue: Float): Float {
        val scale = con.resources.displayMetrics.density
        return dipValue * scale + 0.5f
    }

    inner class Builder(
            private var mLeftImg: Int = -1,                       //左边的控件图标
            private var mLeftImgWidth: Int = -1,                  //左边的控件图标宽
            private var mLeftImgHeight: Int = -1,                 //左边的控件图标高
            private var mLeftText: String? = null,           //左边的文本
            private var mLeftTextSize: Int = -1,                  //左边的文本文字大小
            private var mLeftTextColor: Int = -1,                 //左边的文本文字颜色
            private var mLeftAttr: Attr? = null,             //图文组合属性

            private var mRightOneImg: Int = -1,                   //右边第一个控件图标，从右往左数
            private var mRightOneImgWidth: Int = -1,              //右边第一个控件图标宽，从右往左数
            private var mRightOneImgHeight: Int = -1,             //右边第一个控件图标高，从右往左数
            private var mRightOneText: String? = null,       //右边第一个控件文本，从右往左数
            private var mRightOneTextSize: Int = -1,              //右边第一个控件文本文字大小，从右往左数
            private var mRightOneTextColor: Int = -1,             //右边第一个控件文本文字颜色，从右往左数
            private var mRightOneAttr: Attr? = null,         //右边第一个图文组合属性，从右往左数

            private var mRightTwoImg: Int = -1,                   //右边第二个控件图标，从右往左数
            private var mRightTwoImgWidth: Int = -1,              //右边第二个控件图标宽，从右往左数
            private var mRightTwoImgHeight: Int = -1,             //右边第二个控件图标高，从右往左数
            private var mRightTwoText: String? = null,       //右边第二个控件文本，从右往左数
            private var mRightTwoTextSize: Int = -1,              //右边第二个控件文本文字大小，从右往左数
            private var mRightTwoTextColor: Int = -1,             //右边第二个控件文本文字颜色，从右往左数
            private var mRightTwoAttr: Attr? = null,         //右边第二个图文组合属性，从右往左数

            private var mTitleText: String? = null,          //标题文本
            private var mTitleTextSize: Int = -1,                 //标题文本文字大小
            private var mTitleTextColor: Int = -1,                //标题文本文字颜色
            private var mTitleLines: Int = 1,                     //标题文本行数

            private var mLeftClick: ((View) -> Unit)? = null,        //左边点击事件
            private var mRightOneClick: ((View) -> Unit)? = null,    //右边第一个点击事件
            private var mRightTwoClick: ((View) -> Unit)? = null,     //右边第二个点击事件
            private var mTitleClick: ((View) -> Unit)? = null       //标题点击事件
    ) {


        /**
         * 左边的控件图标
         *
         * @param leftImg 图标资源
         */
        fun setLeftImg(@DrawableRes leftImg: Int): Builder {
            this.mLeftImg = leftImg
            return this
        }

        /**
         * 左边的控件图标
         *
         * @param leftImg  图标资源
         * @param widthDp  宽，单位DP
         * @param heightDp 高，单位DP
         */
        fun setLeftImg(@DrawableRes leftImg: Int, width: Int, height: Int): Builder {
            this.mLeftImg = leftImg
            this.mLeftImgWidth = width
            this.mLeftImgHeight = height
            return this
        }

        /**
         * 左边的文本
         *
         * @param leftText
         */
        fun setLeftText(leftText: String?): Builder {
            this.mLeftText = leftText
            return this
        }

        /**
         * 左边的文本
         *
         * @param leftText   文本
         * @param textSizeSp 文字大小，单位SP
         * @param textColor  文字颜色
         */
        fun setLeftText(leftText: String?, textSize: Int, textColor: Int): Builder {
            this.mLeftText = leftText
            this.mLeftTextSize = textSize
            this.mLeftTextColor = textColor
            return this
        }

        /**
         * 左边图文结合
         *
         * @param attr 左边按钮属性类
         */
        fun setLeft(attr: Attr?): Builder {
            this.mLeftAttr = attr
            return this
        }

        /**
         * 左边图文结合
         *
         * @param leftText
         * @param leftImg
         */
        fun setLeft(leftText: String?, @DrawableRes leftImg: Int): Builder {
            val attr = Attr()
            attr.text = leftText
            attr.img = leftImg
            this.mLeftAttr = attr
            return this
        }


        /**
         * 右边第一个控件图标，从右往左数
         *
         * @param rightOneImg
         */
        fun setRightOneImg(@DrawableRes rightOneImg: Int): Builder {
            this.mRightOneImg = rightOneImg
            return this
        }

        /**
         * 右边第一个控件图标，从右往左数
         *
         * @param rightOneImg 图标资源
         * @param widthDp     宽，，单位DP
         * @param heightDp    高，，单位DP
         */
        fun setRightOneImg(@DrawableRes rightOneImg: Int, width: Int, height: Int): Builder {
            this.mRightOneImg = rightOneImg
            this.mRightOneImgWidth = width
            this.mRightOneImgHeight = height
            return this
        }

        /**
         * 右边第一个控件文本，从右往左数
         *
         * @param rightOneText
         */
        fun setRightOneText(rightOneText: String?): Builder {
            this.mRightOneText = rightOneText
            return this
        }

        /**
         * 右边第一个控件文本，从右往左数
         *
         * @param rightOneText 文本
         * @param textSize   文字大小，单位SP
         * @param textColor    文字颜色
         */
        fun setRightOneText(rightOneText: String?, textSize: Int, textColor: Int): Builder {
            this.mRightOneText = rightOneText
            this.mRightOneTextSize = textSize
            this.mRightOneTextColor = textColor
            return this
        }

        /**
         * 右边第一个图文结合，从右往左数
         *
         * @param attr 属性类，不可空
         */
        fun setRinghtOne(attr: Attr?): Builder {
            this.mRightOneAttr = attr
            return this
        }

        /**
         * 右边第一个图文结合，从右往左数
         *
         * @param rightOneText
         * @param rightOneImg
         */
        fun setRinghtOne(rightOneText: String?, @DrawableRes rightOneImg: Int): Builder {
            val attr = Attr()
            attr.text = rightOneText
            attr.img = rightOneImg
            this.mRightOneAttr = attr
            return this
        }


        /**
         * 右边第二个控件图标，从右往左数
         *
         * @param rightTwoImg
         */
        fun setRightTwoImg(@DrawableRes rightTwoImg: Int): Builder {
            this.mRightTwoImg = rightTwoImg
            return this
        }

        /**
         * 右边第二个控件图标，从右往左数
         *
         * @param rightTwoImg 图标资源
         * @param widthDp     宽，单位DP
         * @param heightDp    高，单位DP
         */
        fun setRightTwoImg(@DrawableRes rightTwoImg: Int, width: Int, height: Int): Builder {
            this.mRightTwoImg = rightTwoImg
            this.mRightTwoImgWidth = width
            this.mRightTwoImgHeight = height
            return this
        }

        /**
         * 右边第二个控件文本，从右往左数
         *
         * @param rightTwoText
         */
        fun setRightTwoText(rightTwoText: String?): Builder {
            this.mRightTwoText = rightTwoText
            return this
        }

        /**
         * 右边第二个控件文本，从右往左数
         *
         * @param rightTwoText 文本
         * @param textSizeSp   文字大小，单位SP
         * @param textColor    文字颜色
         */
        fun setRightTwoText(rightTwoText: String?, textSize: Int, textColor: Int): Builder {
            this.mRightTwoText = rightTwoText
            this.mRightTwoTextSize = textSize
            this.mRightTwoTextColor = textColor
            return this
        }

        /**
         * 右边第二个图文结合，从右往左数
         *
         * @param attr 属性类，不可空
         */
        fun setRinghtTwo(attr: Attr?): Builder {
            this.mRightTwoAttr = attr
            return this
        }

        /**
         * 右边第二个图文结合，从右往左数
         *
         * @param rightTwoText
         * @param rightTwoImg
         */
        fun setRinghtTwo(rightTwoText: String?, @DrawableRes rightTwoImg: Int): Builder {
            val attr = Attr()
            attr.text = rightTwoText
            attr.img = rightTwoImg
            this.mRightTwoAttr = attr
            return this
        }

        /**
         * 标题文本
         *
         * @param titleText
         */
        fun setTitleText(titleText: String?): Builder {
            this.mTitleText = titleText
            return this
        }

        /**
         * @param titleText       标题
         * @param titleTextSizeSp 标题文字大小，单位sp
         * @param titleTextColor  标题文字颜色
         */
        fun setTitleText(titleText: String?, titleTextSizeSp: Int, titleTextColor: Int): Builder {
            this.mTitleText = titleText
            this.mTitleTextSize = titleTextSizeSp
            this.mTitleTextColor = titleTextColor
            return this
        }

        /**
         * 设置标题行数
         *
         * @param lines
         * @return
         */
        fun setTitleLine(lines: Int): Builder {
            this.mTitleLines = lines
            return this
        }

        /**
         * 设置左边点击事件
         *
         * @param listener
         */
        fun setLeftClickListener(click: (View) -> Unit): Builder {
            this.mLeftClick = click
            return this
        }

        /**
         * 设置右边第一个点击事件，从右往左数
         *
         * @param listener
         */
        fun setRightOneClickListener(click: (View) -> Unit): Builder {
            this.mRightOneClick = click
            return this
        }

        /**
         * 设置右边第二个点击事件，从右往左数
         *
         * @param listener
         */
        fun setRightTwoClickListener(click: (View) -> Unit): Builder {
            this.mRightTwoClick = click
            return this
        }

        /**
         * 设置标题点击事件
         *
         * @param listener
         * @return
         */
        fun setTitleClickListener(click: (View) -> Unit): Builder {
            this.mTitleClick = click
            return this
        }

        fun build() {

            this@JTitleBar.mLeftImg = mLeftImg                      //左边的控件图标
            this@JTitleBar.mLeftImgWidth = mLeftImgWidth                  //左边的控件图标宽
            this@JTitleBar.mLeftImgHeight = mLeftImgHeight                //左边的控件图标高
            this@JTitleBar.mLeftText = mLeftText          //左边的文本
            this@JTitleBar.mLeftTextSize = mLeftTextSize                 //左边的文本文字大小
            this@JTitleBar.mLeftTextColor = mLeftTextColor                //左边的文本文字颜色
            this@JTitleBar.mLeftAttr = mLeftAttr             //图文组合属性


            this@JTitleBar.mRightOneImg = mRightOneImg
            this@JTitleBar.mRightOneImgWidth = mRightOneImgWidth
            this@JTitleBar.mRightOneImgHeight = mRightOneImgHeight
            this@JTitleBar.mRightOneText = mRightOneText
            this@JTitleBar.mRightOneTextSize = mRightOneTextSize
            this@JTitleBar.mRightOneTextColor = mRightOneTextColor
            this@JTitleBar.mRightOneAttr = mRightOneAttr

            this@JTitleBar.mRightTwoImg = mRightTwoImg
            this@JTitleBar.mRightTwoImgWidth = mRightTwoImgWidth
            this@JTitleBar.mRightTwoImgHeight = mRightTwoImgHeight
            this@JTitleBar.mRightTwoText = mRightTwoText
            this@JTitleBar.mRightTwoTextSize = mRightTwoTextSize
            this@JTitleBar.mRightTwoTextColor = mRightTwoTextColor
            this@JTitleBar.mRightTwoAttr = mRightTwoAttr

            this@JTitleBar.mTitleText = mTitleText
            this@JTitleBar.mTitleTextSize = mTitleTextSize
            this@JTitleBar.mTitleTextColor = mTitleTextColor
            this@JTitleBar.mTitleLines = mTitleLines

            this@JTitleBar.mLeftClick = mLeftClick
            this@JTitleBar.mRightOneClick = mRightOneClick
            this@JTitleBar.mRightTwoClick = mRightTwoClick
            this@JTitleBar.mTitleClick = mTitleClick
            this@JTitleBar.build()
        }

    }
}

