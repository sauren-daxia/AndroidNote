package com.example.hp.myapplication.widget;

import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class AudioManager {
	private MediaRecorder mRecorder; // 录音对象
	private String mDir; // 文件夹名
	private String dstPath; // 文件夹路径

	private boolean isPrepared;
	private static AudioManager mInstance;

	private AudioManager(String dir) {
		mDir = dir;
	}

	/*
	 * 回调
	 */
	public interface AudioStateListener {
		void wellPrepared();
	}

	public AudioStateListener mListener;

	public void setOnAudioStateListener(AudioStateListener linstener) {
		mListener = linstener;
	}

	public static AudioManager getInstance(String dir) {
		if (mInstance == null) {
			synchronized (AudioManager.class) {
				if (mInstance == null) {
					mInstance = new AudioManager(dir);
				}
			}
		}
		return mInstance;
	}

	/*
	 * 开始，准备
	 */
	public void prepareAudio() {
		isPrepared = false;
		try {
			File dir = new File(mDir);
			if (!dir.exists()) {
				dir.mkdirs();
			}
			String fileName = getFileName();
			File file = new File(dir, fileName);
			dstPath = file.getAbsolutePath();
			mRecorder = new MediaRecorder();
			// 设置输出文件
			mRecorder.setOutputFile(file.getAbsolutePath());
			// 设置MediaRecorder的数据来源，来源于麦克风
			mRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			// 设置文件格式
			mRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
			// 设置音频编码
			mRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
			// 准备
			mRecorder.prepare();
			// 开始
			mRecorder.start();
			// 准备结束
			isPrepared = true;
			if (mListener != null) {
				mListener.wellPrepared();
			}
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/*
	 * 随机生成文件名称
	 */
	private String getFileName() {
		return UUID.randomUUID() + ".amr";
	}

	/*
	 * 获取音量等级
	 */
	public int getVoiceLevel(int maxLevel) {
		// 正在播放情况
		Log.d("info", "maxLevel" + maxLevel);
		if (isPrepared) {
			try {
				// mRecorder.getAudioSourceMax()方法可以实时获取音量等级由1~32768
				// Log.d("info",
				// "正在获取音量 " + maxLevel * mRecorder.getAudioSourceMax()
				// / 32768 + 1);
				return maxLevel * mRecorder.getAudioSourceMax() / 32768 + 1; // 控制为7个等级
			} catch (Exception e) {
				// TODO Auto-generated catch block
			}
		}

		return 1; // 默认是1级
	}

	/*
	 * 释放
	 */
	public void release() {
		mRecorder.stop();
		mRecorder.release();
		mRecorder = null;
	}

	/*
	 * 取消录音
	 */
	public void cancel() {
		release();
		if (dstPath != null) {
			File file = new File(dstPath);
			file.delete();
		}
		dstPath = null;
	}

	public String getFilePath() {
		// TODO Auto-generated method stub
		return dstPath;
	}
}
