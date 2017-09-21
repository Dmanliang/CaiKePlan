package com.example.caikeplan.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.EntryContract;
import com.example.caikeplan.logic.EntryPresenter;
import com.example.caikeplan.logic.MyGridView;
import com.example.caikeplan.logic.WifiAdmin;
import com.example.caikeplan.logic.adapter.RankAdapter;
import com.example.caikeplan.logic.adapter.TitleGrildAdapter;
import com.example.caikeplan.logic.message.LotteryTitle;
import com.example.caikeplan.logic.message.PlanBaseMessage;
import com.example.caikeplan.logic.message.UserMessage;
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
 * Created by dell on 2017/8/14.
 */

public class RankActivity extends BaseActivity implements View.OnClickListener,EntryContract.View{

    private SwipeRefreshLayout      swipeRefreshLayout;
    private RelativeLayout          program_toolbar;
    private RelativeLayout          loading;
    private LinearLayout            title_layout;
    private ImageView               title_arrow;
    private ListView                ranklistview;
    private List<PlanBaseMessage>   rankList = new ArrayList<>();
    private RankAdapter             rankAdapter;
    private View                    header;
    private ImageView               rank_message;
    private TextView                num_one_plan,num_two_plan,num_three_plan,hot_count1,hot_count2,hot_count3;
    private TextView                toolbar_title;
    private View                    lottery_window;
    private PopupWindow             titleWindow;
    private String                  lottery_id = "1";
    private LinearLayout            plan_one_layout,plan_two_layout,plan_three_layout;
    private EntryPresenter          mPresenter;
    private Map<String, String>     messagemap = new HashMap<>();
    private List<LotteryTitle>      mlistLotteryTitle = new ArrayList<>();
    private String[]                lottery_title   = {"重庆时时彩", "天津时时彩", "新疆时时彩","上海11选5","广东11选5","山东11选5","北京PK10","",""};
    private String[]                lottery_ids     = {"1","3","7","22","9","10","27","",""};
    private String[]                lottery_type    = {"1","1","1","3","3","3","10","",""};
    private int[]                   lottery_resid   = {R.drawable.lottery_cq_ssc,R.drawable.lottery_tj_ssc,R.drawable.lottery_xj_ssc,R.drawable.lottery_sh_11x5,R.drawable.lottery_gd_11x5,R.drawable.lottery_sd_11x5,R.drawable.lottery_bj_pk10,0,0};
    //网络无法连接
    private RelativeLayout          nointernetLayout;
    private RelativeLayout          nodataLayout;
    private Button                  reload_button;
    private Button                  dataload_button;
    private TextView                no_internet_text;
    //监测网络状
    private ConnectivityManager     manager;
    //自动连接网
    private WifiAdmin               wifiAdmin;
    private View                    loadview;
    private boolean				    isCheckData  = false;
    private int                     index = 0;
    private List<LotteryTitle>	    lottery_titleList = new ArrayList<>();
    private GridView                titleGridView;
    private TitleGrildAdapter       titleGrildAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.rank_activity);
        initView();
    }

    public void requestMessage(){
        messagemap.put("user_id", UserMessage.getInstance().getUser_id());
        messagemap.put("token",UserMessage.getInstance().getToken());
        mPresenter.message(messagemap);
    }

    public void requestHotData(int position){
        index = position;
        enable(false);
        OKHttpManager httpManager = new OKHttpManager();
        Map<String,String> map = new HashMap<>();
        map.put("user_id",UserMessage.getInstance().getUser_id());
        map.put("lottery_id",lottery_ids[position]);
        httpManager.setToken(UserMessage.getInstance().getToken());
        httpManager.post(Constants.API + Constants.HOT_SCHEME, map, new OnNetRequestCallback() {
            @Override
            public void onFailed(String reason) {
                checkDataState(false);
            }

            @Override
            public void onSuccess(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String success  = jsonObject.getString("success");
                    if(success.equals("1")) {
                        JSONArray data = jsonObject.getJSONArray("data");
                        PlanBaseMessage planBaseMessage;
                        ArrayList<PlanBaseMessage> newArray = new ArrayList<>();
                        for (int i = 0; i < data.length(); i++) {
                            jsonObject = data.getJSONObject(i);
                            String scheme_name  = jsonObject.getString("scheme_name");
                            String s_id         = jsonObject.getString("s_id");
                            String plan_name    = jsonObject.getString("plan_name");
                            String plan_id      = jsonObject.getString("plan_id");
                            String cls_name     = jsonObject.getString("cls_name");
                            String count        = jsonObject.getString("count");
                            String lottery_name = jsonObject.getString("lottery_name");
                            String lottery_id   = jsonObject.getString("lottery_id");
                            String jcp          = jsonObject.getString("is_jcp");
                            planBaseMessage     = new PlanBaseMessage(lottery_name,lottery_id, s_id, scheme_name, plan_id,plan_name,cls_name,count,jcp);
                            if(i<9){
                                planBaseMessage.setId("0"+(i+1)+"");
                            }else{
                                planBaseMessage.setId((i+1)+"");
                            }
                            newArray.add(planBaseMessage);
                        }
                        rankList.clear();
                        rankList.addAll(newArray);
                        Message msg = new Message();
                        msg.what = 1;
                        recomdHandler.sendMessage(msg);
                        checkDataState(true);
                        enable(true);
                    }else if(success.equals("-1")){
                        Util.ShowMessageDialog(RankActivity.this);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    checkDataState(false);
                }
            }
        },true);
    }

    //设置热门列表
    public void setHotGridView() {
        rankAdapter     = new RankAdapter(this,rankList);
        ranklistview.setAdapter(rankAdapter);
        rankAdapter.notifyDataSetChanged();
        num_one_plan.setText(rankList.get(0).getScheme_name()+rankList.get(0).getPlan_name().substring(0,2));
        num_two_plan.setText(rankList.get(1).getScheme_name()+rankList.get(1).getPlan_name().substring(0,2));
        num_three_plan.setText(rankList.get(2).getScheme_name()+rankList.get(2).getPlan_name().substring(0,2));
        hot_count1.setText(rankList.get(0).getCount());
        hot_count2.setText(rankList.get(1).getCount());
        hot_count3.setText(rankList.get(2).getCount());
        ranklistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position!=0){
                    goPlan(position+2);
                }
            }
        });
    }

    public void goPlan(int position){
        Intent intent = new Intent(RankActivity.this, PlanProgram.class);
        Bundle bundle = new Bundle();
        bundle.putString("plan_id", rankList.get(position).getPlan_id());
        bundle.putString("s_id", rankList.get(position).getS_id());
        bundle.putString("scheme_name", rankList.get(position).getScheme_name());
        bundle.putString("plan_name",rankList.get(position).getPlan_name());
        bundle.putString("cls_name",rankList.get(position).getCls_name());
        bundle.putString("lottery_name",rankList.get(position).getLottery_name());
        bundle.putString("is_jcp",rankList.get(position).getIs_jcp());
        bundle.putString("type","1");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void initView(){
        swipeRefreshLayout  =   (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_rank);
        program_toolbar     =   (RelativeLayout) findViewById(R.id.ranktoolbar);
        loading             =   (RelativeLayout) findViewById(R.id.loading);
        title_layout        =   (LinearLayout)findViewById(R.id.title_layout);
        ranklistview        =   (ListView)findViewById(R.id.ranklistview);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        title_arrow         =   (ImageView)findViewById(R.id.title_arrow);
        rank_message        =   (ImageView)findViewById(R.id.message);
        header              =   LayoutInflater.from(this).inflate(R.layout.rank_header,null,false);
        plan_one_layout     =   (LinearLayout)header.findViewById(R.id.plan_one_layout);
        plan_two_layout     =   (LinearLayout)header.findViewById(R.id.plan_two_layout);
        plan_three_layout   =   (LinearLayout)header.findViewById(R.id.plan_three_layout);
        num_one_plan        =   (TextView)header.findViewById(R.id.num_one_plan);
        num_two_plan        =   (TextView)header.findViewById(R.id.num_two_plan);
        num_three_plan      =   (TextView)header.findViewById(R.id.num_three_plan);
        hot_count1          =   (TextView)header.findViewById(R.id.hot_count1);
        hot_count2          =   (TextView)header.findViewById(R.id.hot_count2);
        hot_count3          =   (TextView)header.findViewById(R.id.hot_count3);
        nointernetLayout    =   (RelativeLayout) findViewById(R.id.reload_internet);
        nodataLayout        =   (RelativeLayout) findViewById(R.id.no_data);
        reload_button       =   (Button)findViewById(R.id.reload_button);
        dataload_button     =   (Button)findViewById(R.id.dataload_button);
        no_internet_text    =   (TextView)findViewById(R.id.no_internet_text);
        mlistLotteryTitle = setLotteryTitleData();
        reload_button.setOnClickListener(this);
        dataload_button.setOnClickListener(this);
        toolbar_title.setText(lottery_title[index]);
        rank_message.setVisibility(View.VISIBLE);
        ranklistview.addHeaderView(header);
        ranklistview.setDividerHeight(0);
        mPresenter = new EntryPresenter(this,this);
        toolbar_title.setOnClickListener(this);
        rank_message.setOnClickListener(this);
        plan_one_layout.setOnClickListener(this);
        plan_two_layout.setOnClickListener(this);
        plan_three_layout.setOnClickListener(this);
        title_layout.setOnClickListener(this);
        title_arrow.setVisibility(View.VISIBLE);
        setSwipeRefreshLayout();
        ranklistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
        ranklistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        rankAdapter.setmBusy(true);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        rankAdapter.setmBusy(false);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        rankAdapter.setmBusy(false);
                        break;
                    default:
                        break;
                }
                rankAdapter.notifyDataSetChanged();
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
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
        ranklistview.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                requestHotData(index);
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    //设置标题信息
    public List<LotteryTitle> setLotteryTitleData() {
        LotteryTitle lotteryTitle;
        for (int i = 0; i < lottery_title.length; i++) {
            lotteryTitle = new LotteryTitle(lottery_ids[i],lottery_title[i],lottery_type[i],lottery_resid[i], false);
            mlistLotteryTitle.add(lotteryTitle);
        }
        return mlistLotteryTitle;
    }

    //选择彩种计划头列表
    public void showLotteryTitle() {
        lottery_window = RankActivity.this.getLayoutInflater().inflate(R.layout.lottery_choice, null,false);
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
                    requestHotData(position);
                    Message msg = new Message();
                    msg.what = 3;
                    recomdHandler.sendMessageDelayed(msg,500);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.message:
                startActivity(new Intent(RankActivity.this,MessageActivity.class));
                break;
            case R.id.plan_one_layout:
                goPlan(0);
                break;
            case R.id.plan_two_layout:
                goPlan(1);
                break;
            case R.id.plan_three_layout:
                goPlan(2);
                break;
            case R.id.reload_button:
                no_internet_text.setText("正在重新加载网络,请稍等!");
                wifiAdmin = new WifiAdmin(RankActivity.this);
                wifiAdmin.openWifi();
                break;
            case R.id.dataload_button:
                requestHotData(index);
                break;
            case R.id.toolbar_title:
                showLotteryTitle();
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
                    //显示热门彩种信息
                    setHotGridView();
                    break;
                case 2:
                    break;
                case 3:
                    titleWindow.dismiss();
                    break;
            }
        }
    };

    @Override
    public void showLoadingAnimation() {

    }

    @Override
    public void disableLoadingAnimation() {

    }

    @Override
    public void onMessage(String message) {
        rank_message.setBackgroundResource(R.drawable.icon_message);
    }

    @Override
    public void toHome(UserMessage userMessage) {
    }

    @Override
    public void toSuccessAction(String message) {
        rank_message.setBackgroundResource(R.drawable.icon_getmessage);
    }

    @Override
    public void toFailAction(String message) {

    }

    @Override
    protected void onResume() {
        checkNetWorkState();
        requestMessage();
        if(!isCheckData){
            requestHotData(index);
        }
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
            isCheckData = false;
            setDatalayout();
        } else {
            isCheckData = true;
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

    private void enable(boolean enable) {
        if (enable) {
            loading.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.VISIBLE);
        }
    }
}
