package com.example.caikeplan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import com.example.NextPage.LotteryNextActitivty;
import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.OptionsPickerView;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.WifiAdmin;
import com.example.caikeplan.logic.adapter.HotAdapter;
import com.example.caikeplan.logic.adapter.LookAdapter;
import com.example.caikeplan.logic.MyGridView;
import com.example.caikeplan.logic.adapter.LotteryTitleAdapter;
import com.example.caikeplan.logic.adapter.PlanAdapter;
import com.example.caikeplan.logic.TipView;
import com.example.caikeplan.logic.adapter.TitleGrildAdapter;
import com.example.caikeplan.logic.message.LotteryTitle;
import com.example.caikeplan.logic.message.MainListBean;
import com.example.caikeplan.logic.message.PlanBaseMessage;
import com.example.caikeplan.logic.message.PlanTypeMessage;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.caikeplan.logic.refresh.SHSwipeRefreshLayout;
import com.example.getJson.HttpTask;
import com.youth.xframe.base.XActivity;
import com.youth.xframe.cache.XCache;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by dell on 2017/5/17.
 */

public class ProgramActivity extends XActivity implements View.OnClickListener {

    private SHSwipeRefreshLayout swipeRefreshLayout;
    private LinearLayout    title_layout;
    private RelativeLayout  program_toolbar;
    private RelativeLayout  toolbar_container;
    private RelativeLayout  pk10layout;
    private RelativeLayout  k3layout;
    private RelativeLayout  lotterylayout;
    private RelativeLayout  lookthrough_layout;
    private ImageView       back;
    private TextView        back_text;
    private LinearLayout    program_back;
    private TextView        play_select;
    private ImageView       title_arrow;
    private TextView        toolbar_set;
    private TextView        toolbar_save;
    private ImageView       btn_copy_plan;
    private ImageView       btn_video,btn_video_pk10,btn_video_k3;
    private ScrollView      scroll;
    private ImageView       history_ssc,history_pk10,history_k3;
    private ImageView       lottery_open;
    private ImageView       lottery_open_pk10;
    private ImageView       lottery_open_k3;
    private TextView        textplan;
    private TextView        homeTitle;
    private TextView        homeTitle_pk10;
    private TextView        homeTitle_k3;
    private TextView        home_date_end;
    private TextView        home_date_end_pk10;
    private TextView        home_date_end_k3;
    private TextView        balls_1, balls_2, balls_3, balls_4, balls_5;
    private ImageView       type2_ball1,type2_ball2,type2_ball3,type2_ball4,type2_ball5,type2_ball6,type2_ball7,type2_ball8,type2_ball9,type2_ball10;
    private ImageView       type3_ball1,type3_ball2,type3_ball3;
    private TextView        home_date;
    private TextView        home_date_pk10;
    private TextView        home_date_k3;
    private TextView        text_time;
    private TextView        text_time_pk10;
    private TextView        text_time_k3;
    private ProgressBar     openTimeProgress;
    private ProgressBar     openTimeProgress_pk10;
    private ProgressBar     openTimeProgress_k3;
    private MyGridView      planGridView;
    private MyGridView      lookGridView;
  //  private MyGridView      hotGridView;
    private ArrayList<PlanTypeMessage>              options1Items       = new ArrayList<>();        //用于保存第一层选择器名称
    private ArrayList<ArrayList<String>>            options2Items       = new ArrayList<>();        //用于保存第二层选择器名称
    private ArrayList<ArrayList<ArrayList<String>>> options3Items       = new ArrayList<>();        //用于保存第三层选择器名称
    private List<List<List<MainListBean>>>          planAllSelectList   = new ArrayList<>();        //保存总的三计划玩法层数据按play_cls区分
    private List<List<MainListBean>>                planSecondSelectList= new ArrayList<>();        //保存第二层计划玩法数据按play_id区分
    private List<MainListBean>                      mainListBeen        = new ArrayList<>();        //用于保存请求所有计划玩法数据
    private List<PlanBaseMessage>   planList                            = new ArrayList<>();        //用于保存请求筛选后的数据
    private List<PlanBaseMessage>   lookList                            = new ArrayList<>();        //用于保存浏览后的数据
    private List<PlanBaseMessage>   hotList                             = new ArrayList<>();        //用于保存请求热门的数据
    private List<PlanBaseMessage>   recommendList                       = new ArrayList<>();        //用于保存请求推荐的数据
    private String                  iss,tempIss,lastTime,templastTime;
    private boolean                 isNextOpen   = true;
    private boolean                 isFistTime   = true;
    private boolean                 isLastTime   = true;
    private boolean                 isChanger    = false;
    private PlanAdapter             planAdapter;
    private LookAdapter             lookAdapter;
    private HotAdapter              hotAdapter;
    private RelativeLayout          switch_plan;
    private TextView                toolbar_title;
    private TipView                 tipView1, tipView2, tipView3, tipView4;
    private OptionsPickerView       pickerView;
    private List<String>            tiplist1 = new ArrayList<>();
    private List<String>            tiplist2 = new ArrayList<>();
    private List<String>            tiplist3 = new ArrayList<>();
    private List<String>            tiplist4 = new ArrayList<>();
    private String                  URL;                        //请求链接
    private String                  lotteryId = "1";            //彩种id
    private int                     play_cls = 1;               //彩种类别
    private String                  play_id;                    //玩法id
    private String                  plan_id;                    //计划id
    private String                  lottery_name    = "重庆时时彩";
    private String[]                lottery_title   = {"重庆时时彩", "天津时时彩", "新疆时时彩","上海11选5","广东11选5","山东11选5","北京PK10","",""};
    private String[]                lottery_ids     = {"1","3","7","22","9","10","27","",""};
    private String[]                lottery_type    = {"1","1","1","3","3","3","10","",""};
    private int[]                   lottery_resid   = {R.drawable.lottery_cq_ssc,R.drawable.lottery_tj_ssc,R.drawable.lottery_xj_ssc,R.drawable.lottery_sh_11x5,R.drawable.lottery_gd_11x5,R.drawable.lottery_sd_11x5,R.drawable.lottery_bj_pk10,0,0};
    private String[]                types           = {"定位", "直选", "组选", "大小", "单双", "和值", "单式"};
    private int[]                   pknums={R.drawable.type2_ball_1,R.drawable.type2_ball_2,R.drawable.type2_ball_3,R.drawable.type2_ball_4,
                                    R.drawable.type2_ball_5,R.drawable.type2_ball_6, R.drawable.type2_ball_7,R.drawable.type2_ball_8,R.drawable.type2_ball_9,R.drawable.type2_ball_10};
    private int[]                   k3nums={R.drawable.type3_ball_1,R.drawable.type3_ball_2,R.drawable.type3_ball_3,R.drawable.type3_ball_4,R.drawable.type3_ball_5,R.drawable.type3_ball_6};
    private List<LotteryTitle>      mlistLotteryTitle = new ArrayList<>();
    private PopupWindow             titleWindow,videoWindow;
    private View        lottery_window,video_window;
    private WebView     video_webview;
    private ImageView   video_close;
    private String      server_time;
    private String[]    numList;                //保存开奖号码
    private String      nums;                   //球码号
    private String      th, left;               //th当前期数,left剩下期数
    private String      expect_time;            //上期开奖时间
    private String      isue_last;              //已开奖期数
    private String      isue_now;               //即将开奖期数
    private String      expect_time_now;        //下期开奖时间
    private int         allissues;              //总期数
    private int         ps, db, sg;             //个位,十位,百位的开奖号码
    private String      plan_name;              //计划名称
    private String      big = "大", little = "小", doub = "双", sige = "单";
    public  SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
    private long        min, secd, maxtime, lastmin, lastsecd, processtime;
    private String      tempPlay_cls    = "1";
    private String      tempPlay_id     = "10001";
    private String      tempPlan_id     = "1-10001-3-4";
    private int         index=0;
    private int         lookindex=0;
    private PlanTypeMessage     planTypeMessage = new PlanTypeMessage();
    private PlanTypeMessage.BitBean bitBean = new PlanTypeMessage.BitBean();
    private XCache              xCache;
    private int                 cacheindex=0;
    private boolean             isComing        = false;
    //网络无法连接
    private RelativeLayout      nointernetLayout;
    private RelativeLayout      nodataLayout;
    private Button              reload_button;
    private TextView            no_internet_text;
    //监测网络状
    private ConnectivityManager manager;
    //自动连接网
    private WifiAdmin           wifiAdmin;
    private List<LotteryTitle>	lottery_titleList = new ArrayList<>();
    private GridView            titleGridView;
    private TitleGrildAdapter   titleGrildAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
         //   getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initViews();
        //开奖倒计时
        timerHandler.post(timeRunnable);
        //每60秒钟请求一次数据
        datasHandler.postDelayed(dataRunnable, 60000);
    }

    //请求链接
    public void setURL(String lottery_id, String play_cls, String mac) {
        URL = SendMessage.getInstance().getBestURL() + "/plan/find_plan_list?lottery_id=" + lottery_id
                + "&play_cls=" + play_cls + "&mac=" + mac;
    }

    @Override
    public int getLayoutId() {
        return R.layout.program_activity;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    @Override
    public void initView() {

    }

    //初始化控件
    public void initViews() {
        xCache = XCache.get(this);
        if(xCache.getAsString("cacheindex")!=null){
            cacheindex=Integer.parseInt(xCache.getAsString("cacheindex"));
        }else{
            cacheindex=0;
        }
        lookthrough_layout  =   (RelativeLayout)findViewById(R.id.lookthrough_layout);
        title_layout        =   (LinearLayout)findViewById(R.id.title_layout);
        scroll              =   (ScrollView)findViewById(R.id.scroll);
        program_toolbar     =   (RelativeLayout)findViewById(R.id.program_toolbar);
        pk10layout          =   (RelativeLayout)findViewById(R.id.pk10layout);
        k3layout            =   (RelativeLayout)findViewById(R.id.k3layout);
        lotterylayout       =   (RelativeLayout)findViewById(R.id.lotterylayout);
        program_back        =   (LinearLayout)findViewById(R.id.program_back);
        back                =   (ImageView)findViewById(R.id.back);
        back_text           =   (TextView)findViewById(R.id.back_text);
        play_select         =   (TextView)findViewById(R.id.play_select);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        title_arrow         =   (ImageView)findViewById(R.id.title_arrow);
        toolbar_set         =   (TextView)findViewById(R.id.toolbar_set);
        toolbar_save        =   (TextView)findViewById(R.id.toolbar_save);
        btn_copy_plan       =   (ImageView)findViewById(R.id.btn_copy_plan);
        lottery_open        =   (ImageView)findViewById(R.id.lottery_open);
        lottery_open_pk10   =   (ImageView)findViewById(R.id.lottery_open_pk10);
        lottery_open_k3     =   (ImageView)findViewById(R.id.lottery_open_k3);
        btn_video           =   (ImageView)findViewById(R.id.btn_video);
        btn_video_pk10      =   (ImageView)findViewById(R.id.btn_video_pk10);
        btn_video_k3        =   (ImageView)findViewById(R.id.btn_video_k3);
        textplan            =   (TextView)findViewById(R.id.textplan);
        homeTitle           =   (TextView)findViewById(R.id.home_title);
        homeTitle_pk10      =   (TextView)findViewById(R.id.home_title_pk10);
        homeTitle_k3        =   (TextView)findViewById(R.id.home_title_k3);
        home_date_end       =   (TextView)findViewById(R.id.home_date_end);
        home_date_end_pk10  =   (TextView)findViewById(R.id.home_date_end_pk10);
        home_date_end_k3    =   (TextView)findViewById(R.id.home_date_end_k3);
        balls_1             =   (TextView)findViewById(R.id.balls_1);
        balls_2             =   (TextView)findViewById(R.id.balls_2);
        balls_3             =   (TextView)findViewById(R.id.balls_3);
        balls_4             =   (TextView)findViewById(R.id.balls_4);
        balls_5             =   (TextView)findViewById(R.id.balls_5);
        type2_ball1         =   (ImageView)findViewById(R.id.type2_ball1);
        type2_ball2         =   (ImageView)findViewById(R.id.type2_ball2);
        type2_ball3         =   (ImageView)findViewById(R.id.type2_ball3);
        type2_ball4         =   (ImageView)findViewById(R.id.type2_ball4);
        type2_ball5         =   (ImageView)findViewById(R.id.type2_ball5);
        type2_ball6         =   (ImageView)findViewById(R.id.type2_ball6);
        type2_ball7         =   (ImageView)findViewById(R.id.type2_ball7);
        type2_ball8         =   (ImageView)findViewById(R.id.type2_ball8);
        type2_ball9         =   (ImageView)findViewById(R.id.type2_ball9);
        type2_ball10        =   (ImageView)findViewById(R.id.type2_ball10);
        type3_ball1         =   (ImageView)findViewById(R.id.type3_ball1);
        type3_ball2         =   (ImageView)findViewById(R.id.type3_ball2);
        type3_ball3         =   (ImageView)findViewById(R.id.type3_ball3);
        home_date           =   (TextView)findViewById(R.id.home_date);
        home_date_pk10      =   (TextView)findViewById(R.id.home_date_pk10);
        home_date_k3        =   (TextView)findViewById(R.id.home_date_k3);
        text_time           =   (TextView)findViewById(R.id.text_time);
        text_time_pk10      =   (TextView)findViewById(R.id.text_time_pk10);
        text_time_k3        =   (TextView)findViewById(R.id.text_time_k3);
        history_ssc         =   (ImageView)findViewById(R.id.history_ssc);
        history_pk10        =   (ImageView)findViewById(R.id.history_pk10);
        history_k3          =   (ImageView)findViewById(R.id.history_k3);
        openTimeProgress    =   (ProgressBar)findViewById(R.id.openTimeProgress);
        openTimeProgress_pk10    =   (ProgressBar)findViewById(R.id.openTimeProgress_pk10);
        openTimeProgress_k3    =   (ProgressBar)findViewById(R.id.openTimeProgress_k3);
        planGridView        =   (MyGridView)findViewById(R.id.plan_gridview);
        lookGridView        =   (MyGridView)findViewById(R.id.look_gridview);
        //hotGridView         =   (MyGridView)     findViewById(R.id.hot_gridview);
        switch_plan         =   (RelativeLayout)findViewById(R.id.switch_plan);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        tipView1            =   (TipView)findViewById(R.id.tip1);
        tipView2            =   (TipView)findViewById(R.id.tip2);
        tipView3            =   (TipView)findViewById(R.id.tip3);
        tipView4            =   (TipView)findViewById(R.id.tip4);
        nointernetLayout    =   (RelativeLayout)findViewById(R.id.reload_internet);
        nodataLayout        =   (RelativeLayout)findViewById(R.id.no_data);
        reload_button       =   (Button)findViewById(R.id.reload_button);
        no_internet_text    =   (TextView)findViewById(R.id.no_internet_text);
        reload_button.setOnClickListener(this);
        title_layout.setOnClickListener(this);
        switch_plan.setOnClickListener(this);
        toolbar_title.setOnClickListener(this);
        title_arrow.setOnClickListener(this);
        btn_copy_plan.setOnClickListener(this);
        btn_video.setOnClickListener(this);
        btn_video_pk10.setOnClickListener(this);
        btn_video_k3.setOnClickListener(this);
        history_ssc.setOnClickListener(this);
        history_pk10.setOnClickListener(this);
        history_k3.setOnClickListener(this);
        title_arrow.setVisibility(View.VISIBLE);
        btn_copy_plan.setVisibility(View.VISIBLE);
        lotterylayout.setVisibility(View.VISIBLE);
        pk10layout.setVisibility(View.GONE);
        k3layout.setVisibility(View.GONE);
        mlistLotteryTitle = setLotteryTitleData();
        initSwipeRefreshLayout();
        requestData();
        //requestPlanList();
        setLookGridView();
        setPlanGridView();
        SendMessage.getInstance().setLotteryName(lottery_name);
    }

    //scrollview下拉刷新
    private void initSwipeRefreshLayout() {
        swipeRefreshLayout = (SHSwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        /**
         * 这里Sample中的headerview使用默认设置，即可通过setRefreshViewText（String）来更改headerview中TextView的文字；
         * 而footview
         */
        LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        final View view = inflater.inflate(R.layout.refresh_view, null,false);
        final TextView textView = (TextView) view.findViewById(R.id.title);
     //   swipeRefreshLayout.setFooterView(view);
        swipeRefreshLayout.setLoadmoreEnable(false);
        swipeRefreshLayout.setOnRefreshListener(new SHSwipeRefreshLayout.SHSOnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishRefresh();
                        ToastUtil.getShortToastByString(ProgramActivity.this,"刷新完成");
                    }
                }, 2000);
            }

            @Override
            public void onLoading() {
               /* swipeRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        swipeRefreshLayout.finishLoadmore();
                        ToastUtil.getShortToastByString(ProgramActivity.this,"加载完成");
                    }
                }, 2000);*/
            }

            /**
             * 监听下拉刷新过程中的状态改变
             * @param percent 当前下拉距离的百分比（0-1）
             * @param state 分三种状态{NOT_OVER_TRIGGER_POINT：还未到触发下拉刷新的距离；OVER_TRIGGER_POINT：已经到触发下拉刷新的距离；START：正在下拉刷新}
             */
            @Override
            public void onRefreshPulStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setRefreshViewText("下拉刷新");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        swipeRefreshLayout.setRefreshViewText("松开刷新");
                        break;
                    case SHSwipeRefreshLayout.START:
                        requestData();
                        textplan.setText("玩法选择");
                        swipeRefreshLayout.setRefreshViewText("正在刷新");
                        break;
                }
            }

            @Override
            public void onLoadmorePullStateChange(float percent, int state) {
                switch (state) {
                    case SHSwipeRefreshLayout.NOT_OVER_TRIGGER_POINT:
                        textView.setText("上拉加载");
                        break;
                    case SHSwipeRefreshLayout.OVER_TRIGGER_POINT:
                        textView.setText("松开加载");
                        break;
                    case SHSwipeRefreshLayout.START:
                        textView.setText("正在加载...");
                        break;
                }
            }
        });
    }

    //请求数据
    public void requestData() {
        requestTime();
        setURL(getLotteryId(), getPlay_cls() + "", SendMessage.getInstance().getMac());
        requestOpenCurrentData();
        requestRecommend();
        requestPlanContent();
    }

    public void requestRefreshData(){
        requestTime();
        requestOpenCurrentData();
        requestRecommend();
    }


    //添加浏览记录
    public void addLookItem(int position,List<PlanBaseMessage> addlist){
        if(lookList.size() == 9){
            if(!repeat(addlist.get(position))){
                lookList.remove(lookindex);
                xCache.remove(cacheindex+"");
                lookList.add(addlist.get(position));
                xCache.put(cacheindex+"",addlist.get(position));
                cacheindex=(cacheindex+1)%9;
                xCache.put("cacheindex",cacheindex+"");
            }
        }else{
            if(!repeat(addlist.get(position))){
                lookList.add(addlist.get(position));
                xCache.put(cacheindex+"",addlist.get(position));
                cacheindex=(cacheindex+1)%9;
            }
        }
        Message msg = new Message();
        msg.what = 3;
        recomdHandler.sendMessage(msg);
    }

    //设置热门列表
   /* public void setHotGridView() {
        hotAdapter = new HotAdapter(this, hotList);
        hotGridView.setAdapter(hotAdapter);
        hotAdapter.notifyDataSetChanged();
        hotGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addLookItem(position,hotList);
                Intent intent = new Intent(ProgramActivity.this, PlanProgram.class);
                Bundle bundle = new Bundle();
                bundle.putString("plan_id", hotList.get(position).getPlan_id());
                bundle.putString("s_id", hotList.get(position).getS_id());
                bundle.putString("scheme_name", hotList.get(position).getScheme_name());
                bundle.putString("plan_name",hotList.get(position).getPlan_name());
                bundle.putString("cls_name",hotList.get(position).getCls_name());
                bundle.putString("lottery_name",hotList.get(position).getLottery_name());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }*/

    //设置推荐列表
    public void setRecommend(int position){
        addLookItem(position,recommendList);
        Intent intent = new Intent(ProgramActivity.this, PlanProgram.class);
        Bundle bundle = new Bundle();
        bundle.putString("plan_id", recommendList.get(position).getPlan_id());
        bundle.putString("s_id", recommendList.get(position).getS_id());
        bundle.putString("scheme_name", recommendList.get(position).getScheme_name());
        bundle.putString("plan_name",recommendList.get(position).getPlan_name());
        bundle.putString("cls_name",recommendList.get(position).getCls_name());
        bundle.putString("lottery_name",recommendList.get(position).getLottery_name());
        bundle.putString("is_jcp",recommendList.get(position).getIs_jcp());
        bundle.putString("type","1");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //设置浏览列表
    public void setLookGridView() {
        int index=cacheindex;
        int time=1;
        while (true){
            if(xCache.getAsObject(index+"")!=null && time<=9){
                lookList.add((PlanBaseMessage)xCache.getAsObject(index+""));
                cacheindex=(index+1)%9;
                index = (index+1)%9;
                time++;
            }else{
                break;
            }
        }
        lookAdapter = new LookAdapter(this, lookList);
        lookGridView.setAdapter(lookAdapter);
        lookAdapter.notifyDataSetChanged();
        lookGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProgramActivity.this, PlanProgram.class);
                Bundle bundle = new Bundle();
                bundle.putString("plan_id", lookList.get(position).getPlan_id());
                bundle.putString("s_id", lookList.get(position).getS_id());
                bundle.putString("scheme_name", lookList.get(position).getScheme_name());
                bundle.putString("plan_name",lookList.get(position).getPlan_name());
                bundle.putString("cls_name",lookList.get(position).getCls_name());
                bundle.putString("lottery_name",lookList.get(position).getLottery_name());
                bundle.putString("is_jcp",lookList.get(position).getIs_jcp());
                bundle.putString("type","1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        Message msg = new Message();
        msg.what = 3;
        recomdHandler.sendMessage(msg);
    }

    //设置计划列表
    public void setPlanGridView() {
        planAdapter = new PlanAdapter(this, planList);
        planGridView.setAdapter(planAdapter);
        planAdapter.notifyDataSetChanged();
        planGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                addLookItem(position,planList);
                Intent intent = new Intent(ProgramActivity.this, PlanProgram.class);
                Bundle bundle = new Bundle();
                bundle.putString("plan_id", planList.get(position).getPlan_id());
                bundle.putString("s_id",    planList.get(position).getS_id());
                bundle.putString("scheme_name", planList.get(position).getScheme_name());
                bundle.putString("plan_name",planList.get(position).getPlan_name());
                bundle.putString("cls_name",planList.get(position).getCls_name());
                bundle.putString("lottery_name",planList.get(position).getLottery_name());
                bundle.putString("is_jcp",planList.get(position).getIs_jcp());
                bundle.putString("type","1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //添加计划去重操作
    public boolean repeat(PlanBaseMessage planbaseMessage){
        boolean isreapeat=false;
        for(int i=0;i<lookList.size();i++){
            if(planbaseMessage.getPlan_id().equals(lookList.get(i).getPlan_id())&&planbaseMessage.getS_id().equals(lookList.get(i).getS_id())){
                isreapeat = true;
                break;
            }
        }
        return isreapeat;
    }

    //请求推荐信息
    public void requestRecommend() {
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API + Constants.RECOMMEND + lotteryId);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    ArrayList<PlanBaseMessage> list = new ArrayList<>();
                    ArrayList<String> tlist1 = new ArrayList<>();
                    ArrayList<String> tlist2 = new ArrayList<>();
                    ArrayList<String> tlist3 = new ArrayList<>();
                    ArrayList<String> tlist4 = new ArrayList<>();
                    PlanBaseMessage planBaseMessage;
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject = data.getJSONObject(i);
                        String scheme_name  = jsonObject.getString("scheme_name");
                        String s_id         = jsonObject.getString("s_id");
                        String plan_id      = jsonObject.getString("plan_id");
                        String plan_name    = jsonObject.getString("plan_name");
                        String cls_name     = jsonObject.getString("cls_name");
                        planBaseMessage     = new PlanBaseMessage(SendMessage.getInstance().getLotteryName(),lotteryId, s_id, scheme_name, plan_id, plan_name,cls_name,"","0");
                        list.add(planBaseMessage);
                        if(i==0 || i==1){
                            tlist1.add(planBaseMessage.getScheme_name()+planBaseMessage.getCls_name());
                        }else if(i==2 || i==3){
                            tlist2.add(planBaseMessage.getScheme_name()+planBaseMessage.getCls_name());
                        }else if(i==4 || i==5){
                            tlist3.add(planBaseMessage.getScheme_name()+planBaseMessage.getCls_name());
                        }else if(i==6 || i==7){
                            tlist4.add(planBaseMessage.getScheme_name()+planBaseMessage.getCls_name());
                        }
                    }
                    recommendList.clear();
                    tiplist1.clear();
                    tiplist2.clear();
                    tiplist3.clear();
                    tiplist4.clear();
                    recommendList.addAll(list);
                    tiplist1.addAll(tlist1);
                    tiplist2.addAll(tlist2);
                    tiplist3.addAll(tlist3);
                    tiplist4.addAll(tlist4);
                    Message msg = new Message();
                    msg.what = 1;
                    recomdHandler.sendMessage(msg);
                    checkDataState(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    checkDataState(false);
                }
            }

            @Override
            public void taskFailed() {
                checkDataState(false);
            }
        });
    }

    //请求开奖数据
    public void requestOpenCurrentData() {
        //获取已开奖结果数据
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API + Constants.LAST_RESULT + lotteryId);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONArray ls = data.getJSONArray(lotteryId);
                    JSONObject lottery = ls.getJSONObject(0);
                    isue_last = lottery.optString("issue");
                    nums = lottery.optString("nums");
                    expect_time = lottery.optString("expect_time");
                    left = lottery.optString("left");
                    th = lottery.optString("th");
                    allissues = Integer.parseInt(left) + Integer.parseInt(th);
                    numList = nums.split(",");
                    showData();
                    checkDataState(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    checkDataState(false);
                }
            }

            @Override
            public void taskFailed() {
                checkDataState(false);
            }
        });

        //获取当前开奖数据
        HttpTask httpTask1 = new HttpTask();
        httpTask1.execute(Constants.API + Constants.OPEN_LOTTERY + lotteryId);
        httpTask1.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject data = jsonObject.getJSONObject("data");
                    JSONObject id = data.getJSONObject(lotteryId);
                    isue_now = id.optString("issue");
                    expect_time_now = id.optString("expect_time");
                    setNowMessage(isue_now,expect_time_now);
                    checkDataState(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    checkDataState(false);
                }
            }

            @Override
            public void taskFailed() {
                checkDataState(false);
            }
        });
    }

    //设置倒计时和开奖中数据
    public void setNowMessage(String home_date, String expect_time){
        try{
            iss = home_date;
            if(isFistTime){
                tempIss        = home_date;
                isFistTime    = false;
            }else{
                if(!tempIss.equals(iss)&& !isChanger){
                    isChanger   = true;
                }else if(!tempIss.equals(iss)&& isChanger && isNextOpen){
                    tempIss     = iss;
                    isChanger   = false;
                }
            }
            min         = (format.parse(expect_time_now).getTime() - format.parse(server_time).getTime()) / 60000;
            secd        = (format.parse(expect_time_now).getTime() - format.parse(server_time).getTime()) / 1000 - min * 60;
            lastmin     = (format.parse(expect_time_now).getTime() - format.parse(expect_time).getTime()) / 60000;
            lastsecd    = (format.parse(expect_time_now).getTime() - format.parse(expect_time).getTime()) / 1000 - lastmin * 60;
            maxtime     = lastsecd + lastmin * 60;
            processtime = secd + min * 60;
            openTimeProgress.setMax(600);
            openTimeProgress_pk10.setMax(300);
            openTimeProgress_k3.setMax(600);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    //请求热门计划数据
    public void requestHotPlan() {
        hotList.clear();
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
                        String plan_name    = jsonObject.getString("plan_name");
                        String plan_id      = jsonObject.getString("plan_id");
                        String cls_name     = jsonObject.getString("cls_name");
                        planBaseMessage     = new PlanBaseMessage(SendMessage.getInstance().getLotteryName(),lotteryId, s_id, scheme_name, plan_id,plan_name,cls_name,"","0");
                        hotList.add(planBaseMessage);
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
        planSecondSelectList.clear();
        mainListBeen.clear();
        options2Items.clear();
        options3Items.clear();
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API+Constants.PLAN_CONTENT+lotteryId);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    ArrayList<PlanTypeMessage>              options1      = new ArrayList<>();
                    List<List<List<MainListBean>>>          planAllList   = new ArrayList<>();
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
                            planAllList.add(planSecondSelectList);
                            options1.add(planTypeMessage);
                        }
                    }
                    planAllSelectList.clear();
                    options1Items.clear();
                    planAllSelectList.addAll(planAllList);
                    options1Items.addAll(options1);
                    setSelectTitle();
                    Message msg = new Message();
                    msg.what = 4;
                    recomdHandler.sendMessage(msg);
                    checkDataState(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    checkDataState(false);
                }
            }

            @Override
            public void taskFailed() {
                checkDataState(false);
            }
        });
    }

    //请求计划玩法数据
    public void requestPlanList() {
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API + Constants.PLAN + lotteryId);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        jsonObject = data.getJSONObject(i);
                        MainListBean newsBean = new MainListBean();
                        plan_name = jsonObject.getString("plan_name");
                        newsBean.setPlan_name(plan_name);
                        String cls      = jsonObject.getString("play_cls");
                        String plan_id  = jsonObject.getString("plan_id");
                        String play_id  = jsonObject.getString("play_id");
                        newsBean.setPlan_id(jsonObject.getString("plan_id"));
                        newsBean.setPlay_id(jsonObject.getString("play_id"));
                        newsBean.setIssue_count(jsonObject.getString("issue_count"));
                        newsBean.setNum_count(jsonObject.getString("num_count"));
                        newsBean.setPlay_cls(cls);
                        newsBean.setIsCollect(jsonObject.getString("iscollect"));
                        newsBean.setLottery_id(jsonObject.getString("lottery_id"));
                        newsBean.setLog_id(jsonObject.getString("log_id"));
                        if (tempPlay_cls.equals(cls)) {
                            if (tempPlay_id.equals(play_id)) {
                                mainListBeen.add(newsBean);
                                bitBean.setName(plan_name.substring(2,4));
                                bitBean.getIssues_list().add(plan_name.substring(4,plan_name.length()));
                            } else {
                                planSecondSelectList.add(mainListBeen);
                                mainListBeen = new ArrayList<>();
                                mainListBeen.add(newsBean);
                                tempPlay_id = play_id;
                                planTypeMessage.getBitBeen().add(bitBean);
                                bitBean = new PlanTypeMessage.BitBean();
                                bitBean.newList();
                                bitBean.setName(plan_name.substring(2,4));
                                bitBean.getIssues_list().add(plan_name.substring(4,plan_name.length()));
                            }
                        } else {
                            planSecondSelectList.add(mainListBeen);
                            planAllSelectList.add(planSecondSelectList);
                            mainListBeen = new ArrayList<>();
                            planSecondSelectList = new ArrayList<>();
                            mainListBeen.add(newsBean);
                            tempPlay_cls = cls;
                            tempPlay_id = play_id;
                            planTypeMessage.getBitBeen().add(bitBean);
                            bitBean = new PlanTypeMessage.BitBean();
                            planTypeMessage.setName(types[index]);
                            options1Items.add(planTypeMessage);
                            planTypeMessage = new PlanTypeMessage();
                            index++;
                        }
                    }
                    planAllSelectList.add(planSecondSelectList);
                    planTypeMessage.setName(types[index]);
                    options1Items.add(planTypeMessage);
                    setSelectTitle();
                    Message msg = new Message();
                    msg.what = 4;
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
        planList.clear();
        HttpTask httpTask = new HttpTask();
        String plan_id ="";
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
                        String cls_name     = jsonObject.getString("cls_name");
                        String plan_name    = options2Items.get(item1).get(item2)+options3Items.get(item1).get(item2).get(item3);
                        planBaseMessage     = new PlanBaseMessage(SendMessage.getInstance().getLotteryName(),lotteryId, s_id, scheme_name, plan_id,plan_name,cls_name,"","0");
                        planList.add(planBaseMessage);
                    }
                    Message msg = new Message();
                    msg.what = 5;
                    recomdHandler.sendMessage(msg);
                    checkDataState(true);
                } catch (Exception e) {
                    e.printStackTrace();
                    checkDataState(false);
                }
            }

            @Override
            public void taskFailed() {
                checkDataState(false);
            }
        });

    }

    //更新UI线程信息
    public Handler recomdHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    //显示推荐彩种信息
                    setTipView();
                    break;
                case 2:
                    //显示热门彩种信息
                    //setHotGridView();
                    break;
                case 3:
                    if(lookList.size()!=0){
                        lookthrough_layout.setVisibility(View.VISIBLE);
                        lookAdapter.notifyDataSetChanged();
                    }else{
                        lookthrough_layout.setVisibility(View.GONE);
                    }
                    break;
                case 4:
                    //设置计划选择器
                    initOptionPicker();
                    requestSelectPlanData(0,0,0);
                    break;
                case 5:
                    //更新筛选列表
                    setPlanGridView();
                    break;
                case 6:
                    titleWindow.dismiss();
                    break;
            }
        }
    };

    //设置玩法选择
    public void setSelectTitle() {
        for(int i=0;i<options1Items.size();i++){
            ArrayList<String>               bits = new ArrayList<>();
            ArrayList<ArrayList<String>>    issues = new ArrayList<>();
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

    //设置推荐信息
    public void setTipView() {
        tipView1.setTipList(tiplist1);
        tipView2.setTipList(tiplist2);
        tipView3.setTipList(tiplist3);
        tipView4.setTipList(tiplist4);
        tipView1.setOnClickListener(this);
        tipView2.setOnClickListener(this);
        tipView3.setOnClickListener(this);
        tipView4.setOnClickListener(this);
    }

    //显示开奖信息
    public void showData() {
        lastTime = isue_last;
        if(isLastTime){
            templastTime = isue_last;
            isLastTime   = false;
        }else{
            if(templastTime.equals(lastTime)&& isChanger){
                isNextOpen    = false;
                lastTime      = tempIss;
            }else if(!templastTime.equals(lastTime)&& !isNextOpen) {
                isNextOpen    = true;
                templastTime  = lastTime;
            }
        }
        if(lotteryId.equals("27")){
            type2_ball1.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[0]) - 1]));
            type2_ball2.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[1]) - 1]));
            type2_ball3.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[2]) - 1]));
            type2_ball4.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[3]) - 1]));
            type2_ball5.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[4]) - 1]));
            type2_ball6.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[5]) - 1]));
            type2_ball7.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[6]) - 1]));
            type2_ball8.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[7]) - 1]));
            type2_ball9.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[8]) - 1]));
            type2_ball10.setBackground(ContextCompat.getDrawable(this, pknums[Integer.parseInt(numList[9]) - 1]));
            if(!isNextOpen){
                home_date_end_pk10.setText("第 " + lastTime + " 期");
                lottery_open_pk10.setVisibility(View.VISIBLE);
            }else{
                home_date_end_pk10.setText("第 " + lastTime + " 期已开");
                lottery_open_pk10.setVisibility(View.GONE);
            }
            homeTitle_pk10.setText(lottery_name);
            home_date_pk10.setText("全天共开" + allissues + "期，当前" + th + "期，剩余" + left + "期");
        }else if(lotteryId.equals("23")){
            type3_ball1.setBackground(ContextCompat.getDrawable(this, k3nums[Integer.parseInt(numList[0]) - 1]));
            type3_ball2.setBackground(ContextCompat.getDrawable(this, k3nums[Integer.parseInt(numList[1]) - 1]));
            type3_ball3.setBackground(ContextCompat.getDrawable(this, k3nums[Integer.parseInt(numList[2]) - 1]));
            if(!isNextOpen){
                home_date_end_k3.setText("第 " + lastTime + " 期");
                lottery_open_k3.setVisibility(View.VISIBLE);
            }else{
                home_date_end_k3.setText("第 " + lastTime + " 期已开");
                lottery_open_k3.setVisibility(View.GONE);
            }
            homeTitle_k3.setText(lottery_name);
            home_date_k3.setText("全天共开" + allissues + "期，当前" + th + "期，剩余" + left + "期");
        }else{
            balls_1.setText(numList[0]);
            balls_2.setText(numList[1]);
            balls_3.setText(numList[2]);
            balls_4.setText(numList[3]);
            balls_5.setText(numList[4]);
            if(!isNextOpen){
                home_date_end.setText("第 " + lastTime + " 期");
                lottery_open.setVisibility(View.VISIBLE);
            }else{
                home_date_end.setText("第 " + lastTime + " 期已开");
                lottery_open.setVisibility(View.GONE);
            }
            homeTitle.setText(lottery_name);
            home_date.setText("全天共开" + allissues + "期，当前" + th + "期，剩余" + left + "期");
        }
    }

    //重新初始化一些数据
    public void resetData(){
        iss             =   "";
        tempIss         =   "";
        lastTime        =   "";
        templastTime    =   "";
        isNextOpen      =   true;
        isFistTime      =   true;
        isLastTime      =   true;
        isChanger       =   false;
        planList.clear();
        planAdapter.notifyDataSetChanged();
        textplan.setText("玩法选择");
    }

    //设置选择器
    public void initOptionPicker() {
        pickerView = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, final int options2, int options3, View v) {
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

    //选择彩种计划头列表
    public void showLotteryTitle() {
        lottery_window = ProgramActivity.this.getLayoutInflater().inflate(R.layout.lottery_choice, null,false);
        titleWindow = new PopupWindow(lottery_window, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        titleWindow.setTouchable(true);
        titleWindow.setOutsideTouchable(true);
        lottery_window.setBackgroundColor(Color.parseColor("#80000000"));
        if (titleWindow.isShowing()) {
            titleWindow.dismiss();
        } else {
            titleWindow.setFocusable(true);
            titleWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    titleWindow.setFocusable(false);
                    titleWindow.dismiss();
                    return false;
                }
            });
        }
        setLotteryView();
        int xOffset = program_toolbar.getWidth() / 2 - lottery_window.getMeasuredWidth() / 2;
        titleWindow.showAsDropDown(program_toolbar, xOffset, 0);
    }

    public void setLotteryView(){
        titleGridView = (MyGridView)lottery_window.findViewById(R.id.lottery_title_gridview);
        titleGrildAdapter = new TitleGrildAdapter(this,mlistLotteryTitle);
        titleGridView.setAdapter(titleGrildAdapter);
        titleGrildAdapter.notifyDataSetChanged();
        titleGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position < lottery_title.length-2){
                    toolbar_title.setText(mlistLotteryTitle.get(position).getLottery_title());
                    EnterPlan(position);
                    Message msg = new Message();
                    msg.what = 6;
                    recomdHandler.sendMessageDelayed(msg,500);
                }
            }
        });
    }

    public void showVideo(){
        final String url;
        if(lotteryId.equals("27")){
            url = Constants.API+Constants.PK10_VIDEO+lotteryId;
        }else if(lotteryId.equals("8") && lotteryId.equals("9") && lotteryId.equals("10")){
            url = Constants.API+Constants.GD11X5_VIDEO+lotteryId;
        }else if(lotteryId.equals("23")){
            url = Constants.API+Constants.K3_VIDEO+lotteryId;
        }else{
            url = Constants.API+Constants.SSC_VIDEO+lotteryId;
        }
        video_window = ProgramActivity.this.getLayoutInflater().inflate(R.layout.video_layout, null,false);
        videoWindow = new PopupWindow(video_window, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        videoWindow.setTouchable(true);
        videoWindow.setOutsideTouchable(true);
        if (videoWindow.isShowing()) {
            videoWindow.dismiss();
        } else {
            videoWindow.setFocusable(true);
            videoWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    videoWindow.setFocusable(false);
                    videoWindow.dismiss();
                    return false;
                }
            });
        }
        video_window.setBackgroundColor(Color.parseColor("#80000000"));
        video_webview = (WebView)video_window.findViewById(R.id.video_webview);
        WebSettings setting = video_webview.getSettings();
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setUseWideViewPort(true);               //关键点
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setDisplayZoomControls(false);
        setting.setJavaScriptEnabled(true);             // 设置支持javascript脚本
        setting.setAllowFileAccess(true);               // 允许访问文件
        setting.setBuiltInZoomControls(true);           // 设置显示缩放按钮
        setting.setLoadWithOverviewMode(true);          // 缩放至屏幕的大小
        setting.setSupportZoom(true);                   // 支持缩放
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        setting.setLoadsImagesAutomatically(true);
        setting.setLoadWithOverviewMode(true);
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int mDensity = metrics.densityDpi;
        if (mDensity == 240) {
            setting.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        } else if (mDensity == 160) {
            setting.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        } else if(mDensity == 120) {
            setting.setDefaultZoom(WebSettings.ZoomDensity.CLOSE);
        }else if(mDensity == DisplayMetrics.DENSITY_XHIGH){
            setting.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else if (mDensity == DisplayMetrics.DENSITY_TV){
            setting.setDefaultZoom(WebSettings.ZoomDensity.FAR);
        }else{
            setting.setDefaultZoom(WebSettings.ZoomDensity.MEDIUM);
        }
        video_webview.addJavascriptInterface(this, "android");
        //Webview兼容cookie
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().setAcceptThirdPartyCookies(video_webview,true);
        video_webview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(url);
                return false;
            }
        });
        video_webview.loadUrl(url);
        int xOffset = program_toolbar.getWidth() / 2 - video_window.getMeasuredWidth() / 2;
        videoWindow.showAsDropDown(program_toolbar, xOffset, 0);
    }

    //设置标题信息
    public List<LotteryTitle> setLotteryTitleData() {
        LotteryTitle lotteryTitle;
        for (int i = 0; i < lottery_title.length; i++) {
            lotteryTitle = new LotteryTitle(lottery_ids[i],lottery_title[i],lottery_type[i],lottery_resid[i], false);
            mlistLotteryTitle.add(lotteryTitle);
        }
        return mlistLotteryTitle;
    }

    //获取当前时间
    public Date time() { //获取本地时间
        Date date = new Date();
        return date;
    }

    //请求服务器时间
    public void requestTime(){
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API+Constants.SERVER_TIME);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject data = jsonObject.getJSONObject("data");
                    server_time     = data.getString("server_time");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {

            }
        });
    }

    //选择彩种计划
    public void EnterPlan(int position) {
        setPlanData(position);
    }

    public void setPlanData(int position){
        if(position == 6){
            lotteryId = lottery_ids[position];
            setURL(lotteryId, getPlay_cls() + "", SendMessage.getInstance().getMac());
            SendMessage.getInstance().setLotteryId(Integer.parseInt(lottery_ids[position]));
            SendMessage.getInstance().setLotteryName(lottery_title[position]);
            lottery_name = lottery_title[position];
            lotterylayout.setVisibility(View.GONE);
            k3layout.setVisibility(View.GONE);
            pk10layout.setVisibility(View.VISIBLE);
            resetData();
            requestData();
        }/*else if(position == 7){
            lotteryId = lottery_ids[position];
            setURL(lotteryId, getPlay_cls() + "", SendMessage.getInstance().getMac());
            SendMessage.getInstance().setLotteryId(Integer.parseInt(lottery_ids[position]));
            SendMessage.getInstance().setLotteryName(lottery_title[position]);
            lottery_name = lottery_title[position];
            lotterylayout.setVisibility(View.GONE);
            k3layout.setVisibility(View.VISIBLE);
            pk10layout.setVisibility(View.GONE);
            resetData();
            requestData();
        }*/else if(position>=0 && position<6){
            lotteryId = lottery_ids[position];
            setURL(lotteryId, getPlay_cls() + "", SendMessage.getInstance().getMac());
            SendMessage.getInstance().setLotteryId(Integer.parseInt(lottery_ids[position]));
            SendMessage.getInstance().setLotteryName(lottery_title[position]);
            lottery_name = lottery_title[position];
            lotterylayout.setVisibility(View.VISIBLE);
            pk10layout.setVisibility(View.GONE);
            k3layout.setVisibility(View.GONE);
            resetData();
            requestData();
        }
    }

    /**
     * 开奖时间线程
     * 每秒向服务器校对一次 分针和秒针
     */
    public TimerHandler timerHandler = new TimerHandler(this);
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Message msg = new Message();
                timerHandler.sendMessage(msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private static class TimerHandler extends Handler {
        WeakReference<ProgramActivity> reference;

        public TimerHandler(ProgramActivity programActivity) {
            reference = new WeakReference<>(programActivity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgramActivity programActivity = reference.get();
            programActivity.reBackTime();
            programActivity.timerHandler.postDelayed(programActivity.timeRunnable, 1000);
        }
    }

    /**
     * 每15秒请求一次数据
     */
    public DatasHandler datasHandler = new DatasHandler(ProgramActivity.this);
    public Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Message msg = new Message();
                datasHandler.sendMessage(msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private static class DatasHandler extends Handler {
        WeakReference<ProgramActivity> reference;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProgramActivity programActivity = reference.get();
            if (programActivity != null) {
                programActivity.requestTime();
                programActivity.requestOpenCurrentData();
                programActivity.datasHandler.postDelayed(programActivity.dataRunnable, 60000);
            }
        }

        DatasHandler(ProgramActivity programActivity) {
            reference = new WeakReference<>(programActivity);
        }
    }

    //倒计时操作
    public void reBackTime() {
        if(lotteryId.equals("27")){
            openTimeProgress_pk10.setProgress((int) processtime--);
            //输出显示
            if (secd <= 60 && secd > 0 && min >= 10) {
                secd--;
                if (secd >= 10) {
                    text_time_pk10.setText(min + ":" + secd);
                } else {
                    text_time_pk10.setText(min + ":0" + secd);
                }
            } else if (secd <= 60 && secd > 0 && min < 10 && min >= 0) {
                secd--;
                if (secd >= 10) {
                    text_time_pk10.setText("0" + min + ":" + secd);
                } else {
                    text_time_pk10.setText("0" + min + ":0" + secd);
                }
            } else if (min > 0 && secd == 0) {
                min--;
                secd = 59;
                if (min >= 10) {
                    text_time_pk10.setText(min + ":" + secd);
                } else {
                    text_time_pk10.setText("0" + min + ":" + secd);
                }
            } else if (secd == 0 && min == 0) {
                text_time_pk10.setText("00:00");
                requestTime();
                requestOpenCurrentData();
            }
        }else if(lotteryId.equals("23")){
            openTimeProgress_k3.setProgress((int) processtime--);
            //输出显示
            if (secd <= 60 && secd > 0 && min >= 10) {
                secd--;
                if (secd >= 10) {
                    text_time_k3.setText(min + ":" + secd);
                } else {
                    text_time_k3.setText(min + ":0" + secd);
                }
            } else if (secd <= 60 && secd > 0 && min < 10 && min >= 0) {
                secd--;
                if (secd >= 10) {
                    text_time_k3.setText("0" + min + ":" + secd);
                } else {
                    text_time_k3.setText("0" + min + ":0" + secd);
                }
            } else if (min > 0 && secd == 0) {
                min--;
                secd = 59;
                if (min >= 10) {
                    text_time_k3.setText(min + ":" + secd);
                } else {
                    text_time_k3.setText("0" + min + ":" + secd);
                }
            } else if (secd == 0 && min == 0) {
                text_time_k3.setText("00:00");
                requestTime();
                requestOpenCurrentData();
            }
        } else{
            openTimeProgress.setProgress((int) processtime--);
            //输出显示
            if (secd <= 60 && secd > 0 && min >= 10) {
                secd--;
                if (secd >= 10) {
                    text_time.setText(min + ":" + secd);
                } else {
                    text_time.setText(min + ":0" + secd);
                }
            } else if (secd <= 60 && secd > 0 && min < 10 && min >= 0) {
                secd--;
                if (secd >= 10) {
                    text_time.setText("0" + min + ":" + secd);
                } else {
                    text_time.setText("0" + min + ":0" + secd);
                }
            } else if (min > 0 && secd == 0) {
                min--;
                secd = 59;
                if (min >= 10) {
                    text_time.setText(min + ":" + secd);
                } else {
                    text_time.setText("0" + min + ":" + secd);
                }
            } else if (secd == 0 && min == 0) {
                text_time.setText("00:00");
                requestTime();
                requestOpenCurrentData();
            }
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.switch_plan:
                if (pickerView != null) {
                    pickerView.show();
                }
                break;
            case R.id.toolbar_title:
                showLotteryTitle();
                break;
            case R.id.btn_copy_plan:
                Intent intent = new Intent(ProgramActivity.this, CopyPlan.class);
                Bundle bundle = new Bundle();
                bundle.putString("lotteryId",lotteryId);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.tip1:
                setRecommend((tipView1.getCurTipIndex()-1+tipView1.getTipList().size())%tipView1.getTipList().size());
                break;
            case R.id.tip2:
                setRecommend((tipView2.getCurTipIndex()-1+tipView2.getTipList().size())%tipView2.getTipList().size()+2);
                break;
            case R.id.tip3:
                setRecommend((tipView3.getCurTipIndex()-1+tipView3.getTipList().size())%tipView3.getTipList().size()+4);
                break;
            case R.id.tip4:
                setRecommend((tipView4.getCurTipIndex()-1+tipView4.getTipList().size())%tipView4.getTipList().size()+6);
                break;
            case R.id.history_pk10:
                Intent intent1 = new Intent(ProgramActivity.this, LotteryNextActitivty.class);
                Bundle bundle1 = new Bundle();
                bundle1.putString("lottery_id",lotteryId);
                bundle1.putString("lottery_name",SendMessage.getInstance().getLotteryName());
                intent1.putExtras(bundle1);
                startActivity(intent1);
                break;
            case R.id.history_ssc:
                Intent intent2 = new Intent(ProgramActivity.this, LotteryNextActitivty.class);
                Bundle bundle2 = new Bundle();
                bundle2.putString("lottery_id",lotteryId);
                bundle2.putString("lottery_name",SendMessage.getInstance().getLotteryName());
                intent2.putExtras(bundle2);
                startActivity(intent2);
                break;
            case R.id.history_k3:
                Intent intent3 = new Intent(ProgramActivity.this, LotteryNextActitivty.class);
                Bundle bundle3 = new Bundle();
                bundle3.putString("lottery_id",lotteryId);
                bundle3.putString("lottery_name",SendMessage.getInstance().getLotteryName());
                intent3.putExtras(bundle3);
                startActivity(intent3);
                break;
            case R.id.btn_video:
                showVideo();
                break;
            case R.id.btn_video_pk10:
                showVideo();
                break;
            case R.id.btn_video_k3:
                showVideo();
            case R.id.reload_button:
                no_internet_text.setText("正在重新加载网络,请稍等!");
                wifiAdmin = new WifiAdmin(ProgramActivity.this);
                wifiAdmin.openWifi();
                break;
        }
    }

    @Override
    protected void onDestroy() {
        timerHandler.removeCallbacks(timeRunnable);
        datasHandler.removeCallbacks(dataRunnable);
        super.onDestroy();
    }

 /*   @Override
    protected void onResume() {
        requestDataUpdate();
        if(!isComing){
            //开奖倒计时
            timerHandler.post(timeRunnable);
            //每60秒钟请求一次数据
            datasHandler.postDelayed(dataRunnable, 15000);
            isComing = true;
        }
        super.onResume();
    }*/

   /* @Override
    protected void onPause() {
        timerHandler.removeCallbacks(timeRunnable);
        datasHandler.removeCallbacks(dataRunnable);
        isComing = false;
        super.onPause();
    }*/

    @Override
    protected void onResume() {
        checkNetWorkState();
        super.onResume();
    }

    /**
     * 检测网络是否连接
     *
     * @return
     */
    private void checkNetWorkState() {
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
    }

    private void checkDataState(boolean flag){
        if (!flag) {
            setDatalayout();
        } else {
            isDataAvailable();
        }
    }

    /**
     * 网络未连接时,调用设置方法
     */
    private void setNetWork() {
        swipeRefreshLayout.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    private void isNetworkAvailable() {
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void setDatalayout(){
        swipeRefreshLayout.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
    }

    private void isDataAvailable(){
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.GONE);
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public int getPlay_cls() {
        return play_cls;
    }

    public void setPlay_cls(int play_cls) {
        this.play_cls = play_cls;
    }

}
