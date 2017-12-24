package com.bjesc.app.rlimtest.im;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.bjesc.app.rlimtest.BuildConfig;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECInitParams;
import com.yuntongxun.ecsdk.ECNotifyOptions;
import com.yuntongxun.ecsdk.SdkErrorCode;

import static android.util.Config.LOGD;

/**
 * Created by Jason Chen on 2017/8/25.
 * 通讯SDK管理类
 */

public class IMManager implements ECDevice.InitListener, ECDevice.OnECDeviceConnectListener, ECDevice.OnLogoutListener {
    private static IMManager IM;

    public static IMManager getInstance() {
        if (IM == null) {
            synchronized (IMManager.class) {
                if (IM == null) {
                    IM = new IMManager();
                }
            }
        }
        return IM;
    }

    //--------------------------------------华丽丽的分割线
    private Context con;
    private Handler handler;
    private boolean mKickOff;
    private ECInitParams mInitParams;
    private ECNotifyOptions mOptions;
    private ECInitParams.LoginMode mMode = ECInitParams.LoginMode.FORCE_LOGIN;
    private ECDevice.ECConnectState mConnect = ECDevice.ECConnectState.CONNECT_FAILED;

    /**
     * 提供对外的初始化方法
     *
     * @param con
     */
    public static void init(Context con) {
        init(con, ECInitParams.LoginMode.AUTO);
    }

    public static void init(Context con, ECInitParams.LoginMode mode) {
        getInstance().mKickOff = false;
        getInstance().mMode = mode;
        getInstance().con = con;
        if (!ECDevice.isInitialized()) {
            getInstance().mConnect = ECDevice.ECConnectState.CONNECTING;
            ECDevice.initial(getInstance().con, getInstance());
            return;
        }
        getInstance().onInitialized();
    }


    /**
     * 初始化成功
     */
    @Override
    public void onInitialized() {
        // 设置接收VoIP来电事件通知Intent
        // 呼入界面activity、开发者需修改该类
        // 设置SDK注册结果回调通知，当第一次初始化注册成功或者失败会通过该引用回调
        // 通知应用SDK注册状态
        // 当网络断开导致SDK断开连接或者重连成功也会通过该设置回调
        ECDevice.setOnDeviceConnectListener(this);                          //设置连接回调
        ECDevice.setOnChatReceiveListener(IMChattingManager.getInstance()); //设置消息回调
        //创建登录对象
        mInitParams = ECInitParams.createParams();
        //设置登录参数
        login("");
    }

    /**
     * 登录，使用账号登录
     *
     * @param name 账号
     */
    public void login(String name) {
        //自定义登录方式
        //推荐使用客户项目APP的登录帐号，测试阶段Userid可以填写手机号
        mInitParams.setUserid(name);
        //应用ID；登陆官网查看控制台→应用列表→应用管理→APP ID
        mInitParams.setAppKey(BuildConfig.IM_APP_KEY);
        //应用Token；登陆官网查看控制台→应用列表→应用管理→APP TOKEN
        mInitParams.setToken(BuildConfig.IM_APP_TOKEN);
        //设置登陆验证模式：自定义登录方式
        mInitParams.setAuthType(ECInitParams.LoginAuthType.NORMAL_AUTH);
        //LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO。使用方式详见注意事项）
        mInitParams.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        //验证参数是否正确
        if (mInitParams.validate()) {
            // 登录函数
            ECDevice.login(mInitParams);
        }
    }

    /**
     * 通过注册的容联账号登录
     *
     * @param name     账号
     * @param password 密码
     */
    public void login(String name, String password) {
        //账号验密登录方式
        //通讯帐号；通过创建子帐户接口获得
        mInitParams.setUserid(name);
        //通讯密码；通过创建子帐户接口获得
        mInitParams.setPwd(password);
        //应用ID；登陆官网查看控制台→应用列表→应用管理→APP ID
        mInitParams.setAppKey(BuildConfig.IM_APP_KEY);
        //设置登陆验证模式：通讯账号验密登录方式
        mInitParams.setAuthType(ECInitParams.LoginAuthType.PASSWORD_AUTH);
        //LoginMode（强制上线：FORCE_LOGIN  默认登录：AUTO）
        mInitParams.setMode(ECInitParams.LoginMode.FORCE_LOGIN);
        //验证参数是否正确
        if (mInitParams.validate()) {
            // 登录函数
            ECDevice.login(mInitParams);
        }
    }


    /**
     * 退出登录
     */
    public void logout() {
        ECDevice.logout(this);
    }

    /**
     * 回调链接状态
     *
     * @param state   状态
     * @param ecError 错误码 200是正常
     */
    @Override
    public void onConnectState(ECDevice.ECConnectState state, ECError ecError) {
        if (state == ECDevice.ECConnectState.CONNECT_FAILED) {
            if (ecError.errorCode == SdkErrorCode.SDK_KICKED_OFF) {
                MLog.d("IM", "账号异地登录");
            } else {
                MLog.d("IM", "登录失败，错误码：" + ecError.errorCode);
            }
            return;
        } else if (state == ECDevice.ECConnectState.CONNECT_SUCCESS) {
            MLog.d("IM", "登录成功");
        }
    }

    @Override
    public void onError(Exception e) {
        MLog.d("IM", "登录或初始化失败 = "+e.getMessage());
    }


    //不需要操作
    @Override
    public void onConnect() {
    }

    //不需要操作
    @Override
    public void onDisconnect(ECError ecError) {
    }

    @Override
    public void onLogout() {
    }
}
