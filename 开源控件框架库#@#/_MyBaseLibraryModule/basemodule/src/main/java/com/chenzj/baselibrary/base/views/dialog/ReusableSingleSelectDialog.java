package com.chenzj.baselibrary.base.views.dialog;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenzj.baselibrary.R;
import com.chenzj.baselibrary.base.views.BaseDialog;


/**
 * Created by Jason Chen on 2017/8/31.
 * 可重复使用的Dialog
 */

public class ReusableSingleSelectDialog extends BaseDialog {
    private TextView contextTv;
    private Button sureBtn;
    private String content;
    private String sureText;
    private View.OnClickListener surelClickListener;

    @Override
    protected int getRootView() {
        return R.layout.dialog_reusable_single_select;
    }

    @Override
    protected void initView() {
        contextTv = (TextView) rootView.findViewById(R.id.tv_content);
        sureBtn = (Button) rootView.findViewById(R.id.btn_sure);
        if (surelClickListener != null) {
            sureBtn.setOnClickListener(surelClickListener);
        } else {
            rootView.findViewById(R.id.btn_sure).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        contextTv.setText(content);
        sureBtn.setText(sureText);
        this.setCancelable(isEnableCancel);
    }


    /**
     * 设置内容
     *
     * @param content  内容
     * @param sureText 按钮文字
     */
    public ReusableSingleSelectDialog setContext(String content, String sureText) {
        this.content = content;
        this.sureText = sureText;
        return this;
    }

    public void setClickListener(View.OnClickListener surelClickListener) {
        this.surelClickListener = surelClickListener;
    }

    public void show(FragmentManager manager, String content, String sureText, View.OnClickListener surelClickListener, boolean isCancel) {
        this.setContext(content, sureText);
        this.setClickListener(surelClickListener);
        this.isEnableCancel = isCancel;
        if (this.isAdded()) {
            manager.beginTransaction().show(this);
        } else {
            this.show(manager, "singleDialog");
        }
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.add(this, tag);
        fragmentTransaction.commitAllowingStateLoss();
    }
}
