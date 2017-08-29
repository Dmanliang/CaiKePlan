package com.example.collect;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.activity.BaseActivity;
import com.example.caikeplan.activity.PlanProgram;
import com.example.caikeplan.activity.RankActivity;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.getJson.HttpTask;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class CollectActivity extends BaseActivity implements OnClickListener {

	public  int					requstType = 0;
	public  static final int 	TYPE  = 0;
	public  static final int 	TYPE1 = 1;
	public  static final int 	TYPE2 = 2;
	public  static final int 	TYPE3 = 3;
	public  static final int 	TYPE4 = 4;
	public  static final int 	TYPE5 = 5;
	private SwipeRefreshLayout  swipeRefreshLayout;
	private RelativeLayout      nodataLayout;
	private RelativeLayout      main_toolbar;
	private RelativeLayout 		noCollect;
	private LinearLayout		program_back;
	private CollectListAdapter	adapter;
	private ListView 			mListview;
	private TextView 			toolbar_title;
	private ImageView			choice_collect;
	private PopupWindow			lotteryWindow;
	private RelativeLayout		lottery_cq_ssc_layout,lottery_tj_ssc_layout,lottery_xj_ssc_layout,lottery_pk10_layout,lottery_11x5_layout;
	public  List<CollectBean>	newsBeanList = new ArrayList<>();
	private Button				dataload_button;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.program_collect);
		initView();
		requestCollection();
	}

	public void initView(){
		swipeRefreshLayout  =   (SwipeRefreshLayout)findViewById(R.id.swipe_refresh_collect);
		program_back		=	(LinearLayout)findViewById(R.id.program_back);
		mListview 			= 	(ListView)findViewById(R.id.program_collect_listview);
		main_toolbar    	=   (RelativeLayout)findViewById(R.id.toolbar_container);
		nodataLayout    	=   (RelativeLayout)findViewById(R.id.no_data);
		choice_collect 		= 	(ImageView)findViewById(R.id.choice_collect);
		toolbar_title		=	(TextView)findViewById(R.id.toolbar_title);
		noCollect			=	(RelativeLayout)findViewById(R.id.no_collect);
		dataload_button		=	(Button)findViewById(R.id.dataload_button);
		adapter = new CollectListAdapter(CollectActivity.this, newsBeanList);
		mListview.setAdapter(adapter);
		toolbar_title.setText("收藏");
		program_back.setVisibility(View.VISIBLE);
		choice_collect.setVisibility(View.VISIBLE);
		program_back.setOnClickListener(this);
		dataload_button.setOnClickListener(this);
		choice_collect.setOnClickListener(this);
		setSwipeRefreshLayout();
		mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				goPlan(position);
			}
		});
		mListview.setOnScrollListener(new AbsListView.OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				switch (scrollState) {
					case AbsListView.OnScrollListener.SCROLL_STATE_FLING:
						adapter.setmBusy(true);
						break;
					case AbsListView.OnScrollListener.SCROLL_STATE_IDLE:
						adapter.setmBusy(false);
						break;
					case AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
						adapter.setmBusy(false);
						break;
					default:
						break;
				}
				adapter.notifyDataSetChanged();
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
				switch (requstType){
					case TYPE:
						requestCollection();
						break;
					case TYPE1:
						requestSelectLottery("1");
						break;
					case TYPE2:
						requestSelectLottery("3");
						break;
					case TYPE3:
						requestSelectLottery("7");
						break;
					case TYPE4:
						requestSelectLottery("27");
						break;
					case TYPE5:
						requestSelectLottery("9");
						break;
				}
				swipeRefreshLayout.setRefreshing(false);
			}
		}
	};

	public void goPlan(int position){
		Intent intent = new Intent(CollectActivity.this, PlanProgram.class);
		Bundle bundle = new Bundle();
		bundle.putString("plan_id", newsBeanList.get(position).getPlan_id());
		bundle.putString("s_id", newsBeanList.get(position).getS_id());
		bundle.putString("scheme_name", newsBeanList.get(position).getScheme_name());
		bundle.putString("plan_name",newsBeanList.get(position).getPlan_name());
		bundle.putString("cls_name",newsBeanList.get(position).getCls_name());
		bundle.putString("lottery_name",newsBeanList.get(position).getLottery_name());
		bundle.putString("is_jcp","0");
		bundle.putString("type","0");
		intent.putExtras(bundle);
		startActivity(intent);
	}

	public void requestCollection(){
		HttpTask httpTask = new HttpTask();
		httpTask.execute(Constants.API+Constants.FIND_FAVORITE + UserMessage.getInstance().getUser_id());
		httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
			@Override
			public void taskSuccessful(String json) {
				JSONObject jsonObject;
				CollectBean newsBean;
				try {
					jsonObject = new JSONObject(json);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					ArrayList<CollectBean> list = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						jsonObject = jsonArray.getJSONObject(i);
						String plan_id 		= 	jsonObject.getString("plan_id");
						String play_id 		= 	jsonObject.getString("play_id");
						int lottery_id 		= 	jsonObject.getInt("lottery_id");
						String log_id 		= 	jsonObject.getString("log_id");
						String plan_name 	= 	jsonObject.getString("plan_name");
						String s_id			= 	jsonObject.getString("s_id");
						String schemem_name	= 	jsonObject.getString("scheme_name");
						String lottery_name = 	jsonObject.getString("lottery_name");
						String cls_name		=	jsonObject.getString("cls_name");
						newsBean = new CollectBean(lottery_name,cls_name,plan_id,play_id,log_id,plan_name,s_id,schemem_name,lottery_id,true);
						list.add(newsBean);
					}
					newsBeanList.clear();
					newsBeanList.addAll(list);
					adapter.notifyDataSetChanged();
					checkDataState(true);
					checkCollectState();
				} catch (JSONException e) {
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

	public void requestSelectLottery(String lotteryid){
		HttpTask httpTask = new HttpTask();
		httpTask.execute(Constants.API+Constants.FIND_FAVORITE + UserMessage.getInstance().getUser_id() + "&lottery_id="+lotteryid);
		httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
			@Override
			public void taskSuccessful(String json) {
				JSONObject jsonObject;
				CollectBean newsBean;
				try {
					jsonObject = new JSONObject(json);
					JSONArray jsonArray = jsonObject.getJSONArray("data");
					ArrayList<CollectBean> list = new ArrayList<>();
					for (int i = 0; i < jsonArray.length(); i++) {
						jsonObject = jsonArray.getJSONObject(i);
						String plan_id 		= 	jsonObject.getString("plan_id");
						String play_id 		= 	jsonObject.getString("play_id");
						int lottery_id 		= 	jsonObject.getInt("lottery_id");
						String log_id 		= 	jsonObject.getString("log_id");
						String plan_name 	= 	jsonObject.getString("plan_name");
						String s_id			= 	jsonObject.getString("s_id");
						String schemem_name	= 	jsonObject.getString("scheme_name");
						String lottery_name = 	jsonObject.getString("lottery_name");
						String cls_name		=	jsonObject.getString("cls_name");
						newsBean = new CollectBean(lottery_name,cls_name,plan_id,play_id,log_id,plan_name,s_id,schemem_name+plan_name.substring(0,2),lottery_id,true);
						list.add(newsBean);
					}
					newsBeanList.clear();
					newsBeanList.addAll(list);
					adapter.notifyDataSetChanged();
					checkDataState(true);
					checkCollectState();
				} catch (JSONException e) {
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

	//显示彩种选择
	public void showLotterySelect(View view){
		view = CollectActivity.this.getLayoutInflater().inflate(R.layout.lottery_choice, null,false);
		lotteryWindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT, true);
		lotteryWindow.setTouchable(true);
		lotteryWindow.setOutsideTouchable(true);
		view.setBackgroundColor(Color.parseColor("#80000000"));
		if (lotteryWindow.isShowing()) {
			lotteryWindow.dismiss();
		} else {
			lotteryWindow.setFocusable(true);
			lotteryWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					lotteryWindow.setFocusable(false);
					lotteryWindow.dismiss();
					return false;
				}
			});
		}
		setLotteryView(view);
		int xOffset = main_toolbar.getWidth() / 2 - view.getMeasuredWidth() / 2;
		lotteryWindow.showAsDropDown(main_toolbar, xOffset, 0);
	}

	public void setLotteryView(View view){
		lottery_cq_ssc_layout 	= (RelativeLayout)view.findViewById(R.id.lottery_cq_ssc_layout);
		lottery_tj_ssc_layout 	= (RelativeLayout)view.findViewById(R.id.lottery_tj_ssc_layout);
		lottery_xj_ssc_layout 	= (RelativeLayout)view.findViewById(R.id.lottery_xj_ssc_layout);
		lottery_11x5_layout 	= (RelativeLayout)view.findViewById(R.id.lottery_11x5_layout);
		lottery_pk10_layout 	= (RelativeLayout)view.findViewById(R.id.lottery_pk10_layout);
		lottery_cq_ssc_layout.setOnClickListener(this);
		lottery_tj_ssc_layout.setOnClickListener(this);
		lottery_xj_ssc_layout.setOnClickListener(this);
		lottery_pk10_layout.setOnClickListener(this);
		lottery_11x5_layout.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.program_back:
				finish();
				break;
			case R.id.choice_collect:
				showLotterySelect(v);
				break;
			case R.id.lottery_cq_ssc_layout:
				requestSelectLottery("1");
				Message msg1 = new Message();
				msg1.what = 1;
				recomdHandler.sendMessageDelayed(msg1,500);
				requstType = 1;
				break;
			case R.id.lottery_tj_ssc_layout:
				requestSelectLottery("3");
				Message msg2 = new Message();
				msg2.what = 1;
				recomdHandler.sendMessageDelayed(msg2,500);
				requstType = 2;
				break;
			case R.id.lottery_xj_ssc_layout:
				requestSelectLottery("7");
				Message msg3 = new Message();
				msg3.what = 1;
				recomdHandler.sendMessageDelayed(msg3,500);
				requstType = 3;
				break;
			case R.id.lottery_pk10_layout:
				requestSelectLottery("27");
				Message msg4 = new Message();
				msg4.what = 1;
				recomdHandler.sendMessageDelayed(msg4,500);
				requstType = 4;
				break;
			case R.id.lottery_11x5_layout:
				requestSelectLottery("9");
				Message msg5 = new Message();
				msg5.what = 1;
				recomdHandler.sendMessageDelayed(msg5,500);
				requstType = 5;
				break;
			case R.id.dataload_button:
				requestCollection();
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
					lotteryWindow.dismiss();
					break;
			}
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void checkCollectState(){
		if(newsBeanList.size()==0){
			swipeRefreshLayout.setVisibility(View.GONE);
			noCollect.setVisibility(View.VISIBLE);
		}else{
			swipeRefreshLayout.setVisibility(View.VISIBLE);
			noCollect.setVisibility(View.GONE);
		}
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
}
