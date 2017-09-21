package com.example.caikeplan.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
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

import static com.example.util.Util.md5;

/**
 * Created by dell on 2017/9/19.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener,EntryContract.View,TextView.OnEditorActionListener{

    private EditText        register_edit_user;
    private EditText        register_edit_password;
    private EditText        register_edit_phone;
    private EditText        register_edit_code;
    private ImageView       register_icon_eye;
    private ImageView       register_close;
    private TextView        phones_check;
    private TextView        username_check;
    private Button          reigster_button_code;
    private Button          button_register;
    private RelativeLayout  loading;
    private EntryPresenter  mPresenter;
    private boolean         ispswOpen = false;
    private boolean         isExist = true;
    private boolean         isnameExist = true;
    private String          user_name,phone;
    private String          codetype = " ";
    private int             second = 60;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.register_activity);
        initView();
    }

    public void initView(){
        register_edit_user      =   (EditText)findViewById(R.id.register_edit_user);
        register_edit_password  =   (EditText)findViewById(R.id.register_edit_password);
        register_edit_phone     =   (EditText)findViewById(R.id.register_edit_phone);
        register_edit_code      =   (EditText)findViewById(R.id.register_edit_code);
        register_icon_eye       =   (ImageView)findViewById(R.id.register_icon_eye);
        register_close          =   (ImageView)findViewById(R.id.register_close);
        reigster_button_code    =   (Button)findViewById(R.id.reigster_button_code);
        button_register         =   (Button)findViewById(R.id.button_register);
        loading                 =   (RelativeLayout)findViewById(R.id.loading);
        phones_check            =   (TextView)findViewById(R.id.phones_check);
        username_check          =   (TextView)findViewById(R.id.username_check);
        register_icon_eye.setOnClickListener(this);
        reigster_button_code.setOnClickListener(this);
        register_close.setOnClickListener(this);
        button_register.setOnClickListener(this);
        mPresenter = new EntryPresenter(this,this);
        register_edit_phone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    codetype = "2";
                    CheckPhone();
                }
            }
        });
        register_edit_user.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    CheckUserName();
                }
            }
        });
    }

    public void sendCode(){
        phone = register_edit_phone.getText().toString();
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
        phone = register_edit_phone.getText().toString();
        if(phone.length() != 0){
            if(!Validator.isMobile(phone)) {
                ToastUtil.getShortToastByString(this,"手机号码格式错误,请重新填写!");
                register_edit_phone.setText("");
            }else{
                Map<String, String> map = new HashMap<>();
                map.put("type","1");
                map.put("phone",phone);
                mPresenter.validuser(map);
            }
        }else{
            phones_check.setText("");
        }
    }

    public void CheckUserName(){
        user_name = register_edit_user.getText().toString();
        if(user_name.length() != 0){
            Map<String, String> map = new HashMap<>();
            map.put("username",user_name);
            map.put("type","0");
            mPresenter.validuser(map);
        }else{
            username_check.setText("");
        }
    }

    public void Register(){
        String username  = register_edit_user.getText().toString();
        String phoneCode = register_edit_code.getText().toString().toLowerCase();
        String password  = register_edit_password.getText().toString();
        String phone     = register_edit_phone.getText().toString();
        if(username.length() == 0){
            ToastUtil.getShortToastByString(this, "请输入账号");
        }else if(password.length() == 0){
            ToastUtil.getShortToastByString(this, "请输入密码");
        }else if(phoneCode.length() == 0){
            ToastUtil.getShortToastByString(this, "请输入验证码");
        }else if(phone.length() == 0){
            ToastUtil.getShortToastByString(this, "请输入手机号码");
        }else if(Validator.isChinese(password)){
            ToastUtil.getShortToastByString(this,"密码不符合规则");
        }else if(isExist){
            ToastUtil.getShortToastByString(this,"手机号码已绑定,请重新填写!");
        }else if(!Validator.isMobile(phone)) {
            ToastUtil.getShortToastByString(this,"手机号码格式错误,请重新填写!");
        }else if(isnameExist){
            ToastUtil.getShortToastByString(this,"用户名已存在,请重新填写!");
        } else if(password.length()<6 || password.length()>20){
            ToastUtil.getShortToastByString(this,"密码不符合6-20位");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("username", username);
            map.put("password", md5(password));
            map.put("phone", phone);
            map.put("code", phoneCode);
            mPresenter.register(map);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.reigster_button_code:
                codetype = "1";
                if(!isExist){
                    CheckPhone();
                }
                break;
            case R.id.button_register:
                Register();
                break;
            case R.id.register_icon_eye:
                if(!ispswOpen){
                    ispswOpen = true;
                    register_icon_eye.setBackground(this.getResources().getDrawable(R.drawable.icon_open));
                    register_edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    ispswOpen = false;
                    register_icon_eye.setBackground(this.getResources().getDrawable(R.drawable.icon_display));
                    register_edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
            case R.id.register_close:
                finish();
                break;
        }
    }

    Handler timerHandler = new Handler();
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                second--;
                reigster_button_code.setText(second+"s");
                if(second >= 0){
                    timerHandler.postDelayed(this, 1000);
                }else{
                    reigster_button_code.setBackgroundResource(R.color.redText);
                    reigster_button_code.setTextColor(getResources().getColor(R.color.whileText));
                    reigster_button_code.setClickable(true);
                    reigster_button_code.setText("获取验证码");
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
            button_register.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            button_register.setEnabled(false);
            loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean isOK = true;
        switch (actionId) {
            case EditorInfo.IME_ACTION_NONE:
                break;
            case EditorInfo.IME_ACTION_GO:
                Register();
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
        ShowMessageDialog(userMessage);
    }

    @Override
    public void toSuccessAction(String message) {
        if(message.equals("code") && !isExist){
            reigster_button_code.setClickable(false);
            reigster_button_code.setBackgroundResource(R.color.timeback);
            reigster_button_code.setTextColor(getResources().getColor(R.color.timetext));
            reigster_button_code.setText(second+"s");
            timerHandler.post(timeRunnable);
        }else if(codetype.equals("1") && message.equals("手机通过")){
            isExist = false;
            phones_check.setTextColor(this.getResources().getColor(R.color.green));
            phones_check.setText(message);
            sendCode();
        }else if(codetype.equals("2") && message.equals("手机通过")){
            isExist = false;
            phones_check.setTextColor(this.getResources().getColor(R.color.green));
            phones_check.setText(message);
        }else if(message.equals("用户名通过")){
            isnameExist = false;
            username_check.setTextColor(this.getResources().getColor(R.color.green));
            username_check.setText(message);
        }
    }

    @Override
    public void toFailAction(String message) {
        if(message.equals("用户名已存在")){
            isnameExist = true;
            username_check.setTextColor(this.getResources().getColor(R.color.red));
            username_check.setText(message);
        }else if(message.equals("手机已绑定")){
            isExist = true;
            phones_check.setTextColor(this.getResources().getColor(R.color.red));
            phones_check.setText(message);
        }
    }

    @Override
    protected void onDestroy() {
        timerHandler.removeCallbacks(timeRunnable);
        super.onDestroy();
    }

    public void ShowMessageDialog(final UserMessage userMessage){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("系统提示")//设置对话框标题
                .setMessage("注册成功！试用时间3小时!")//设置显示的内容
                .setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
                    @Override
                    public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
                        dialog.dismiss();
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle bundle= new Bundle();
                        bundle.putString("user_id",userMessage.getUser_id());
                        bundle.putString("username",userMessage.getUsername());
                        bundle.putString("token",userMessage.getToken());
                        bundle.putString("role_id",userMessage.getRole_id());
                        bundle.putString("due_time",userMessage.getDue_time());
                        bundle.putString("phone",userMessage.getPhone());
                        bundle.putString("power_add",userMessage.getPower_add());
                        bundle.putString("parent_id",userMessage.getParent_id());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });//在按键响应事件中显示此对话框
        AlertDialog alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                    return true;
                }
                else {
                    return false; //默认返回 false
                }
            }
        });
        alertDialog.show();
    }
}
