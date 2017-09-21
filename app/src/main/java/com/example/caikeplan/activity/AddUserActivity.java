package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.base.Validator;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.EntryContract;
import com.example.caikeplan.logic.EntryPresenter;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.util.Util;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

/**
 * Created by dell on 2017/8/17.
 */

public class AddUserActivity extends BaseActivity implements View.OnClickListener,EntryContract.View,TextView.OnEditorActionListener{

    private RelativeLayout  openUserlayout;
    private LinearLayout    program_back;
    private TextView        toolbarTitle;
    private TextView        username_check;
    private EditText        adduserUsername;
    private EditText        editAdduserNewpsw;
    private EditText        editAdduserRenewpsw;
    private Switch          switchTrycount;
    private Switch          switchOpenuser;
    private Button          adduserButton;
    private RelativeLayout  loading;
    private String          username,newpsw,renewpsw,hours_count="0",power_add="0";
    private EntryPresenter  mPresenter;
    private boolean         isTry = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.adduser_activity);
        initView();
    }

    public void initView(){
        program_back        = (LinearLayout)findViewById(R.id.program_back);
        toolbarTitle        = (TextView)findViewById(R.id.toolbar_title);
        username_check      = (TextView)findViewById(R.id.username_check);
        adduserUsername     = (EditText) findViewById(R.id.adduser_username);
        editAdduserNewpsw   = (EditText) findViewById(R.id.edit_adduser_newpsw);
        editAdduserRenewpsw = (EditText)findViewById(R.id.edit_adduser_renewpsw);
        switchTrycount      = (Switch) findViewById(R.id.switch_trycount);
        switchOpenuser      = (Switch)findViewById(R.id.switch_openuser);
        adduserButton       = (Button)findViewById(R.id.adduser_button);
        loading             = (RelativeLayout) findViewById(R.id.loading);
        openUserlayout      = (RelativeLayout) findViewById(R.id.openUserlayout);
        mPresenter = new EntryPresenter(this,this);
        program_back.setVisibility(View.VISIBLE);
        toolbarTitle.setText("添加用户");
        program_back.setOnClickListener(this);
        adduserButton.setOnClickListener(this);
        editAdduserRenewpsw.setOnEditorActionListener(this);
        if(UserMessage.getInstance().getRole_id().equals("1")){
            openUserlayout.setVisibility(View.VISIBLE);
        }else{
            openUserlayout.setVisibility(View.GONE);
        }
        switchTrycount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    hours_count = "3";
                    isTry = true;
                }else{
                    hours_count = "0";
                    isTry = false;
                }
            }
        });
        switchOpenuser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    power_add = "1";
                }else{
                    power_add = "0";
                }
            }
        });
        adduserUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    username = adduserUsername.getText().toString();
                    if(username.length() != 0){
                        Map<String, String> map = new HashMap<>();
                        map.put("username",username);
                        map.put("type","0");
                        mPresenter.validuser(map);
                    }else{
                        username_check.setText("");
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.program_back:
                finish();
                break;
            case R.id.adduser_button:
                addUser();
                break;
        }
    }

    public void addUser(){
        username = adduserUsername.getText().toString();
        newpsw   = editAdduserNewpsw.getText().toString();
        renewpsw = editAdduserRenewpsw.getText().toString();
        if(username.length() == 0){
            ToastUtil.getShortToastByString(this,"用户名不能为空,请填写!");
        }else if(newpsw.length() == 0){
            ToastUtil.getShortToastByString(this,"密码不能为空,请填写!");
        }else if(renewpsw.length() == 0){
            ToastUtil.getShortToastByString(this,"密码不能为空,请填写!");
        }else if(!newpsw.equals(renewpsw)){
            ToastUtil.getShortToastByString(this,"密码不匹配,请重新填写!");
        } else if(Validator.isChinese(newpsw) || Validator.isChinese(renewpsw)){
            ToastUtil.getShortToastByString(this,"密码不符合规则");
        } else if(newpsw.length()<6 || newpsw.length()>20){
            ToastUtil.getShortToastByString(this,"密码不符合6-20位");
        }else{
            if(isTry){
                Map<String, String> map = new HashMap<>();
                map.put("user_id",UserMessage.getInstance().getUser_id());
                map.put("token",UserMessage.getInstance().getToken());
                map.put("power_add",power_add);
                map.put("username",username);
                map.put("password",Util.md5(newpsw));
                map.put("hours_count",hours_count);
                mPresenter.addusers(map);
            }else{
                Map<String, String> map = new HashMap<>();
                map.put("user_id",UserMessage.getInstance().getUser_id());
                map.put("token",UserMessage.getInstance().getToken());
                map.put("power_add",power_add);
                map.put("username",username);
                map.put("password",Util.md5(newpsw));
                mPresenter.addusers(map);
            }
        }
    }

    private void enable(boolean enable) {
        if (enable) {
            adduserButton.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            adduserButton.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingAnimation() {
        enable(false);
    }

    @Override
    public void disableLoadingAnimation() {
        enable(true);
    }

    @Override
    public void onMessage(String msg) {
        if(!msg.equals("")){
            ToastUtil.getShortToastByString(this,msg);
        }
    }

    @Override
    public void toHome(UserMessage userMessage) {
        finish();
    }

    @Override
    public void toSuccessAction(String msg) {
        if(!msg.equals("")){
            username_check.setTextColor(this.getResources().getColor(R.color.green));
            username_check.setText(msg);
        }
    }

    @Override
    public void toFailAction(String msg) {
        username_check.setTextColor(this.getResources().getColor(R.color.red));
        username_check.setText(msg);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_NONE:
                break;
            case EditorInfo.IME_ACTION_GO:
                addUser();
                break;
            case EditorInfo.IME_ACTION_SEARCH:
                break;
            case EditorInfo.IME_ACTION_SEND:
                break;
            case EditorInfo.IME_ACTION_NEXT:
                break;
            case EditorInfo.IME_ACTION_DONE:
                break;
            default:
                isOK = false;
                break;
        }
        return isOK;
    }

}
