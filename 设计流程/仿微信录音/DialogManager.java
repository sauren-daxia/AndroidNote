package com.example.hp.myapplication.widget;


import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hp.myapplication.R;

public class DialogManager {
	private Dialog mDialog;
	private ImageView mIcon;
	private ImageView mVocie;
	private TextView mDialogtext;
	private Context mContext;

	public DialogManager(Context context) {
		this.mContext = context;
	}

	public void showRecordingDialog() {
		mDialog = new Dialog(mContext, R.style.Theme_AudioDialog);
		View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_type,
				null);
		mDialog.setContentView(view);

		//
		mIcon = (ImageView) view.findViewById(R.id.dialog_icon);

		mVocie = (ImageView) view.findViewById(R.id.dialog_voice);
		mDialogtext = (TextView) view.findViewById(R.id.text_dialog);
		mDialog.show();
	}

	/*
 * 正在录音时的Dialog的样式
 */
	public void recording() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVocie.setVisibility(View.VISIBLE);
			mDialogtext.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.mipmap.recorder);
			mDialogtext.setText(R.string.cancle);
		}
	}

	/*
	 * 取消发送时的Dialog的样式
	 */
	public void wantToCancel() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVocie.setVisibility(View.GONE);
			mDialogtext.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.mipmap.cancel);
			mDialogtext.setText(R.string.hello_want_cancel);
		}
	}

	/*
	 * 录音时间过段时的Dialog样式
	 */
	public void tooShort() {
		if (mDialog != null && mDialog.isShowing()) {
			mIcon.setVisibility(View.VISIBLE);
			mVocie.setVisibility(View.GONE);
			mDialogtext.setVisibility(View.VISIBLE);

			mIcon.setImageResource(R.mipmap.voice_to_short);
			mDialogtext.setText(R.string.recording_short);
		}
	}

	public void dismissDialog() {
		if (mDialog != null && mDialog.isShowing()) {
			mDialog.dismiss();
			mDialog = null;
		}
	}

	public void updataVoiceLevel(int level) {
		if (mDialog != null && mDialog.isShowing()) {

			// mIcon.setVisibility(View.VISIBLE);
			// mVocie.setVisibility(View.VISIBLE);
			// mDialogtext.setVisibility(View.VISIBLE);
			Log.d("info", "执行到了这里");
			int resource = mContext.getResources().getIdentifier("v" + level,
					"drawable", mContext.getPackageName());
			Log.d("info", "这张图片的ID" + resource);
			mVocie.setImageResource(resource);
			mDialogtext.setText(R.string.hello_want_cancel);
		}
	}
}
