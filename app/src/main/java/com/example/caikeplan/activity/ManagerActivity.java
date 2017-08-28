package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.EntryContract;
import com.example.caikeplan.logic.EntryPresenter;
import com.example.caikeplan.logic.RefreshableView;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.adapter.ManagerAdpater;
import com.example.caikeplan.logic.message.PersonMessage;
import com.example.caikeplan.logic.message.UserMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dell on 2017/8/17.
 */

public class ManagerActivity extends BaseActivity implements View.OnClickListener,EntryContract.View {

    private SwipeRefreshLayout      swipeRefreshLayout;
    private LinearLayout            program_back;
    private TextView                toolbar_title;
    private TextView                group_username,group_endtime;
    private ListView                manager_listview;
    private List<PersonMessage>     managerlist = new ArrayList<>();
    private ManagerAdpater          managerAdpater;
    private View                    header;
    private EntryPresenter          mPresenter;
    private RelativeLayout          loading;
    private Map<String, String>     map = new HashMap<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.manager_activity);
        initView();
    }

    public void requestData(){
        map.put("user_id",UserMessage.getInstance().getUser_id());
        map.put("token",UserMessage.getInstance().getToken());
        mPresenter.managerlist(map);
    }

    public void initView(){
        mPresenter          =   new EntryPresenter(this,this);
        loading             =   (RelativeLayout)findViewById(R.id.loading);
        swipeRefreshLayout  =   (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_manager);
        program_back        =   (LinearLayout)findViewById(R.id.program_back);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        manager_listview    =   (ListView)findViewById(R.id.manager_listview);
        header              =   LayoutInflater.from(this).inflate(R.layout.manangerlist_header,null,false);
        group_username      =   (TextView)header.findViewById(R.id.group_username);
        group_endtime       =   (TextView)header.findViewById(R.id.group_endtime);
        managerAdpater      =   new ManagerAdpater(this,managerlist);
        manager_listview.setAdapter(managerAdpater);
        manager_listview.addHeaderView(header);
        manager_listview.setDividerHeight((int)0.1);
        toolbar_title.setText("用户管理");
        program_back.setVisibility(View.VISIBLE);
        program_back.setOnClickListener(this);
        setSwipeRefreshLayout();
        manager_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position !=0) {
                    Intent intent = new Intent(ManagerActivity.this, ManagerPersonActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("user_id", managerlist.get(position).getUser_id());
                    bundle.putString("power_add", managerlist.get(position).getPower_add());
                    bundle.putString("due_time", managerlist.get(position).getDue_time());
                    bundle.putString("username", managerlist.get(position).getUsername());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        manager_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
                        managerAdpater.setmBusy(true);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
                        managerAdpater.setmBusy(false);
                        break;
                    case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
                        managerAdpater.setmBusy(false);
                        break;
                    default:
                        break;
                }
                managerAdpater.notifyDataSetChanged();
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
        manager_listview.setOnScrollListener(new AbsListView.OnScrollListener() {
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
                requestData();
                swipeRefreshLayout.setRefreshing(false);
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.program_back:
                finish();
                break;
        }
    }

    private void enable(boolean enable) {
        if (enable) {
            loading.setVisibility(View.GONE);
        } else {
            loading.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void showLoadingAnimation() {
        enable(false);
    }

    @Override
    public void disableLoadingAnimation() {
        enable(true);
    }

    @Override
    public void onMessage(String message) {
        ToastUtil.getShortToastByString(this,message);
    }

    @Override
    public void toHome(UserMessage userMessage) {

    }

    @Override
    public void toSuccessAction(String message) {
        managerlist.clear();
        managerlist.addAll(UserMessage.getInstance().getPersonlist());
        managerAdpater.notifyDataSetChanged();
        group_username.setText("群主:"+managerlist.get(0).getUsername());
        group_endtime.setText(managerlist.get(0).getDue_time());
    }

    @Override
    public void toFailAction(String message) {

    }

    @Override
    protected void onResume() {
        requestData();
        super.onResume();
    }
}
