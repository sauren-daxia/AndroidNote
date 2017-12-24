package com.bwf.recipeapp.view;

import com.bwf.recipeapp.R;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

public class ResynclerImageView extends ImageView{
	private float  leftTop;
	private float  leftBottom;
	private float  RightTop;
	private float  RightBottom;
	public RoundedCornerRecyclingImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,R.styleable.RoundedCornerRecyclingImageView); 
		leftTop = a.getDimension(R.styleable.RoundedCornerRecyclingImageView_left_top, 0);
		leftBottom = a.getDimension(R.styleable.RoundedCornerRecyclingImageView_left_bottom, 0);
		RightTop = a.getDimension(R.styleable.RoundedCornerRecyclingImageView_right_top, 0);
		RightBottom = a.getDimension(R.styleable.RoundedCornerRecyclingImageView_right_bottom, 0);
		a.recycle();
	}
	@Override
	public void setImageBitmap(Bitmap bm) {
		super.setImageBitmap(getBitmap(this, bm));
	}
	private Bitmap getBitmap(ImageView img,Bitmap bitmapSrc){
		int imgWidth = img.getWidth();
		int imgHeight = img.getHeight();
		if(imgWidth == 0 || imgHeight == 0){
			return bitmapSrc;
		}
		int bitmapW = bitmapSrc.getWidth();
		int bitmapH = bitmapSrc.getHeight();
		Bitmap bitmapDst = Bitmap.createBitmap(imgWidth, imgHeight, Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmapDst);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		//
		Path path = new Path();
		path.moveTo(0,leftTop);
		path.lineTo(imgWidth, RightTop);
		path.lineTo(imgWidth, imgHeight-RightBottom);
		path.lineTo(0, imgHeight-leftBottom);
		path.close();
		canvas.drawPath(path, paint);
		//为画笔设置Xfermode
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));

		RectF dstRect = new RectF(0, 0, imgWidth, imgHeight);

		Rect src = null;
		if(1f*imgWidth/imgHeight > 1f*bitmapW/bitmapH){
			//代表截图的图片的高度
			int finalHeight = imgHeight*bitmapW/imgWidth;
			src = new Rect(0, (bitmapH-finalHeight)/2, bitmapW, bitmapH-(bitmapH-finalHeight)/2);
		}else{
			//代表截图的图片的宽度
			int finalWidth = imgWidth*bitmapH/imgHeight;
			src = new Rect((bitmapW-finalWidth)/2, 0, bitmapW-(bitmapW-finalWidth)/2, bitmapH);
		}
		canvas.drawBitmap(bitmapSrc, src , dstRect, paint);
		return bitmapDst;
	}
	private Bitmap bitmap;
	@Override
	protected void onDraw(Canvas canvas) {
		if(bitmap == null && getDrawable() != null){
			if(getDrawable() instanceof BitmapDrawable){
				BitmapDrawable bd = (BitmapDrawable) getDrawable();
				bitmap = bd.getBitmap();
				setImageBitmap(bitmap);
			}
		}
		super.onDraw(canvas);
	}


}
