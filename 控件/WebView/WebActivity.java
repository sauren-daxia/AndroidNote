package com.bjesc.app.testpronject2;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Toast;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;

/**
 * Created by Jason Chen on 2017/8/26.
 */
@Route(path = "/testproject/view/web")
public class WebActivity extends AppCompatActivity {
    private WebView webview;
    @Autowired
    String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ARouter.getInstance().inject(this);
        setContentView(R.layout.activity_web);
        webview = (WebView) findViewById(R.id.web);
        initWeb();
    }

    private void initWeb() {
        webview.loadUrl(url);
        webview.addJavascriptInterface(new JSInterface(),"android");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.setWebChromeClient(new WebChromeClient(){});
    }


private class JSInterface{
        @JavascriptInterface
        public String showToast(String text){
            Toast.makeText(WebActivity.this, text, Toast.LENGTH_SHORT).show();
            return "22222";
        }
    }



    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {    //判断是否按了返回键
            if (webview.canGoBack()) {    //判断是否有上一级页面
                webview.goBack();    //如果有上一级页面，有则返回
                return true;
            } else {
                System.exit(0);        //如果没有上一级页面则退出程序
            }
        }
        return super.onKeyDown(keyCode,event);
    }
}
