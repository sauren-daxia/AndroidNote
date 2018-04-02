package com.test.jasonchen.assemblydemo.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.test.jasonchen.assemblydemo.R


/**
 * Created by CHENJSON on 2018/3/12.
 * github : https://github.com/c297131019
 * @version 1.0
 *
 * //下一个版本增加
 * 1、指定宽高不正常的话，取比例最大的矩形，并且居中显示
 * 2、默认选中，未选中图片资源
 * 3、如不想使用资源图片，提供默认形状，三角形，五角星，圆形，矩形，都包含选中未选中
 * 4、如果使用形状，则提供中间可以描述文字(暂未定，功能复杂)
 * 5、可修改形状边框色，形状填充色
 * 6、提供整体控件边框色
 * 7、提供选中动效
 */
class JRatingBar : View {

    private var mPan = Paint()      //画笔
    private var starRects = mutableListOf<Rect>()     //星星个框框的集合
    var progress = 0           //当前值
        private set

    var maxProgress = 5      //最大值
        private set

    var defaultDistance = 10   //默认间距
        private set

    var leftAndRightDistance = 10 //左右两边的间距，默认0
        private set

    var topAndBottomDistance = 10//上下两边的间距，默认0
        private set

    var indexStar = 0f           //当前星星的位置
        private set
        get() =
            if (progress > 0) {
                progress / (1f * maxProgress / starCount)
            } else {
                0f
            }

    private var defaultStarWidth = 50  //默认星星宽度
        private set

    private var defaultStarHeight = 50 //默认星星高度
        private set

    var selectIcon = R.mipmap.ic_grade_big_star_select      //选中的图标，暂时没有默认
        private set

    var unSelectIcon = R.mipmap.ic_grade_big_star_un_select    //未选中图标，暂时没有默认
        private set

    private lateinit var selectBitmap: Bitmap //选中的Bitmap
    private lateinit var unSelectBitmap: Bitmap //未选中的Bitmap


    private val TYPE_NONE = 2           //不可触摸
    private val TYPE_CLICK = 1          //可点击
    private val TYPE_TOUCH = 0          //可触摸

    var isTouch = TYPE_TOUCH    //是否可以触摸，到时候添加三个属性，可触摸(也可点击) = 0，可点击(只可以点击) = 1，不可触摸 = 2


    var starCount = 5       //默认星星数量，当修改星星数的时候会去添加同样数量的框框
        private set(value) {
            field = value
            starRects.clear()
            addRectf()
        }

    private var onProgressListener: ((Float, Int) -> Unit)? = null

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
        getAttres(attrs)
    }


    /**
     * 获取属性
     */
    private fun getAttres(attrs: AttributeSet?) {
        attrs?.let {
            val typeArray = context.obtainStyledAttributes(attrs, R.styleable.JRatingBar)
            topAndBottomDistance = typeArray.getDimension(R.styleable.JRatingBar_TopAndBottomDistance, 10f).toInt()
            leftAndRightDistance = typeArray.getDimension(R.styleable.JRatingBar_leftAndRightDistance, 10f).toInt()

            defaultStarWidth = typeArray.getDimension(R.styleable.JRatingBar_starWidth, 50f).toInt()
            defaultStarHeight = typeArray.getDimension(R.styleable.JRatingBar_starHeight, 50f).toInt()

            maxProgress = typeArray.getInteger(R.styleable.JRatingBar_maxProgress, 5)
            progress = typeArray.getInteger(R.styleable.JRatingBar_progress, 0)
            starCount = typeArray.getInteger(R.styleable.JRatingBar_starCount, 5)

            selectIcon = typeArray.getResourceId(R.styleable.JRatingBar_selectIcon, selectIcon)
            unSelectIcon = typeArray.getResourceId(R.styleable.JRatingBar_unSelectIcon, unSelectIcon)

            isTouch = typeArray.getInteger(R.styleable.JRatingBar_isTouch, TYPE_TOUCH)
            defaultDistance = typeArray.getInteger(R.styleable.JRatingBar_distance, 10)

            defaultMin()
        }
    }

    init {
        mPan.isAntiAlias = true
    }

    /**
     * 添加框框个数
     */
    fun init() {
        addRectf()
    }

    /**
     * 添加Rectf个数
     */
    private fun addRectf() {
        (0 until starCount).forEach {
            starRects.add(Rect())
        }
    }

    /**
     * 构造选中、未选中的Bitmap
     */
    private fun buildBitmap() {
        selectBitmap = createBitmap(selectIcon)
        unSelectBitmap = createBitmap(unSelectIcon)
    }

    /**
     * 按控件大小缩放bitmap
     */
    fun createBitmap(sourceID: Int): Bitmap {
        //"如果指定宽高的比例不正常，需要去拿到最符合的比例"

        //因为selectIcon和unSelectIcon后续会添加默认，所以不怕为空为0
        var bitmap = BitmapFactory.decodeResource(resources, sourceID)
        var bitmapWidth = bitmap.width
        var bitmapHeight = bitmap.height
        var newWidth = (width - ((starCount - 1) * defaultDistance) - (leftAndRightDistance * 2) - paddingLeft - paddingRight) / starCount
        var newHeight = height - (topAndBottomDistance * 2)
        var square =    //当前版本暂未用到，取值最大比例的矩形
                if (newWidth - newHeight >= 0) {
                    newHeight
                } else {
                    newWidth
                }
        val matrix = Matrix()

        //createBitmap要求必须>0，所以小于0的时候统一给1，也就是最小是1的大小
        if (newWidth <= 0) newWidth = 1
        if (newHeight <= 0) newHeight = 1
        if (bitmapWidth <= 0) bitmapWidth = 1
        if (bitmapHeight <= 0) bitmapHeight = 1

        matrix.postScale(newWidth / bitmapWidth.toFloat(), newHeight / bitmapHeight.toFloat())
        return Bitmap.createBitmap(bitmap, 0, 0, bitmapWidth, bitmapHeight, matrix, true)
    }

    /**
     * 确定每个RectF的位置
     */
    private fun initRectf() {
        (0 until starCount).forEach { i ->
            var rect = starRects[i]
            rect.top = topAndBottomDistance
            rect.bottom = rect.top + selectBitmap.height
            when (i) {
                0 -> {
                    rect.left = leftAndRightDistance
                    rect.right = rect.left + selectBitmap.width
                }
                else -> {
                    var lastRectf = starRects[i - 1]
                    rect.left = lastRectf.right + defaultDistance
                    rect.right = rect.left + selectBitmap.width
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        buildBitmap()
        initRectf()

        (0 until starCount).forEach { i ->
            var rect = starRects[i]
//            canvas.drawRect(rect, mPan)
            canvas.drawBitmap(unSelectBitmap, rect.left.toFloat(), rect.top.toFloat(), mPan)
        }

        //选中了多少
        var selectCount = progress.toFloat() / (maxProgress.toFloat() / starCount)


        (0 until selectCount.toInt()).forEach { i ->
            //绘制完全选中的
            var rect = starRects[i]
            canvas.drawBitmap(selectBitmap, rect.left.toFloat(), rect.top.toFloat(), mPan)
        }

        //未满一个的部分比例
        var extra = selectCount - selectCount.toInt()

        //绘制直选中一部分的
        if (extra > 0) {
            var rect = Rect(starRects[selectCount.toInt()])
            //Bitmap构造截取的新宽度不能 <=0,只好做一个判断，这个误差应该看不出的，只有1
            var newWidth =
                    if ((selectBitmap.width * extra).toInt() == 0) {
                        1
                    } else {
                        (selectBitmap.width * extra).toInt()
                    }
            val bmp = Bitmap.createBitmap(selectBitmap, 0, 0, newWidth, selectBitmap.height, null, false)
            canvas.drawBitmap(bmp, rect.left.toFloat(), rect.top.toFloat(), mPan)
            bmp.recycle()
        }

        onProgressListener?.invoke(indexStar, progress)

        selectBitmap.recycle()
        unSelectBitmap.recycle()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        if (isTouch != TYPE_NONE) {   //如果不可点击就不用去计算了
            computeProgress(event)
        }

        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_UP -> {
                if (isTouch == TYPE_NONE) return false  //如果不可以触摸点击
                postInvalidateOnAnimation()
            }
            MotionEvent.ACTION_MOVE -> {
                postInvalidateOnAnimation()
                if (isTouch == TYPE_CLICK) return false  //如果不可以点击
            }
        }
        return true
    }

    /**
     * 计算值
     */
    fun computeProgress(event: MotionEvent) {


        var a = IntArray(2)
        //获取控件在屏幕的坐标点
        getLocationInWindow(a)
        //计算event.rawX触摸屏幕哪个点开始算触摸到了控件
        var touchPosition = event.rawX - a[0]
        /**
         * 用event.rawX 比 event.x更准确和更灵活
         * 因为event.x只能获取到在控件内部的活动轨迹
         * 但有些人想滑动的时候滑动出控件外部，所以
         * 用event.rawX来计算边界外的计算更精准
         */

        for ((i, rect) in starRects.withIndex()) {

            /**
             * 这个判断是用来处理边界的
             */
            if (i == 0) {
                if (touchPosition < rect.left) {
                    //第一个小于左边都为0
                    progress = 0
                    indexStar = 0f
                    break
                }
            } else if (i == starRects.size - 1) {
                if (touchPosition > rect.right) {
                    //最后一个大于右边的都是max
                    progress = maxProgress
                    indexStar = starCount.toFloat()
                    break
                }
            }

            /**
             * 如果不是用来处理边界问题的，用event.x还是比较方便的
             */
            if ((event.x >= rect.left && event.x <= rect.right)) {
                var rectWidth = rect.right - rect.left                      //计算单个星星所在的矩形宽度
                var average = maxProgress.toFloat() / starCount          //计算单个星星所占的进度
                var percent = rectWidth.toFloat() / average                 //计算单个星星所占进度的比例
                var consume = (rectWidth - (event.x - rect.left)) / percent //计算不满一个星星时所占的进度
                progress = ((i + 1) * average - consume).toInt() + 1        //计算总共选中了多少个星星，再减去不满一个星星时的进度
                /**
                 * 这里的progress做了+1的操作，是为了处理边界的问题，一般来说max=100的话
                 * progress的值的范围应该是0 - 99 或者 1 - 100，但是因为event.x的值即使
                 * 在rect.left = 0的情况下，也是选中了的，所以也是需要有进度，所以这里的判
                 * 断必须保证event.x是否在rect.left和rect.right之内才算精准
                 */
                break
            }
        }
    }

    /**
     * 测量宽高，设置宽高
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if ((layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT && layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT) ||
                (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                        layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT || layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT ||
                        layoutParams.width == ViewGroup.LayoutParams.MATCH_PARENT || layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT)) {
            //说明有某个使用了match或都为warp，那就使用星星默认大小或者星星大小的值
            var customWidth = ((starCount - 1) * defaultDistance) + (leftAndRightDistance * 2) + paddingLeft + paddingRight + (starCount * defaultStarWidth)
            var customHeight = paddingTop + paddingBottom + (topAndBottomDistance * 2) + defaultStarHeight
            setMeasuredDimension(customWidth, customHeight)
        } else {
            //否则指定了大小，就用指定的值
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }

    /**
     * 修改参数必须调用
     */
    fun newBuild() = Build(progress, maxProgress, defaultDistance, leftAndRightDistance, topAndBottomDistance, defaultStarWidth, defaultStarHeight, selectIcon, unSelectIcon, isTouch, starCount)

    /**
     * 数值监听
     */
    fun setOnProgressListener(action: (Float, Int) -> Unit) {
        onProgressListener = action
    }

    /**
     * 设置默认最小值
     */
    fun defaultMin() {
        if (defaultStarWidth <= 0) defaultStarWidth = 1
        if (defaultStarHeight <= 0) defaultStarHeight = 1
        if (leftAndRightDistance <= 0) leftAndRightDistance = 0
        if (topAndBottomDistance <= 0) topAndBottomDistance = 0
        if (progress <= 0) progress = 0
        if (maxProgress <= 0) maxProgress = 1
        if (starCount <= 0) starCount = 1
    }

    inner class Build(
            var progress: Int = 0,          //当前值
            var maxProgress: Int = 5,     //最大值
            var defaultDistance: Int = 10,  //默认间距
            var leftAndRightDistance: Int = 10,//左右两边的间距，默认0
            var topAndBottomDistance: Int = 10,//上下两边的间距，默认0
            var defaultStarWidth: Int = 50,  //默认星星宽度
            var defaultStarHeight: Int = 50, //默认星星高度
            var selectIcon: Int = R.mipmap.ic_grade_big_star_select,     //选中的图标，暂时没有默认
            var unSelectIcon: Int = R.mipmap.ic_grade_big_star_un_select,    //未选中图标，暂时没有默认
            var isTouch: Int = TYPE_TOUCH,    //是否可以触摸，到时候添加三个属性，可触摸(也可点击) = 0，可点击(只可以点击) = 1，不可触摸 = 2
            var starCount: Int = 5      //默认星星数量，当修改星星数的时候会去添加同样数量的框框
    ) {

        fun build() {
            var params = layoutParams

            if (this@JRatingBar.leftAndRightDistance < leftAndRightDistance) {
                params.width = params.width + (leftAndRightDistance * 2)
            } else if (this@JRatingBar.leftAndRightDistance > leftAndRightDistance) {
                params.width = params.width - (leftAndRightDistance * 2)
            }

            if (this@JRatingBar.defaultDistance < defaultDistance) {
                params.width = params.width + (defaultDistance * 2)
            } else if (this@JRatingBar.defaultDistance > defaultDistance) {
                params.width = params.width - (defaultDistance * 2)
            }

            if (progress >= maxProgress) {
                progress = maxProgress
            }

            this@JRatingBar.progress = progress
            this@JRatingBar.maxProgress = maxProgress
            this@JRatingBar.defaultDistance = defaultDistance
            this@JRatingBar.leftAndRightDistance = leftAndRightDistance
            this@JRatingBar.topAndBottomDistance = topAndBottomDistance
            this@JRatingBar.defaultStarWidth = defaultStarWidth
            this@JRatingBar.defaultStarHeight = defaultStarHeight
            this@JRatingBar.selectIcon = selectIcon
            this@JRatingBar.unSelectIcon = unSelectIcon
            this@JRatingBar.isTouch = isTouch
            this@JRatingBar.starCount = starCount

            defaultMin()
            layoutParams = params
            postInvalidateOnAnimation()
        }
    }
}