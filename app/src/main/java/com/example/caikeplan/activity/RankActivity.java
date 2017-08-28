package com.example.caikeplan.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
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
import com.example.caikeplan.logic.WifiAdmin;
import com.example.caikeplan.logic.adapter.RankAdapter;
import com.example.caikeplan.logic.message.PlanBaseMessage;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.getJson.HttpTask;

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

    private SwipeRefreshLayout  swipeRefreshLayout;
    private RelativeLayout      ranktoolbar;
    private ListView            ranklistview;
    private List<PlanBaseMessage>   rankList = new ArrayList<>();
    private RankAdapter         rankAdapter;
    private View                header;
    private ImageView           num_one,num_two,num_three;
    private ImageView           rank_message;
    private TextView            num_one_plan,num_two_plan,num_three_plan,hot_count1,hot_count2,hot_count3;
    private TextView            toolbar_title;
    private String              lottery_id = "1";
    private LinearLayout        plan_one_layout,plan_two_layout,plan_three_layout;
    private EntryPresenter      mPresenter;
    private Map<String, String> messagemap = new HashMap<>();
    //网络无法连接
    private RelativeLayout      nointernetLayout;
    private RelativeLayout      nodataLayout;
    private Button              reload_button;
    private Button              dataload_button;
    private TextView            no_internet_text;
    //监测网络状
    private ConnectivityManager manager;
    //自动连接网
    private WifiAdmin           wifiAdmin;
    private PopupWindow         windowItem;
    private View                loadview;
    private boolean				isCheckData  = false;
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

    public void requestHotData(){
        Message msg = new Message();
        msg.what = 2;
        recomdHandler.sendMessage(msg);
        HttpTask httpTask = new HttpTask();
        httpTask.execute(Constants.API+Constants.HOT_SCHEME+lottery_id);
        httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
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
                        planBaseMessage     = new PlanBaseMessage(lottery_name,lottery_id, s_id, scheme_name, plan_id,plan_name,cls_name,count);
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
        windowItem.dismiss();
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
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void initView(){
        swipeRefreshLayout  =   (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_rank);
        ranktoolbar         =   (RelativeLayout)findViewById(R.id.ranktoolbar);
        ranklistview        =   (ListView)findViewById(R.id.ranklistview);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        rank_message        =   (ImageView)findViewById(R.id.message);
        header              =   LayoutInflater.from(this).inflate(R.layout.rank_header,null,false);
        plan_one_layout     =   (LinearLayout)header.findViewById(R.id.plan_one_layout);
        plan_two_layout     =   (LinearLayout)header.findViewById(R.id.plan_two_layout);
        plan_three_layout   =   (LinearLayout)header.findViewById(R.id.plan_three_layout);
        num_one             =   (ImageView)header.findViewById(R.id.num_one);
        num_two             =   (ImageView)header.findViewById(R.id.num_two);
        num_three           =   (ImageView)header.findViewById(R.id.num_three);
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
        reload_button.setOnClickListener(this);
        dataload_button.setOnClickListener(this);
        toolbar_title.setText("点击率排行榜");
        rank_message.setVisibility(View.VISIBLE);
        ranklistview.addHeaderView(header);
        ranklistview.setDividerHeight(0);
        mPresenter = new EntryPresenter(this,this);
        rank_message.setOnClickListener(this);
        plan_one_layout.setOnClickListener(this);
        plan_two_layout.setOnClickListener(this);
        plan_three_layout.setOnClickListener(this);
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

    //加载数据弹框
    public void showItem() {
        loadview = RankActivity.this.getLayoutInflater().inflate(R.layout.loadingdata, null,false);
        windowItem = new PopupWindow(loadview, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, true);
        windowItem.setOutsideTouchable(true);
        windowItem.update();
        if (!windowItem.isShowing()) {
            windowItem.showAtLocation(loadview, Gravity.CENTER, 0, 0);
            windowItem.setFocusable(true);
        }
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
                requestHotData();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

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
                requestHotData();
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
                    showItem();
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
            requestHotData();
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
        windowItem.dismiss();
        swipeRefreshLayout.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
    }

    private void isDataAvailable(){
        windowItem.dismiss();
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.GONE);
    }
}
