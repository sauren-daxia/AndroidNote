package com.ddtx.kexiansen.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.ddtx.kexiansen.R;
import com.ddtx.kexiansen.adapter.PayOrderGoodsNameAdapter;
import com.ddtx.kexiansen.base.BaseActivity;
import com.ddtx.kexiansen.model.PayResultBean;
import com.ddtx.kexiansen.util.LogUtil;
import com.ddtx.kexiansen.util.SignUtils;
import com.ddtx.kexiansen.widget.NoScrollListView;
import com.ddtx.kexiansen.widget.PayBackDialog;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;


public class PayForNowActivity extends BaseActivity implements View.OnClickListener {


    private NoScrollListView goodsNameNslv;
    private Dialog payDialog;
    private TextView transationNumTv,totalPriceTv,consigneeNameTv,consigneeTelTv,detailAddressTv;
    private TextView payMoneyTv,continueTv,checkOrderTv;
    private LinearLayout paySuccessRlty;
    private TextView titleTv;
    private TextView hintTv;            //提示语
    // 商户PID
    public static final String PARTNER = "2088121404397850";  //旧的
    // 商户收款账号
    public static final String SELLER = "1409578950@qq.com";
    // 商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdQIBADANBgkqhkiG9w0BAQEFAASCAl8wggJbAgEAAoGBAN3ghYWY7x7zi1XiotV6qxh63vpzScF+m8UUC3HT3ppI4fXmH37iVByZwb/NzJ3ajYE33sOE0UdleYFzrs8F7qin5gd1S24nMgbw+YrMzu6uc/TQ3YVWPtPVst4YTksGMUibSaiUCJGxKmPHwxq+JfqIvY/FcWysTvrrKWtMS1hxAgMBAAECgYB3sRkWeuv0DHnL+RjShPwrlo0XGn5dTYmGhUuvOhZJsKuE1YGEF2A8lY15hPGu7imL3bHkU4/6LfmgJeHVeGPHwrW6ZFDOFC8cNOAByKGzAXNbSvn62uN1VaZHpJnnp+8Du+rwnWbAC7d/yUAEFPPjXg9OllXoYpjvuqz90YlZyQJBAPBl0kPK8ug6Wn9QzGC70m1eOxsrOet0QVq+ClcCxsVHznnQg5eNyGk+cCCL+753qU4jRRLpMcAScFrLlGGGrTMCQQDsRvsmGIat/jst+5dUku6VeCtZLPWL6NpGRfYDaLXtpkwKvvV0GzkQmnbxQ4IUB0sAgpgqKkeTlP7bo6d0lfvLAkA4yjUD4tvDVJWwYewGJhit0YbxAiMqpkoSH7bdm0BNtormaAXONaZUhnWCms8bzbTnSpm0v/YKw9BbAqDI8UHVAkBMFc9qRRuZi/SiqhHhOi96EmhjHwczNpiTaJQAJQfwr0xtaXDBIsMNhbAnoQWSTYbpcyRs75B9Bc3MDLcv49xhAkAaXgZvahKbupbNJU+hTUOzPeAYXvaEyOprH7E8nZZz/VogQkCsQBBNUNtH4yPyNWE5ndthZgb8On2rKIdSdntY";
    // 支付宝公钥
//    public static final String RSA_PUBLIC = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDgR2BLLb0YIi12h5YQMQFVOkScia30G3DDGJAs05CPK2mN4Rn+/NntjK8wVpjfJPF8QqKmYApVuxUT1fS5VFaYjdS4zpOiiDKC50Ns8fdTHULFzMRDuV6P1cT3eMWYnHp49uqvi1Gc7hrSNNSRH5bsdaf/C0fYKGjPSIenGTT+fwIDAQAB";

    private int payType = 1;        //1= 线上支付，2=货到付款
    private String consigneeName;       //收货人名
    private String consigneeTel;        //收货人电话
    private String detailAddress;       //收货详情地址
    private List<String> goodsNames;           //商品名称
    private int dispatching= 1;                 //配送方式

    //支付号
    private String pay_no;
    //支付成功回调服务器地址
    private String notifyUrl;
    private String totalMoney;          //传给支付宝的总金额
    private String title;               //传给支付宝的商品标题
    private String details;             //传给支付宝的详情介绍

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_for_now);
        //未知测试地址
//        notifyUrl = "http://" + EConfig.HOST + EConfig.KEY_HOST_WEB_BISUNESS + "pay/alipayNotify";
        //测试回调通知服务器地址
//        notifyUrl = "http://" + EConfig.HOST_DEBUG + EConfig.KEY_HOST_WEB_BISUNESS + "callback.appNotify";
        //线上服务器回调地址
        notifyUrl = "http://api.v3.kexiansen.com/callback.appNotify";

        payType = getIntent().getIntExtra("payType", 1);
        payDialog = new PayBackDialog(this, this);
        pay_no = getIntent().getStringExtra("pay_no");
        title = getIntent().getStringExtra("title");                        //商品标题
        details = getIntent().getStringExtra("details");                    //详情介绍
        dispatching = getIntent().getIntExtra("dispatching", 1);                           //配送方式
        totalMoney = getIntent().getStringExtra("totalMoney");              //总金额
        consigneeName = getIntent().getStringExtra("consigneeName");        //收货人姓名
        consigneeTel = getIntent().getStringExtra("consigneeTel");          //收货人电话
        detailAddress = getIntent().getStringExtra("detailAddress");        //收货人地址
        goodsNames = getIntent().getStringArrayListExtra("goodsNames");   //所有商品名
        Log.d(TAG, "onCreate: payTyep = "+payType +" , pay_no = "+ pay_no+" , \ntitle = "+ title+" , details = "+ details + ", \n totalMoney = " + totalMoney);
        initView();
    }

    private void initView() {
        findViewById(R.id.iv_return).setOnClickListener(this);
        findViewById(R.id.rlyt_alipay).setOnClickListener(this);
        hintTv= (TextView) findViewById(R.id.tv_hint);
        transationNumTv = (TextView) findViewById(R.id.tv_transaction_num);
        if (payType == 1) {
            findViewById(R.id.llyt_paynow).setVisibility(View.VISIBLE);
            findViewById(R.id.llyt_payrecomment).setVisibility(View.GONE);
            findViewById(R.id.rlyt_alipay).setVisibility(View.VISIBLE);
            if(dispatching ==1){
                hintTv.setText(getString(R.string.dispatching_three));
            }else{
                hintTv.setText(getString(R.string.dispatching_four));
            }
        } else {
            findViewById(R.id.llyt_paynow).setVisibility(View.GONE);
            findViewById(R.id.llyt_payrecomment).setVisibility(View.VISIBLE);
            findViewById(R.id.rlyt_alipay).setVisibility(View.GONE);
            if(dispatching ==1){
                hintTv.setText(getString(R.string.dispatching_one));
            }else{
                hintTv.setText(getString(R.string.dispatching_two));
            }
        }



        findViewById(R.id.llyt_iknow).setOnClickListener(this);
        totalPriceTv = (TextView) findViewById(R.id.tv_order_price);
        consigneeNameTv = (TextView) findViewById(R.id.tv_order_name);
        consigneeTelTv = (TextView) findViewById(R.id.tv_order_tel);
        detailAddressTv = (TextView) findViewById(R.id.tv_order_add);
        payMoneyTv = (TextView) findViewById(R.id.tv_paymoney);
        continueTv = (TextView) findViewById(R.id.tv_continue);
        checkOrderTv = (TextView) findViewById(R.id.tv_checkorder);
        paySuccessRlty = (LinearLayout) findViewById(R.id.rlty_paysuccess);
        continueTv.setOnClickListener(this);
        checkOrderTv.setOnClickListener(this);
        findViewById(R.id.btn_iknow).setOnClickListener(this);
        goodsNameNslv = (NoScrollListView) findViewById(R.id.nslv_goodsname);
        if(goodsNames!=null && goodsNames.size() > 0){
            PayOrderGoodsNameAdapter goodsNameAdapter = new PayOrderGoodsNameAdapter(this, goodsNames, payType);
            goodsNameNslv.setAdapter(goodsNameAdapter);
        }
        transationNumTv.setText("订单提交成功，请您尽快付款！\n系统交易号：" + pay_no);     //设置系统交易号
        totalPriceTv.setText(totalMoney);
        consigneeNameTv.setText(consigneeName);
        consigneeTelTv.setText(consigneeTel);
        detailAddressTv.setText(detailAddress);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                if (payType == 1 ) {
                    payDialog.show();
                } else if(payType ==2){
                    intentCheckOrder("new");
                }else {
                    finish();
                }
                break;
            case R.id.llyt_iknow:
                intentCheckOrder("new");
                break;
            case R.id.tv_sure:
                intentCheckOrder("new");
                break;
            case R.id.rlyt_alipay:      //支付宝支付
                loadPay();
                break;
            case R.id.tv_continue:      //继续逛
                startActivity(new Intent(PayForNowActivity.this,IndexActivity.class));
                finish();
                break;
            case R.id.tv_checkorder:    //查看订单
            case R.id.btn_iknow:
                intentCheckOrder("all");
                break;
            default:
                break;
        }
    }

    private void intentCheckOrder(String order){
        Intent intent = new Intent(this, OrderImportListActivity.class);
        intent.putExtra("status", order);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        if (payType == 1) {
            payDialog.show();
        } else {
            finish();
        }
    }

    private void loadPay() {
        if (TextUtils.isEmpty(PARTNER) || TextUtils.isEmpty(RSA_PRIVATE)
                || TextUtils.isEmpty(SELLER)) {
            new AlertDialog.Builder(this)
                    .setTitle("警告")
                    .setMessage("需要配置PARTNER | RSA_PRIVATE| SELLER")
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialoginterface, int i) {
                                    //
                                    finish();
                                }
                            }).show();
            return;
        }

        //标题、详情长度截取
        if(!TextUtils.isEmpty(title)&&title.length()>20){
            title = title.substring(0,20);
        }
        if(!TextUtils.isEmpty(details)&&details.length()>20){
            details = details.substring(0,20);
        }
        // 订单
        String orderInfo = getOrderInfo(title,details,totalMoney);
//        String orderInfo = getOrderInfo("Android支付测试", "请返回测试结果", "0.1");
        Log.d(TAG, "loadPay: title = "+title +"datails = "+details +" totalMoeny"+totalMoney);

        LogUtil.i("log", "orderInfo===" + orderInfo);
        // 对订单做RSA 签名
        String sign = sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask alipay = new PayTask(PayForNowActivity.this);
                // 调用支付接口，获取支付结果
                String result = alipay.pay(payInfo, true);
                Message msg = new Message();
                msg.what = 1;
                msg.obj = result;
                mPayHandler.sendMessage(msg);
            }
        };
        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }

    private static final String TAG = "PayForNowActivity";
    private Handler mPayHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch ((int) msg.what) {
                case 1: {
                    Log.d(TAG, "handleMessage: json" + (String) msg.obj);
                    PayResultBean payResult = new PayResultBean((String) msg.obj);
                    //支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();
                    Log.d(TAG, "handleMessage: resultInfo" + resultInfo);
                    String resultStatus = payResult.getResultStatus();

                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        paySuccessRlty.setVisibility(View.VISIBLE);
                        payMoneyTv.setText(totalMoney);
                        Toast.makeText(PayForNowActivity.this, "支付成功", Toast.LENGTH_SHORT).show();
                        transationNumTv.setText("支付成功!");
                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(PayForNowActivity.this, "支付结果确认中", Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(PayForNowActivity.this, "支付失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                default:
                    break;
            }
        }

        ;
    };

    /**
     * create the order info. 创建订单信息
     */
    public String getOrderInfo(String subject, String body, String price) {

        LogUtil.i("log", "===" + getOutTradeNo() + "=====" + price);
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + getOutTradeNo() + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        Log.d(TAG, "getOrderInfo: "+"&subject=" + "\"" + subject + "\""+"&body=" + "\"" + body + "\""+"&total_fee=" + "\"" + price + "\"");

        // 服务器异步通知页面路径
        orderInfo += "&notify_url=" + "\"" + notifyUrl + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";
        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content 待签名订单信息
     */
    public String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     */
    public String getOutTradeNo() {
//        Log.d(TAG, "getOutTradeNo: pay_no"+pay_no);
//        SimpleDateFormat format = new SimpleDateFormat(pay_no, Locale.getDefault());
//        Date date = new Date(System.currentTimeMillis());
//        String key = format.format(date);
//        Random r = new Random();
//        key = key + r.nextInt();
//        key = key.substring(0, 16);     //支付单号，具体多少位按每个公司制定的支付号定制
//        Log.d(TAG, "getOutTradeNo: KEY = "+key);
        Log.d(TAG, "getOutTradeNo: key = "+pay_no);
        return pay_no;
    }

    /**
     * get the sign type we use. 获取签名方式
     */
    public String getSignType() {
        return "sign_type=\"RSA\"";
    }
}
