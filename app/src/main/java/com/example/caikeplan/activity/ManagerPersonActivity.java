package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.EntryContract;
import com.example.caikeplan.logic.EntryPresenter;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.caikeplan.logic.picker.DateTimePicker;

import org.apache.http.protocol.RequestDate;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by dell on 2017/8/18.
 */

public class ManagerPersonActivity extends BaseActivity implements View.OnClickListener,EntryContract.View{

    private     LinearLayout        program_back;
    private     TextView            toolbarTitle;
    private     TextView            personUsername;
    private     TextView            duetime;
    private     Switch              switchManagerOpenuser;
    private     Button              personUpdateButton;
    private     Button              personDeleteButton;
    private     RelativeLayout      loading;
    private     String              power_add,operator_id,username,due_time;
    private     EntryPresenter      mPresenter;
    private     RelativeLayout      resetpassword_layout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.manager_personal);
        getData();
        initView();
    }

    public void getData(){
        Bundle bundle   =   this.getIntent().getExtras();
        username        =   bundle.getString("username");
        operator_id     =   bundle.getString("user_id");
        power_add       =   bundle.getString("power_add");
        due_time        =   bundle.getString("due_time");
    }

    public void initView(){
        resetpassword_layout    =   (RelativeLayout)findViewById(R.id.resetpassword_layout);
        program_back            =   (LinearLayout)findViewById(R.id.program_back);
        toolbarTitle            =   (TextView)findViewById(R.id.toolbar_title);
        personUsername          =   (TextView)findViewById(R.id.person_username);
        duetime                 =   (TextView)findViewById(R.id.duetime);
        switchManagerOpenuser   =   (Switch)findViewById(R.id.switch_manager_openuser);
        personUpdateButton      =   (Button)findViewById(R.id.person_update_button);
        personDeleteButton      =   (Button)findViewById(R.id.person_delete_button);
        loading                 =   (RelativeLayout) findViewById(R.id.loading);
        program_back.setVisibility(View.VISIBLE);
        program_back.setOnClickListener(this);
        resetpassword_layout.setOnClickListener(this);
        duetime.setOnClickListener(this);
        personUpdateButton.setOnClickListener(this);
        personDeleteButton.setOnClickListener(this);
        mPresenter = new EntryPresenter(this,this);
        toolbarTitle.setText("用户管理");
        personUsername.setText(username);
        duetime.setText(due_time);
        if(power_add.equals("0")){
            switchManagerOpenuser.setChecked(false);
        }else{
            switchManagerOpenuser.setChecked(true);
        }
        switchManagerOpenuser.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    power_add = "1";
                }else{
                    power_add = "0";
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
            case R.id.duetime:
                onYearMonthDayTimePicker(v);
                break;
            case R.id.person_update_button:
                Map<String, String> update_map = new HashMap<>();
                update_map.put("user_id",UserMessage.getInstance().getUser_id());
                update_map.put("token",UserMessage.getInstance().getToken());
                update_map.put("operator_id",operator_id);
                update_map.put("power_add",power_add);
                update_map.put("due_time",due_time);
                mPresenter.updateperson(update_map);
                break;
            case R.id.person_delete_button:
                Map<String, String> delete_map = new HashMap<>();
                delete_map.put("user_id",UserMessage.getInstance().getUser_id());
                delete_map.put("token",UserMessage.getInstance().getToken());
                delete_map.put("operator_id",operator_id);
                delete_map.put("power_add",power_add);
                delete_map.put("due_time",due_time);
                mPresenter.deleteperson(delete_map);
                break;
            case R.id.resetpassword_layout:
                Intent intent = new Intent(ManagerPersonActivity.this,PersonResetPswActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("username",username);
                bundle.putString("operator_id",operator_id);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    public void onYearMonthDayTimePicker(View view) {
        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_24);
        picker.setDateRangeStart(2017, 1, 1);
        picker.setDateRangeEnd(2200, 12, 31);
        picker.setTimeRangeStart(0, 0);
        picker.setTimeRangeEnd(20, 30);
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                due_time = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":00";
                duetime.setText(due_time);
            }
        });
        picker.show();
    }


    private void enable(boolean enable) {
        if (enable) {
            personUpdateButton.setEnabled(true);
            loading.setVisibility(View.GONE);
        } else {
            personUpdateButton.setEnabled(false);
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
        if(msg.equals("")){
            finish();
        }
    }

    @Override
    public void toFailAction(String message) {
        ToastUtil.getShortToastByString(this,message);
    }

}
