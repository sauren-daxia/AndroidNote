package com.dsfa.lovepartybuild.ui.widget.friendcircle;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * Created by hp on 2017/6/2.
 */
public abstract class SpannableClickable extends ClickableSpan implements View.OnClickListener {

    private int DEFAULT_COLOR_ID = 0xFF8290AF;
    /**
     * text颜色
     */
    private int textColor ;

    public SpannableClickable() {
        this.textColor = DEFAULT_COLOR_ID;
    }

    public SpannableClickable(int textColor){
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);

        ds.setColor(textColor);
        ds.setUnderlineText(false);
        ds.clearShadowLayer();
    }

}
