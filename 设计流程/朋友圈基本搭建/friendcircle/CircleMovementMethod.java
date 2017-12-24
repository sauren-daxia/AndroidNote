package com.dsfa.lovepartybuild.ui.widget.friendcircle;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.BaseMovementMethod;
import android.text.method.Touch;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

import com.dsfa.lovepartybuild.APP;
import com.dsfa.lovepartybuild.R;

/**
 * Created by hp on 2017/6/1.
 */
public class CircleMovementMethod extends BaseMovementMethod{
    public final String TAG = CircleMovementMethod.class.getSimpleName();
    private final static int DEFAULT_COLOR_ID = R.color.transparent;
    private final static int DEFAULT_CLICKABLEA_COLOR_ID = 0xFF546a92;
    /**整个textView的背景色*/
    private int textViewBgColor;
    /**点击部分文字时部分文字的背景色*/
    private int clickableSpanBgClor;

    private BackgroundColorSpan mBgSpan;
    private ClickableSpan[] mClickLinks;
    private boolean isPassToTv = true;
    /**
     * true：响应textview的点击事件， false：响应设置的clickableSpan事件
     */
    public boolean isPassToTv() {
        return isPassToTv;
    }
    private void setPassToTv(boolean isPassToTv){
        this.isPassToTv = isPassToTv;
    }

    public CircleMovementMethod(){
        this.textViewBgColor = APP.getContext().getResources().getColor(DEFAULT_COLOR_ID);
        this.clickableSpanBgClor = DEFAULT_CLICKABLEA_COLOR_ID;
    }

    /**
     *
     * @param clickableSpanBgClor  点击选中部分时的背景色
     */
    public CircleMovementMethod(int clickableSpanBgClor){
        this.clickableSpanBgClor = clickableSpanBgClor;
        this.textViewBgColor = APP.getContext().getResources().getColor(DEFAULT_COLOR_ID);
    }

    /**
     *
     * @param clickableSpanBgClor 点击选中部分时的背景色
     * @param textViewBgColor 整个textView点击时的背景色
     */
    public CircleMovementMethod(int clickableSpanBgClor, int textViewBgColor){
        this.textViewBgColor = textViewBgColor;
        this.clickableSpanBgClor = clickableSpanBgClor;
    }


    private Spannable buffer;
    private TextView widget;

    public boolean onTouchEvent(TextView widget, Spannable buffer,
                                MotionEvent event) {
        this.buffer = buffer;
        this.widget = widget;

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                mClickLinks = buffer.getSpans(off, off, ClickableSpan.class);
                if(mClickLinks.length > 0){
                    // 点击的是Span区域，不要把点击事件传递
                    setPassToTv(false);
                    Selection.setSelection(buffer,
                            buffer.getSpanStart(mClickLinks[0]),
                            buffer.getSpanEnd(mClickLinks[0]));
                    //设置点击区域的背景色
                    mBgSpan = new BackgroundColorSpan(clickableSpanBgClor);
                    buffer.setSpan(mBgSpan,
                            buffer.getSpanStart(mClickLinks[0]),
                            buffer.getSpanEnd(mClickLinks[0]),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }else{
                    setPassToTv(true);
                    // textview选中效果
                    widget.setBackgroundColor(textViewBgColor);
                }

                break;

            case MotionEvent.ACTION_UP:
                if(mClickLinks.length > 0){
                    mClickLinks[0].onClick(widget);
                    if(mBgSpan != null){//移除点击时设置的背景span
                        buffer.removeSpan(mBgSpan);
                    }
                }else{

                }
                Selection.removeSelection(buffer);
                widget.setBackgroundResource(R.color.transparent);

                break;
            default:
                cancelLongClickcStyle();
                break;
        }


        return Touch.onTouchEvent(widget, buffer, event);
    }

    public void cancelLongClickcStyle(){
        if(buffer!= null && widget != null) {
            if (mBgSpan != null) {
                buffer.removeSpan(mBgSpan);
            }
            Selection.removeSelection(buffer);
            widget.setBackgroundResource(R.color.transparent);
        }
    }
}
