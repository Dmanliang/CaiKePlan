package com.example.caikeplan.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import static com.example.util.Util.md5;

/**
 * Created by dell on 2017/8/16.
 */

public class ForgetPswActivity extends BaseActivity implements View.OnClickListener,EntryContract.View,TextView.OnEditorActionListener{

    private EditText        forgetpsw_phone;
    private EditText        forgetpsw_code;
    private EditText        forgetpsw_new_password;
    private ImageView       forgetpsw_close;
    private Button          forgetpsw_button_code;
    private Button          forgetpsw_button;
    private EntryPresenter  mPresenter;
    private RelativeLayout  loading;
    private String          phone,code,newpaswd;
    private int             second =60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.forget_password);
        initView();
    }

    public void initView(){
        forgetpsw_phone         =   (EditText)findViewById(R.id.forgetpsw_phone);
        forgetpsw_code          =   (EditText)findViewById(R.id.forgetpsw_code);
        forgetpsw_new_password  =   (EditText)findViewById(R.id.forgetpsw_new_password);
        forgetpsw_close         =   (ImageView)findViewById(R.id.forgetpsw_close);
        forgetpsw_button_code   =   (Button)findViewById(R.id.forgetpsw_button_code);
        forgetpsw_button        =   (Button)findViewById(R.id.forgetpsw_button);
        loading                 =   (RelativeLayout)findViewById(R.id.loading);
        forgetpsw_new_password.setOnEditorActionListener(this);
        forgetpsw_close.setOnClickListener(this);
        forgetpsw_button_code.setOnClickListener(this);
        forgetpsw_button.setOnClickListener(this);
        mPresenter = new EntryPresenter(this,this);
    }

    private void enable(boolean enable) {
        if (enable) {
            forgetpsw_button.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            forgetpsw_button.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.forgetpsw_close:
                finish();
                break;
            case R.id.forgetpsw_button_code:
                SendCode();
                break;
            case R.id.forgetpsw_button:
                forgetPassword();
                break;
        }
    }

    public void SendCode(){
        phone = forgetpsw_phone.getText().toString();
        if(phone.length() == 0){
            ToastUtil.getShortToastByString(this,"手机号码不能为空,请填写!");
        }else if(!Validator.isMobile(phone)) {
            ToastUtil.getShortToastByString(this,"手机号码格式错误,请重新填写!");
        }else{
            Map<String, String> mapsend = new HashMap<>();
            mapsend.put("phone",phone);
            mapsend.put("os_type","1");
            mPresenter.sendCode(mapsend);
        }
    }

    public void forgetPassword(){
        phone       = forgetpsw_phone.getText().toString();
        code        = forgetpsw_code.getText().toString();
        newpaswd    = forgetpsw_new_password.getText().toString();
        if(phone.length() == 0){
            ToastUtil.getShortToastByString(this,"手机号码不能为空,请填写!");
        }else if(!Validator.isMobile(phone)) {
            ToastUtil.getShortToastByString(this,"手机号码格式错误,请重新填写!");
        } else if(code.length() == 0){
            ToastUtil.getShortToastByString(this,"验证码不能为空,请填写!");
        }else if(newpaswd.length() == 0){
            ToastUtil.getShortToastByString(this,"新密码错误,请填写!");
        }else if(Validator.isChinese(newpaswd)){
            ToastUtil.getShortToastByString(this,"密码不符合规则");
        } else if(newpaswd.length()<6 || newpaswd.length()>20){
            ToastUtil.getShortToastByString(this,"密码不符合6-20位");
        }else {
            Map<String, String> mapbind = new HashMap<>();
            mapbind.put("phone",phone);
            mapbind.put("code",code);
            mapbind.put("password",md5(newpaswd));
            mapbind.put("os_type","1");
            mPresenter.resetPassword(mapbind);
        }
    }

    Handler timerHandler = new Handler();
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                second--;
                forgetpsw_button_code.setText(second+"s");
                if(second >= 0){
                    timerHandler.postDelayed(this, 1000);
                }else{
                    forgetpsw_button_code.setBackgroundResource(R.color.redText);
                    forgetpsw_button_code.setTextColor(getResources().getColor(R.color.whileText));
                    forgetpsw_button_code.setClickable(true);
                    forgetpsw_button_code.setText("获取验证码");
                    second = 60;
                    timerHandler.removeCallbacks(this);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

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
        ToastUtil.getShortToastByString(this,message);
    }

    @Override
    public void toHome(UserMessage userMessage) {
    }

    @Override
    public void toSuccessAction(String message) {
        if(message.equals("code")){
            forgetpsw_button_code.setClickable(false);
            forgetpsw_button_code.setBackgroundResource(R.color.timeback);
            forgetpsw_button_code.setTextColor(getResources().getColor(R.color.timetext));
            forgetpsw_button_code.setText(second+"s");
            timerHandler.post(timeRunnable);
        }else if(message.equals("psw")){
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
                forgetPassword();
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

    @Override
    protected void onDestroy() {
        timerHandler.removeCallbacks(timeRunnable);
        super.onDestroy();
    }
}
