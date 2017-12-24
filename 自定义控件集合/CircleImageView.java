package view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Jason Chen on 2017/10/30.
 */

public class CircleImageView extends ImageView {
    public CircleImageView(Context context) {
        super(context);
        initColor(null);
    }
    public CircleImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initColor(attrs);
    }
    public CircleImageView(Context context, AttributeSet attrs,
                           int defStyle) {
        super(context, attrs, defStyle);
        initColor(attrs);
    }

    private Paint paint;
    private Path path;
    private boolean init = false;
    private int background = Color.WHITE;

    private void initColor(AttributeSet attrs){
        if(attrs != null){
            String v = attrs.getAttributeValue("http://schemas.android.com/apk/res/android", "background");
            if(v != null){
                if(v.startsWith("#")){
                    background = Color.parseColor(v);
                }else{
                    background = getResources().getColor(Integer.parseInt(v.replaceAll("@", "")));
                }
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if(!init){
            initPaint();
        }
    }

    private void initPaint(){
        paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(background);
        //paint.setColor(Color.TRANSPARENT);
        paint.setAntiAlias(true);
        paint.setStrokeWidth(2);
        //paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
        int width = getMeasuredWidth();
        int radius = width/2;

        path = new Path();
        path.moveTo(0, radius);
        path.lineTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width, width);
        path.lineTo(0, width);
        path.lineTo(0, radius);
        //圆弧左边中间起点是180,旋转360度
        path.arcTo(new RectF(0, 0, radius*2, radius*2), 180, -359);
        path.close();

        init = true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);

    }
}
