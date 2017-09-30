package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

/**
 * Created by dell on 2017/8/17.
 */

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener,EntryContract.View,TextView.OnEditorActionListener{

    private     LinearLayout        program_back;
    private     EditText            editOldpsw;
    private     EditText            editNewpsw;
    private     EditText            editRenewpsw;
    private     Button              resetpswButton;
    private     TextView            toolbarTitle;
    private     TextView            resetpsw_username;
    private     EntryPresenter      mPresenter;
    private     RelativeLayout      loading;
    private     String              old_password,new_password,renew_password;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.resetpsw_activity);
        initView();
    }

    public void initView(){
        program_back        =   (LinearLayout)findViewById(R.id.program_back);
        loading             =   (RelativeLayout)findViewById(R.id.loading);
        toolbarTitle        =   (TextView)findViewById(R.id.toolbar_title);
        resetpsw_username   =   (TextView)findViewById(R.id.resetpsw_username);
        editOldpsw          =   (EditText)findViewById(R.id.edit_oldpsw);
        editNewpsw          =   (EditText)findViewById(R.id.edit_newpsw);
        editRenewpsw        =   (EditText)findViewById(R.id.edit_renewpsw);
        resetpswButton      =   (Button)findViewById(R.id.resetpsw_button);
        toolbarTitle.setText("重置密码");
        resetpsw_username.setText("当前账户名:"+UserMessage.getInstance().getUsername());
        program_back.setVisibility(View.VISIBLE);
        editRenewpsw.setOnEditorActionListener(this);
        program_back.setOnClickListener(this);
        resetpswButton.setOnClickListener(this);
        mPresenter = new EntryPresenter(this,this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.program_back:
                finish();
                break;
            case R.id.resetpsw_button:
                resetPassword();
                break;
        }
    }

    public void resetPassword(){
        old_password    = editOldpsw.getText().toString();
        new_password    = editNewpsw.getText().toString();
        renew_password  = editRenewpsw.getText().toString();
        if(old_password.length() == 0){
            ToastUtil.getShortToastByString(this,"旧密码不能为空,请填写!");
        }else if(new_password.length() == 0){
            ToastUtil.getShortToastByString(this,"新密码不能为空,请填写!");
        }else if(renew_password.length() == 0){
            ToastUtil.getShortToastByString(this,"重输密码不能为空,请填写!");
        }else if(!new_password.equals(renew_password)){
            ToastUtil.getShortToastByString(this,"重设密码不匹配,请重新填写!");
        }else if(Validator.isChinese(new_password) || Validator.isChinese(old_password) || Validator.isChinese(renew_password)){
            ToastUtil.getShortToastByString(this,"密码不符合规则");
        } else if(new_password.length()<6 || new_password.length()>20){
            ToastUtil.getShortToastByString(this,"密码不符合6-20位");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("user_id",UserMessage.getInstance().getUser_id());
            map.put("token",UserMessage.getInstance().getToken());
            map.put("old_password",Util.md5(old_password));
            map.put("new_password", Util.md5(new_password));
            map.put("os_type","1");
            mPresenter.setNewPassword(map);
        }
    }

    private void enable(boolean enable) {
        if (enable) {
            resetpswButton.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            resetpswButton.setEnabled(false);
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

    }

    @Override
    public void toSuccessAction(String msg) {
        if(msg.equals("reset")){
            finish();
        }
    }

    @Override
    public void toFailAction(String message) {

    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_NONE:
                break;
            case EditorInfo.IME_ACTION_GO:
                resetPassword();
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
