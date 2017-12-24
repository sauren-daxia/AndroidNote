package com.bjesc.app.rlimtest.im;

import android.util.Log;

import com.google.gson.Gson;
import com.yuntongxun.ecsdk.ECChatManager;
import com.yuntongxun.ecsdk.ECDevice;
import com.yuntongxun.ecsdk.ECError;
import com.yuntongxun.ecsdk.ECMessage;
import com.yuntongxun.ecsdk.OnChatReceiveListener;
import com.yuntongxun.ecsdk.im.ECMessageNotify;
import com.yuntongxun.ecsdk.im.ECTextMessageBody;
import com.yuntongxun.ecsdk.im.group.ECGroupNoticeMessage;

import java.util.List;

/**
 * Created by Jason Chen on 2017/8/25.
 * 聊天基础管理类
 */

public class IMChattingManager implements OnChatReceiveListener, ECChatManager.OnDownloadMessageListener {
    public static IMChattingManager IMChat;
    private OnMessageListener listener;
    private ECMessage msg = ECMessage.createECMessage(ECMessage.Type.TXT);      //必须要的变量

    public static IMChattingManager getInstance() {
        if (IMChat == null) {
            synchronized (IMChattingManager.class) {
                if (IMChat == null) {
                    IMChat = new IMChattingManager();
                }
            }
        }
        return IMChat;
    }

    //--------------------------------------华丽丽的分割线

    /**
     * 主要功能，接收即时通讯信息
     *
     * @param msg
     */
    @Override
    public void OnReceivedMessage(ECMessage msg) {
        if (msg == null) {
            return;
        }
        ECMessage.Type type = msg.getType();
        if (type == ECMessage.Type.TXT) {
            //文本类型
            ECTextMessageBody textMessageBody = (ECTextMessageBody) msg.getBody();
            String json = textMessageBody.getMessage();
            MLog.d("IM", "--------收到的信息 msg = " + json);
            try {
                IMMsgBean msgBean = new Gson().fromJson(json, IMMsgBean.class);
                if (listener != null) {
                    listener.onMsg(msgBean);
                }
            } catch (Exception e) {
                Log.d("IM", "--------收到的信息 解析异常 = " + e.getMessage());
            }

        }
    }

    /**
     * 发送普通文本
     * @param otherID   对方的ID
     * @param text      要发送的消息
     * @param lat       纬度
     * @param lon       经度
     */
    public void sendText(String otherID,String text,String lat,String lon) {
        try {
            IMMsgBean bean = new IMMsgBean(text,lat,lon);
            //对方账号
            msg.setTo(otherID);
            //发送的消息内容
            ECTextMessageBody msgBody = new ECTextMessageBody(new Gson().toJson(bean));
            //把消息体存放到消息对象中
            msg.setBody(msgBody);
            ECChatManager manager = ECDevice.getECChatManager();
            manager.sendMessage(msg, new ECChatManager.OnSendMessageListener() {
                @Override
                public void onSendMessageComplete(ECError error, ECMessage message) {

                    // 处理消息发送结果
                    if (message == null) {
                        MLog.d("IM","自己发送的消息为空 msg = "+error.errorMsg);
                        return;
                    }
                    ECTextMessageBody textMessageBody = (ECTextMessageBody) msg.getBody();
                    MLog.d("IM","自己发送的消息 msg = "+textMessageBody.getMessage());
                    // 将发送的消息更新到本地数据库并刷新UI
                }

                @Override
                public void onProgress(String msgId, int totalByte, int progressByte) {
                    // 处理文件发送上传进度（尽上传文件、图片时候SDK回调该方法）
                }
            });
        } catch (Exception e) {
            // 处理发送异常
            MLog.d("IM","自己发送的消息异常 msg = "+e.getMessage());
        }
    }

    @Override
    public void onOfflineMessageCount(int i) {
        //返回有多少条历史记录，通过onGetOfflineMessage决定获取多少条
    }

    @Override
    public void onReceiveOfflineMessage(List<ECMessage> list) {
        //TODO
        //离线消息，这里可能会用到,获取最新的消息，处理方式和获取普通消息一样

    }

    @Override
    public void onReceiveOfflineMessageCompletion() {
        // SDK通知应用离线消息拉取完成
    }

    @Override
    public void onServicePersonVersion(int i) {
        // SDK通知应用当前帐号的个人信息版本号
    }

    @Override
    public int onGetOfflineMessage() {
        //决定要获取多少条历史记录
        // ECDevice.SYNC_OFFLINE_MSG_ALL;全部
        //0，不获取
        return 0;
    }

    @Override
    public void onReceiveMessageNotify(ECMessageNotify ecMessageNotify) {
    }

    //群组消息，暂时不理会
    @Override
    public void OnReceiveGroupNoticeMessage(ECGroupNoticeMessage ecGroupNoticeMessage) {
    }

    @Override
    public void onReceiveDeskMessage(ECMessage ecMessage) {
    }

    @Override
    public void onSoftVersion(String s, int i) {
    }

    @Override
    public void onDownloadMessageComplete(ECError ecError, ECMessage ecMessage) {
    }

    @Override
    public void onProgress(String s, int i, int i1) {
    }

    public interface OnMessageListener {
        void onMsg(IMMsgBean bean);
    }

    public void setOnMessageListener(OnMessageListener listener) {
        this.listener = listener;
    }
}
