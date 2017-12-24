package com.example.rx2retrofit.base;

import com.example.rx2retrofit.client.Rx;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by hp on 2017/5/22.
 * Observer基类
 */
public abstract class BaseObserver<T> extends Rx implements Observer<T> {

    @Override
    public void onSubscribe(Disposable d) {

    }

    @Override
    public void onNext(T value) {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onComplete() {

    }

	/**
	*
	  protected boolean isShowDialog;
    protected SweetAlertDialog loadDialog;      //第三方库Dialog，可改其他的
    protected Context dialogContext;

    public BaseObserver() {

    }

    public BaseObserver(Context dialogContext,boolean isShowDialog ) {
        this.isShowDialog = isShowDialog;
        this.dialogContext = dialogContext;
    }

    @Override
    public void onSubscribe(Disposable d) {
        if(isShowDialog && dialogContext!=null){
            loadDialog = new SweetAlertDialog(dialogContext,SweetAlertDialog.PROGRESS_TYPE);
            loadDialog.setTitleText("正在加载中...");
            loadDialog.show();
            loadDialog.setCancelable(true);
        }
    }

    @Override
    public void onNext(T value) {
        if(loadDialog !=null ){
            loadDialog.dismissWithAnimation();
            loadDialog.dismiss();
        }
    }

    @Override
    public void onError(Throwable e) {
        if(loadDialog !=null ){
            loadDialog.dismissWithAnimation();
            loadDialog.dismiss();
        }
    }

    @Override
    public void onComplete() {
        if(loadDialog !=null ){
            loadDialog.dismissWithAnimation();
            loadDialog.dismiss();
        }
    }
	*/
}
