package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.EntryContract;
import com.example.caikeplan.logic.EntryPresenter;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.adapter.MessageAdapter;
import com.example.caikeplan.logic.message.NoticeMessage;
import com.example.caikeplan.logic.message.UserMessage;
import com.youth.xframe.base.XActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/8/22.
 */

public class MessageActivity extends XActivity implements View.OnClickListener,EntryContract.View{
    private SwipeRefreshLayout  swipeRefreshLayout;
    private RelativeLayout      nodataLayout;
    private Button              dataload_button;
    private RelativeLayout      nomessagelayout;
    private LinearLayout        program_back;
    private TextView            toolbar_title;
    private ListView            messagelistview;
    private MessageAdapter      messageAdapter;
    private List<NoticeMessage> messageList = new ArrayList<>();
    private EntryPresenter      mPresenter;
    private Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
          //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        initView();
    }

    @Override
    public int getLayoutId() {
        return R.layout.message_activity;
    }

    @Override
    public void initData(Bundle savedInstanceState) {

    }

    public void initView(){
        swipeRefreshLayout  =   (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_message);
        nodataLayout        =   (RelativeLayout) findViewById(R.id.no_data);
        nomessagelayout     =   (RelativeLayout) findViewById(R.id.no_message);
        program_back        =   (LinearLayout)findViewById(R.id.program_back);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        messagelistview     =   (ListView)findViewById(R.id.message_listview);
        dataload_button     =   (Button)findViewById(R.id.dataload_button);
        messageList         =   UserMessage.getInstance().getNoticeMessageList();
        messageAdapter      =   new MessageAdapter(this,messageList);
        messagelistview.setAdapter(messageAdapter);
        messageAdapter.notifyDataSetChanged();
        mPresenter = new EntryPresenter(this,this);
        program_back.setVisibility(View.VISIBLE);
        program_back.setOnClickListener(this);
        dataload_button.setOnClickListener(this);
        toolbar_title.setText("消息公告");
        setSwipeRefreshLayout();
        messagelistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                UserMessage.getInstance().getNoticeMessageList().get(position).setRead(true);
                messageList.get(position).setRead(true);
                messageAdapter.notifyDataSetChanged();
                Intent intent = new Intent(MessageActivity.this,BannerLinkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url",messageList.get(position).getUrl());
                bundle.putString("title",messageList.get(position).getName());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        messagelistview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        messageAdapter.setmBusy(true);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        messageAdapter.setmBusy(false);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        messageAdapter.setmBusy(false);
                        break;
                    default:
                        break;
                }
                messageAdapter.notifyDataSetChanged();
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
        messagelistview.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                requestMessage();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    public void requestMessage(){
        messageList.clear();
        map.put("user_id",UserMessage.getInstance().getUser_id());
        map.put("token",UserMessage.getInstance().getToken());
        mPresenter.message(map);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.program_back:
                finish();
                break;
            case R.id.dataload_button:
                requestMessage();
                break;
        }
    }

    @Override
    public void showLoadingAnimation() {

    }

    @Override
    public void disableLoadingAnimation() {

    }

    @Override
    public void onMessage(String message) {
        if(!message.equals("")){
            ToastUtil.getShortToastByString(this,message);
        }
    }

    @Override
    public void toHome(UserMessage userMessage) {

    }

    @Override
    public void toSuccessAction(String message) {
        messageList = UserMessage.getInstance().getNoticeMessageList();
        messageAdapter.notifyDataSetChanged();
        checkDataState(true);
    }

    @Override
    public void toFailAction(String message) {
        checkDataState(false);
    }

    private void checkDataState(boolean flag){
        if (!flag) {
            setDatalayout();
        } else {
            isDataAvailable();
        }
    }

    private void setDatalayout(){
        swipeRefreshLayout.setVisibility(View.GONE);
        nodataLayout.setVisibility(View.VISIBLE);
    }

    private void isDataAvailable(){
        swipeRefreshLayout.setVisibility(View.VISIBLE);
        nodataLayout.setVisibility(View.GONE);
    }

    private void checkMessageData(){
        if(messageList.size() != 0){
            swipeRefreshLayout.setVisibility(View.VISIBLE);
            nomessagelayout.setVisibility(View.GONE);
        }else{
            swipeRefreshLayout.setVisibility(View.GONE);
            nomessagelayout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        checkMessageData();
        super.onResume();
    }
}
