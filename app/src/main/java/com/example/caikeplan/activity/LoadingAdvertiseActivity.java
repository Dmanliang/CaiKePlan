package com.example.caikeplan.activity;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.base.Constants;
import com.example.base.DownLoadService;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.ToastUtil;
import com.example.getJson.HttpTask;
import com.example.util.Util;
import com.tbruyelle.rxpermissions.RxPermissions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rx.functions.Action1;

/**
 * Created by dell on 2017/3/2.
 */

public class LoadingAdvertiseActivity extends Activity{

    private int                 delayTime =4;//广告倒计时
    private ImageView           welcomView,adImg;
    private RelativeLayout      rl;
    private Button              skipBtn;
    private Runnable            runnable;
    private Boolean             isSkip=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
          /*  getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
        }
        setContentView(R.layout.loading_advertise_activity);
        initView();
    }

    public void initView() {
        welcomView  = (ImageView) findViewById(R.id.iv_welcome_img);
        rl          = (RelativeLayout) findViewById(R.id.rl_show_ad);
        skipBtn     = (Button) findViewById(R.id.ad_skip_btn);
        adImg       = (ImageView) findViewById(R.id.iv_ad_img);
        skipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSkip=true;
                handler.removeCallbacks(runnable);
                jumpToMain();
            }
        });

        runnable = new Runnable() {
            @Override
            public void run() {
                if(!isSkip){
                    Message message = new Message();
                    message.what = delayTime;
                    handler.sendMessage(message);
                }
            }
        };
        handler.post(runnable);
    }

    Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what>=0){
                skipBtn.setText("跳过"+msg.what);
                delayTime--;
                handler.postDelayed(runnable,1000);
            }else{
                jumpToMain();
                handler.removeCallbacks(runnable);
            }
        }
    };

    /**
     * 跳过广告进入主页
     * */
    public void jumpToMain(){
        startActivity(new Intent(LoadingAdvertiseActivity.this, LoginActivity.class));
        finish();
    }

}
