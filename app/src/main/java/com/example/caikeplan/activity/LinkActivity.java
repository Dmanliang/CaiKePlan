package com.example.caikeplan.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.example.caikeplan.R;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by dell on 2017/9/1.
 */

public class LinkActivity extends BaseActivity {

    private WebView linkwebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.link_activity);
        initView();
    }

    public void initView(){
        linkwebView = (WebView)findViewById(R.id.link_webview);
        final String url   = getIntent().getExtras().getString("url");
        linkwebView.requestFocusFromTouch();
        WebSettings setting = linkwebView.getSettings();
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);
        setting.setUseWideViewPort(true);       //关键点
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setDisplayZoomControls(true);
        setting.setJavaScriptEnabled(true);             // 设置支持javascript脚本
        setting.setAllowFileAccess(true);               // 允许访问文件
        setting.setLoadWithOverviewMode(true);          // 缩放至屏幕的大小
        setting.setBuiltInZoomControls(true);           // 设置显示缩放按钮
        setting.setSupportZoom(true);                   // 支持缩放
        setting.setUseWideViewPort(true);               //将图片调整到适合webview的大小
        setting.setAppCacheEnabled(false);              //加缓存
        setting.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);               //支持数据库
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//关闭webview中缓存
        setting.setAllowFileAccess(true);               //设置可以访问文件
        setting.setNeedInitialFocus(true);              //当webview调用requestFocus时为webview设置节点
        setting.setLoadsImagesAutomatically(true);      //支持自动加载图片
        setting.setDefaultTextEncodingName("utf-8");    //设置编码格式
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        linkwebView.addJavascriptInterface(this, "android");
        //Webview兼容cookie
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().setAcceptThirdPartyCookies(linkwebView,true);
        linkwebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return false;
            }
        });
        linkwebView.loadUrl(url);
    }

    @JavascriptInterface
    public void actionFromJs() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && linkwebView.canGoBack()) {
            linkwebView.goBack();// 返回前一个页面
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
