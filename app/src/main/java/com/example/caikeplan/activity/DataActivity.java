package com.example.caikeplan.activity;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
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
import com.example.caikeplan.logic.adapter.TitleGrildAdapter;
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

    private LinearLayout            title_layout;
    private RelativeLayout          data_toolbar;
    private TextView                toolbar_title;
    private ImageView               title_arrow;
    private ListView                lottery_listview;
    private PopupWindow             titleWindow;
    private List<LotteryTitle>      mlistLotteryTitle   =   new ArrayList<>();
    private String[]                lottery_title       =   {"重庆时时彩", "天津时时彩", "新疆时时彩","上海11选5","广东11选5","山东11选5","北京PK10","",""};
    private String[]                lottery_ids         =   {"1","3","7","22","9","10","27","",""};
    private String[]                lottery_type        =   {"1","1","1","3","3","3","10","",""};
    private int[]                   lottery_resid       =   {R.drawable.lottery_cq_ssc,R.drawable.lottery_tj_ssc,R.drawable.lottery_xj_ssc,R.drawable.lottery_sh_11x5,R.drawable.lottery_gd_11x5,R.drawable.lottery_sd_11x5,R.drawable.lottery_bj_pk10,0,0};
    private GridView                titleGridView;
    private TitleGrildAdapter       titleGrildAdapter;
    //网络无法连接
    private RelativeLayout          nointernetLayout;
    private Button                  reload_button;
    private TextView                no_internet_text;
    //监测网络状
    private ConnectivityManager     manager;
    //自动连接网
    private WifiAdmin               wifiAdmin;
    private View                    loadview;
    private WebView                 datawebview;
    private String                  url = Constants.API + Constants.ZST_DATA + "1&type=1";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.data_activity);
        initView();
    }

    //初始化控件
    public void initView(){
        title_layout        =   (LinearLayout)findViewById(R.id.title_layout);
        data_toolbar        =   (RelativeLayout)findViewById(R.id.data_toolbar);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        title_arrow         =   (ImageView)findViewById(R.id.title_arrow);
        nointernetLayout    =   (RelativeLayout) findViewById(R.id.reload_internet);
        reload_button       =   (Button)findViewById(R.id.reload_button);
        no_internet_text    =   (TextView)findViewById(R.id.no_internet_text);
        datawebview         =   (WebView)findViewById(R.id.data_webview);
        reload_button.setOnClickListener(this);
        title_arrow.setVisibility(View.VISIBLE);
        title_layout.setOnClickListener(this);
        mlistLotteryTitle   = setLotteryTitleData();
        setWebView();
    }

    public void setWebView(){
        datawebview.requestFocusFromTouch();
        WebSettings setting = datawebview.getSettings();
        datawebview.requestFocus();
        setting.setJavaScriptCanOpenWindowsAutomatically(true);
        setting.setDomStorageEnabled(true);
        setting.setUseWideViewPort(true);               //关键点
        setting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        setting.setDisplayZoomControls(true);
        setting.setJavaScriptEnabled(true);             //  设置支持javascript脚本
        setting.setAllowFileAccess(true);               //  允许访问文件
        setting.setLoadWithOverviewMode(true);          //  缩放至屏幕的大小
        setting.setBuiltInZoomControls(true);           //  设置显示缩放按钮
        setting.setSupportZoom(true);                   //  支持缩放
        setting.setUseWideViewPort(true);               //  将图片调整到适合webview的大小
        setting.setAppCacheEnabled(false);              //  加缓存
        setting.setRenderPriority(WebSettings.RenderPriority.HIGH);  //提高渲染的优先级
        setting.setDomStorageEnabled(true);
        setting.setDatabaseEnabled(true);               //  支持数据库
        setting.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//关闭webview中缓存
        setting.setAllowFileAccess(true);               //设置可以访问文件
        setting.setNeedInitialFocus(true);              //当webview调用requestFocus时为webview设置节点
        setting.setLoadsImagesAutomatically(true);      //支持自动加载图片
        setting.setDefaultTextEncodingName("utf-8");    //设置编码格式
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        datawebview.addJavascriptInterface(this, "android");
        //Webview兼容cookie
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            CookieManager.getInstance().setAcceptThirdPartyCookies(datawebview,true);
        datawebview.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    view.loadUrl(request.getUrl().toString());
                } else {
                    view.loadUrl(request.toString());
                }
                return false;
            }
        });
        datawebview.loadUrl(url);
    }


    //选择彩种计划头列表
    public void showLotteryTitle(View view) {
        view        = DataActivity.this.getLayoutInflater().inflate(R.layout.lottery_choice, null);
        titleWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
        titleWindow.setTouchable(true);
        titleWindow.setOutsideTouchable(true);
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
        setLotteryView(view);
        int xOffset = data_toolbar.getWidth() / 2 - view.getMeasuredWidth() / 2;
        titleWindow.showAsDropDown(data_toolbar, xOffset, 0);
    }

    public void setLotteryView(View view){
        titleGridView = (MyGridView)view.findViewById(R.id.lottery_title_gridview);
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
                    msg.what = 1;
                    recomdHandler.sendMessageDelayed(msg,500);
                }
            }
        });
    }

    //设置标题信息
    public List<LotteryTitle> setLotteryTitleData() {
        LotteryTitle lotteryTitle;
        for (int i = 0; i < lottery_title.length; i++) {
            if(i == 0){
                lotteryTitle = new LotteryTitle(lottery_ids[i],lottery_title[i],lottery_type[i],lottery_resid[i], true);
            }else{
                lotteryTitle = new LotteryTitle(lottery_ids[i],lottery_title[i],lottery_type[i],lottery_resid[i], false);
            }
            mlistLotteryTitle.add(lotteryTitle);
        }
        return mlistLotteryTitle;
    }

    public void EnterPlan(int position){
        url = Constants.API + Constants.ZST_DATA + mlistLotteryTitle.get(position).getLottery_id() + "&type=" + mlistLotteryTitle.get(position).getType();
        datawebview.reload();
        datawebview.loadUrl(url);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
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

    /**
     * 网络未连接时,调用设置方法
     */
    private void setNetWork() {
        datawebview.setVisibility(View.GONE);
        nointernetLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 网络已经连接，然后去判断是wifi连接还是GPRS连接
     * 设置一些自己的逻辑调用
     */
    private void isNetworkAvailable() {
        datawebview.setVisibility(View.VISIBLE);
        nointernetLayout.setVisibility(View.GONE);
    }
}
