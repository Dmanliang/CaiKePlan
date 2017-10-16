package com.example.caikeplan.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.EntryContract;
import com.example.caikeplan.logic.EntryPresenter;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.collect.CollectActivity;
import com.example.getJson.HttpTask;
import com.example.personal.PersonalAbout;
import com.example.personal.PersonalCopy;
import com.example.util.Util;
import com.youth.xframe.base.XActivity;
import com.youth.xframe.cache.XCache;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.OnClick;

/**
 * Created by dell on 2017/8/16.
 */

public class PersonalActivity extends XActivity implements View.OnClickListener,EntryContract.View{

    private     TextView            endtime;
    private     TextView            versiontext;
    private     TextView            username;
    private     TextView            exitlogin;
    private     RelativeLayout      usernameLayout;
    private     RelativeLayout      endtimeLayout;
    private     RelativeLayout      collectLayout;
    private     RelativeLayout      toolsLayout;
    private     RelativeLayout      adduserLayout;
    private     RelativeLayout      peopleLayout;
    private     RelativeLayout      resetpswLayout;
    private     RelativeLayout      copycontentLayout;
    private     RelativeLayout      freeLayout;
    private     RelativeLayout      recharge_layout;
    private     TextView            toolbar_title;
    private     TextView            all_count;
    private     ImageView           message;
    private     EntryPresenter      mPresenter;
    private     Map<String, String> messagemap = new HashMap<>();
    private     XCache              xCache;
    private     String              due_time;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //    getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.personal_setting;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

    }

    public void initViews() {
        xCache              = XCache.get(this);
        usernameLayout      = (RelativeLayout)findViewById(R.id.username_layout);
        endtimeLayout       = (RelativeLayout)findViewById(R.id.endtime_layout);
        collectLayout       = (RelativeLayout)findViewById(R.id.collect_layout);
        toolsLayout         = (RelativeLayout)findViewById(R.id.tools_layout);
        adduserLayout       = (RelativeLayout)findViewById(R.id.adduser_layout);
        peopleLayout        = (RelativeLayout)findViewById(R.id.people_layout);
        resetpswLayout      = (RelativeLayout)findViewById(R.id.resetpsw_layout);
        copycontentLayout   = (RelativeLayout)findViewById(R.id.copycontent_layout);
        freeLayout          = (RelativeLayout)findViewById(R.id.free_layout);
        recharge_layout     = (RelativeLayout)findViewById(R.id.recharge_layout);
        toolbar_title       = (TextView)findViewById(R.id.toolbar_title);
        username            = (TextView)findViewById(R.id.username);
        message             = (ImageView)findViewById(R.id.message);
        exitlogin           = (TextView)findViewById(R.id.exitlogin);
        endtime             = (TextView)findViewById(R.id.endtime);
        versiontext         = (TextView)findViewById(R.id.versiontext);
        all_count           = (TextView)findViewById(R.id.all_count);
        versiontext.setText("当前版本:"+getVersion());
        toolbar_title.setText("设置");
        username.setText(UserMessage.getInstance().getUsername());
        endtime.setText(UserMessage.getInstance().getDue_time());
        message.setVisibility(View.VISIBLE);
        usernameLayout.setOnClickListener(this);
        endtimeLayout.setOnClickListener(this);
        collectLayout.setOnClickListener(this);
        toolsLayout.setOnClickListener(this);
        adduserLayout.setOnClickListener(this);
        peopleLayout.setOnClickListener(this);
        resetpswLayout.setOnClickListener(this);
        copycontentLayout.setOnClickListener(this);
        freeLayout.setOnClickListener(this);
        exitlogin.setOnClickListener(this);
        message.setOnClickListener(this);
        recharge_layout.setOnClickListener(this);
        mPresenter = new EntryPresenter(this,this);
        if(UserMessage.getInstance().getPower_add().equals("0")){
            adduserLayout.setVisibility(View.GONE);
        }else{
            adduserLayout.setVisibility(View.VISIBLE);
        }
        if(UserMessage.getInstance().getRole_id().equals("1")){
            peopleLayout.setVisibility(View.VISIBLE);
        }else{
            peopleLayout.setVisibility(View.GONE);
        }
        if(UserMessage.getInstance().getRole_id().equals("1") || UserMessage.getInstance().getRole_id().equals("3")){
            recharge_layout.setVisibility(View.VISIBLE);
        }else{
            recharge_layout.setVisibility(View.GONE);
        }
    }

    public void requestMessage(){
        messagemap.put("user_id",UserMessage.getInstance().getUser_id());
        messagemap.put("token",UserMessage.getInstance().getToken());
        messagemap.put("os_type","1");
        mPresenter.message(messagemap);
    }

    public void requsetDueTime(){
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API + Constants.RECHARGE_DUETIME+"?user_id="+UserMessage.getInstance().getUser_id());
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try{
                    JSONObject jsonObject = new JSONObject(json);
                    String success  = jsonObject.getString("success");
                    if(success.equals("true")) {
                        due_time = jsonObject.getString("due_time");
                        UserMessage.getInstance().setDue_time(due_time);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {
            }
        });
    }


    @OnClick({R.id.message,R.id.endtime_layout, R.id.collect_layout, R.id.tools_layout, R.id.adduser_layout, R.id.people_layout, R.id.resetpsw_layout, R.id.copycontent_layout, R.id.free_layout, R.id.exitlogin})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.endtime_layout:
                break;
            case R.id.collect_layout:
                startActivity(new Intent(PersonalActivity.this, CollectActivity.class));
                break;
            case R.id.tools_layout:
                Intent intent = new Intent(PersonalActivity.this,LinkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", Constants.API+Constants.ALL_TOOLS);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.adduser_layout:
                startActivity(new Intent(PersonalActivity.this, AddUserActivity.class));
                break;
            case R.id.people_layout:
                startActivity(new Intent(PersonalActivity.this, ManagerActivity.class));
                break;
            case R.id.resetpsw_layout:
                startActivity(new Intent(PersonalActivity.this, ResetPasswordActivity.class));
                break;
            case R.id.copycontent_layout:
                startActivity(new Intent(PersonalActivity.this, PersonalCopy.class));
                break;
            case R.id.free_layout:
                startActivity(new Intent(PersonalActivity.this, PersonalDisclaimer.class));
                break;
            case R.id.recharge_layout:
                startActivity(new Intent(PersonalActivity.this,RechargeActivity.class));
                break;
            case R.id.exitlogin:
                Intent intentexit = new Intent(PersonalActivity.this, LoginActivity.class);
                intentexit.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intentexit);
                xCache.clear();
                break;
            case R.id.message:
                startActivity(new Intent(PersonalActivity.this,MessageActivity.class));
                break;
        }
    }

    /**
     * 获取版本号
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return this.getString(R.string.app_name) +"V"+version;
        } catch (Exception e) {
            e.printStackTrace();
            return this.getString(R.string.can_not_find_version_name);
        }
    }

    @Override
    public void showLoadingAnimation() {

    }

    @Override
    public void disableLoadingAnimation() {

    }

    @Override
    public void onMessage(String msg) {
        message.setBackgroundResource(R.drawable.icon_message);
    }

    @Override
    public void toHome(UserMessage userMessage) {

    }

    @Override
    public void toSuccessAction(String msg) {
        message.setBackgroundResource(R.drawable.icon_getmessage);
    }

    @Override
    public void toFailAction(String message) {

    }

    @Override
    protected void onResume() {
        requestMessage();
        requsetDueTime();
        all_count.setText(UserMessage.getInstance().getUse_count()+"/"+UserMessage.getInstance().getAll_count());
        endtime.setText(UserMessage.getInstance().getDue_time());
        super.onResume();
    }

}
