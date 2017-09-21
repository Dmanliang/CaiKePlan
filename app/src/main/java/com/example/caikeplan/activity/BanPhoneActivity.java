package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2017/8/14.
 */

public class BanPhoneActivity extends BaseActivity implements View.OnClickListener,EntryContract.View,TextView.OnEditorActionListener{

    private EditText        edit_phone;
    private EditText        edit_code;
    private Button          button_code;
    private Button          button_banphone;
    private ImageView       banphone_close;
    private TextView        phones_check;
    private EntryPresenter  mPresenter;
    private String          user_id,phone,code,token;
    private RelativeLayout  loading;
    private boolean         isExist = true;
    private int             second = 60;
    private String          codetype;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.banphone_activity);
        getDate();
        initView();
    }

    public void getDate(){
        Bundle bundle = getIntent().getExtras();
        user_id  = bundle.getString("user_id");
        token    = bundle.getString("token");
    }

    public void initView(){
        phones_check    = (TextView)findViewById(R.id.phones_check);
        edit_phone      = (EditText)findViewById(R.id.edit_phone);
        edit_code       = (EditText)findViewById(R.id.edit_code);
        button_code     = (Button)findViewById(R.id.button_code);
        button_banphone = (Button)findViewById(R.id.button_banphone);
        banphone_close  = (ImageView)findViewById(R.id.banphone_close);
        loading         = (RelativeLayout)findViewById(R.id.loading);
        button_code.setOnClickListener(this);
        banphone_close.setOnClickListener(this);
        button_banphone.setOnClickListener(this);
        edit_code.setOnEditorActionListener(this);
        mPresenter = new EntryPresenter(this,this);
        edit_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    codetype = "2";
                    CheckPhone();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_code:
                codetype = "1";
                CheckPhone();
                break;
            case R.id.banphone_close:
                finish();
                break;
            case R.id.button_banphone:
                BanPhone();
                break;
        }
    }

    public void SendCode(){
        phone = edit_phone.getText().toString();
        if(phone.length() == 0){
            ToastUtil.getShortToastByString(this,"手机号码不能为空,请填写!");
        }else if(!Validator.isMobile(phone)) {
            ToastUtil.getShortToastByString(this,"手机号码格式错误,请重新填写!");
        }else if(isExist){
            ToastUtil.getShortToastByString(this,"手机号码已绑定,请重新填写!");
        }else{
            Map<String, String> mapsend = new HashMap<>();
            mapsend.put("phone",phone);
            mPresenter.sendCode(mapsend);
        }
    }

    public void CheckPhone(){
        phone = edit_phone.getText().toString();
        if(phone.length() != 0){
            Map<String, String> map = new HashMap<>();
            map.put("type","1");
            map.put("phone",phone);
            mPresenter.validuser(map);
        }else{
            phones_check.setText("");
        }
    }

    public void BanPhone(){
        phone = edit_phone.getText().toString();
        code  = edit_code.getText().toString();
        if(phone.length() == 0){
            ToastUtil.getShortToastByString(this,"手机号码不能为空,请填写!");
        }else if(!Validator.isMobile(phone)) {
            ToastUtil.getShortToastByString(this,"手机号码格式错误,请重新填写!");
        }else if(code.length() == 0){
            ToastUtil.getShortToastByString(this,"验证码不能为空,请填写!");
        }else if(isExist){
            ToastUtil.getShortToastByString(this,"手机号码已绑定,请重新填写!");
        }else{
            Map<String, String> mapbind = new HashMap<>();
            mapbind.put("user_id",user_id);
            mapbind.put("phone",phone);
            mapbind.put("code",code);
            mapbind.put("token",token);
            mPresenter.banphone(mapbind);
        }
    }

    Handler timerHandler = new Handler();
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                second--;
                button_code.setText(second+"s");
                if(second >= 0){
                    timerHandler.postDelayed(this, 1000);
                }else{
                    button_code.setBackgroundResource(R.color.redText);
                    button_code.setTextColor(getResources().getColor(R.color.whileText));
                    button_code.setClickable(true);
                    button_code.setText("获取验证码");
                    second = 60;
                    timerHandler.removeCallbacks(this);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private void enable(boolean enable) {
        if (enable) {
            button_banphone.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            button_banphone.setEnabled(false);
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
        ToastUtil.getShortToastByString(this,message);
    }

    @Override
    public void toHome(UserMessage userMessage) {
        Intent intent = new Intent(BanPhoneActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void toSuccessAction(String message) {
        if(message.equals("code") && !isExist){
            button_code.setClickable(false);
            button_code.setBackgroundResource(R.color.timeback);
            button_code.setTextColor(getResources().getColor(R.color.timetext));
            button_code.setText(second+"s");
            timerHandler.post(timeRunnable);
        }else if(codetype.equals("1")){
            isExist = false;
            phones_check.setTextColor(this.getResources().getColor(R.color.green));
            phones_check.setText(message);
            SendCode();
        }else if(codetype.equals("2")){
            isExist = false;
            phones_check.setTextColor(this.getResources().getColor(R.color.green));
            phones_check.setText(message);
        }
    }

    @Override
    public void toFailAction(String message) {
        isExist = true;
        phones_check.setTextColor(this.getResources().getColor(R.color.red));
        phones_check.setText(message);
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_NONE:
                break;
            case EditorInfo.IME_ACTION_GO:
                BanPhone();
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
