package com.example.caikeplan.activity;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.LotteryTitle;
import com.example.caikeplan.logic.adapter.LotteryTitleAdapter;
import com.example.caikeplan.logic.WifiAdmin;
import com.example.caikeplan.logic.wheelview.OnWheelChangedListener;
import com.example.caikeplan.logic.wheelview.WheelView;
import com.example.caikeplan.logic.wheelview.adapter.ArrayWheelAdapter;
import com.example.collect.CollectActivity;
import com.example.getJson.HttpTask;
import com.example.mainlist.MainListAdapter;
import com.example.mainlist.MainListBean;
import com.example.next.list.ProgramMainNext;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.next.list.listview.ListView;
import com.example.next.list.listview.PullToRefreshLayout;

public class FragmentProgram extends Activity implements OnClickListener {

    private RelativeLayout                  relativeTab;
    private Button                          mark_btn, collect_btn;
    private HorizontalScrollView            scrollView;
    private RadioGroup                      radio_group;
    private ListView                        mListview;
    private TextView                        spinner;
    private int                             lotteryId = 1;
    private String                          play_id;
    private String                          URL;
    private String                          plan_id;
    private String                          play_cls = "1";
    private String                          num_count = "", issue_count = "";
    private String                          planname;
    private MainListAdapter                 adapter;
    private View                            popupView,lottery_window;
    private String                          num, issue;
    private Button                          cancel, confirm;
    private List<String>                    lis;
    private int                             panduan;
    private int                             pos;
    private WheelView                       screen_wheelview;
    private ArrayWheelAdapter<String>       mWheelAdapter;
    private List<MainListBean>              mainListBeen;
    private String[]                        DingWei         = {"3码4期", "3码5期", "4码3期", "4码4期", "5码2期", "5码3期", "6码2期", "7码1期", "8码1期", "9码1期"};
    private String[]                        ZhiXuan         = {"5码5期", "6码4期", "6码5期", "7码3期", "7码4期", "7码5期", "8码2期", "8码3期", "8码4期", "8码5期","9码2期"};
    private String[]                        ZuXuan          = {"7码4期", "7码5期", "8码2期", "8码3期", "8码4期", "9码2期"};
    private String[]                        DaXiao          = {"1码2期", "1码3期", "1码4期"};
    private String[]                        DanShuang       = {"1码2期", "1码3期", "1码4期"};
    private String[]                        HeZhi           = {"6码3期", "6码4期", "7码3期", "7码4期", "7码5期"};
    private String[]                        lottery_title   = {"  重庆时时彩", "  天津时时彩", "  新疆时时彩"};
    private List<LotteryTitle>              mlistLotteryTitle;
    private android.widget.ListView         lottery_listview;
    private PopupWindow                     titleWindow;
    private int index = 0;
    //网络无法连接
    private RelativeLayout                  nointernetLayout;
    private ImageView                       no_internet_icon;
    private Button                          reload_button;
    private TextView                        no_internet_text;
    //监测网络状态
    private ConnectivityManager             manager;
    //自动连接网络
    private WifiAdmin                       wifiAdmin;

    public void setURL(int lottery_id, String play_cls, String mac) {
        this.URL = SendMessage.getInstance().getBestURL() + "/plan/find_plan_list?lottery_id=" + lottery_id
                + "&play_cls=" + play_cls + "&mac=" + mac;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pragram_main);
        initView();
    }

    public void initView() {
        panduan               =   1;
        mark_btn              =   (Button) findViewById(R.id.mark_btn);
        collect_btn           =   (Button) findViewById(R.id.collect_btn_y);
        scrollView            =   (HorizontalScrollView) findViewById(R.id.main_scrollview);
        radio_group           =   (RadioGroup) findViewById(R.id.radioGroup_main);
        mListview             =   (ListView) findViewById(R.id.program_main_listview);
        relativeTab           =   (RelativeLayout) findViewById(R.id.relativeTab);
        nointernetLayout      =   (RelativeLayout) findViewById(R.id.reload_internet);
        no_internet_icon      =   (ImageView) findViewById(R.id.no_internet_icon);
        reload_button         =   (Button) findViewById(R.id.reload_button);
        no_internet_text      =   (TextView) findViewById(R.id.no_internet_text);
        spinner               =   (TextView)findViewById(R.id.spinner);
        Display d             =   FragmentProgram.this.getWindowManager().getDefaultDisplay();
        DisplayMetrics dm     =   new DisplayMetrics();
        d.getMetrics(dm);
        final int screenHalf  =   d.getWidth() / 2;
        setURL(getLotteryId(), getPlay_cls(), SendMessage.getInstance().getMac());
        setLotteryId(1);
        SendMessage.getInstance().setLotteryId(1);
        SendMessage.getInstance().setLotteryName("重庆时时彩");
        requestData();

        mListview.getListView().setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    int arg2, long arg3) {
                Intent intent = new Intent(FragmentProgram.this, ProgramMainNext.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position",pos);
                intent.putExtras(bundle);
                TextView name = (TextView) arg1.findViewById(R.id.program_main_list_item_name);
                TextView id = (TextView) arg1.findViewById(R.id.program_main_list_item_id);
                SendMessage.getInstance().setNextPageName(name.getText().toString());
                SendMessage.getInstance().setProgram_Plan_id(id.getText().toString());
                startActivity(intent);
            }
        });

        mListview.setLoadDataListener(new PullToRefreshLayout.LoadDataListener() {
            @Override
            //下拉刷新调用
            public void onRefresh() {
                requestData();
            }

            @Override
            //下拉加载更多调用
            public void onLoadMore() {
            }
        });

        radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                pos=radio_group.indexOfChild(radio_group.findViewById(checkedId));
                int scrollX = scrollView.getScrollX();
                RadioButton rb = (RadioButton) findViewById(checkedId);
                int left = rb.getRight();
                int leftScreen = left - scrollX;
                scrollView.smoothScrollBy((leftScreen - screenHalf), 0);
                switch (radio_group.indexOfChild(radio_group.findViewById(checkedId))) {
                    case 0:
                        panduan = 1;
                        setPlay_cls("1");
                        SendMessage.getInstance().setPlay_cls("1");
                        break;
                    case 1:
                        panduan = 1;
                        setPlay_cls("2");
                        SendMessage.getInstance().setPlay_cls("2");
                        break;
                    case 2:
                        panduan = 1;
                        setPlay_cls("3");
                        SendMessage.getInstance().setPlay_cls("3");
                        break;
                    case 3:
                        panduan = 1;
                        setPlay_cls("4");
                        SendMessage.getInstance().setPlay_cls("4");
                        break;
                    case 4:
                        panduan = 1;
                        setPlay_cls("5");
                        SendMessage.getInstance().setPlay_cls("5");
                        break;
                    case 5:
                        panduan = 1;
                        setPlay_cls("6");
                        SendMessage.getInstance().setPlay_cls("6");
                        break;
                    default:
                        break;
                }
                setURL(getLotteryId(), getPlay_cls(), SendMessage.getInstance().getMac());
                requestData();
            }

        });

        mark_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showSelectPic(v);
            }
        });

        collect_btn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                startActivity(new Intent(FragmentProgram.this, CollectActivity.class));
            }
        });
        reload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_internet_text.setText("正在重新加载网络,请稍等!");
                wifiAdmin = new WifiAdmin(FragmentProgram.this);
                wifiAdmin.openWifi();
            }
        });

        spinner.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showLotteryTitle(view);
                titleWindow.showAsDropDown(view);
            }
        });

    }

    public void requestData(){
        mainListBeen  =  new ArrayList<>();
        HttpTask httpTask =new HttpTask();
        httpTask.execute(getURL());
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++)
                    {
                        jsonObject = data.getJSONObject(i);
                        MainListBean newsBean = new MainListBean();
                        if (panduan == 1)
                        {
                            newsBean.setPlan_name(jsonObject.getString("plan_name"));
                            newsBean.setPlan_id(jsonObject.getString("plan_id"));
                            newsBean.setPlay_id(jsonObject.getString("play_id"));
                            newsBean.setIssue_count(jsonObject.getString("issue_count"));
                            newsBean.setNum_count(jsonObject.getString("num_count"));
                            newsBean.setPlay_cls(jsonObject.getString("play_cls"));
                            newsBean.setIsCollect(jsonObject.getString("iscollect"));
                            newsBean.setLottery_id(jsonObject.getString("lottery_id"));
                            newsBean.setLog_id(jsonObject.getString("log_id"));
                            setPlanName(newsBean.getPlan_name());
                            setPlan_id(newsBean.getPlan_id());
                            setPlayId(newsBean.getPlay_id());
                            setIssue_count(newsBean.getIssue_count());
                            setNum_count(newsBean.getNum_count());
                            mainListBeen.add(newsBean);
                            setListView(mainListBeen);
                            SendMessage.getInstance().setPlan_id(jsonObject.getString("plan_id"));
                        }
                        else if (panduan == 0)
                        {
                            if (jsonObject.getString("issue_count").equals(issue) && jsonObject.getString("num_count").equals(num))
                            {
                                newsBean.setPlan_name(jsonObject.getString("plan_name"));
                                newsBean.setPlan_id(jsonObject.getString("plan_id"));
                                newsBean.setPlay_id(jsonObject.getString("play_id"));
                                newsBean.setIssue_count(jsonObject.getString("issue_count"));
                                newsBean.setNum_count(jsonObject.getString("num_count"));
                                newsBean.setPlay_cls(jsonObject.getString("play_cls"));
                                newsBean.setIsCollect(jsonObject.getString("iscollect"));
                                newsBean.setLottery_id(jsonObject.getString("lottery_id"));
                                newsBean.setLog_id(jsonObject.getString("log_id"));
                                mainListBeen.add(newsBean);
                                setListView(mainListBeen);
                                setPlanName(newsBean.getPlan_name());
                                setPlan_id(newsBean.getPlan_id());
                                setPlayId(newsBean.getPlay_id());
                                setIssue_count(newsBean.getIssue_count());
                                setNum_count(newsBean.getNum_count());
                                SendMessage.getInstance().setPlan_id(jsonObject.getString("plan_id"));
                            }
                        }
                        mListview.onLoadComplete(true, false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            public void taskFailed() {
                mListview.onLoadComplete(true, true);
            }
        });

    }

    public void setListView(List<MainListBean> mainListBeen){
        adapter = new MainListAdapter(FragmentProgram.this, mainListBeen);
        mListview.getListView().setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public void showLotteryTitle(View view){
        lottery_window = FragmentProgram.this.getLayoutInflater().inflate(R.layout.lottery_listview, null);
        titleWindow = new PopupWindow(lottery_window, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        titleWindow.setTouchable(true);
        titleWindow.setOutsideTouchable(true);
        titleWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
        if (titleWindow.isShowing()) {
            titleWindow.dismiss();
        } else {
            titleWindow.setFocusable(true);
            titleWindow.getContentView().setOnTouchListener( new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    titleWindow.setFocusable(false);
                    titleWindow.dismiss();
                    return false;
                }
            });
        }
        lottery_listview         =   (android.widget.ListView) lottery_window.findViewById(R.id.lottery_listview);
        mlistLotteryTitle        =   new ArrayList<>();
        setLotteryTitleData();
        final LotteryTitleAdapter ltAdapter = new LotteryTitleAdapter(FragmentProgram.this,mlistLotteryTitle);
        lottery_listview.setAdapter(ltAdapter);
        ltAdapter.notifyDataSetChanged();
        lottery_listview.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                spinner.setText(mlistLotteryTitle.get(position).getLottery_title());
                if(!mlistLotteryTitle.get(position).isFocus()){
                    resetFocues();
                    mlistLotteryTitle.get(position).setFocus(true);
                }else {
                    resetFocues();
                }
                ltAdapter.notifyDataSetChanged();
                titleWindow.dismiss();
                EnterPlan(position);
            }
        });
    }

    public void EnterPlan(int position){
        if (position == 0) {
            lotteryId = 1;
            setURL(1, getPlay_cls(), SendMessage.getInstance().getMac());
            setLotteryId(1);
            SendMessage.getInstance().setLotteryId(1);
            SendMessage.getInstance().setLotteryName("重庆时时彩");
            requestData();
        }
        else if (position == 1) {
            lotteryId = 3;
            setURL(3, getPlay_cls(), SendMessage.getInstance().getMac());
            setLotteryId(3);
            SendMessage.getInstance().setLotteryId(3);
            SendMessage.getInstance().setLotteryName("天津时时彩");
            requestData();
        }
        else if (position == 2) {
            lotteryId = 7;
            setURL(7, getPlay_cls(), SendMessage.getInstance().getMac());
            setLotteryId(7);
            SendMessage.getInstance().setLotteryId(7);
            SendMessage.getInstance().setLotteryName("新疆时时彩");
            requestData();
        }
    }

    public void resetFocues(){
        for(int i=0;i<mlistLotteryTitle.size();i++){
            mlistLotteryTitle.get(i).setFocus(false);
        }
    }

    public void setLotteryTitleData(){
        LotteryTitle lotteryTitle;
        for(int i = 0;i < lottery_title.length ; i++){
            lotteryTitle = new LotteryTitle(lottery_title[i],false);
            mlistLotteryTitle.add(lotteryTitle);
        }
    }

    public void showSelectPic(View view) {
        panduan = 0;
        popupView = FragmentProgram.this.getLayoutInflater().inflate(R.layout.alert_dialog, null);
        final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setTouchable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setAnimationStyle(R.style.popup_window_anim);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        if (popupWindow.isShowing()) {
            popupWindow.dismiss();
        } else {
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            popupWindow.setFocusable(true);
            popupWindow.getContentView().setOnTouchListener( new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    popupWindow.setFocusable(false);
                    popupWindow.dismiss();
                    return false;
                }
            });
        }
        cancel = (Button) popupView.findViewById(R.id.mark_cancel);
        confirm = (Button) popupView.findViewById(R.id.mark_confirm);
        screen_wheelview = (WheelView) popupView.findViewById(R.id.screen_wheelView);

        cancel.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        confirm.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                num     =   lis.get(screen_wheelview.getCurrentItem()).toString().substring(0, 1);
                issue   =   lis.get(screen_wheelview.getCurrentItem()).toString().substring(2, 3);
                setURL(getLotteryId(), getPlay_cls(), SendMessage.getInstance().getMac());
                requestData();
                popupWindow.dismiss();
            }
        });

        switch (getPlay_cls()) {
            case "1":
                lis = Arrays.asList(DingWei);
                mWheelAdapter = new ArrayWheelAdapter<>(FragmentProgram.this, DingWei);
                break;
            case "2":
                lis = Arrays.asList(ZhiXuan);
                mWheelAdapter = new ArrayWheelAdapter<>(FragmentProgram.this, ZhiXuan);
                break;
            case "3":
                lis = Arrays.asList(ZuXuan);
                mWheelAdapter = new ArrayWheelAdapter<>(FragmentProgram.this, ZuXuan);
                break;
            case "4":
                lis = Arrays.asList(DaXiao);
                mWheelAdapter = new ArrayWheelAdapter<>(FragmentProgram.this, DaXiao);
                break;
            case "5":
                lis = Arrays.asList(DanShuang);
                mWheelAdapter = new ArrayWheelAdapter<>(FragmentProgram.this, DanShuang);
                break;
            case "6":
                lis = Arrays.asList(HeZhi);
                mWheelAdapter = new ArrayWheelAdapter<>(FragmentProgram.this, HeZhi);
                break;
            default:
                break;
        }

        screen_wheelview.setViewAdapter(mWheelAdapter);
        mWheelAdapter.setTextColor(this.getResources().getColor(R.color.redCaiZhong));
        mWheelAdapter.setTextSize(24);
        screen_wheelview.setCyclic(false);//是否可循环滑动
        screen_wheelview.setCurrentItem(lis.indexOf(0));
        screen_wheelview.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                screen_wheelview.setCurrentItem(newValue);
            }
        });

    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    private boolean checkNetWorkState() {
        boolean flag = false;
        //得到网络连接信息
        manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        //进行网络判断是否连接
        if (manager.getActiveNetworkInfo() != null) {
            flag = manager.getActiveNetworkInfo().isAvailable();
        }
        if (!flag) {
            setNetWork();
        } else {
            isNetworkAvailable();
        }
        return flag;
    }

    /**
     * 网络未连接时,调用设置方法
     */

    private void setNetWork() {
        relativeTab.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    private void isNetworkAvailable() {
        relativeTab.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
    }

    /**
     * 滑动ViewPager时调整ScroollView的位置以便显示按钮
     */
    @Override
    public void onClick(View v) {
    }

    public void setLotteryId(int lotteryId) {
        this.lotteryId = lotteryId;
    }

    public int getLotteryId() {
        return this.lotteryId;
    }

    public String getURL() {
        return this.URL;
    }

    public void setPlayId(String play_id) {
        this.play_id = play_id;
    }

    public String getPlayId() {
        return this.play_id;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlay_cls() {
        return play_cls;
    }

    public void setPlay_cls(String play_cls) {
        this.play_cls = play_cls;
    }

    public void setPlanName(String planname) {
        this.planname = planname;
    }

    public String getPlanName() {
        return this.planname;
    }

    public void setNum_count(String num_count) {
        this.num_count = num_count;
    }

    public String getNum_count() {
        return this.num_count;
    }

    public void setIssue_count(String issue_count) {
        this.issue_count = issue_count;
    }

    public String getIssue_count() {
        return this.issue_count;
    }

    @Override
    protected void onResume() {
        checkNetWorkState();
        super.onResume();
    }


}
