package com.example.caikeplan.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.NextPage.LotteryNextActitivty;
import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.AdFragment;
import com.example.caikeplan.logic.adapter.AdvAdapter;
import com.example.caikeplan.logic.message.Banner;
import com.example.caikeplan.logic.message.CaiZhongMessage;
import com.example.caikeplan.logic.adapter.MyListViewAdapter;
import com.example.caikeplan.logic.RefreshableView;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.caikeplan.logic.WifiAdmin;
import com.example.getJson.HttpTask;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@SuppressLint("SimpleDateFormat")
public class LotteryActivity extends BaseActivity {

    //广告轮播
    private ViewPager               adViewpager;
    private ImageView[]             imageViews  = null;
    private ImageView               imageView   = null;
    private ViewGroup               group;      //装载indicator
    private AtomicInteger           what        = new AtomicInteger(0);
    private static final int        DE          = 2000;
    private boolean                 isContinue  = true;
    private AdvAdapter              mAdvApter;                        //广告适配器
    private ArrayList<AdFragment>   mAdFragments = new ArrayList<>();
    private ArrayList<Banner>       mBanners     = new ArrayList<>();
    private int                     imagenum     = 2;
    //彩种开奖列表
    //下拉刷新
    private TextView                toolbar_title;
    private RelativeLayout          relativeList;
    private RefreshableView         refreshableView;
    private ListView                listView;
    private MyListViewAdapter       mAdpater;
    private List<CaiZhongMessage>   messages = new ArrayList<>();
    //数据
    private String[]        homeTitles  = {"重庆时时彩", "新疆时时彩", "天津时时彩"};
    private String[]        lotteryids  = {"1","7","3"};
    private String          lottery="1,7,3";
    //网络无法连接
    private RelativeLayout  nointernetLayout;
    private Button          reload_button;
    private TextView        no_internet_text;
    //监测网络状
    private ConnectivityManager manager;
    //自动连接网
    private WifiAdmin wifiAdmin;
    //请求链
    private HttpTask Httptask1, Httptask2, Httptask3;
    //获取彩种开奖结果
    private String url1 = "/lottery/get_lottery_last_result?lottery_ids=";
    //获取服务器时间(对时)
    private String url2 = "/lottery/get_server_time";
    //获取彩种当前期信息
    private String url3 = "/lottery/get_lottery_current_issue?lottery_ids=";
    //请求时间数据
    public SimpleDateFormat     format          = new SimpleDateFormat("yyyyMMddHHmmss");
    private long[]              min             = new long[3];
    private long[]              secd            = new long[3];
    private String[]            iss             = new String[3];
    private String[]            tempIss         = new String[3];
    //上期开奖数据
    private String              issue           = null;
    private String              num             = null;
    private String              expect_time     = null;
    private String              server_time     = null;
    private String[]            lastTime        = new String[3];
    private String[]            templastTime    = new String[3];
    //本期开奖数据
    private String              issueNow        = null;
    private String              expect_timeNow  = null;
    private boolean             isOpenNum       = false;
    private static final int    DELAY           = 2000;
    private boolean[]           isNextOpen      = new boolean[3];
    private boolean[]           isFistTime      = new boolean[3];
    private boolean[]           isLastTime      = new boolean[3];
    private boolean[]           isChanger       = new boolean[3];
    private boolean             isComing        = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.lottery_activity);
        initView();
      //  requestData();
      /*  //开奖倒计时
        timeHandler.post(timeRunnable);
        //每60秒钟请求一次数据
        dataHandler.postDelayed(dataRunnable, 60000);*/
    }

    //初始化控件
    public void initView() {
        messages = setData();
        for(int i=0;i<isFistTime.length;i++){
            isFistTime[i]   =   true;
            isNextOpen[i]   =   true;
            isLastTime[i]   =   true;
            isChanger[i]    =   false;
        }
        requestBanner();
        View header = LayoutInflater.from(LotteryActivity.this).inflate(R.layout.header, null);
        adViewpager = (ViewPager) header.findViewById(R.id.ad_viewpager);
        group       = (ViewGroup) header.findViewById(R.id.viewGroup);
        adViewpager.setAdapter(mAdvApter = new AdvAdapter(getSupportFragmentManager(), mAdFragments));
        adViewpager.addOnPageChangeListener(new GuidePageChangeListener());
        adViewpager.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                    case MotionEvent.ACTION_MOVE:
                        isContinue = false;
                        break;
                    case MotionEvent.ACTION_UP:
                        isContinue = true;
                        break;
                    default:
                        isContinue = true;
                        break;
                }
                return false;
            }
        });
        addIndicator(group);//广告轮播图添加指示器
        //广告自动切换线程
        viewHandler.postDelayed(viewRunnable, DE);
        relativeList        =   (RelativeLayout) findViewById(R.id.relativeList);
        refreshableView     =   (RefreshableView)findViewById(R.id.refreshable_view);
        nointernetLayout    =   (RelativeLayout) findViewById(R.id.reload_internet);
        reload_button       =   (Button)    findViewById(R.id.reload_button);
        no_internet_text    =   (TextView)  findViewById(R.id.no_internet_text);
        toolbar_title       =   (TextView)  findViewById(R.id.toolbar_title);
        listView            =   (ListView)  findViewById(R.id.listrview_caizhong);
        mAdpater            =   new MyListViewAdapter(LotteryActivity.this, messages);
        toolbar_title.setText("开  奖");
        listView.setAdapter(mAdpater);
        listView.addHeaderView(header);
        //列表点击监听
        mAdpater.setOnItemListener(new MyListViewAdapter.ListViewOnListener() {
            @Override
            public void onItemClick(int position, MyListViewAdapter.CaiZhongViewHolder holder) {
                Intent intent = new Intent(LotteryActivity.this, LotteryNextActitivty.class);
                Bundle bundle = new Bundle();
                bundle.putString("lottery_id",messages.get(position).getLottery_id());
                bundle.putString("lottery_name", messages.get(position).getHome_title());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        //下拉列表刷新监听
        refreshableView.setOnRefreshListener(new RefreshableView.PullToRefreshListener() {
            @Override
            public void onRefresh() {
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        refreshableView.finishRefreshing();
                        requestData();
                        requestBanner();
                    }
                }).start();
            }
        }, 0);

        //重新连接网络
        reload_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                no_internet_text.setText("正在重新加载网络,请稍等!");
                wifiAdmin = new WifiAdmin(LotteryActivity.this);
                wifiAdmin.openWifi();
            }
        });
    }

    //广告轮播小图标
    public void addIndicator(ViewGroup group) {
        if (group.getChildCount() > 0) {
            group.removeViews(0, group.getChildCount());
        }
        //小图标
        imageViews = new ImageView[mBanners.size()];
        for (int i = 0; i < mBanners.size(); i++) {
            imageView = new ImageView(LotteryActivity.this);
            imageView.setLayoutParams(new ViewGroup.LayoutParams(22, 22));
            imageViews[i] = imageView;
            if (i == 0) {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_focused);
            } else {
                imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
            }
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(15,
                    15));
            layoutParams.leftMargin = 15;
            layoutParams.rightMargin = 15;
            group.addView(imageViews[i], layoutParams);
        }
    }

    //切换广告监听
    private final class GuidePageChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            position = position % mAdFragments.size();
            for (int i = 0; i < imageViews.length; i++) {
                imageViews[position].setBackgroundResource(R.drawable.page_indicator_focused);
                if (position != i) {
                    imageViews[i].setBackgroundResource(R.drawable.page_indicator_unfocused);
                }
                what.set(position);
            }
        }
    }

    private Runnable viewRunnable = new Runnable() {
        @Override
        public void run() {
            if (isContinue) {
                viewHandler.sendEmptyMessage(what.get());
                whatOption();
                viewHandler.postDelayed(viewRunnable, DELAY);
            }
        }
    };

    private void whatOption() {
        what.incrementAndGet();
        if (what.get() > imageViews.length - 1) {
            what.getAndAdd(-imageViews.length);
        }
    }

    private static class ViewHandler extends Handler {
        WeakReference<LotteryActivity> mReferencer;

        ViewHandler(LotteryActivity lottery) {
            mReferencer = new WeakReference<>(lottery);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LotteryActivity lottery = mReferencer.get();
            if (lottery != null) {
                lottery.adViewpager.setCurrentItem(msg.what);
            }
        }

    }

    //启动广告
    private final ViewHandler viewHandler = new ViewHandler(this);

    public List<CaiZhongMessage> setData() {
        for (int i = 0; i < homeTitles.length; i++) {
            CaiZhongMessage czs = new CaiZhongMessage(lotteryids[i],homeTitles[i], "第 20170101001 期已开", "", "", "", "", "", "第 20170101001 期开奖倒计时：", "00:00", LotteryNextActitivty.class);
            messages.add(czs);
        }
        return messages;
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
        relativeList.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    private void isNetworkAvailable() {
        relativeList.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
    }

    public Date time() { //获取本地时间
        Date date = new Date();
        return date;
    }

    //请求数据
    public void requestData() {
        requestTime();
        requestMessage(lottery);
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

    //请求开奖轮播图
    public void requestBanner(){
        mBanners.clear();
        HttpTask httptask = new HttpTask();
        httptask.execute(Constants.API+Constants.ADV_IMAGE+imagenum);
        httptask.setTaskHandler(new HttpTask.HttpTaskHandler() {
            @Override
            public void taskSuccessful(String json) {
                try{
                    JSONObject jsonObject = new JSONObject(json);
                    JSONArray data = jsonObject.getJSONArray("data");
                    Banner banner;
                    for(int i=0;i<data.length();i++){
                        jsonObject = data.getJSONObject(i);
                        String      imageurl    = jsonObject.getString("image");
                        String      link        = jsonObject.getString("url");
                        String      title       = jsonObject.getString("title");
                        banner                  = new Banner(imageurl,link,title);
                        mBanners.add(banner);
                    }
                    if (mAdvApter != null) {
                        FragmentManager     fragmentManager = getSupportFragmentManager();
                        FragmentTransaction transaction     = fragmentManager.beginTransaction();
                        List<Fragment>      fragments       = fragmentManager.getFragments();
                        if (fragments != null && fragments.size() > 0) {
                            for (int i = 0; i < fragments.size(); i++) {
                                fragments.remove(i);
                            }
                        }
                        transaction.commit();
                    }
                    mAdFragments.clear();
                    for (int i = 0; i < mBanners.size(); i++) {
                        mAdFragments.add(AdFragment.newInstance(mBanners.get(i).getImgUrl(), mBanners.get(i).getLink(), mBanners.get(i).getTitle()));
                    }
                    //添加轮播图
                    mAdvApter.notifyDataSetChanged();
                    addIndicator(group);//添加indicator
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void taskFailed() {

            }
        });
    }

    public void requestMessage(final String nums) {
        //获取当前开奖数据
        Httptask3 = new HttpTask();
        Httptask3.execute(SendMessage.getInstance().getBestURL() + url3+nums);
        Httptask3.setTaskHandler(new HttpTask.HttpTaskHandler() {
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject data = jsonObject.getJSONObject("data");
                    for(int i=0;i<lotteryids.length;i++){
                        JSONObject id = data.getJSONObject(lotteryids[i]);
                        issueNow = id.optString("issue");
                        expect_timeNow = id.optString("expect_time");
                        setNowMessage(issueNow, expect_timeNow, i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void taskFailed() {
            }
        });

        //获取已开奖结果数据
        Httptask1 = new HttpTask();
        Httptask1.execute(SendMessage.getInstance().getBestURL() + url1+nums+"&count=1");
        Httptask1.setTaskHandler(new HttpTask.HttpTaskHandler() {
            public void taskSuccessful(String json) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    JSONObject data = jsonObject.getJSONObject("data");
                    for(int i=0;i<lotteryids.length;i++){
                        JSONArray   ls = data.getJSONArray(lotteryids[i]);
                        JSONObject  lottery = ls.getJSONObject(0);
                        issue = lottery.optString("issue");
                        num = lottery.optString("nums");
                        expect_time = lottery.optString("expect_time");
                        String[] numList = num.split(",");
                        setMessage(numList, issue, i);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void taskFailed() {
            }
        });
    }

    public void setMessage(String[] num, String issue, int pos) {
        lastTime[pos] = issue;
        if(isLastTime[pos]){
            templastTime[pos] = issue;
            isLastTime[pos]   = false;
        }else{
            if(templastTime[pos].equals(lastTime[pos])&& isChanger[pos]){
                isNextOpen[pos]     = false;
                lastTime[pos]       = tempIss[pos];
            }else if(!templastTime[pos].equals(lastTime[pos])&& !isNextOpen[pos]) {
                isNextOpen[pos]     = true;
                templastTime[pos]   = lastTime[pos];
            }
        }
        messages.get(pos).setBall1(num[0]);
        messages.get(pos).setBall2(num[1]);
        messages.get(pos).setBall3(num[2]);
        messages.get(pos).setBall4(num[3]);
        messages.get(pos).setBall5(num[4]);
        if(!isNextOpen[pos]){
            messages.get(pos).setHome_date_end("第 " + lastTime[pos] + " 期开奖中");
        }else{
            messages.get(pos).setHome_date_end("第 " + lastTime[pos] + " 期已开");
        }

    }

    public void setNowMessage(String home_date, String expect_time, int pos) {
        try {
            iss[pos] = home_date;
            if(isFistTime[pos]){
                tempIss[pos]        = home_date;
                isFistTime[pos]     = false;
            }else{
               if(!tempIss[pos].equals(iss[pos])&& !isChanger[pos]){
                   isChanger[pos]   = true;
                }else if(!tempIss[pos].equals(iss[pos])&& isChanger[pos] && isNextOpen[pos]){
                   tempIss[pos]     = iss[pos];
                   isChanger[pos]   = false;
               }
            }
            long minutes    = (format.parse(expect_time).getTime() - format.parse(server_time).getTime()) / 60000;
            long seconds    = (format.parse(expect_time).getTime() - format.parse(server_time).getTime()) / 1000 - minutes * 60;
            min[pos]        = minutes;
            secd[pos]       = seconds;
            messages.get(pos).setHome_date("第 " + home_date + " 期开奖倒计时：");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开奖时间线程
     * 每秒向服务器校对一次 分针和秒针
     */
    public TimeHandler timeHandler = new TimeHandler(this);
    Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Message msg = new Message();
                timeHandler.sendMessage(msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };


    private static class TimeHandler extends Handler {
        WeakReference<LotteryActivity> reference;
        public TimeHandler(LotteryActivity lottery) {
            reference = new WeakReference<>(lottery);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LotteryActivity lottery = reference.get();
            if (lottery != null) {
                for (int i = 0; i < 3; i++) {
                    lottery.reBackTime(i);
                }
                lottery.mAdpater.notifyDataSetChanged();
                lottery.timeHandler.postDelayed(lottery.timeRunnable, 1000);
            }
        }
    }


    /**
     * 每15秒请求一次数据
     */
    public DataHandler dataHandler = new DataHandler(this);
    public Runnable dataRunnable = new Runnable() {
        @Override
        public void run() {
            try {
                Message msg = new Message();
                dataHandler.sendMessage(msg);
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    };

    private static class DataHandler extends Handler {
        WeakReference<LotteryActivity> reference;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            LotteryActivity lottery = reference.get();
            if (lottery != null) {
                lottery.requestData();
                lottery.mAdpater.notifyDataSetChanged();
                lottery.dataHandler.postDelayed(lottery.dataRunnable, 60000);
            }
        }

        DataHandler(LotteryActivity lottery) {
            reference = new WeakReference<>(lottery);
        }
    }

    //倒计时操作
    public void reBackTime(int pos) {
        //输出显示
        if (secd[pos] <= 60 && secd[pos] > 0 && min[pos] >= 10) {
            secd[pos]--;
            if (secd[pos] >= 10) {
                messages.get(pos).setHome_timer(min[pos] + ":"  + secd[pos]);
            } else {
                messages.get(pos).setHome_timer(min[pos] + ":0" + secd[pos]);
            }
        } else if (secd[pos] <= 60 && secd[pos] > 0 && min[pos] < 10 && min[pos] >= 0) {
            secd[pos]--;
            if (secd[pos] >= 10) {
                messages.get(pos).setHome_timer("0" + min[pos] + ":" + secd[pos]);
            } else {
                messages.get(pos).setHome_timer("0" + min[pos] + ":0" + secd[pos]);
            }
        } else if (min[pos] > 0 && secd[pos] == 0) {
            min[pos]--;
            secd[pos] = 59;
            if (min[pos] >= 10) {
                messages.get(pos).setHome_timer(min[pos] + ":" + secd[pos]);
            } else {
                messages.get(pos).setHome_timer("0" + min[pos] + ":" + secd[pos]);
            }
        } else if (secd[pos] == 0 && min[pos] == 0) {
            messages.get(pos).setHome_timer("00:00");
            requestData();
        }
    }

    //重新初始化一些数据
    public void resetData(){
        for(int i=0;i<3;i++){
            iss[i]="";
            tempIss[i]="";
            lastTime[i]="";
            templastTime[i]="";
            isNextOpen[i]   = true;
            isFistTime[i]   = true;
            isLastTime[i]   = true;
            isChanger[i]    = false;
        }
    }

    @Override
    protected void onResume() {
        checkNetWorkState();
        resetData();
        /*requestData();
        if(!isComing){
            //开奖倒计时
            timeHandler.post(timeRunnable);
            //每60秒钟请求一次数据
            dataHandler.postDelayed(dataRunnable, 15000);
            isComing = true;
        }*/
        super.onResume();
    }

    /*@Override
    protected void onPause() {
        timeHandler.removeCallbacks(timeRunnable);
        dataHandler.removeCallbacks(dataRunnable);
        isComing = false;
        super.onPause();
    }
*/
    @Override
    protected void onDestroy() {
        timeHandler.removeCallbacks(timeRunnable);
        dataHandler.removeCallbacks(dataRunnable);
        super.onDestroy();
    }
}