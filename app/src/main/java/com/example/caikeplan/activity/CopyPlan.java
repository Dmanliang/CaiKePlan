package com.example.caikeplan.activity;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.MyGridView;
import com.example.caikeplan.logic.OptionsPickerView;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.adapter.CopyAdapter;
import com.example.caikeplan.logic.adapter.CopyAddAdapter;
import com.example.caikeplan.logic.adapter.PlanAdapter;
import com.example.caikeplan.logic.message.PlanBaseMessage;
import com.example.caikeplan.logic.message.PlanMessage;
import com.example.caikeplan.logic.message.PlanTypeMessage;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.getJson.HttpTask;
import com.example.mainlist.MainListBean;
import com.example.personal.PersonalCopy;
import com.youth.xframe.cache.XCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2017/5/22.
 */

public class CopyPlan extends BaseActivity implements View.OnClickListener{

    private EditText            copy_plan_title,copy_content_title,rangestitle,planstitle,numstitle,resultstitle,edit_end_time,copylist,edit_end_text;
    private TextView            mostplan;
    private RelativeLayout      showmessage_layout;
    private RelativeLayout      copylist_layout;
    private RelativeLayout      copyplan_toolbar;
    private RelativeLayout      toolbar_container;
    private ImageView           back;
    private TextView            back_text;
    private LinearLayout        program_back;
    private TextView            play_select;
    private TextView            toolbar_title;
    private ImageView           title_arrow;
    private TextView            toolbar_set;
    private TextView            toolbar_save;
    private ImageView           btn_copy_plan;
    private TextView            save_copy;
    private TextView            textplan;
    private RelativeLayout      copyadditem;
    private RelativeLayout      switch_copy_plan;
    private MyGridView          copy_add_gridview;
    private MyGridView          copy_gridview;
    private Button              checkplan;
    private CopyAddAdapter      copyAddAdapter;
    private CopyAdapter         copyAdapter;
    private OptionsPickerView   pickerView;
    private PopupWindow         copyeditWindow,windowItem;
    private View                copyeditView;
    private ArrayList<PlanTypeMessage>              options1Items               = new ArrayList<>();        //用于保存第一层选择器名称
    private ArrayList<ArrayList<String>>            options2Items               = new ArrayList<>();        //用于保存第二层选择器名称
    private ArrayList<ArrayList<ArrayList<String>>> options3Items               = new ArrayList<>();        //用于保存第三层选择器名称
    private List<List<List<MainListBean>>>  planAllSelectList                   = new ArrayList<>();        //保存总的三计划玩法层数据按play_cls区分
    private List<List<MainListBean>>        planSecondSelectList                = new ArrayList<>();        //保存第二层计划玩法数据按play_id区分
    private List<MainListBean>              mainListBeen                        = new ArrayList<>();        //用于保存请求所有计划玩法数据
    private List<PlanBaseMessage>           copyList                            = new ArrayList<>();        //用于保存请求筛选后的数据
    private List<PlanBaseMessage>           copyAddList                         = new ArrayList<>();        //用于保存请求筛选后的数据
    private List<PlanMessage> 	            mList           =   new ArrayList<>();
    private PlanTypeMessage                 planTypeMessage =   new PlanTypeMessage();
    private PlanTypeMessage.BitBean         bitBean         =   new PlanTypeMessage.BitBean();
    private String[]                        types           =   {"定位", "直选", "组选", "大小", "单双", "和值", "单式"};
    private String[]                        dangshi_play_id =   {"10040","10041","10042","10043","10044","10045","10046"};
    private String                          lotteryId,plan_name,plan_s_ids="";
    private String                          tempPlay_cls    =   "1";
    private String                          tempPlay_id     =   "10001";
    private int                             index=0;
    private Bundle                          bundle;
    private String 					        update_time;
    private SimpleDateFormat                format  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private XCache                          xCache;
    private int                             cacheindex=0;
    private int                             titleindex=0;
    private String                          plan_id;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.copyplan);
        initView();
    }

    //请求数据
    public void requestData(){
        requestHotPlan();
        requestPlanContent();
    }

    //初始化控件
    public void initView(){
        xCache              =   XCache.get(this);
        bundle              =   this.getIntent().getExtras();
        lotteryId           =   bundle.getString("lotteryId");
        mostplan            =   (TextView)findViewById(R.id.mostplan);
        copyplan_toolbar    =   (RelativeLayout)findViewById(R.id.copyplan_toolbar);
        toolbar_container   =   (RelativeLayout)findViewById(R.id.toolbar_container);
        copylist_layout     =   (RelativeLayout)findViewById(R.id.copylist_layout);
        showmessage_layout  =   (RelativeLayout)findViewById(R.id.showmessage_layout);
        program_back        =   (LinearLayout)findViewById(R.id.program_back);
        back                =   (ImageView)findViewById(R.id.back);
        back_text           =   (TextView)findViewById(R.id.back_text);
        play_select         =   (TextView)findViewById(R.id.play_select);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        textplan            =   (TextView)findViewById(R.id.textplan);
        title_arrow         =   (ImageView)findViewById(R.id.title_arrow);
        toolbar_set         =   (TextView)findViewById(R.id.toolbar_set);
        toolbar_save        =   (TextView)findViewById(R.id.toolbar_save);
        btn_copy_plan       =   (ImageView)findViewById(R.id.btn_copy_plan);
        copy_add_gridview   =   (MyGridView)findViewById(R.id.copy_add_gridview);
        copy_gridview       =   (MyGridView)findViewById(R.id.copy_gridview);
        copyadditem         =   (RelativeLayout)findViewById(R.id.copyadditem);
        switch_copy_plan    =   (RelativeLayout)findViewById(R.id.switch_copy_plan);
        checkplan           =   (Button)findViewById(R.id.checkplan);
        toolbar_title.setText("复制计划");
        program_back.setVisibility(View.VISIBLE);
        toolbar_set.setVisibility(View.VISIBLE);
        program_back.setVisibility(View.VISIBLE);
        program_back.setOnClickListener(this);
        toolbar_set.setOnClickListener(this);
        switch_copy_plan.setOnClickListener(this);
        checkplan.setOnClickListener(this);
        setCopyAddGridView();
        SendMessage.getInstance().setTempMid();
        SendMessage.getInstance().setTempdivider();
        requestData();
    }

    //设置选择器
    public void initOptionPicker(){
        pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, final int options2, int options3, View v) {
                copyList.clear();
                textplan.setText(options2Items.get(options1).get(options2) + options3Items.get(options1).get(options2).get(options3));
                requestSelectPlanData(options1,options2,options3);
            }
        })
                .setTitleText("玩法选择")
                .setSubCalSize(13)
                .setTitleSize(14)
                .setContentTextSize(14)
                .setTitleColor(this.getResources().getColor(R.color.blackText))
                .setSubmitColor(this.getResources().getColor(R.color.blueText3))
                .setCancelColor(this.getResources().getColor(R.color.blueText3))
                .setTitleBgColor(this.getResources().getColor(R.color.grayBack))
                .setBgColor(this.getResources().getColor(R.color.whileBack))
                .setDividerColor(this.getResources().getColor(R.color.grayDivider2))
                .setTextColorCenter(this.getResources().getColor(R.color.grayContent))
                .setSelectOptions(0, 0, 0)
                .setOutSideCancelable(false)
                .build();
        if(options1Items.size() != 0){
            pickerView.setPicker(options1Items, options2Items, options3Items);
        }
    }

    //设置复制计划列表
    public void setCopyGridView() {
        copyAdapter = new CopyAdapter(this, copyList);
        copy_gridview.setAdapter(copyAdapter);
        copyAdapter.notifyDataSetChanged();
        copy_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(copyAddList.size()>=9){
                    ToastUtil.getShortToastByString(CopyPlan.this,"计划添加已上限!");
                    mostplan.setTextColor(getResources().getColor(R.color.redlight));
                }else{
                    mostplan.setTextColor(getResources().getColor(R.color.whileDivider));
                    if(!repeat(copyList.get(position))){
                        addItemCache(copyList.get(position));
                    }else{
                        ToastUtil.getShortToastByString(CopyPlan.this,"计划已添加!");
                    }
                }
                SendMessage.getInstance().getTest2().clear();
            }
        });
    }

    //请求整个查询计划的数据
    public void requestCheckData(){
        plan_s_ids="";
        for(int i=0;i<copyAddList.size();i++){
            if(i==copyAddList.size()-1){
                plan_s_ids = plan_s_ids+copyAddList.get(i).getPlan_id()+":"+copyAddList.get(i).getS_id();
            }else{
                plan_s_ids = plan_s_ids+copyAddList.get(i).getPlan_id()+":"+copyAddList.get(i).getS_id()+",";
            }
        }
    }

    //请求计划分页数据
    public void requestPlanResult(){
        SendMessage.getInstance().getTest2().clear();
        titleindex=0;
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API+Constants.COPY_PLAN+plan_s_ids);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try{
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    PlanMessage planMessage;
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject          = data.getJSONObject(i);
                        String  plan_num    = jsonObject.getString("plan_num");
                        String  nums        = jsonObject.getString("nums");
                        String  play_id     = jsonObject.getString("play_id");
                        String  lottery_id  = jsonObject.getString("lottery_id");
                        String  ranges      = jsonObject.getString("ranges");
                        String  plan_id     = jsonObject.getString("plan_id");
                        String  hit         = jsonObject.getString("hit");
                        String  issue       = jsonObject.getString("issue")+"期";
                        String  nums_type   = jsonObject.getString("nums_type");
                        hit     =	hit.replaceAll("-1", "进行中").replaceAll("1", "中").replaceAll("0", "未中");
                        setCopyMessage();
                        if(i%5==0 && i!=0){
                            SendMessage.getInstance().setLotteryTitle(copyAddList.get(titleindex).getScheme_name());
                            titleindex++;
                        }
                        if(isDanshi(play_id)){
                            plan_num    = dividerNum(plan_num);
                            SendMessage.getInstance().setCopyPlan(ranges,nums_type,plan_num,issue+" "+nums,"  "+hit,i,true);
                        }else{
                            SendMessage.getInstance().setCopyPlan(ranges,nums_type,plan_num,issue+" "+nums,"  "+hit,i,false);
                        }
                        if(i== data.length()-1){
                            SendMessage.getInstance().setLotteryTitle(copyAddList.get(titleindex).getScheme_name());
                        }
                        index++;
                        planMessage = new PlanMessage(plan_num,nums,play_id,lottery_id,ranges,plan_id,hit,issue,nums_type);
                        mList.add(planMessage);
                    }
                    Message msg = new Message();
                    msg.what = 4;
                    recomdHandler.sendMessage(msg);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {
            }
        });
    }

    //对单式进行分割
    public String dividerNum(String plan_n){
        plan_n = plan_n.replace(" ",SendMessage.getInstance().getDivider());
        return plan_n;
    }

    //获取复制头中尾信息
    public void setCopyMessage(){
        if(xCache.getAsString("head")!=null){
            SendMessage.getInstance().setCopyhead(xCache.getAsString("head"));
        }
        if(xCache.getAsString("mid")!=null){
            SendMessage.getInstance().setCopymid(xCache.getAsString("mid"));
        }
        if(xCache.getAsString("end")!=null){
            SendMessage.getInstance().setCopyEnd(xCache.getAsString("end"));
        }
        if(xCache.getAsString("divider")!=null){
            SendMessage.getInstance().setDivider(xCache.getAsString("divider"));
        }
    }

    //添加计划去重操作
    public boolean repeat(PlanBaseMessage planbaseMessage){
        boolean isreapeat=false;
        for(int i=0;i<copyAddList.size();i++){
            if(planbaseMessage.getPlan_id().equals(copyAddList.get(i).getPlan_id())&&planbaseMessage.getS_id().equals(copyAddList.get(i).getS_id())){
                isreapeat = true;
                break;
            }
        }
        return isreapeat;
    }

    //添加子项
    public void addItemCache(PlanBaseMessage planBaseMessage){
        for(int i=0;i<9;i++){
            if(xCache.getAsObject("c"+i)==null){
                planBaseMessage.setIndex(i);
                copyAddList.add(planBaseMessage);
                xCache.put("c"+i,planBaseMessage);
                break;
            }
        }
        Message msg = new Message();
        msg.what = 3;
        recomdHandler.sendMessage(msg);
    }

    //设置添加复制计划列表
    public void setCopyAddGridView() {
        for(int i=0;i<9;i++){
            if(xCache.getAsObject("c"+i)!=null){
                copyAddList.add((PlanBaseMessage)xCache.getAsObject("c"+i));
            }
        }
        copyAddAdapter = new CopyAddAdapter(this, copyAddList);
        copy_add_gridview.setAdapter(copyAddAdapter);
        copyAddAdapter.notifyDataSetChanged();
        copy_add_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                removeCache(position);
                mostplan.setTextColor(getResources().getColor(R.color.whileDivider));
            }
        });
        copyAddAdapter.setColseClick(new CopyAddAdapter.CloseClickListener() {
            @Override
            public void CloseClick(CopyAddAdapter.AddGridViewHolder viewHolder, int position) {
                removeCache(position);
                mostplan.setTextColor(getResources().getColor(R.color.whileDivider));
            }
        });
        Message msg = new Message();
        msg.what = 3;
        recomdHandler.sendMessage(msg);
    }

    //移除内存选项
    public void removeCache(int position){
        cacheindex=copyAddList.get(position).getIndex();
        copyAddList.remove(position);
        xCache.remove("c"+cacheindex);
        Message msg = new Message();
        msg.what = 3;
        recomdHandler.sendMessage(msg);
    }

    //请求热门计划数据
    public void requestHotPlan() {
        copyList.clear();
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API + Constants.HOT_SCHEME + lotteryId);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    PlanBaseMessage planBaseMessage;
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject = data.getJSONObject(i);
                        String scheme_name  = jsonObject.getString("scheme_name");
                        String s_id         = jsonObject.getString("s_id");
                        String plan_id      = jsonObject.getString("plan_id");
                        String plan_name    = jsonObject.getString("plan_name");
                        String cls_name     = jsonObject.getString("cls_name");
                        planBaseMessage = new PlanBaseMessage(SendMessage.getInstance().getLotteryName(),lotteryId, s_id, scheme_name+plan_name.substring(0,2), plan_id,plan_name,cls_name,"");
                        copyList.add(planBaseMessage);
                    }
                    Message msg = new Message();
                    msg.what = 2;
                    recomdHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void taskFailed() {

            }
        });
    }

    public void requestPlanContent(){
        planAllSelectList.clear();
        options1Items.clear();
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API+Constants.PLAN_CONTENT+lotteryId);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for(int h=0;h<data.length();h++){
                        jsonObject = data.getJSONObject(h);
                        JSONArray child= jsonObject.getJSONArray("child");
                        for (int i = 0; i < child.length(); i++) {
                            JSONObject object = child.getJSONObject(i);
                            planSecondSelectList= new ArrayList<>();
                            PlanTypeMessage planTypeMessage = new PlanTypeMessage();
                            String cls_name = object.getString("name");
                            planTypeMessage.setName(cls_name);
                            JSONArray child1 = object.getJSONArray("child");
                            for(int j = 0;j < child1.length();j++){
                                JSONObject object1 = child1.getJSONObject(j);
                                mainListBeen = new ArrayList<>();
                                PlanTypeMessage.BitBean bitBean = new PlanTypeMessage.BitBean();
                                String plan_name = object1.getString("name");
                                bitBean.setName(plan_name);
                                bitBean.newList();
                                JSONArray child2 = object1.getJSONArray("child");
                                for(int k = 0;k <child2.length();k++){
                                    JSONObject object2= child2.getJSONObject(k);
                                    MainListBean newsBean = new MainListBean();
                                    String play_name = object2.getString("name");
                                    newsBean.setPlay_name(play_name);
                                    bitBean.getIssues_list().add(play_name);
                                    String plan_id = object2.getString("id");
                                    newsBean.setPlan_id(plan_id);
                                    mainListBeen.add(newsBean);
                                }
                                planTypeMessage.getBitBeen().add(bitBean);
                                planSecondSelectList.add(mainListBeen);
                            }
                            planAllSelectList.add(planSecondSelectList);
                            options1Items.add(planTypeMessage);
                        }
                    }
                    setSelectTitle();
                    Message msg = new Message();
                    msg.what = 1;
                    recomdHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {

            }
        });
    }

    //请求筛选后的计划玩法数据
    public void requestSelectPlanData(final int item1,final int item2,final int item3) {
        copyList.clear();
        HttpTask httpTask = new HttpTask();
        if(planAllSelectList.size()!=0){
            plan_id = planAllSelectList.get(item1).get(item2).get(item3).getPlan_id();
        }
        httpTask.execute(Constants.API + Constants.SCHEME_PLAN + plan_id);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    PlanBaseMessage planBaseMessage;
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject = data.getJSONObject(i);
                        String scheme_name  = jsonObject.getString("scheme_name");
                        String s_id         = jsonObject.getString("s_id");
                        String plan_id      = jsonObject.getString("plan_id");
                        String plan_name    = options2Items.get(item1).get(item2);
                        String cls_name     = jsonObject.getString("cls_name");
                        planBaseMessage = new PlanBaseMessage(SendMessage.getInstance().getLotteryName(),lotteryId, s_id, scheme_name+plan_name.toString().substring(0,2), plan_id,plan_name,cls_name,"");
                        copyList.add(planBaseMessage);
                    }
                    Message msg = new Message();
                    msg.what = 2;
                    recomdHandler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {

            }
        });
    }

    //设置玩法选择
    public void setSelectTitle() {
        for(int i=0;i<options1Items.size();i++){
            ArrayList<String>   bits = new ArrayList<>();
            ArrayList<ArrayList<String>>   issues = new ArrayList<>();
            for(int c=0;c<options1Items.get(i).getBitBeen().size();c++){
                String bitName = options1Items.get(i).getBitBeen().get(c).getName();
                bits.add(bitName);
                ArrayList<String>   bit_issueList = new ArrayList<>();
                if(options1Items.get(i).getBitBeen().get(c).getIssues_list() == null ||
                        options1Items.get(i).getBitBeen().get(c).getIssues_list().size() == 0){
                    bit_issueList.add("");
                }else{
                    for(int d=0;d<options1Items.get(i).getBitBeen().get(c).getIssues_list().size();d++){
                        String issueName = options1Items.get(i).getBitBeen().get(c).getIssues_list().get(d);
                        bit_issueList.add(issueName);
                    }
                }
                issues.add(bit_issueList);
            }
            options2Items.add(bits);
            options3Items.add(issues);
        }

    }

    //显示复制编辑弹框
    public void showCopyEdit(){
        copyeditView = CopyPlan.this.getLayoutInflater().inflate(R.layout.edit_copy_text, null,false);
        copyeditWindow = new PopupWindow(copyeditView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, true);
        copyeditWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        copyeditWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        copyeditWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        copyeditWindow.setAnimationStyle(R.style.popup_window_anim);
        copyeditWindow.setOutsideTouchable(true);
        copyeditWindow.update();
        if (copyeditWindow.isShowing()) {
            copyeditWindow.dismiss();
        } else {
            copyeditWindow.showAtLocation(copyeditView, Gravity.CENTER, 0, 0);
            copyeditWindow.setFocusable(true);
            copyeditWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    copyeditWindow.setFocusable(false);
                    copyeditWindow.dismiss();
                    return false;
                }
            });
        }
        setCopyEdit(copyeditView);
        copyeditWindow.showAsDropDown(copyeditView);
    }

    //设置复制编辑控件
    public void setCopyEdit(View view){
        update_time         = format.format(time());
        save_copy           = (TextView)view.findViewById(R.id.save_copy);
        copy_plan_title     = (EditText)view.findViewById(R.id.copy_plan_title);
        copy_content_title  = (EditText)view.findViewById(R.id.copy_content_title);
        edit_end_time       = (EditText)view.findViewById(R.id.edit_end_time);
        copylist            = (EditText)view.findViewById(R.id.copy_list);
        edit_end_text       = (EditText)view.findViewById(R.id.edit_end_text);
        copy_content_title.setFocusable(true);
        copy_content_title.setFocusableInTouchMode(true);
        copy_content_title.requestFocus();
        if(xCache.getAsString("head")!=null){
            copy_plan_title.setText(xCache.getAsString("head"));
        }
        if(xCache.getAsString("end")!=null){
            edit_end_text.setText(xCache.getAsString("end"));
        }
        if(xCache.getAsString("mid")!=null){
            SendMessage.getInstance().setCopymid(xCache.getAsString("mid"));
        }
        if(xCache.getAsString("divider")!=null){
            SendMessage.getInstance().setDivider(xCache.getAsString("divider"));
        }
        SendMessage.getInstance().updateMid();
        copylist.setText(CopyPlanList());
        edit_end_time.setText("更新时间："+update_time);
        save_copy.setOnClickListener(this);
    }

    //加载数据弹框
    public void showItem(View view) {
        view = CopyPlan.this.getLayoutInflater().inflate(R.layout.loadingdata, null);
        windowItem = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, true);
        windowItem.setOutsideTouchable(true);
        windowItem.update();
        if (!windowItem.isShowing()) {
            windowItem.showAtLocation(view, Gravity.CENTER, 0, 0);
            windowItem.setFocusable(true);
        }
    }

    //判断是否是单式
    public boolean isDanshi(String play_id){
        for(int i=0;i<dangshi_play_id.length;i++){
            if(play_id.equals(dangshi_play_id[i])){
                return true;
            }
        }
        return false;
    }

    //复制并保存计划
    public String CopyAndSavePlan(){
        update_time=edit_end_time.getText().toString();
        String copyString = "";
        copyString +=copy_plan_title.getText().toString()+":"+copy_content_title.getText().toString();
        copyString +=copy_content_title.getText().toString();
        copyString +=getResources().getString(R.string.line);
        copyString +=getResources().getString(R.string.lottery_title);
        copyString +=copylist.getText();
        copyString +=getResources().getString(R.string.line);
        copyString +=edit_end_text.getText().toString()+"\n";
        copyString +=getResources().getString(R.string.update);
        copyString +=update_time;
        return copyString;
    }

    @Override
    public void onClick(View v)  {
        switch (v.getId()){
            case R.id.switch_copy_plan:
                if(pickerView!=null){
                    pickerView.show();
                }
                break;
            case R.id.checkplan:
                setCopyMessage();
                requestCheckData();
                requestPlanResult();
                showItem(v);
                break;
            case R.id.program_back:
                finish();
                break;
            case R.id.toolbar_set:
                Intent intent = new Intent(CopyPlan.this, PersonalCopy.class);
                startActivity(intent);
                break;
            case R.id.save_copy:
                setCopyMessage();
                ClipboardManager cms = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cms.setText(CopyAndSavePlan());
                ToastUtil.getShortToastByString(CopyPlan.this,"复制成功！");
                copyeditWindow.dismiss();
                break;
        }
    }

    //更新UI线程信息
    public Handler recomdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //设置计划选择器
                    initOptionPicker();
                    break;
                case 2:
                    //更新筛选列表
                    setCopyGridView();
                    break;
                case 3:
                    if(copyAddList.size()!=0){
                        showmessage_layout.setVisibility(View.GONE);
                        copylist_layout.setVisibility(View.VISIBLE);
                        copyAddAdapter.notifyDataSetChanged();
                    }else{
                        copylist_layout.setVisibility(View.GONE);
                        showmessage_layout.setVisibility(View.VISIBLE);
                    }
                    break;
                case 4:
                    windowItem.dismiss();
                    showCopyEdit();
                    break;
            }
        }
    };

    //复制计划
    public String CopyPlanList(){
        String copyString = "";
        for (int k = SendMessage.getInstance().getTest2().size()-1; k >= 0; k--) {
            if(k%6==0 && k!=0){
                copyString  += SendMessage.getInstance().getCopyPlan(k);
                copyString  += getResources().getString(R.string.line1);
            }else{
                copyString  += SendMessage.getInstance().getCopyPlan(k);
            }
        }
        return copyString;
    }

    //获取本地时间
    public Date time() {
        Date date = new Date();
        return date;
    }

}
