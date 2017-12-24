package view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.dinghong.studyviewdemo1.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jason Chen on 2017/11/27.
 */

public class CustomRatingBar extends View {
    private float progress;     //当前值
    private float maxCount = 0;     //最大值
    private float starWidth = 35;    //星星的宽度，默认35
    private float starHeight = 35;   //星星的高度,默认35
    private float distance = 10;     //每个星星的间距
    private float starCount = 5;    //星星个数，默认5
    private int selectStarIcon = 0;//选中的星星大小
    private int unSelectstarIcon = 0;//未选中的星星大小
    private int width = (int) ((starWidth * 5) + (distance * 3));   //总长度
    private int height = (int) starHeight;  //总高度
    private Paint paint = new Paint();
    private List<Rect> rects = new ArrayList<>();//储存每个星星的坐标
    private boolean isTouch = true;         //是否可触控


    public CustomRatingBar(Context context) {
        super(context,null);

    }

    public CustomRatingBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs, 0);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomRatingBar);

        maxCount = typedArray.getInteger(R.styleable.CustomRatingBar_maxCount, 0);
        progress = typedArray.getInteger(R.styleable.CustomRatingBar_progress, 0);
        unSelectstarIcon = typedArray.getResourceId(R.styleable.CustomRatingBar_un_select_icon, 0);
        selectStarIcon = typedArray.getResourceId(R.styleable.CustomRatingBar_select_icon, 0);
        distance = typedArray.getInteger(R.styleable.CustomRatingBar_distance, 0);
        isTouch = typedArray.getBoolean(R.styleable.CustomRatingBar_isTouch, true);
        init();
    }



    private void init() {
        paint.setAntiAlias(true);
        for (int i = 0; i < starCount; i++) {
            Rect rect = new Rect();
            rects.add(rect);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Bitmap selectBitmap = changeBitmap(selectStarIcon);
        Bitmap unSelectBitmap = changeBitmap(unSelectstarIcon);
        if (selectBitmap == null || unSelectBitmap == null) {
            Log.e("Custom", "onDraw: 选中或未选中的资源为空");
            return;
        }
        int starLocaionX = 0;
        int starLocaionY = 0;
        if (getWidth() != width) {
            //如果总长度不相等则计算中间值再画
            starLocaionX = (int) (((getWidth() - (selectBitmap.getWidth() * starCount) - (distance * 3))) / 2 - (distance / 2));
        }
        if (starLocaionY != height) {
            starLocaionY = (getHeight() - (selectBitmap.getHeight())) / 2;
        }

        for (int i = 0; i < starCount; i++) {
            Rect rect = rects.get(i);
            rect.left = (int) (starLocaionX + (distance * i) + (i * selectBitmap.getWidth()));
            rect.top = starLocaionY;
            rect.right = rect.left + selectBitmap.getWidth();
            rect.bottom = starLocaionY + selectBitmap.getHeight();
            if (i < progress) {
                canvas.drawBitmap(selectBitmap, rect.left, rect.top, paint);
            } else {
                canvas.drawBitmap(unSelectBitmap, rect.left, rect.top, paint);
            }
        }
    }

    /**
     * 修改Bitmap大小
     *
     * @param resource
     * @return
     */
    public Bitmap changeBitmap(int resource) {
        if (resource <= 0) {
            return null;
        }
        Bitmap bitMap = BitmapFactory.decodeResource(getResources(), resource);
        int width = bitMap.getWidth();
        int height = bitMap.getHeight();
        // 设置想要的大小
        int newWidth = (int) (getWidth() / starCount - (distance));
        int newHeight = newWidth;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        return Bitmap.createBitmap(bitMap, 0, 0, width, height, matrix, true);
    }

    /**
     * 动态改变星星个数
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (isTouch && rects.size() > 0) {  //是否可以触控
                float rawX = event.getRawX();
                float rawY = event.getRawY();
                for (int i = 0; i < rects.size(); i++) {
                    if ((rawX > rects.get(i).left && rawX < rects.get(i).right)) {  //是否在某个星星内
                        progress = i + 1;
                    } else if ((rawX < rects.get(0).left)) {                        //是否小于第一个星星
                        progress = 0;
                    } else if (rawX > rects.get((int) (rects.size() - 1)).right) {     //是否大于最后一个星星
                        progress = starCount;
                    }
                }
                postInvalidate();
            }
        }
        return true;
    }

    /**
     * 设置选中的星星Icon
     *
     * @param resource
     */
    public void setSelectStarIcon(int resource) {
        this.selectStarIcon = resource;
    }

    /**
     * 设置未选中星星Icon
     *
     * @param resource
     */
    public void setUnSelectStarIcon(int resource) {
        this.unSelectstarIcon = resource;
    }

    /**
     * 最小选中个数
     *
     * @return
     */
    public void setProgress(float progress) {
        if (progress > 5) {
            this.progress = 5;
        } else {
            this.progress = progress;
        }
    }

    /**
     * 获取最大个数
     *
     * @return
     */
    public float getMaxCount() {
        return maxCount;
    }

    /**
     * 设置最大数值，目前最多为5
     *
     * @param maxCount
     */
    public void setMaxCount(float maxCount) {
        if (maxCount > 5) {
            this.maxCount = 5;
        } else {
            this.maxCount = maxCount;
        }
    }

    /**
     * 获取星星个数
     *
     * @return
     */
    public float getStarCount() {
        return starCount;
    }

    /**
     * 设置星星个数，目前最大支持5
     *
     * @param starCount
     */
    public void setStarCount(float starCount) {
        if (starCount > 5) {
            this.starCount = 5;
        } else {
            this.starCount = starCount;
        }
    }

    /**
     * 获取是否支持触摸
     *
     * @return
     */
    public boolean isTouch() {
        return isTouch;
    }

    /**
     * 设置是否支持触摸
     *
     * @param touch
     */
    public void setTouch(boolean touch) {
        isTouch = touch;
    }

    /**
     * 获取间距
     *
     * @return
     */
    public float getDistance() {
        return distance;
    }

    /**
     * 设置间距
     *
     * @param distance
     */
    public void setDistance(float distance) {
        this.distance = distance;
    }
}
