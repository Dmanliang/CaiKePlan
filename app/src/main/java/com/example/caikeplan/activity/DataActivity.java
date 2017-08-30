package com.example.caikeplan.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
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
import com.example.caikeplan.logic.Bar;
import com.example.caikeplan.logic.HistogramView;
import com.example.caikeplan.logic.MyGridView;
import com.example.caikeplan.logic.WifiAdmin;
import com.example.caikeplan.logic.adapter.IssueTitleAdapter;
import com.example.caikeplan.logic.adapter.LotteryTitleAdapter;
import com.example.caikeplan.logic.adapter.PlayTitleAdapter;
import com.example.caikeplan.logic.adapter.TableAdapter;
import com.example.caikeplan.logic.message.IssueTitleMessage;
import com.example.caikeplan.logic.message.LotteryTitle;
import com.example.caikeplan.logic.message.PlayTitleMessage;
import com.example.caikeplan.logic.message.TableMessage;
import com.example.getJson.HttpTask;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by dell on 2017/6/2.
 */

public class DataActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout          data_layout;
    private LinearLayout            title_layout;
    private RelativeLayout          data_toolbar;
    private RelativeLayout          toolbar_container;
    private ImageView               back;
    private TextView                back_text;
    private LinearLayout            program_back;
    private TextView                play_select;
    private TextView                toolbar_title;
    private ImageView               title_arrow;
    private TextView                toolbar_set;
    private TextView                toolbar_save;
    private ImageView               btn_copy_plan;
    private ImageView               issue_drop;
    private TextView                issue_drop_text;
    private String                  loc="00001";            //查询类型
    private String                  lotteryId="1";          //彩种类型
    private int                     count_table=30;         //查询次数
    private float                   maxratio;
    private int                     maxnum;
    private ListView                tableList;
    private ListView                lottery_listview;
    private PopupWindow             titleWindow;
    private TableAdapter            tableAdapter;
    private List<TableMessage>      tlist = new ArrayList<>();
    private List<TableMessage>      missList = new ArrayList<>();
    private HistogramView           miss_table;
    private PopupWindow             dataPlayWindow,dataIssueWindow;
    private MyGridView              play_gridview;
    private PlayTitleAdapter        playTitleAdapter;
    private List<PlayTitleMessage>  playlist= new ArrayList<>();
    private ListView                issuesListView;
    private IssueTitleAdapter       issueTitleAdapter;
    private List<LotteryTitle>      mlistLotteryTitle   =   new ArrayList<>();
    private List<IssueTitleMessage> issueTitleList      =   new ArrayList<>();
    private String[]                lottery_title       =   {"重庆时时彩", "天津时时彩", "新疆时时彩"};
    private String[]                lottery_ids         =   {"1","3","7"};
    private String[]                playType            =   {"个位分析","十位分析","百位分析","千位分析","万位分析","前二分析","后二分析","前三分析","中三分析","后三分析","前四分析","后四分析","五星分析"};
    private String[]                locs                =   {"00001","00010","00100","01000","10000","11000","00011","11100","01110","00111","11110","01111","11111"};
    private String[]                issues              =   {"最近30期","最近50期","最近100期","最近200期"};
    private int[]                   tables              =   {30,50,100,200};
    //网络无法连接
    private RelativeLayout          nointernetLayout;
    private RelativeLayout          nodataLayout;
    private Button                  reload_button;
    private TextView                no_internet_text;
    //监测网络状
    private ConnectivityManager     manager;
    //自动连接网
    private WifiAdmin               wifiAdmin;
    private PopupWindow             windowItem;
    private View                    loadview;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.data_activity);
        initView();
        requestData();
    }

    //初始化控件
    public void initView(){
        title_layout        =   (LinearLayout)findViewById(R.id.title_layout);
        data_toolbar        =   (RelativeLayout)findViewById(R.id.data_toolbar);
        toolbar_container   =   (RelativeLayout)findViewById(R.id.toolbar_container);
        program_back        =   (LinearLayout)findViewById(R.id.program_back);
        back                =   (ImageView)findViewById(R.id.back);
        back_text           =   (TextView)findViewById(R.id.back_text);
        play_select         =   (TextView)findViewById(R.id.play_select);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        title_arrow         =   (ImageView)findViewById(R.id.title_arrow);
        toolbar_set         =   (TextView)findViewById(R.id.toolbar_set);
        toolbar_save        =   (TextView)findViewById(R.id.toolbar_save);
        btn_copy_plan       =   (ImageView)findViewById(R.id.btn_copy_plan);
        tableList           =   (ListView)findViewById(R.id.table_listview);
        issue_drop          =   (ImageView)findViewById(R.id.issue_drop);
        issue_drop_text     =   (TextView)findViewById(R.id.issue_drop_text);
        nointernetLayout    =   (RelativeLayout) findViewById(R.id.reload_internet);
        nodataLayout        =   (RelativeLayout) findViewById(R.id.no_data);
        data_layout         =   (RelativeLayout) findViewById(R.id.data_layout);
        reload_button       =   (Button)findViewById(R.id.reload_button);
        no_internet_text    =   (TextView)findViewById(R.id.no_internet_text);
        reload_button.setOnClickListener(this);
        title_arrow.setVisibility(View.VISIBLE);
        play_select.setVisibility(View.VISIBLE);
        title_layout.setOnClickListener(this);
        play_select.setOnClickListener(this);
        issue_drop.setOnClickListener(this);
        issue_drop_text.setOnClickListener(this);
        playlist            = setPlayTitleData();
        issueTitleList      = setIssuesTitleData();
        mlistLotteryTitle   = setLotteryTitleData();
    }

    //设置表格列表
    public void setTableList(){
        tableAdapter = new TableAdapter(this,tlist);
        tableList.setAdapter(tableAdapter);
        tableAdapter.notifyDataSetChanged();
    }

    //设置走势表数据
    public void setMissTable(){
        miss_table  =   (HistogramView)findViewById(R.id.miss_table);
        ArrayList<Bar> bar7Lists = new ArrayList<>();
        Bar bar0 = new Bar(0, missList.get(0).getRatio(), this.getResources().getColor(R.color.table1), missList.get(0).getDanma(), missList.get(0).getOmit());
        Bar bar1 = new Bar(1, missList.get(1).getRatio(), this.getResources().getColor(R.color.table2), missList.get(1).getDanma(), missList.get(1).getOmit());
        Bar bar2 = new Bar(2, missList.get(2).getRatio(), this.getResources().getColor(R.color.table3), missList.get(2).getDanma(), missList.get(2).getOmit());
        Bar bar3 = new Bar(3, missList.get(3).getRatio(), this.getResources().getColor(R.color.table4), missList.get(3).getDanma(), missList.get(3).getOmit());
        Bar bar4 = new Bar(4, missList.get(4).getRatio(), this.getResources().getColor(R.color.table4), missList.get(4).getDanma(), missList.get(4).getOmit());
        Bar bar5 = new Bar(5, missList.get(5).getRatio(), this.getResources().getColor(R.color.table4), missList.get(5).getDanma(), missList.get(5).getOmit());
        Bar bar6 = new Bar(6, missList.get(6).getRatio(), this.getResources().getColor(R.color.table4), missList.get(6).getDanma(), missList.get(6).getOmit());
        Bar bar7 = new Bar(7, missList.get(7).getRatio(), this.getResources().getColor(R.color.table4), missList.get(7).getDanma(), missList.get(7).getOmit());
        Bar bar8 = new Bar(8, missList.get(8).getRatio(), this.getResources().getColor(R.color.table4), missList.get(8).getDanma(), missList.get(8).getOmit());
        Bar bar9 = new Bar(9, missList.get(9).getRatio(), this.getResources().getColor(R.color.table4), missList.get(9).getDanma(), missList.get(9).getOmit());
        bar7Lists.add(bar0);
        bar7Lists.add(bar1);
        bar7Lists.add(bar2);
        bar7Lists.add(bar3);
        bar7Lists.add(bar4);
        bar7Lists.add(bar5);
        bar7Lists.add(bar6);
        bar7Lists.add(bar7);
        bar7Lists.add(bar8);
        bar7Lists.add(bar9);
        miss_table.setBarLists(bar7Lists);
    }

    //请求数据表数据
    public void requestData(){
        tlist.clear();
        missList.clear();
        HttpTask task = new HttpTask();
        task.execute(Constants.API+Constants.DATA_MISS+lotteryId+"&loc="+loc+"&count="+count_table);
        task.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try{
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray   data = jsonObject.getJSONArray("data");
                    JSONArray   occur = data.getJSONObject(0).getJSONArray("occur");
                    TableMessage tableMessage,tableMessage1;
                    for(int i=0;i<occur.length();i++){
                        jsonObject = occur.getJSONObject(i);
                        String count    = jsonObject.getString("count");
                        String danma    = jsonObject.getString("num");
                        String hotcool  = jsonObject.getString("temp");
                        tableMessage = new TableMessage(count,danma,"",hotcool);
                        tlist.add(tableMessage);
                    }
                    JSONArray   miss = data.getJSONObject(0).getJSONArray("miss");
                    for(int i=0;i<occur.length();i++){
                        jsonObject      = miss.getJSONObject(i);
                        String count    = jsonObject.getString("count");
                        String danma    = jsonObject.getString("num");
                        int c           = Integer.parseInt(count);
                        tableMessage1   = new TableMessage("",danma,count,"");
                        tableMessage1.setMiss(c);
                        if(i == 0){
                            setMaxnum(c);
                        }
                        maxratio = setMaxRatio(c);
                        tableMessage1.setRatio(maxratio);
                        missList.add(tableMessage1);
                        setCount(danma,count);
                    }
                    setMissTable();
                    setTableList();
                    checkDataState(true);
                }catch (Exception e){
                    checkDataState(false);
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {
                checkDataState(false);
            }
        });
    }

    //选择彩种计划头列表
    public void showLotteryTitle(View view) {
        view        = DataActivity.this.getLayoutInflater().inflate(R.layout.lottery_listview, null);
        titleWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        titleWindow.setTouchable(true);
        titleWindow.setOutsideTouchable(true);
     //   titleWindow.setAnimationStyle(R.style.popup_window_anim2);
        titleWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
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
        lottery_listview = (ListView) view.findViewById(R.id.lottery_listview);
        final LotteryTitleAdapter ltAdapter = new LotteryTitleAdapter(DataActivity.this, mlistLotteryTitle);
        lottery_listview.setAdapter(ltAdapter);
        ltAdapter.notifyDataSetChanged();
        lottery_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                toolbar_title.setText(mlistLotteryTitle.get(position).getLottery_title());
                if (!mlistLotteryTitle.get(position).isFocus()) {
                    resetFocues();
                    mlistLotteryTitle.get(position).setFocus(true);
                } else {
                    resetFocues();
                }
                ltAdapter.notifyDataSetChanged();
                EnterPlan(position);
                Message msg = new Message();
                msg.what = 1;
                recomdHandler.sendMessageDelayed(msg,500);
            }
        });
        int xOffset = data_toolbar.getWidth() / 2 - view.getMeasuredWidth() / 2;
        titleWindow.showAsDropDown(data_toolbar, xOffset, 0);

    }

    //设置标题信息
    public List<LotteryTitle> setLotteryTitleData() {
        LotteryTitle lotteryTitle;
        for (int i = 0; i < lottery_title.length; i++) {
            if(i == 0){
                lotteryTitle = new LotteryTitle(lottery_ids[i],lottery_title[i], true);
            }else{
                lotteryTitle = new LotteryTitle(lottery_ids[i],lottery_title[i], false);
            }
            mlistLotteryTitle.add(lotteryTitle);
        }
        return mlistLotteryTitle;
    }

    //设置玩法标题信息
    public List<PlayTitleMessage> setPlayTitleData() {
        PlayTitleMessage playTitleMessage;
        for (int i = 0; i < playType.length; i++) {
            if(i==0){
                playTitleMessage = new PlayTitleMessage(playType[i],locs[i],true);
            }else {
                playTitleMessage = new PlayTitleMessage(playType[i],locs[i],false);
            }
            playlist.add(playTitleMessage);
        }
        return playlist;
    }

    //设置玩法标题信息
    public List<IssueTitleMessage> setIssuesTitleData() {
        IssueTitleMessage issueTitleMessage;
        for (int i = 0; i < issues.length; i++) {
            if(i == 0){
                issueTitleMessage = new IssueTitleMessage(issues[i],tables[i],true);
            }else{
                issueTitleMessage = new IssueTitleMessage(issues[i],tables[i],false);
            }
            issueTitleList.add(issueTitleMessage);
        }
        return issueTitleList;
    }

    //选择彩种计划
    public void EnterPlan(int position) {
        if (position == 0) {
            lotteryId = "1";
            requestData();
        } else if (position == 1) {
            lotteryId = "3";
            requestData();
        } else if (position == 2) {
            lotteryId = "7";
            requestData();
        }
    }

    //统计遗漏次数
    public void setCount(String danma,String count){
        for(int i=0;i<tlist.size();i++){
            if(danma.equals(tlist.get(i).getDanma())){
                tlist.get(i).setOmit(count);
            }
        }
    }

   //设置走势图比率最大值
    public void setMaxnum(int c){
        maxnum = c;
    }

    //设置走势图比率
    public float setMaxRatio(int c){
        float ratio=0;
        ratio = (float) c/maxnum;
        return ratio;
}

    //选择数据玩法头列表
    public void showPlayTitle(View view) {
        view = DataActivity.this.getLayoutInflater().inflate(R.layout.data_play_title, null);
        dataPlayWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        dataPlayWindow.setTouchable(true);
        dataPlayWindow.setOutsideTouchable(true);
      //  dataPlayWindow.setAnimationStyle(R.style.popup_window_anim2);
        dataPlayWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        if (dataPlayWindow.isShowing()) {
            dataPlayWindow.dismiss();
        } else {
            dataPlayWindow.setFocusable(true);
            dataPlayWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    dataPlayWindow.setFocusable(false);
                    dataPlayWindow.dismiss();
                    return false;
                }
            });
        }
        play_gridview       = (MyGridView) view.findViewById(R.id.play_gridview);
        playTitleAdapter    = new PlayTitleAdapter(this,playlist);
        play_gridview.setAdapter(playTitleAdapter);
        playTitleAdapter.notifyDataSetChanged();
        play_gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!playlist.get(position).isFocus()) {
                    resetPlayFocues();
                    playlist.get(position).setFocus(true);
                } else {
                    resetPlayFocues();
                }
                play_select.setText(playlist.get(position).getPlay_title());
                loc = playlist.get(position).getLoc();
                playTitleAdapter.notifyDataSetChanged();
                requestData();
                Message msg = new Message();
                msg.what = 2;
                recomdHandler.sendMessageDelayed(msg,500);
            }
        });
        int xOffset = data_toolbar.getWidth() / 2 - view.getMeasuredWidth() / 2;
        dataPlayWindow.showAsDropDown(data_toolbar, xOffset, 0);
    }

    //选择数据期数头列表
    public void showIssuesTitle(View view) {
        view = DataActivity.this.getLayoutInflater().inflate(R.layout.data_issues, null);
        dataIssueWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        dataIssueWindow.setTouchable(true);
        dataIssueWindow.setOutsideTouchable(true);
       // dataIssueWindow.setAnimationStyle(R.style.popup_window_anim);
        dataIssueWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
        if (dataIssueWindow.isShowing()) {
            dataIssueWindow.dismiss();
        } else {
            dataIssueWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);
            dataIssueWindow.setFocusable(true);
            dataIssueWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    dataIssueWindow.setFocusable(false);
                    dataIssueWindow.dismiss();
                    return false;
                }
            });
        }
        issuesListView       = (ListView) view.findViewById(R.id.data_issues_listview);
        issueTitleAdapter    = new IssueTitleAdapter(this,issueTitleList);
        issuesListView.setAdapter(issueTitleAdapter);
        issueTitleAdapter.notifyDataSetChanged();
        issuesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (!issueTitleList.get(position).isFocus()) {
                    resetIssuesFocues();
                    issueTitleList.get(position).setFocus(true);
                } else {
                    resetIssuesFocues();
                }
                issue_drop_text.setText(issueTitleList.get(position).getIssueTitle());
                count_table = issueTitleList.get(position).getTables();
                issueTitleAdapter.notifyDataSetChanged();
                requestData();
                Message msg = new Message();
                msg.what = 3;
                recomdHandler.sendMessageDelayed(msg,500);
            }
        });
        dataIssueWindow.showAsDropDown(view);
    }

    //重设选中玩法标题信息
    public void resetPlayFocues() {
        for (int i = 0; i < playlist.size(); i++) {
            playlist.get(i).setFocus(false);
        }
    }

    //重设选中玩法标题信息
    public void resetIssuesFocues() {
        for (int i = 0; i < issueTitleList.size(); i++) {
            issueTitleList.get(i).setFocus(false);
        }
    }

    //重设选中标题信息
    public void resetFocues() {
        for (int i = 0; i < mlistLotteryTitle.size(); i++) {
            mlistLotteryTitle.get(i).setFocus(false);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.issue_drop_text:
                showIssuesTitle(v);
                break;
            case R.id.play_select:
                showPlayTitle(v);
                break;
            case R.id.title_layout:
                showLotteryTitle(v);
                break;
            case R.id.reload_button:
                no_internet_text.setText("正在重新加载网络,请稍等!");
                wifiAdmin = new WifiAdmin(DataActivity.this);
                wifiAdmin.openWifi();
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
                    titleWindow.dismiss();
                    break;
                case 2:
                    dataPlayWindow.dismiss();
                    break;
                case 3:
                    dataIssueWindow.dismiss();
                    break;
            }
        }
    };

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
        data_layout.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    private void isNetworkAvailable() {
        data_layout.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void setDatalayout(){
        data_layout.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
    }

    private void isDataAvailable(){
        data_layout.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.GONE);
    }
}
