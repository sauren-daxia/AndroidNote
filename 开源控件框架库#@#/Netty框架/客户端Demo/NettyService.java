package com.sisiam.batmessage.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;
import android.util.Base64;

import com.google.gson.Gson;
import com.sisiam.batmessage.activity.ReceiveInvitationActivity;
import com.sisiam.batmessage.applicatioin.Iapplication;
import com.sisiam.batmessage.utils.LogUtil;
import com.sisiam.batmessage.utils.Utils;
import com.sisisan.data.bean.BaseBean;
import com.sisisan.data.bean.nio.ChatMessageBean;
import com.sisisan.data.bean.nio.NioConfig;
import com.sisisan.data.bean.nio.NioLoginBean;
import com.sisisan.data.bean.nio.NioWebRtcBean;
import com.sisisan.data.bean.responsebean.LoginBean;
import com.sisisan.data.bean.responsebean.RequestAddFriendBean;
import com.sisisan.data.utils.MySPUtils;
import com.sisisan.data.utils.PackageUtils;
import com.sisisan.data.utils.TimeUtils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Netty服务
 * Created by 张亮 on 2017/8/29.
 */

public class NettyService extends IntentService {
    private static final String ACTION_START_NETTY_CLIENT = "com.sisisan.NettyService";
    private static final String TAG = "NettyService";
    private Channel open;
    private LoginBean loginBean;
    private static final int PACKETLENGTH = 5000;  //切包长度（超过此长将分包）
    private static int OVERTIME = 10;  //发心跳后如超过这个时间未收到回复则判断通讯中断
    private boolean isSendPonG = false;  //是否发送心跳
    private Gson gson;


    public NettyService() {
        super("NettyService");
    }
    public static void startNettyService(Context context ,LoginBean loginBean){
        Intent intent = new Intent(context, NettyService.class);
        intent.setAction(ACTION_START_NETTY_CLIENT);
        intent.putExtra("LoginBean", loginBean);
        context.startService(intent);
    }
    public class GetNettyClient extends Binder {
        public NettyService getService() {
            return NettyService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new GetNettyClient();
    }
    @Override
    public void onCreate() {
        super.onCreate();
        gson = new Gson();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START_NETTY_CLIENT.equals(action)) {
                loginBean = (LoginBean) intent.getSerializableExtra("LoginBean");
                String msgServer = loginBean.getMsgServer();
                int i = msgServer.indexOf(":");
                String host = msgServer.substring(0,i);
                String port = msgServer.substring(i+1);
                connect(Integer.parseInt(port),host);
            }
        }
    }
    private void connect(int port, String host){
        NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, true);
            bootstrap.group(nioEventLoopGroup);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ByteBuf buf = Unpooled.copiedBuffer("####".getBytes());
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024*10, buf));
                    //ch.pipeline().addLast(new LineBasedFrameDecoder(2048));
                    // Charset c = Charset.forName("utf-8");
                    //ch.pipeline().addLast(new FixedLengthFrameDecoder(48));
                    //ch.pipeline().addLast(new StringDecoder(c));
                    // ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(2048, 0, 19));
                    //ch.pipeline().addLast("encoder", new LengthFieldPrepender(19, false));
                    // 添加心跳Handler
                    ch.pipeline().addLast("idleStateHandler", new IdleStateHandler(20000, 20000, 20000, TimeUnit.MILLISECONDS));
                    ch.pipeline().addLast(new SocketChannelHandler());
                }
            });
            ChannelFuture f = null;

            f = bootstrap.connect(host, port).sync();
            if (f.isSuccess()) {
                open = f.channel();
            }
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
            nioEventLoopGroup.shutdownGracefully();
        }

    }
    private class SocketChannelHandler extends ChannelHandlerAdapter{
        @Override
        public void channelActive(ChannelHandlerContext ctx) throws Exception {
            LogUtil.d(TAG,loginBean.getmUid()+"  :连接--成功");
            NioLoginBean niologinBean = new NioLoginBean();
            niologinBean.setUid(loginBean.getmUid());
            niologinBean.setMsgServer(loginBean.getMsgServer());
            niologinBean.setTime(String.valueOf(TimeUtils.getCurrentTime()));
            niologinBean.setToken(loginBean.getmToken());
            String s1 = new Gson().toJson(niologinBean);
            byte[] bytes = PackageUtils.getInstance().DataPackaging(s1,1);
            LogUtil.d(TAG,loginBean.getmUid()+"  :WREIT------"+new String(bytes,"UTF-8"));
            ByteBuf buf  = Unpooled.buffer(bytes.length);
            buf.writeBytes(bytes);
            ctx.writeAndFlush(buf);
        }
        @Override
        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
            try{
                ByteBuf buf = (ByteBuf) msg;
                byte[] buff = new byte[buf.readableBytes()];
                buf.readBytes(buff);
                LogUtil.d(TAG,"数据未解包："+new String(buff));
                PackageUtils.getInstance().DataParsing(buff, new PackageUtils.CodeListener() {
                    @Override
                    public void Listener(String data, int code1, int code) {
                        LogUtil.d(TAG,"数据成功与否："+code1+"   数据类型:"+code + "  数据："+data);
                        if (code1==100){
                            switch (code){
                                case NioConfig.TCP_TRANSFER:  //服务器内部中转消息
                                    break;
                                case NioConfig.TCP_LOGIN: //登录
                                    getOfflineMessage();  //请求离线消息
                                    break;
                                case NioConfig.TCP_TEXT: //单聊消息
                                    HandleSingleMessage(data, NioConfig.MSG_TEXT);
                                    break;
                                case NioConfig.TCP_GROUP://群聊
                                    break;
                                case NioConfig.TCP_WEBRTC: //webrtc 信令
                                    HandleFileMessage(data);
                                    break;
                                case NioConfig.TCP_REQ_ADD_FRIEND://请求添加好友
                                    break;
                                case NioConfig.TCP_RES_ADD_FRIEND://回复添加好友
                                    break;
                                case NioConfig.TCP_GET_OFFLINE_MSG://获取离线消息
                                    break;
                                case NioConfig.TCP_GET_FRIEND_LIST://获取好友列表
                                    break;
                                case NioConfig.TCP_GET_ADD_FRIEND://收到添加好友请求
                                    try {
                                        BaseBean bean = new BaseBean();
                                        bean.setCode(Utils.EVENT_BUS_REFRESH_ADD_FRIEND_EVENT);
                                        EventBus.getDefault().post(bean);//通知更新添加好友事件的数量
                                        RequestAddFriendBean requestAddFriendBean =  new Gson().fromJson(data, RequestAddFriendBean.class);
                                        Utils.createNotification(NettyService.this, "Add friend verification",
                                                requestAddFriendBean.getmNickname()+" want add you as a friend", Utils.REQUEST_ADD_FRIEND_NOTIFY, ReceiveInvitationActivity.class);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case NioConfig.TCP_FRIEND_STATUS_CHANGE://好友上下线通知
                                    break;
                                case NioConfig.TCP_PROMPT_MSG://提示信息
                                    ChatMessageBean chatMessageBean = gson.fromJson(data, ChatMessageBean.class);
                                    if (!TextUtils.isEmpty(chatMessageBean.getFp())){
                                        HandleSingleMessage(data,NioConfig.MSG_HZ);
                                    }
                                    break;
                                case NioConfig.TCP_PING://发送ping心跳包
                                    isSendPonG = false;
                                    break;
                                case NioConfig.TCP_SINGLE_CHAT_RECALL_MSG://撤回单聊消息
                                    break;
                                case NioConfig.TCP_PRIVATE_MSG://私密聊天
                                    break;
                                case NioConfig.TCP_OK_ADD_FRIEND://收到对方同意添加好友的请求
                                    try {
                                        RequestAddFriendBean requestAddFriendBean =  new Gson().fromJson(data, RequestAddFriendBean.class);
                                        if(!TextUtils.isEmpty(requestAddFriendBean.getmNickname())){
                                            BaseBean bean = new BaseBean();
                                            bean.setCode(Utils.EVENT_BUS_REFRESH_ADD_FRIEND_SUCCESS);
                                            EventBus.getDefault().post(bean);//通知更新添加好友事件的数量
                                            Utils.createNotification(NettyService.this, "Add friend verification",
                                                    requestAddFriendBean.getmNickname()+" has passed your friend verification", Utils.REPOSE_ADD_FRIEND_NOTITY, null);

                                        }
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                    break;
                                case NioConfig.TCP_SEND_IMAGE://:发送图片
                                    HandleSingleMessage(data,NioConfig.MSG_IMAGE);
                                    break;
                                case NioConfig.TCP_SEND_VOICE://发语音
                                    HandleSingleMessage(data,NioConfig.MSG_VOICE);
                                    break;
                                case NioConfig.TCP_OK_READ://对方阅读了单聊消息
                                    HandleSingleMessage(data,NioConfig.MSG_READ);
                                    break;
                                case NioConfig.TCP_BCARD://发送名片
                                    HandleSingleMessage(data,NioConfig.MSG_BCARD);
                                    break;
                                case NioConfig.TCP_ADDRSS://发地址
                                    HandleSingleMessage(data,NioConfig.MSG_ADDRSS);
                                    break;
                                case NioConfig.TCP_VIDEO://视频
                                    HandleSingleMessage(data,NioConfig.MSG_VIDEO);
                                    break;
                            }
                        }else if (code1==101){
                        }
                    }
                });
                buf.retain();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            cause.printStackTrace();
            LogUtil.d(TAG,":通道关闭 error！！！"+cause.getMessage());
            ctx.close();
            open.close();
            open = null;
            Iapplication.getServiceHandler().startNettyService();
        }
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if (evt instanceof IdleStateEvent) {
                IdleStateEvent e = (IdleStateEvent) evt;
                if (e.state() == IdleState.READER_IDLE) {
//                    LogUtil.d(TAG,"READER_IDLE 读超时");
                } else if (e.state() == IdleState.WRITER_IDLE) {
//                    LogUtil.d(TAG,"WRITER_IDLE 写超时");
                } else if (e.state() == IdleState.ALL_IDLE) {
//                    LogUtil.d(TAG, "all idle超时");
                    LogUtil.d(TAG, "发送心跳包~~~");
                    String ping = "bat_msg";
                    byte[] bytes = ping.getBytes();
                    byte[] bytes1 = PackageUtils.getInstance().DataPackaging(bytes,12);
                    ByteBuf buffer = Unpooled.buffer(bytes1.length);
                    buffer.writeBytes(bytes1);
                    open.writeAndFlush(buffer);
                    isSendPonG = true;
                }
            }
        }
    }
    /**
     * 处理服务器发来的文件消息
     * @param str
     */
    private void HandleFileMessage(String str){
        try {
            NioWebRtcBean nioWebRtcBean = gson.fromJson(str,NioWebRtcBean.class);
            EventBus.getDefault().post(nioWebRtcBean);//通知更新添加好友事件的数量
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 处理TCP发过来的单聊消息
     */
    private void HandleSingleMessage(String msg,String type){
        try {
            ChatMessageBean chatMessageBean = gson.fromJson(msg, ChatMessageBean.class);
            chatMessageBean.setType(type);
            EventBus.getDefault().post(chatMessageBean);//通知更新添加好友事件的数量
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取离线消息
     */
    private void getOfflineMessage(){
        Map<String,String> has = new HashMap<>();
        has.put("uid", MySPUtils.getUid());
        has.put("token", MySPUtils.getToken());
        String s = new Gson().toJson(has);
        send(s,7);
    }

    public boolean send(String data,int code){
        if(open != null){
            try {
                if (!open.isOpen()){
                    open.close();
                    open = null;
                    return false;
                }
                byte[] bytes1 = PackageUtils.getInstance().DataPackaging(data,code);
                LogUtil.d(TAG,"WREIT------发送的文件包长度（带封包数据）"+new String(bytes1));
                ByteBuf buffer = Unpooled.buffer(bytes1.length);
                buffer.writeBytes(bytes1);
                open.writeAndFlush(buffer);
                return true;
            }catch (Exception e){
                LogUtil.d(TAG,"错误:"+e.toString());
                e.printStackTrace();
                return false;
            }
        }else {
            return false;
        }
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
