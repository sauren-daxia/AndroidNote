package com.example.hp.myapplication.widget;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.example.hp.myapplication.R;


public class AudioButton extends Button implements AudioManager.AudioStateListener {
	//用于标志按钮的状态
	private static final int STATE_NORMAL = 1; // 默认状态
	private static final int STATE_RECORDING = 2; // 录音状态
	private static final int STATE_WANT_CANCEL = 3; // 取消录音
	// 标识当前状态
	private int mCurState = STATE_NORMAL;
	// 判断是否已经录音
	private boolean isRecording;

	// 用于设置触摸控件的Y的范围。超出则取消录音
	public static final int CANCEL_Y = 50;

	// 创建Dialog
	private DialogManager mDialog;

	// 创建录音对象
	private AudioManager mAudioManage;

	public AudioButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated construtor stub

		mDialog = new DialogManager(context);

		String dir = null;

		// 创建录音文件保存的路径
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			dir = Environment.getExternalStorageDirectory().getAbsolutePath()
					+ "/recordingS";
		}

		// 在控件初始化时就将保存录音的文件路径传给录音对象
		mAudioManage = AudioManager.getInstance(dir);

		mAudioManage.setOnAudioStateListener(this);
		// 监听长按时间
		setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				// TODO Auto-generated method stub


				mReady = true;
				// 当长按的时候，让录音对象准备
				mAudioManage.prepareAudio();
				return true;
			}
		});
	}

	public interface AudioFinishRecorderListener {
		void onFinish(float seconds, String filePath);
	}
	//回调接口变量
	private AudioFinishRecorderListener mListener;
	//回调接口
	public void setAudioFinishRecorderListener(
			AudioFinishRecorderListener listener) {
		this.mListener = listener;
	}

	private float time; // 计时录音时长，也用于判断录音过短
	/*
	 * 实时获取音量
	 */
	//实时获取音量的线程
	private Runnable mGetVoiceLevelRunnable = new Runnable() {
		@Override
		public void run() {
			while (isRecording) {
				try {
					Thread.sleep(100);
					time += 0.1f;
					// 通知录音对象更新音量
					Log.d("info", "计时" + time);
					handler.sendEmptyMessage(MSG_VOICE);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	};
	//用于标志Dialog的显示状态
	private static final int MSG_PREPARED = 0x001; // 显示Dialog
	private static final int MSG_VOICE = 0x002; // 录音
	private static final int MSG_DISMISS = 0x003; // 隐藏Dialog
	//用于创建、隐藏、更新Dialog
	private Handler handler = new Handler(new Handler.Callback() {
		@Override
		public boolean handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch (msg.what) {
				case MSG_PREPARED:
					// 修改是否在录音的boolean变量
					isRecording = true;
					// 当用户长按时初始化Dialog对象
					mDialog.showRecordingDialog();
					//开启线程计时并且实时获取录音等级
					Log.d("info", "准备开启线程去计时");
					new Thread(mGetVoiceLevelRunnable).start();
					break;
				case MSG_VOICE:
					// 更新音量
					mDialog.updataVoiceLevel(mAudioManage.getVoiceLevel(7));
					break;
				case MSG_DISMISS:
					// 当松开手的时候取消Dialog的显示
					mDialog.dismissDialog();
					break;

			}

			return false;
		}
	});

	@Override
	public void wellPrepared() {
		// TODO Auto-generated method stub
		Log.d("info", "开启音量监听");
		handler.sendEmptyMessage(MSG_PREPARED);
	}

	public AudioButton(Context context) {
		this(context, null);
		// TODO Auto-generated constructor stub
	}

	// 判断触发LongClick
	private boolean mReady;

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		Log.d("info", "点击了");
		// 获取控件在整父容器中的坐标
		int action = event.getAction();
		switch (action) {
			case MotionEvent.ACTION_DOWN:

				changeState(STATE_RECORDING);
				break;
			case MotionEvent.ACTION_MOVE:
				// 先判断是否在录音
				int x = (int) event.getX();
				int y = (int) event.getY();
				if (isRecording) {
					// 根据x,y的坐标判断是否要取消录音
					if (wantToCancel(x, y)) {
						changeState(STATE_WANT_CANCEL);
					} else {
						changeState(STATE_RECORDING);
					}
				}

				break;
			case MotionEvent.ACTION_UP:
				if (!mReady) {
					reset();
					return super.onTouchEvent(event);
				}
				if (!isRecording || time < 0.6f) {
					mDialog.tooShort();
					mAudioManage.cancel();
					handler.sendEmptyMessageDelayed(MSG_DISMISS, 1300);
				} else if (mCurState == STATE_RECORDING) { // 正常录制结束
					mDialog.dismissDialog();
					mAudioManage.release();
					if (mListener != null) {
						mListener.onFinish(time, mAudioManage.getFilePath());
					}

				} else if (mCurState == STATE_WANT_CANCEL) {
					mDialog.dismissDialog();
					mAudioManage.cancel();
				}
				reset();
				time = 0;
				break;

		}
		return super.onTouchEvent(event);
	}

	/*
	 * 恢复状态以及标志位
	 */
	private void reset() {
		// TODO Auto-generated method stub
		isRecording = false;
		mReady = false;
		changeState(STATE_NORMAL);
		Log.d("info", "1");
		setClickable(false);
	}

	private boolean wantToCancel(int x, int y) {
		// TODO Auto-generated method stub

		if (x < 0 || x > getWidth()) {
			return true;
		}
		if (y < -CANCEL_Y || y > getHeight() + CANCEL_Y) {
			return true;
		}
		return false;
	}

	/*
	 * 主要功能修改按钮的背景色及文字
	 */
	private void changeState(int state) {
		// TODO Auto-generated method stub
		// if (mCurState != state) {
		switch (state) {
			case STATE_NORMAL: // 默认情况下的Dialog显示
				mCurState = STATE_NORMAL;
				setBackgroundResource(R.drawable.button_normal);
				setText(R.string.normal);
				break;
			case STATE_RECORDING: // 正在录音情况下的显示
				mCurState = STATE_RECORDING;
				setBackgroundResource(R.drawable.button_recording);
				setText(R.string.hello_recording);
				if (isRecording) {
					mDialog.recording();
				}
				break;
			case STATE_WANT_CANCEL: // 取消时的显示
				mCurState = STATE_WANT_CANCEL;
				setBackgroundResource(R.drawable.button_recording);
				setText(R.string.hello_want_cancel);
				mDialog.wantToCancel(); // 取消显示
				break;
		}
	}

}
