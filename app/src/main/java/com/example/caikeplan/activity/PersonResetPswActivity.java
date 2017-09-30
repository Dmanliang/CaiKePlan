package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
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
 * Created by dell on 2017/8/28.
 */

public class PersonResetPswActivity extends BaseActivity implements View.OnClickListener,EntryContract.View,TextView.OnEditorActionListener{

    private LinearLayout    program_back;
    private TextView        resetpassword_username;
    private EditText        edit_newpassword;
    private EditText        edit_renewpassword;
    private Button          resetpassword_button;
    private EntryPresenter  mPresenter;
    private RelativeLayout  loading;
    private TextView        toolbarTitle;
    private String          new_password,renew_password,username,operator_id;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.person_resetpassword);
        getData();
        initView();
    }

    public void getData(){
        Bundle bundle   =   this.getIntent().getExtras();
        username        =   bundle.getString("username");
        operator_id     =   bundle.getString("operator_id");
    }

    public void initView(){
        toolbarTitle            =   (TextView)findViewById(R.id.toolbar_title);
        resetpassword_username  =   (TextView)findViewById(R.id.resetpassword_username);
        edit_newpassword        =   (EditText)findViewById(R.id.edit_newpassword);
        edit_renewpassword      =   (EditText)findViewById(R.id.edit_renewpassword);
        resetpassword_button    =   (Button)findViewById(R.id.resetpassword_button);
        program_back            =   (LinearLayout)findViewById(R.id.program_back);
        loading                 =   (RelativeLayout)findViewById(R.id.loading);
        mPresenter = new EntryPresenter(this,this);
        toolbarTitle.setText("修改密码");
        resetpassword_username.setText("账户名:"+username);
        program_back.setVisibility(View.VISIBLE);
        program_back.setOnClickListener(this);
        resetpassword_button.setOnClickListener(this);
        edit_renewpassword.setOnEditorActionListener(this);
    }

    public void resetPassword(){
        new_password    = edit_newpassword.getText().toString();
        renew_password  = edit_renewpassword.getText().toString();
        if(new_password.length() == 0){
            ToastUtil.getShortToastByString(this,"新密码不能为空,请填写!");
        }else if(renew_password.length() == 0){
            ToastUtil.getShortToastByString(this,"重输密码不能为空,请填写!");
        }else if(!new_password.equals(renew_password)){
            ToastUtil.getShortToastByString(this,"重设密码不匹配,请重新填写!");
        }else if(Validator.isChinese(new_password) || Validator.isChinese(renew_password)){
            ToastUtil.getShortToastByString(this,"密码不符合规则");
        } else if(new_password.length()<6 || new_password.length()>20){
            ToastUtil.getShortToastByString(this,"密码不符合6-20位");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("user_id", UserMessage.getInstance().getUser_id());
            map.put("operator_id",operator_id);
            map.put("token",UserMessage.getInstance().getToken());
            map.put("new_password", Util.md5(new_password));
            map.put("os_type","1");
            mPresenter.updatePassword(map);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.resetpassword_button:
                resetPassword();
                break;
            case R.id.program_back:
                finish();
                break;
        }
    }

    private void enable(boolean enable) {
        if (enable) {
            resetpassword_button.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            resetpassword_button.setEnabled(false);
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
    public void onMessage(String message) {
        if(!message.equals("")){
            ToastUtil.getShortToastByString(this,message);
        }
    }

    @Override
    public void toHome(UserMessage userMessage) {

    }

    @Override
    public void toSuccessAction(String message) {
        if(message.equals("reset")){
            Intent intent = new Intent(PersonResetPswActivity.this, ManagerActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
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
