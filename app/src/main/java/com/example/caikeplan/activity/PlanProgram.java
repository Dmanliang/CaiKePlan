package com.example.caikeplan.activity;


import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.adapter.CopyEditAdpater;
import com.example.caikeplan.logic.adapter.PlanProgramAdapter;
import com.example.caikeplan.logic.message.PlanMessage;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.caikeplan.logic.scrollview.BorderScrollView;
import com.example.getJson.HttpTask;
import com.example.personal.PersonalCopy;
import com.youth.xframe.cache.XCache;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2017/5/19.
 */

public class PlanProgram extends BaseActivity implements View.OnClickListener {

    private SwipeRefreshLayout  swipeRefreshLayout;
    private XCache              xCache;
    private RelativeLayout      plan_toolbar;
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
    private EditText            copy_plan_title, copy_content_title, rangestitle, planstitle, numstitle, resultstitle, edit_end_time, copylist, edit_end_text;
    private PopupWindow         windowItem, copyeditWindow,loadItem;
    private View                copyView, header,loadview;
    private Bundle              bundle;
    private ListView            planlistView;
    private android.widget.ListView copy_list;
    private List<PlanMessage>   mList = new ArrayList<>();
    private PlanProgramAdapter  planProgramAdapter;
    private CopyEditAdpater     copyEditAdpater;
    private ImageView           btn_copy, btn_edit,btn_collect;
    private TextView            plan_accuracy, plan_cycle, plan_wrong;
    private String              plan_id, s_id, scheme_name, lottery_name, plan_name, cls_name;            //计划id和对应第几个计划
    private String              bestS_id, bestRate, rate;                                                 //最高命中率的计划和最高命中率,本计划命中率
    private int                 countYes = 0, countNo = 0, countCyecles = 0;                              //统计正确率,错误率和周期
    private String              update_time;
    private SimpleDateFormat    format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
    private String              issue;
    private String              plans;
    private String              nums;
    private String              ranges;
    private String              hit;
    private int                 index = 0;
    private String[]            dangshi_play_id = {"10040", "10041", "10042", "10043", "10044", "10045", "10046"};
    private TextView            copy_ranges;
    private TextView            copy_context;
    private TextView            copy_plays;
    private Button              copy_close;
    private Button              copy_button;
    private BorderScrollView    scrollView;
    private String              textRanges;
    private String              textPlays;
    private String              textNum;
    private TextView            plan;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.plan_program);
        initView();
        requestPlanResult();
    }

    //初始化控件
    public void initView() {
        xCache = XCache.get(this);
        getDataType();
        swipeRefreshLayout  = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_plan);
        plan_toolbar        = (RelativeLayout) findViewById(R.id.plan_toolbar);
        toolbar_container   = (RelativeLayout) findViewById(R.id.toolbar_container);
        program_back        = (LinearLayout) findViewById(R.id.program_back);
        back                = (ImageView) findViewById(R.id.back);
        back_text           = (TextView) findViewById(R.id.back_text);
        play_select         = (TextView) findViewById(R.id.play_select);
        toolbar_title       = (TextView) findViewById(R.id.toolbar_title);
        title_arrow         = (ImageView) findViewById(R.id.title_arrow);
        toolbar_set         = (TextView) findViewById(R.id.toolbar_set);
        toolbar_save        = (TextView) findViewById(R.id.toolbar_save);
        btn_copy_plan       = (ImageView) findViewById(R.id.btn_copy_plan);
        planlistView        = (ListView) findViewById(R.id.planlistView);
        btn_copy            = (ImageView) findViewById(R.id.btn_copy);
        btn_edit            = (ImageView) findViewById(R.id.btn_edit);
        btn_collect         = (ImageView) findViewById(R.id.btn_collect);
        header              = LayoutInflater.from(PlanProgram.this).inflate(R.layout.plan_title, null);
        plan_accuracy       = (TextView) header.findViewById(R.id.plan_accuracy);
        plan_cycle          = (TextView) header.findViewById(R.id.plan_cycle);
        plan_wrong          = (TextView) header.findViewById(R.id.plan_wrong);
        plan                = (TextView) header.findViewById(R.id.plan);
        planlistView.addHeaderView(header);
        setSwipeRefreshLayout();
        btn_copy.setOnClickListener(this);
        btn_edit.setOnClickListener(this);
        btn_collect.setOnClickListener(this);
        toolbar_set.setOnClickListener(this);
        program_back.setOnClickListener(this);
        btn_copy.setClickable(false);
        btn_edit.setClickable(false);
        program_back.setVisibility(View.VISIBLE);
        toolbar_set.setVisibility(View.VISIBLE);
        back.setVisibility(View.VISIBLE);
        back_text.setVisibility(View.VISIBLE);
        toolbar_title.setText(scheme_name+plan_name.substring(0,2));
        plan.setText("当前计划："+lottery_name + plan_name);
        SendMessage.getInstance().setTempMid();
        SendMessage.getInstance().setTempdivider();
    }

    //添加收藏
    public void requestAddURL(String user_id, int lottery_id, String plan_id,String s_id,String scheme_name) {
        String url;
        url = Constants.API+Constants.PLAN_FAVORITE + user_id + "&lottery_id=" + lottery_id + "&plan_id=" + plan_id + "&s_id="+s_id +"&scheme_name=" +scheme_name ;
        HttpTask addhttpTask = new HttpTask();
        addhttpTask.execute(url);
        addhttpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                ToastUtil.getShortToastByString(PlanProgram.this,"收藏成功");
            }

            @Override
            public void taskFailed() {
                ToastUtil.getShortToastByString(PlanProgram.this,"收藏失败");
            }
        });
    }

    //获得请求参数
    public void getDataType() {
        bundle = this.getIntent().getExtras();
        plan_id = bundle.getString("plan_id");
        s_id = bundle.getString("s_id");
        scheme_name = bundle.getString("scheme_name");
        plan_name = bundle.getString("plan_name");
        cls_name = bundle.getString("cls_name");
        lottery_name = bundle.getString("lottery_name");
    }

    public void setSwipeRefreshLayout(){
        swipeRefreshLayout.setSize(SwipeRefreshLayout.DEFAULT);
        swipeRefreshLayout.setColorSchemeResources(R.color.google_blue
                ,R.color.google_green
                ,R.color.google_red
                ,R.color.google_yellow);
        //设置手势滑动监听器
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            public void onRefresh() {
                //发送一个延时1秒的handler信息
                refreshhandler.sendEmptyMessageDelayed(1,2000);
            }
        });

        //给listview设置一个滑动的监听
        planlistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            //当滑动状态发生改变的时候执行
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState){
                    //当不滚动的时候
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:

                        //判断是否是最底部
                        if(view.getLastVisiblePosition()==(view.getCount())-1){
                        }
                        break;
                }
            }
            //正在滑动的时候执行
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
            }
        });
    }

    private Handler refreshhandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(msg.what==1){
                requestPlanResult();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    //请求计划分页数据
    public void requestPlanResult() {
        reSetData();
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API + Constants.PLAN_RUSULT + plan_id + "&s_id=" + s_id + "&page_size=29");
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    bestRate = jsonObject.getString("mrate");
                    bestS_id = jsonObject.getString("ms_id");
                    rate = jsonObject.getString("rate");
                    List<PlanMessage> planMessageList = new ArrayList<>();
                    PlanMessage planMessage;
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject          = data.getJSONObject(i);
                        String plan_num     = jsonObject.getString("plan_num");
                        String nums         = jsonObject.getString("nums");
                        String play_id      = jsonObject.getString("play_id");
                        String lottery_id   = jsonObject.getString("lottery_id");
                        String ranges       = jsonObject.getString("ranges");
                        String plan_id      = jsonObject.getString("plan_id");
                        String hit          = jsonObject.getString("hit");
                        String issue        = jsonObject.getString("issue")+"期";
                        String nums_type    = jsonObject.getString("nums_type");
                        //计算准确率
                        if (hit.equals("0")) {
                            countNo  += 1;
                        } else if (hit.equals("1")) {
                            countYes += 1;
                        }
                        hit = hit.replaceAll("-1", "进行中").replaceAll("1", "中").replaceAll("0", "未中");
                        setCopyMessage();
                        if (isDanshi(play_id)) {
                            plan_num = dividerNum(plan_num);
                            SendMessage.getInstance().setSHUZU(ranges, nums_type, plan_num, issue + " " + nums, "  " + hit, index, true);
                        } else {
                            if(cls_name.equals("单双")){
                                if(plan_num.equals("1")){
                                    plan_num = "单";
                                }else if(plan_num.equals("2")){
                                    plan_num = "双";
                                }
                            }
                            if(cls_name.equals("大小")){
                                if(plan_num.equals("0")){
                                    plan_num = "小";
                                }else if(plan_num.equals("1")){
                                    plan_num = "大";
                                }
                            }
                            SendMessage.getInstance().setSHUZU(ranges, nums_type, plan_num, issue + " " + nums, "  " + hit, index, false);
                        }
                        index++;
                        //计算周期
                        countCyecles++;
                        planMessage = new PlanMessage(plan_num, nums, play_id, lottery_id, ranges, plan_id, hit, issue, nums_type);
                        planMessageList.add(planMessage);
                    }
                    mList.clear();
                    mList.addAll(planMessageList);
                    Message msg = new Message();
                    msg.what = 1;
                    recomdHandler.sendMessage(msg);
                } catch (Exception e) {
                    btn_copy.setClickable(false);
                    btn_edit.setClickable(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {
                btn_copy.setClickable(false);
                btn_edit.setClickable(false);
            }
        });
    }

    //判断是否是单式
    public boolean isDanshi(String play_id) {
        for (int i = 0; i < dangshi_play_id.length; i++) {
            if (play_id.equals(dangshi_play_id[i])) {
                return true;
            }
        }
        return false;
    }

    //显示选项复制弹框
    public void showItem(View view, int pos) {
        view = PlanProgram.this.getLayoutInflater().inflate(R.layout.program_next_detailed_dialog, null);
        windowItem = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        windowItem.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        windowItem.setOutsideTouchable(true);
        windowItem.update();
        if (windowItem.isShowing()) {
            windowItem.dismiss();
        } else {
            windowItem.showAtLocation(view, Gravity.CENTER, 0, 0);
            windowItem.setFocusable(true);
            windowItem.getContentView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    windowItem.setFocusable(false);
                    windowItem.dismiss();
                    return false;
                }
            });
        }
        scrollView = (BorderScrollView)view.findViewById(R.id.myScrollView);
        copy_ranges    = (TextView) view.findViewById(R.id.copy_ranges);
        copy_context   = (TextView) view.findViewById(R.id.copy_context);
        copy_plays     = (TextView) view.findViewById(R.id.copy_plays);
        copy_close       = (Button) view.findViewById(R.id.copy_close);
        copy_button      = (Button) view.findViewById(R.id.copy_button);
        textRanges = mList.get(pos).getRanges();
        textPlays = scheme_name + plan_name;
        textNum = mList.get(pos).getPlan_num();
        Message msg = new Message();
        msg.what = 2;
        recomdHandler.sendMessage(msg);
        final String copyContext = textNum;
        copy_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(copyContext);
                ToastUtil.getShortToastByString(PlanProgram.this, "复制成功");
                windowItem.dismiss();
            }
        });
        copy_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowItem.dismiss();
            }
        });

    }

    //复制子选项内容
    public void copyitemContent() {
        String textNum = mList.get(0).getPlan_num();
        String copyContext = textNum;
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setText(copyContext);
        ToastUtil.getShortToastByString(PlanProgram.this, "复制成功");
    }

    //显示复制编辑弹框
    public void showCopyEdit() {
        copyView = PlanProgram.this.getLayoutInflater().inflate(R.layout.edit_copy_text, null);
        copyeditWindow = new PopupWindow(copyView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        //   copyeditWindow.setAnimationStyle(R.style.popup_window_anim);
        copyeditWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        copyeditWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        copyView.setBackgroundColor(Color.parseColor("#80000000"));
        copyeditWindow.setOutsideTouchable(true);
        copyeditWindow.update();
        if (!copyeditWindow.isShowing()) {
            copyeditWindow.showAtLocation(copyView, Gravity.CENTER, 0, 0);
            copyeditWindow.setFocusable(true);
        }
        copyeditWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                copyeditWindow.setFocusable(false);
                copyeditWindow.dismiss();
                return false;
            }
        });
        setCopyEdit(copyView);
    }


    //设置复制编辑控件
    public void setCopyEdit(View view) {
        update_time = format.format(time());
        save_copy = (TextView) view.findViewById(R.id.save_copy);
        copy_plan_title = (EditText) view.findViewById(R.id.copy_plan_title);
        copy_content_title = (EditText) view.findViewById(R.id.copy_content_title);
        edit_end_time = (EditText) view.findViewById(R.id.edit_end_time);
        copylist = (EditText) view.findViewById(R.id.copy_list);
        edit_end_text = (EditText) view.findViewById(R.id.edit_end_text);
        copy_plan_title.setText("聚彩盆" + "(" + SendMessage.getInstance().getLotteryName() + ")");
        copy_content_title.setFocusable(true);
        copy_content_title.setFocusableInTouchMode(true);
        copy_content_title.requestFocus();
        if (xCache.getAsString("head") != null) {
            copy_plan_title.setText(xCache.getAsString("head"));
        }
        if (xCache.getAsString("end") != null) {
            edit_end_text.setText(xCache.getAsString("end"));
        }
        if (xCache.getAsString("mid") != null) {
            SendMessage.getInstance().setCopymid(xCache.getAsString("mid"));
        }
        if (xCache.getAsString("divider") != null) {
            SendMessage.getInstance().setDivider(xCache.getAsString("divider"));
        }
        SendMessage.getInstance().updateMid();
        copylist.setText(CopyPlanList());
        edit_end_time.setText("更新时间：" + update_time);
        save_copy.setOnClickListener(this);
        SendMessage.getInstance().setTempMid();
    }

    //复制计划
    public String CopyPlanList() {
        String copyString = "";
        for (int k = SendMessage.getInstance().getTest().size() - 1; k >= 0; k--) {
            copyString += SendMessage.getInstance().getSHUZU(k);
        }
        return copyString;
    }

    //复制计划
    public String CopyPlan() {
        update_time = format.format(time());
        String copyString = "";
        copyString = SendMessage.getInstance().getCopyhead();
        copyString += getResources().getString(R.string.line);
        copyString += getResources().getString(R.string.lottery_title);
        for (int k = SendMessage.getInstance().getTest().size() - 1; k >= 0; k--) {
            copyString += SendMessage.getInstance().getSHUZU(k);
        }
        copyString += getResources().getString(R.string.line);
        copyString += SendMessage.getInstance().getCopyEnd() + "\n";
        copyString += getResources().getString(R.string.update);
        copyString += update_time;
        return copyString;
    }

    //复制并保存计划
    public String CopyAndSavePlan() {
        update_time = edit_end_time.getText().toString();
        String copyString = "";
        copyString = copy_plan_title.getText().toString() + ":" + copy_content_title.getText().toString();
        copyString += copy_content_title.getText().toString();
        copyString += getResources().getString(R.string.line);
        copyString += getResources().getString(R.string.lottery_title);
        copyString += copylist.getText();
        copyString += getResources().getString(R.string.line);
        copyString += edit_end_text.getText().toString() + "\n";
        copyString += getResources().getString(R.string.update);
        copyString += update_time;
        return copyString;
    }

    //获取复制头中尾信息
    public void setCopyMessage() {
        if (xCache.getAsString("head") != null) {
            SendMessage.getInstance().setCopyhead(xCache.getAsString("head"));
        }
        if (xCache.getAsString("mid") != null) {
            SendMessage.getInstance().setCopymid(xCache.getAsString("mid"));
        }
        if (xCache.getAsString("end") != null) {
            SendMessage.getInstance().setCopyEnd(xCache.getAsString("end"));
        }
        if (xCache.getAsString("divider") != null) {
            SendMessage.getInstance().setDivider(xCache.getAsString("divider"));
        }
    }

    //获取本地时间
    public Date time() {
        Date date = new Date();
        return date;
    }

    //设置列表数据
    public void setListView(List<PlanMessage> mList){
        planProgramAdapter = new PlanProgramAdapter(this, mList);
        planlistView.setAdapter(planProgramAdapter);
        planProgramAdapter.notifyDataSetChanged();
        planlistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 1) {
                    showItem(view, position - 1);
                }
            }
        });
        planlistView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        planProgramAdapter.setmBusy(true);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        planProgramAdapter.setmBusy(false);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        planProgramAdapter.setmBusy(false);
                        break;
                    default:
                        break;
                }
                planProgramAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
        setTextData();
    }

    //设置当前状态
    public void setTextData() {
        plan_accuracy.setText(rate + "%");
        plan_cycle.setText(countCyecles + "");
        plan_wrong.setText(countNo + "");
    }

    //重设参数
    public void reSetData() {
        countCyecles = 0;
        countNo = 0;
        countYes = 0;
        SendMessage.getInstance().getTest().clear();
    }

    //对单式进行分割
    public String dividerNum(String plan_n) {
        plan_n = plan_n.replace(" ", SendMessage.getInstance().getDivider());
        return plan_n;
    }

    //对码进行排序
    public String orderPlanNum(String plan_n) {
        String[] planNums = plan_n.split(" ");
        Integer[] nums = new Integer[planNums.length];
        for (int i = 0; i < planNums.length; i++) {
            nums[i] = Integer.valueOf(planNums[i]);
        }
        int temp;
        for (int i = 0; i < nums.length - 1; i++) {
            for (int j = 0; j < nums.length - i - 1; j++) {
                if (nums[j + 1] < nums[j]) {
                    temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }
        plan_n = "";
        for (int i = 0; i < nums.length; i++) {
            if (i == nums.length - 1) {
                plan_n += Integer.toString(nums[i]);
            } else {
                plan_n += Integer.toString(nums[i]) + SendMessage.getInstance().getDivider();
            }
        }
        return plan_n;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_copy:
                setCopyMessage();
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cm.setText(CopyPlan());
                ToastUtil.getShortToastByString(PlanProgram.this, "复制成功");
                break;
            case R.id.btn_edit:
                showCopyEdit();
                break;
            case R.id.save_copy:
                setCopyMessage();
                ClipboardManager cms = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                cms.setText(CopyAndSavePlan());
                ToastUtil.getShortToastByString(PlanProgram.this, "复制成功");
                copyeditWindow.dismiss();
                break;
            case R.id.toolbar_set:
                Intent intent = new Intent(PlanProgram.this, PersonalCopy.class);
                startActivity(intent);
                break;
            case R.id.program_back:
                finish();
                break;
            case R.id.btn_collect:
                 requestAddURL(UserMessage.getInstance().getUser_id(),SendMessage.getInstance().getLotteryId(),plan_id,s_id,scheme_name);
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
                    setListView(mList);
                    btn_copy.setClickable(true);
                    btn_edit.setClickable(true);
                    break;
                case 2:
                    copy_ranges.setText("期号: " + textRanges);
                    copy_plays.setText("玩法: " + textPlays);
                    copy_context.setText("内容: " + textNum);
                    break;
            }
        }
    };


}
