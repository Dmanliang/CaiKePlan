package com.example.NextPage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.activity.BaseActivity;
import com.example.caikeplan.activity.CopyPlan;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.getJson.HttpTask;
import com.example.util.OKHttpManager;
import com.example.util.OnNetRequestCallback;
import com.example.util.Util;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class LotteryNextActitivty extends BaseActivity implements OnClickListener{

	private SwipeRefreshLayout  swipeRefreshLayout;
	private ListView 			mListview;
	private List<NextPageBean> 	newsBeanList = new ArrayList<NextPageBean>();
	private NextPageAdapter 	adapter;
	private LinearLayout		program_back;
	private RelativeLayout		loading;
	private TextView			lottery_next_title;
	private LinearLayout		lottery_next_back;
	private String      		big = "大", little = "小", doub = "双", sige = "单";
	private int         		ps, db, sg, th, tt;             //个位,十位,百位,千位,万位的开奖号码
	private String 				startThree,middleThree,endThree,SingleDouble1,SingleDouble2;
	private String 				lottery_id,lottery_name;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.lottery_next);
		getData();
		initViews();
		requestData();
	}

	public void initViews(){
		loading				= (RelativeLayout)findViewById(R.id.loading);
		swipeRefreshLayout  = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_lotterynext);
		program_back		= (LinearLayout)findViewById(R.id.program_back);
		mListview 			= (ListView) findViewById(R.id.lottry_next_list);
		lottery_next_title 	= (TextView)findViewById(R.id.toolbar_title);
		lottery_next_back	= (LinearLayout)findViewById(R.id.program_back);
		lottery_next_title.setText(lottery_name);
		program_back.setVisibility(View.VISIBLE);
		lottery_next_back.setOnClickListener(this);
		program_back.setOnClickListener(this);
		setSwipeRefreshLayout();
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
		mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
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

	//获取字段
	public void getData(){
		Bundle bundle 	= getIntent().getExtras();
		lottery_id		= bundle.getString("lottery_id");
		lottery_name	= bundle.getString("lottery_name");
	}

	//请求数据
	public void requestData(){
		enable(false);
		OKHttpManager okHttpManager = new OKHttpManager();
		okHttpManager.setToken(UserMessage.getInstance().getToken());
		Map<String,String> map = new HashMap<>();
		map.put("user_id",UserMessage.getInstance().getUser_id());
		map.put("lottery_id",lottery_id);
		okHttpManager.post(Constants.API + Constants.LOTTERY_HISTOEY, map, new OnNetRequestCallback() {
			@Override
			public void onFailed(String reason) {

			}

			@Override
			public void onSuccess(String response) {
				try{
					NextPageBean newsBean;
					ArrayList<NextPageBean> list = new ArrayList<>();
					JSONObject jsonObject = new JSONObject(response);
					String success  = jsonObject.getString("success");
					if(success.equals("1")) {
						JSONArray  jsonArray = jsonObject.getJSONArray("data");
						for (int i = 0; i < jsonArray.length(); i++) {
							jsonObject = jsonArray.getJSONObject(i);
							newsBean = new NextPageBean();
							String nums = jsonObject.getString("nums");
							String[] Ballnum = nums.split(",");
							for(int j=0;j<Ballnum.length;j++){
								newsBean.getNums().add(Ballnum[j]);
							}
							newsBean.setIssue(jsonObject.getString("issue"));
							newsBean.setExpect_time(jsonObject.getString("expect_time"));
							if(!lottery_id.equals("27")){
								tt = Integer.parseInt(Ballnum[0]);
								th = Integer.parseInt(Ballnum[1]);
								ps = Integer.parseInt(Ballnum[2]);
								db = Integer.parseInt(Ballnum[3]);
								sg = Integer.parseInt(Ballnum[4]);
								if (db % 2 == 0) {
									if (db >= 5) {
										SingleDouble1=big + doub;
									} else {
										SingleDouble1=little + doub;
									}
								} else {
									if (db >= 5) {
										SingleDouble1=big + sige;
									} else {
										SingleDouble1=little + sige;
									}
								}
								if (sg % 2 == 0) {
									if (sg >= 5) {
										SingleDouble2=big + doub;
									} else {
										SingleDouble2=little + doub;
									}
								} else {
									if (sg >= 5) {
										SingleDouble2=big + sige;
									} else {
										SingleDouble2=little + sige;
									}
								}
								if (ps == db && ps == sg) {
									endThree="豹子";
								} else if (ps != db && ps != sg && db != sg) {
									endThree="组六";
								} else {
									endThree="组三";
								}
								if (ps == db && ps == th) {
									middleThree="豹子";
								} else if (ps != db && ps != th && db != th) {
									middleThree="组六";
								} else {
									middleThree="组三";
								}
								if (ps == tt && ps == th) {
									startThree="豹子";
								} else if (ps != tt && ps != th && tt != th) {
									startThree="组六";
								} else {
									startThree="组三";
								}
								newsBean.setStartthree(startThree);
								newsBean.setMiddlethree(middleThree);
								newsBean.setEndthree(endThree);
								newsBean.setSingledouble(SingleDouble1+"|"+SingleDouble2);
								newsBean.setType("1");
							}else if(lottery_id.equals("27")){
								newsBean.setType("2");
							}else if(lottery_id.equals("23")){
								newsBean.setType("3");
							}
							list.add(newsBean);
						}
						newsBeanList.clear();
						newsBeanList.addAll(list);
						setListView();
					}else if(success.equals("-1")){
						Util.ShowMessageDialog(LotteryNextActitivty.this);
					}
					enable(true);
				}catch (Exception e){
					e.printStackTrace();
				}
			}
		},true);
	}

	//设置列表
	public void setListView(){
		adapter = new NextPageAdapter(LotteryNextActitivty.this, newsBeanList);
		mListview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	//点击事件监听
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
}
