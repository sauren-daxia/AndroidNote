package com.rxretrofit_build.net;

import android.app.ProgressDialog;
import android.content.Context;

import com.rxretrofit_build.util.ApplicationUtil;
import com.rxretrofit_build.widget.ProgressUtil;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/28  18:13
 *
 * @author cree
 * @version 1.0
 */
public class RxSubscriber<T> extends SuperBaseSubscriber<T> {
    protected Context context;
    private boolean isShow = true;

    public RxSubscriber(Context context) {
        this.context = context;
        if (isShow && context != null)
            progressDialog = ProgressUtil.createProgress(context);


    }

    ProgressDialog progressDialog;

    public Context getContext() {
        return context;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (isDialogBoolean())
            progressDialog.show();
    }


    @Override
    public void onCompleted() {
        if (isDialogBoolean())
            progressDialog.dismiss();
    }

    @Override
    public void onError(Throwable e) {
        if (isDialogBoolean())
            progressDialog.dismiss();

    }


    @Override
    public void onNext(T o) {


    }

    public void setShowProgress(Boolean isShow) {
        this.isShow = isShow;
    }

    private boolean isDialogBoolean() {
        return isShow && progressDialog != null && context != null && !ApplicationUtil.isBackground(context);
    }
}
