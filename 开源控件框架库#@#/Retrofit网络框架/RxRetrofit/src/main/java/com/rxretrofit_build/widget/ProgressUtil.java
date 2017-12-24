package com.rxretrofit_build.widget;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/12/2  14:44
 *
 * @author cree
 * @version 1.0
 */
public class ProgressUtil {
    public static ProgressDialog createProgress(Context context) {
        return new ProgressDialog(context);
    }
}
