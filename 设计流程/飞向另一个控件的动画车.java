package com.bwf.duobao.utils;


import com.bwf.duobao.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class AnimationHander {
	private ViewGroup parentView;
	private FrameLayout frameLayout; 
	private int[] mCarLocation;
	private View mCarView;
	private Handler mHandler;
	public void setAnimation(Context context,ImageView lastImage,int endImage){
		if (parentView==null) {
			Activity activity = (Activity) context;
			parentView=(ViewGroup) activity.getWindow().getDecorView();
			frameLayout = new FrameLayout(context);
			parentView.addView(frameLayout);
			//找到购物车View
			mCarView = activity.findViewById(endImage);
			//获取购物车View在窗口中的位置
			mCarLocation = new int[2];
			mCarView.getLocationInWindow(mCarLocation);
			mHandler = new Handler();
		}
		//新建一个imageview
		final ImageView imageView  = new ImageView(context);
		//给1imageview加布局参数
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
				lastImage.getWidth(), lastImage.getHeight());
		//给他设置图片
		Drawable drawable = lastImage.getDrawable();
		imageView.setImageDrawable(drawable);
		//获取 源img在窗口中的位置
				int[] location = new int[2]; //用来存图片的x，y坐标
				lastImage.getLocationInWindow(location);
				params.leftMargin = location[0];
				params.topMargin = location[1];
		//将View添加进imgContainer容器
		frameLayout.addView(imageView, params);
		
		//创建要执行的动画
				AnimationSet set = new AnimationSet(true);
				AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0.4f);
				ScaleAnimation scaleAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f, 
						Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
				
				float toXDelta = mCarLocation[0] - location[0] - (lastImage.getWidth()-mCarView.getWidth())/2;
				float toYDelta = mCarLocation[1] - location[1] - (lastImage.getHeight()-mCarView.getHeight())/2;
				TranslateAnimation translateAnimation = new TranslateAnimation(0, toXDelta, 0, toYDelta);
				
				//将动画添加到AnimationSet
				set.addAnimation(alphaAnimation);
				set.addAnimation(scaleAnimation);
				set.addAnimation(translateAnimation);
				set.setDuration(1000);
				imageView.startAnimation(set);
				set.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						mHandler.post(new Runnable() {
							
							@Override
							public void run() {
								frameLayout.removeView(imageView);
							}
						});
						
					}
				});
	}
}
