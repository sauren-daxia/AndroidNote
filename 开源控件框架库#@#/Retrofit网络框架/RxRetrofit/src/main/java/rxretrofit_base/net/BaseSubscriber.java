package rxretrofit_base.net;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import com.rxretrofit_build.net.RxSubscriber;
import com.rxretrofit_build.util.CustomSharedPreferences;
import com.rxretrofit_build.util.MapUtil;
import com.rxretrofit_build.util.VerifyUtil;

import rxretrofit_base.model.KeyBean;
import rxretrofit_base.model.PushKeyBean;
import rxretrofit_base.model.TokenBean;


/**
 * Title:
 * Description:
 * Copyright:Copyright(c)2016
 * Company: Cree
 * CreateTime:16/11/29  17:22
 *
 * @author cree
 * @version 1.0
 */
public abstract class BaseSubscriber<T> extends RxSubscriber<T> {
    int[] codeError = {
            -1,
            206,
            401,
            402,
            403,
            404,
            2222,
            10003,
            10004,
            10005,
            10006,
            10007,
            10306,
            10308,
            10310,
            10311,
            10600,
            10700,
            10800,
            10801,
            20302,
            20201,
            2000106,
            2000107,
            2000108};


    public BaseSubscriber(Context context) {
        super(context);
    }


    @Override
    public void onNext(T o) {
        super.onNext(o);
        if (o instanceof TokenBean) {
            TokenBean tokenBean = (TokenBean) o;
            if (isShowMessage(tokenBean))
                Toast.makeText(context, ((TokenBean) o).getMessage(), Toast.LENGTH_SHORT).show();
//                SnackbarUtil.showBar(context, ((TokenBean) o).getMessage());
            //---------------------------请求回来都打印返回的信息---------------------------------
            String token = tokenBean.getToken();
            //---------------------------如果token不为空才往本地储存---------------------------------
            saveData(KeyBean.TOKEN, token);

            if (tokenBean.getData() instanceof PushKeyBean) {
                PushKeyBean dataBean = (PushKeyBean) tokenBean.getData();

                String push_key = dataBean.getPush_key();
                //---------------------------如果push_key不为空才往本地储存---------------------------------
                saveData(KeyBean.PUSH_KEY, push_key);
            }
        }

        if (o instanceof TokenBean) {
            //---------------------------如果token失效则重新请求---------------------------------
            if (((TokenBean) o).getCode() == -2)
//                KeXianSenApplication.getInstance().holdRquestBaskToken();
            if (((TokenBean) o).getCode() != 0) {
//                CustomLog.d("<<<< ----Error =错误码 = " + ((TokenBean) o).getCode() + ", Message = " + ((TokenBean) o).getMessage());
                onError(new Throwable(((TokenBean) o).getCode() + ""));
                return;
            }
        }
        onSuccess(o);

    }

    //---------------------------是否显示请求回来的message---------------------------------
    public boolean isShowMessage(TokenBean tokenBean) {
        if (context instanceof Activity && !VerifyUtil.isNullOrEmptyString(tokenBean.getMessage())) {
            if (!tokenBean.getMessage().contains("token"))
                return true;
            for (int i = 0; i < codeError.length; i++) {
                if (codeError[i] == tokenBean.getCode())
                    return true;
            }
        }
        return false;
    }

    /**
     * 保存数据的简化代码
     *
     * @param bean  枚举 key值
     * @param value
     */
    private void saveData(KeyBean bean, String value) {
        CustomSharedPreferences.saveString(context
                , new MapUtil()
                        .put(bean.valueOf(), value)
                        .build()
                , KeyBean.FILE_NAME.valueOf());
    }


    @Override
    public void onError(Throwable e) {
        super.onError(e);
        e.printStackTrace();
//        CustomLog.d(e);
        onFailure(e);
//        MobclickAgent.reportError(context,e);   //友盟Error统计
    }

    public abstract void onSuccess(T o);

    public abstract void onFailure(Throwable e);
}
