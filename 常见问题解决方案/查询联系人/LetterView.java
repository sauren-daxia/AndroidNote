package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.orhanobut.logger.Logger;

import jason.testproject.R;

/**
 * Created by 陈志坚 on 2016/11/22.
 */

public class LetterView extends View {
    public static final String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWSYZ#";
    private Paint letterPaint;

    public LetterView(Context context) {
        super(context, null);
    }

    public LetterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        inits();
    }

    private void inits() {
        letterPaint = new Paint();
        letterPaint.setTextSize(30);
        letterPaint.setAntiAlias(true);
    }

    private int width, height;
    private float baseline;//基准线
    private float singleHeight;
    private int currentIndex;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0) {
            width = getWidth();
            height = getHeight();
            Paint.FontMetrics fontMetrics = letterPaint.getFontMetrics();
            singleHeight = height / LETTERS.length();
            RectF rectF = new RectF(0, 0, width, singleHeight);
            baseline = (rectF.bottom + rectF.top - fontMetrics.bottom - fontMetrics.top) / 2;
        }

        //绘制
        for (int i = 0; i < LETTERS.length(); i++) {
            String text = LETTERS.charAt(i) + "";
            float x = (width - letterPaint.measureText(text)) / 2;
            if (i == currentIndex) {
                letterPaint.setColor(getResources().getColor(R.color.colorFF5021));
            } else {
                letterPaint.setColor(getResources().getColor(R.color.color011763));
            }
            canvas.drawText(text, x, baseline + i * singleHeight, letterPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int index = (int) (event.getY() / singleHeight);
        if (index < 0 || index > singleHeight) {
            return true;
        }
        if (index != currentIndex) {
            if (index >= LETTERS.length()) {
                index = LETTERS.length() - 1;
            }
            currentIndex = index;
            invalidate();
            if (navigationListener != null) {
                Logger.d("<><<>>indexindex = = = " + LETTERS.charAt(index));
                Logger.d("<><<>>index = = = " + LETTERS.charAt(index));
                navigationListener.onNavigation(LETTERS.charAt(index) + "");
            }
        }
        return true;
    }


    public interface OnNavigationListener {
        void onNavigation(String index);
    }

    public OnNavigationListener navigationListener;

    public void setOnNavigationListener(OnNavigationListener listener) {
        this.navigationListener = listener;
    }

    public void changeLetter(char text) {
        if ((text + "").equals(LETTERS.charAt(currentIndex))) {
            return;
        }
        for (int i = 0; i < LETTERS.length(); i++) {
            if (LETTERS.charAt(i) == text) {
                if (i != currentIndex) {
                    currentIndex = i;
                    invalidate();
                }
                return;
            }
        }
    }
}
