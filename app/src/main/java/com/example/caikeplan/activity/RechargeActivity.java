package com.example.caikeplan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.caikeplan.logic.message.VauleMessage;
import com.example.getJson.HttpTask;
import com.example.util.OKHttpManager;
import com.example.util.OnNetRequestCallback;
import com.example.util.Util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/9/19.
 */

public class RechargeActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout  loading;
    private LinearLayout    program_back;
    private TextView        toolbar_title;
    private TextView        recharge_username;
    private ListView        recharge_listview;
    private ImageView       zhifu_close;
    private Button          button_recharge;
    private RechargeAdapter rechargeAdapter;
    private EditText        daili_recharge_time;
    private TextView        daili_new_value;
    private List<VauleMessage>  vauleMessageList = new ArrayList<>();
    private String[]        olds = {"￥240","￥480","￥960"};
    private int             month = 0;
    private int             selectPosition = -1;//用于记录用户选择的变量
    private RelativeLayout  recharge_daili;
    private String          payChannel="21";
    private PopupWindow     zhifuwindow;
    private View            zhifuview;
    private Button          weixin,zhifubao;
    private int             allmoney = 0;
    private int             money = 0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.recharge_activity);
        initView();
        requestRechargeData();
    }

    public void initView(){
        loading             =   (RelativeLayout)findViewById(R.id.loading);
        recharge_username   =   (TextView)findViewById(R.id.recharge_username);
        button_recharge     =   (Button)findViewById(R.id.button_recharge);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        daili_new_value     =   (TextView)findViewById(R.id.daili_new_value);
        program_back        =   (LinearLayout) findViewById(R.id.program_back);
        recharge_listview   =   (ListView)findViewById(R.id.recharge_listview);
        recharge_daili      =   (RelativeLayout)findViewById(R.id.recharge_daili);
        daili_recharge_time =   (EditText)findViewById(R.id.daili_recharge_time);
        program_back.setVisibility(View.VISIBLE);
        program_back.setOnClickListener(this);
        button_recharge.setOnClickListener(this);
        toolbar_title.setText("充值");
        if(UserMessage.getInstance().getRole_id().equals("1")){
            recharge_daili.setVisibility(View.VISIBLE);
            recharge_username.setText("当前账户名:"+UserMessage.getInstance().getUsername());
            recharge_listview.setVisibility(View.GONE);
        }else if(UserMessage.getInstance().getRole_id().equals("3")){
            recharge_daili.setVisibility(View.GONE);
            recharge_listview.setVisibility(View.VISIBLE);
            rechargeAdapter = new RechargeAdapter(this,vauleMessageList);
            recharge_listview.setAdapter(rechargeAdapter);
            rechargeAdapter.notifyDataSetChanged();
            recharge_username.setText("当前账户名:"+UserMessage.getInstance().getUsername());
            recharge_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    selectPosition = position;
                    month = Integer.parseInt(vauleMessageList.get(position).getMouth());
                    money = Integer.parseInt(vauleMessageList.get(position).getMoney());
                    rechargeAdapter.notifyDataSetChanged();
                }
            });
        }
    }

    public void requestRechargeData(){
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API + Constants.RECHARGE+"?user_id="+UserMessage.getInstance().getUser_id());
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try{
                    JSONObject  jsonObject = new JSONObject(json);
                    if(UserMessage.getInstance().getRole_id().equals("1")) {
                        String  package_money   = jsonObject.getString("package_money");
                        money = Integer.parseInt(package_money);
                        daili_new_value.setText("￥"+money);
                    }else  if(UserMessage.getInstance().getRole_id().equals("3")){
                        JSONArray   menu   = jsonObject.getJSONArray("menu");
                        VauleMessage vauleMessage;
                        for(int i=0;i<menu.length();i++){
                            String times = menu.getJSONObject(i).getString("text");
                            String money = menu.getJSONObject(i).getString("money");
                            String month = menu.getJSONObject(i).getString("month");
                            vauleMessage = new VauleMessage(times,olds[i],"￥"+money,month,money);
                            vauleMessageList.add(vauleMessage);
                        }
                        rechargeAdapter.notifyDataSetChanged();
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

    public void requestPayData(){
        enable(false);
        OKHttpManager httpManager = new OKHttpManager();
        Map<String,String> map = new HashMap<>();
        map.put("user_id",UserMessage.getInstance().getUser_id());
        map.put("month",month+"");
        map.put("payChannel",payChannel);
        httpManager.post(Constants.API + Constants.RECHARGE, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                try{
                    JSONObject  jsonObject = new JSONObject(reason);
                    String      url        = jsonObject.getString("url");
                    Intent      intent     = new Intent(RechargeActivity.this,BannerLinkActivity.class);
                    Bundle      bundle     = new Bundle();
                    enable(true);
                    bundle.putString("url",url);
                    bundle.putString("title","二维码支付");
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(String response) {

            }
        },false);
        allmoney = 0;
    }

    public void showZFView(){
        int flag = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        zhifuview = RechargeActivity.this.getLayoutInflater().inflate(R.layout.zhifu_layout, null,false);
        zhifuwindow = new PopupWindow(zhifuview, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, true);
        zhifuwindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        zhifuwindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        zhifuwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        zhifuwindow.setAnimationStyle(R.style.popup_window_anim);
        zhifuwindow.setOutsideTouchable(true);
        zhifuview.setSystemUiVisibility(flag);
        zhifuwindow.update();
        if (zhifuwindow.isShowing()) {
            zhifuwindow.dismiss();
        } else {
            zhifuwindow.showAtLocation(zhifuview, Gravity.BOTTOM, 0, 0);
            zhifuwindow.setFocusable(true);
          /*  zhifuwindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    zhifuwindow.setFocusable(false);
                    zhifuwindow.dismiss();
                    return false;
                }
            });*/
        }
        setZhifuView(zhifuview);
        zhifuwindow.showAsDropDown(zhifuview);
    }

    public void setZhifuView(View view){
        weixin = (Button)view.findViewById(R.id.weixin);
        zhifubao = (Button)view.findViewById(R.id.zhifubao);
        zhifu_close =  (ImageView)view.findViewById(R.id.zhifu_close);
        weixin.setOnClickListener(this);
        zhifubao.setOnClickListener(this);
        zhifu_close.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.program_back:
                finish();
                break;
            case R.id.button_recharge:
                if (Util.isFastDoubleClick()) {
                    return;
                }else{
                    if(UserMessage.getInstance().getRole_id().equals("1")){
                        if(!daili_recharge_time.getText().toString().equals("")){
                            month = Integer.parseInt(daili_recharge_time.getText().toString());
                            allmoney = money*month;
                            daili_new_value.setText("￥"+allmoney);
                            if(allmoney > 3000){
                                ToastUtil.getShortToastByString(RechargeActivity.this,"充值不得超过3000元,请重新填写月份!");
                            }else{
                                showZFView();
                            }
                        }else{
                            ToastUtil.getShortToastByString(RechargeActivity.this,"请输入月份");
                        }
                    }else if(UserMessage.getInstance().getRole_id().equals("3")){
                        if(selectPosition == -1){
                            ToastUtil.getShortToastByString(RechargeActivity.this,"请选择月份");
                        }else{
                            showZFView();
                        }
                    }
                }
                break;
            case R.id.weixin:
                zhifuwindow.dismiss();
                payChannel = "21";
                requestPayData();
                break;
            case R.id.zhifubao:
                zhifuwindow.dismiss();
                payChannel = "30";
                requestPayData();
                break;
            case R.id.zhifu_close:
                zhifuwindow.dismiss();
                break;
        }
    }

    private void enable(boolean enable) {
        if (enable) {
            loading.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.VISIBLE);
        }
    }

    public class RechargeAdapter extends BaseAdapter {
        private Context context;
        private List<VauleMessage>  rechargeList;
        private LayoutInflater mInflater;
        public RechargeAdapter(Context context,List<VauleMessage> mList){
            this.context = context;
            this.rechargeList = mList;
            mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
            return rechargeList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            RechargeViewHolder viewHolder = null;
            if(convertView == null){
                convertView = mInflater.inflate(R.layout.item_recharge,parent,false);
                viewHolder = new RechargeViewHolder();
                viewHolder.recharge_time    = (TextView)convertView.findViewById(R.id.recharge_time);
                viewHolder.old_value        = (TextView)convertView.findViewById(R.id.old_value);
                viewHolder.new_value        = (TextView)convertView.findViewById(R.id.new_value);
                viewHolder.recharge_radiobutton = (RadioButton)convertView.findViewById(R.id.recharge_radiobutton);
                viewHolder.recharger_layout = (RelativeLayout) convertView.findViewById(R.id.recharger_layout);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (RechargeViewHolder)convertView.getTag();
            }
            if (position % 2 == 0) {//设置每个子项的背景颜色
                viewHolder.recharger_layout.setBackgroundResource(R.drawable.shape_while_item);
            } else {
                viewHolder.recharger_layout.setBackgroundResource(R.drawable.shape_shadow);
            }
            viewHolder.old_value.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            viewHolder.recharge_time.setText(rechargeList.get(position).getRecharge_time());
            viewHolder.old_value.setText(rechargeList.get(position).getOld_value());
            viewHolder.new_value.setText(rechargeList.get(position).getNew_value());
            if(selectPosition == position){
                viewHolder.recharge_radiobutton.setChecked(true);
            }
            else{
                viewHolder.recharge_radiobutton.setChecked(false);
            }
            return convertView;
        }

        public class RechargeViewHolder{
            public TextView         recharge_time,old_value,new_value;
            public RadioButton      recharge_radiobutton;
            public RelativeLayout   recharger_layout;
        }

    }
}
