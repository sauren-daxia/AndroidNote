package com.bwf.mycate.mycate.view;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.Scroller;


public class MyAutoScrollViewPager extends ViewPager {
	private Timer timer;
	private TimerTask task;
	private Handler handler = new Handler(new Handler.Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			setCurrentItem(msg.arg1, true);
			return false;
		}
	});

	public MyAutoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		try {
			Field field = ViewPager.class.getDeclaredField("mScroller");
			field.setAccessible(true);
			Scroller mScroller = new Scroller(getContext()) {

				@Override
				public void startScroll(int startX, int startY, int dx, int dy,
						int duration) {
					// TODO Auto-generated method stub
					super.startScroll(startX, startY, dx, dy, 1500);
				}
			};
			field.set(this, mScroller);
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void setAdapter(PagerAdapter arg0) {
		// TODO Auto-generated method stub
		super.setAdapter(arg0);
		startTask();
	}

	public void stopTask() {
		if (timer != null) {
			timer.cancel();
			timer = null;
		}
	}

	@Override
	public boolean onTouchEvent(MotionEvent arg0) {

		switch (arg0.getAction()) {
		case MotionEvent.ACTION_DOWN:
			stopTask();
			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_CANCEL:
			
			startTask();
			break;
		}

		return super.onTouchEvent(arg0);
	}

	public MyAutoScrollViewPager(Context context) {
		super(context, null);
		// TODO Auto-generated constructor stub
	}

	private int index = 1;

	public void startTask() {
		if (timer == null) {
			timer = new Timer();
			task = new TimerTask() {
				@Override
				public void run() {
					index = getCurrentItem();
					index++;
					if (index >= getAdapter().getCount()) {
						index = 0;
					}
					handler.sendMessage(handler.obtainMessage(0, index, 0));
					
				}
			};
			timer.schedule(task, 2500, 2500);
		}
	}

}
