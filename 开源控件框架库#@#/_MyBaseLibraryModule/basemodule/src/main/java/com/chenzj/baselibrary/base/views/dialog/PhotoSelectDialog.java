package com.chenzj.baselibrary.base.views.dialog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.chenzj.baselibrary.R;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.List;

import static android.app.Activity.RESULT_OK;
import static com.luck.picture.lib.config.PictureConfig.LUBAN_COMPRESS_MODE;


/**
 * Created by Jason Chen on 2017/7/31.
 * 选择图片Dialog
 */

public class PhotoSelectDialog extends DialogFragment implements View.OnClickListener {

    private View rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        rootView = LayoutInflater.from(getActivity()).inflate(R.layout.layout_select_photo, null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().getAttributes().windowAnimations = R.style.window_bottom_anim;
        initViews();
        return rootView;
    }

    private void initViews() {
        rootView.findViewById(R.id.text_capture).setOnClickListener(this);
        rootView.findViewById(R.id.text_photo).setOnClickListener(this);
        rootView.findViewById(R.id.text_cancel).setOnClickListener(this);

    }

    @Override
    public void onStart() {
        super.onStart();
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.gravity = Gravity.BOTTOM;
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(params);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    /**
     * 打开相册
     */
    private void openGallery() {
        PictureSelector.create(getActivity())
                .openGallery(PictureMimeType.ofImage())
                .minSelectNum(1)
                .maxSelectNum(1)
                .previewImage(true)
                .enableCrop(true)
                .isZoomAnim(true)
                .compress(true)
                .compressMode(LUBAN_COMPRESS_MODE)
                .hideBottomControls(true)
                .freeStyleCropEnabled(true)
                .showCropFrame(true)
                .previewEggs(true)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }

    /**
     * 打开相机
     */
    private void openCamear() {
        PictureSelector.create(getActivity())
                .openCamera(PictureMimeType.ofImage())
                .previewImage(true)
                .enableCrop(true)
                .compress(true)
                .hideBottomControls(true)
                .freeStyleCropEnabled(true)
                .showCropFrame(true)
                .previewEggs(true)
                .compressMode(LUBAN_COMPRESS_MODE)
                .forResult(PictureConfig.CHOOSE_REQUEST);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == PictureConfig.CHOOSE_REQUEST && data != null) {
            List<LocalMedia> list = PictureSelector.obtainMultipleResult(data);
            if (list != null && list.size() > 0) {
                String path = "";
                if (list.get(0).isCompressed()) {
                    path = list.get(0).getCompressPath();
                } else if (list.get(0).isCut()) {
                    path = list.get(0).getCutPath();
                } else {
                    path = list.get(0).getPath();
                }
                if (!TextUtils.isEmpty(path)) {
                    Intent intent = new Intent();
                    intent.putExtra("path", path);
                    PhotoSelectDialog.this.getActivity().setResult(2, intent);
                } else {
                    Toast.makeText(PhotoSelectDialog.this.getActivity(), "图片选择错误", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.text_cancel) {
            dismiss();
        } else if (i == R.id.text_capture) {//拍照
            openCamear();
        } else if (i == R.id.text_photo) {//单选
            openGallery();
        }
    }
}
