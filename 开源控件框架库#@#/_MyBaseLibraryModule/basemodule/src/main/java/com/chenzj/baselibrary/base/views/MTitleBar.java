package com.chenzj.baselibrary.base.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chenzj.baselibrary.R;


/**
 * Created by wenjin on 2017/4/13.
 */

public class MTitleBar extends LinearLayout {
    private static final String TAG = MTitleBar.class.getSimpleName();
    ImageView commonbarLeftImg;
    LinearLayout commonbarLeftLayout;
    TextView commonbarTitle;
    ImageView commonbarRightImg;
    LinearLayout commonbarRightLayout;
    TextView commonbarLeftText;

    TextView commonbarRightText;

    private View view;

    public MTitleBar(Context context) {
        this(context, null);
    }

    public MTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        view = LayoutInflater.from(context).inflate(R.layout.layout_titlebar, this);
        initViews();
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mTitleBarAttrs);
        String title = typedArray.getString(R.styleable.mTitleBarAttrs_title);
        String leftText = typedArray.getString(R.styleable.mTitleBarAttrs_leftText);
        String rightText = typedArray.getString(R.styleable.mTitleBarAttrs_rightText);
        int leftImg = typedArray.getResourceId(R.styleable.mTitleBarAttrs_leftImg, 0);
        int rightImg = typedArray.getResourceId(R.styleable.mTitleBarAttrs_rightImg, 0);
        setLeftText(leftText);
        setRightText(rightText);
        setLeftImg(leftImg);
        setRightImg(rightImg);
        setTitle(title);
    }

    private void initViews() {
        commonbarRightText = (TextView) view.findViewById(R.id.commonbar_right_text);
        commonbarLeftText = (TextView) view.findViewById(R.id.commonbar_left_text);
        commonbarRightLayout = (LinearLayout) view.findViewById(R.id.commonbar_right_layout);
        commonbarRightImg = (ImageView) view.findViewById(R.id.commonbar_right_img);
        commonbarTitle = (TextView) view.findViewById(R.id.commonbar_title);
        commonbarLeftLayout = (LinearLayout) view.findViewById(R.id.commonbar_left_layout);
        commonbarLeftImg = (ImageView) view.findViewById(R.id.commonbar_left_img);
    }

    /**
     * ���ñ���
     *
     * @param title
     */
    public void setTitle(String title) {
        if (title != null) {
            commonbarTitle.setText(title);
        }
    }

    public String getTitle() {
        if (commonbarTitle.getText() == null) {
            return "";
        } else {
            return commonbarTitle.getText().toString();
        }
    }

    /**
     * ���ñ�����з�ʽ
     */
    public void setTitleGravity(int gravity){
        commonbarTitle.setGravity(gravity);
    }

    /**
     * �����������
     *
     * @param text
     */
    public void setLeftText(String text) {
        if (text == null) {
            text = "";
        }
        commonbarLeftText.setText(text);
    }

    /**
     * �����ұ�����
     *
     * @param text
     */
    public void setRightText(String text) {
        if (text == null) {
            text = "";
        }
        commonbarRightText.setText(text);
    }

    /**
     * �����ұ�������ɫ
     * @param color
     */
    public void setRigthTextColor(int color){
        commonbarRightText.setTextColor(color);
    }

    /**
     * �������ͼƬ
     *
     * @param resourceId
     */
    public void setLeftImg(int resourceId) {
        if (resourceId != 0) {
            commonbarLeftImg.setVisibility(VISIBLE);
            commonbarLeftImg.setImageResource(resourceId);
        } else {
            commonbarLeftImg.setVisibility(GONE);
        }
    }

    /**
     * ������ͼƬ
     *
     * @param resourceId
     */
    public void setRightImg(int resourceId) {
        if (resourceId != 0) {
            commonbarRightImg.setVisibility(VISIBLE);
            commonbarRightImg.setImageResource(resourceId);
        } else {
            commonbarRightImg.setVisibility(GONE);
        }
    }

    /**
     * �����ұ߰�ť����
     *
     * @param onClickListen
     */
    public void setRightBtnOnClickListen(OnClickListener onClickListen) {
        commonbarRightLayout.setOnClickListener(onClickListen);
    }

    /**
     * ������߰�ť����
     *
     * @param onClickListen
     */
    public void setLeftBtnOnClickListen(OnClickListener onClickListen) {
        commonbarLeftLayout.setOnClickListener(onClickListen);
    }

    /**
     * �Ҳ�����
     */
    public void setRightGone(){
        commonbarRightLayout.setVisibility(GONE);
    }
    /**
     * �Ҳ���ʾ
     */
    public void setRightVISIABLE(){
        commonbarRightLayout.setVisibility(VISIBLE);
    }

    public TextView getRightText(){
        return commonbarRightText;
    }
    public TextView getLeftText(){
        return commonbarLeftText;
    }
    public ImageView getRightImg(){
        return commonbarRightImg;
    }
    public ImageView getLeftImg(){
        return commonbarLeftImg;
    }
}
