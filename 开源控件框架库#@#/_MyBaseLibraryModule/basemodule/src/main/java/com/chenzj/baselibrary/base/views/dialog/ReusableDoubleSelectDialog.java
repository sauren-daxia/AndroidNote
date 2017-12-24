package com.chenzj.baselibrary.base.views.dialog;

import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.chenzj.baselibrary.R;
import com.chenzj.baselibrary.base.views.BaseDialog;


/**
 * Created by Jason Chen on 2017/9/9.
 */

public class ReusableDoubleSelectDialog extends BaseDialog {
    private TextView contextTv;
    private Button sureBtn, cancelBtn;
    private String content;
    private String sureText, cancelText;
    private View.OnClickListener sureClickListener, cancelClickListener;

    @Override
    protected int getRootView() {
        return R.layout.dialog_reusable_double_select;
    }

    @Override
    protected void initView() {
        contextTv = (TextView) rootView.findViewById(R.id.tv_content);
        sureBtn = (Button) rootView.findViewById(R.id.tv_sure);
        cancelBtn = (Button) rootView.findViewById(R.id.tv_cancel);


        if (sureClickListener != null) {
            sureBtn.setOnClickListener(sureClickListener);
        } else {
            sureBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        if (cancelClickListener != null) {
            cancelBtn.setOnClickListener(cancelClickListener);
        } else {
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dismiss();
                }
            });
        }
        contextTv.setText(content);
        sureBtn.setText(sureText);
        cancelBtn.setText(cancelText);
        this.setCancelable(isEnableCancel);
    }

    /**
     * 设置内容
     *
     * @param content  内容
     * @param sureText 按钮文字
     */
    public ReusableDoubleSelectDialog setContext(String content, String cancelText, String sureText) {
        this.content = content;
        this.sureText = sureText;
        this.cancelText = cancelText;
        return this;
    }

    public void setClickListener(View.OnClickListener cancelClickListener, View.OnClickListener sureClickListener) {
        this.sureClickListener = sureClickListener;
        this.cancelClickListener = cancelClickListener;
    }

    public void show(FragmentManager manager, String content, String cancelText, String sureText, View.OnClickListener cancelClickListener, View.OnClickListener sureClickListener, boolean isCancel) {
        this.setContext(content, cancelText, sureText);
        this.setClickListener(cancelClickListener, sureClickListener);
        this.isEnableCancel = isCancel;
        if (this.isAdded()) {
            manager.beginTransaction().show(this);
        } else {
            this.show(manager, "singleDialog");
        }
    }
}
