package com.chenzj.baselibrary.base.manager;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Jason Chen on 2017/9/21.
 */

public class ToastManager {
    private static ToastManager manager;
    private Toast toast;
    private static Context con;
    public static ToastManager getInstance(){
        if(manager == null){
            synchronized (ToastManager.class){
                if(manager == null){
                    manager =new ToastManager();
                }
            }
        }
        return manager;
    }

    public static void init(Context context){
        con = context;
    }

    public void showToast(String text){
        if (toast == null) {
            toast = Toast.makeText(con, text, Toast.LENGTH_SHORT);
        } else {
            toast.setText(text);
        }
        toast.show();
    }
}
