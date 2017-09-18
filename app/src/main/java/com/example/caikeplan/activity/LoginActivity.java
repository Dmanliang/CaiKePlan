package com.example.caikeplan.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.AndroidBug5497Workaround;
import com.example.base.Constants;
import com.example.base.Validator;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.Code;
import com.example.caikeplan.logic.EntryContract;
import com.example.caikeplan.logic.EntryPresenter;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.getJson.HttpTask;
import com.youth.xframe.base.XActivity;
import com.youth.xframe.cache.XCache;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static android.text.TextUtils.isEmpty;
import static com.example.util.Util.md5;

/**
 * Created by dell on 2017/8/14.
 */

public class LoginActivity extends XActivity implements View.OnClickListener,EntryContract.View,EditText.OnEditorActionListener{

    private EditText                edit_user;
    private EditText                edit_password;
    private EditText                edit_provider;
    private ImageView               showcode;
    private ImageView               icon_eye;
    private Button                  button_login;
    private TextView                forget_password;
    private RelativeLayout          loading;
    //产生的验证码
    private String                  realCode;
    private EntryPresenter          mPresenter;
    private boolean                 ispswOpen = false;
    private String                  username="",password="";
    private XCache                  xCache;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initViews();
    }

    @Override
    public int getLayoutId() {
        return R.layout.login_activity;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

    }

    public void initViews(){
        xCache = XCache.get(this);
        if(xCache.getAsString("username")!=null || xCache.getAsString("password")!=null){
            username=xCache.getAsString("username");
            password=xCache.getAsString("password");
        }else{
            username="";
            password="";
        }
        edit_user       =   (EditText)findViewById(R.id.edit_user);
        edit_password   =   (EditText)findViewById(R.id.edit_password);
        edit_provider   =   (EditText)findViewById(R.id.edit_provider);
        showcode        =   (ImageView)findViewById(R.id.showcode);
        icon_eye        =   (ImageView)findViewById(R.id.icon_eye);
        button_login    =   (Button)findViewById(R.id.button_login);
        forget_password =   (TextView)findViewById(R.id.forget_password);
        loading         =   (RelativeLayout)findViewById(R.id.loading);
        showcode.setOnClickListener(this);
        button_login.setOnClickListener(this);
        forget_password.setOnClickListener(this);
        icon_eye.setOnClickListener(this);
        //将验证码用图片的形式显示出来
        showcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
        mPresenter = new EntryPresenter(this,this);
        edit_provider.setOnEditorActionListener(this);
        edit_user.setText(username);
        edit_password.setText(password);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.showcode:
                showcode.setImageBitmap(Code.getInstance().createBitmap());
                realCode = Code.getInstance().getCode().toLowerCase();
                break;
            case R.id.button_login:
                login();
                break;
            case R.id.forget_password:
                Intent intent = new Intent(LoginActivity.this,ForgetPswActivity.class);
                startActivity(intent);
                break;
            case R.id.icon_eye:
                if(!ispswOpen){
                    ispswOpen = true;
                    icon_eye.setBackground(this.getResources().getDrawable(R.drawable.icon_open));
                    edit_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else {
                    ispswOpen = false;
                    icon_eye.setBackground(this.getResources().getDrawable(R.drawable.icon_display));
                    edit_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                break;
        }
    }

    public void login(){
        String usname    = edit_user.getText().toString();
        String phoneCode = edit_provider.getText().toString().toLowerCase();
        String password  = edit_password.getText().toString();
        if(usname.length() == 0){
            ToastUtil.getShortToastByString(this, "请输入账号");
        }else if(password.length() == 0){
            ToastUtil.getShortToastByString(this, "请输入密码");
        }else if(edit_provider.getText().toString().length() == 0){
            ToastUtil.getShortToastByString(this, "请输入验证码");
        }else if(!phoneCode.equals(realCode)){
            ToastUtil.getShortToastByString(this,"验证码错误");
        }else if(Validator.isChinese(password)){
            ToastUtil.getShortToastByString(this,"密码不符合规则");
        } else if(password.length()<6 || password.length()>20){
            ToastUtil.getShortToastByString(this,"密码不符合6-20位");
        }else{
            Map<String, String> map = new HashMap<>();
            map.put("user_msg", usname);
            map.put("password", md5(password));
            xCache.put("username",usname);
            xCache.put("password",password);
            mPresenter.login(map);
        }
    }

    private void enable(boolean enable) {
        if (enable) {
            button_login.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            button_login.setEnabled(false);
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
        showcode.setImageBitmap(Code.getInstance().createBitmap());
        realCode = Code.getInstance().getCode().toLowerCase();
    }

    @Override
    public void toHome(UserMessage userMessage) {
        if(isEmpty(userMessage.getPhone()) && !userMessage.getRole_id().toString().equals("2")){
            ToastUtil.getShortToastByString(this,"为注册邮箱账号");
            Intent registerIntent = new Intent(LoginActivity.this,RegisterActivity.class);
            Bundle bundle1 = new Bundle();
            bundle1.putString("user_id",userMessage.getUser_id());
            bundle1.putString("token",userMessage.getToken());
            registerIntent.putExtras(bundle1);
            startActivity(registerIntent);
        }else{
            Intent intent2 = new Intent(LoginActivity.this, MainActivity.class);
            intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Bundle bundle2= new Bundle();
            bundle2.putString("user_id",userMessage.getUser_id());
            bundle2.putString("username",userMessage.getUsername());
            bundle2.putString("token",userMessage.getToken());
            bundle2.putString("role_id",userMessage.getRole_id());
            bundle2.putString("due_time",userMessage.getDue_time());
            bundle2.putString("phone",userMessage.getPhone());
            bundle2.putString("power_add",userMessage.getPower_add());
            bundle2.putString("parent_id",userMessage.getParent_id());
            startActivity(intent2);
            ToastUtil.getShortToastByString(this,"登录成功");
        }

    }



    @Override
    public void toSuccessAction(String message) {

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
                login();
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
