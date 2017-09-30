package com.example.collect;

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
import com.example.caikeplan.activity.PlanProgram;
import com.example.caikeplan.activity.RankActivity;
import com.example.caikeplan.logic.MyGridView;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.adapter.TitleGrildAdapter;
import com.example.caikeplan.logic.message.LotteryTitle;
import com.example.caikeplan.logic.message.PlanBaseMessage;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.getJson.HttpTask;
import com.example.util.OKHttpManager;
import com.example.util.OnNetRequestCallback;
import com.example.util.Util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
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
	public  static final int 	TYPE6 = 6;
	public  static final int 	TYPE7 = 7;
	public  static final int 	TYPE8 = 8;
	private SwipeRefreshLayout  swipeRefreshLayout;
	private RelativeLayout      nodataLayout;
	private RelativeLayout      loading;
	private RelativeLayout      main_toolbar;
	private RelativeLayout 		noCollect;
	private LinearLayout		program_back;
	private CollectListAdapter	adapter;
	private ListView 			mListview;
	private TextView 			toolbar_title;
	private ImageView			choice_collect;
	private PopupWindow			lotteryWindow;
	private List<LotteryTitle>	lottery_titleList = new ArrayList<>();
	private GridView			titleGridView;
	private TitleGrildAdapter   titleGrildAdapter;
	public  List<CollectBean>	newsBeanList = new ArrayList<>();
	private Button				dataload_button;
	private String[]            lottery_title   = {"重庆时时彩", "天津时时彩", "新疆时时彩","上海11选5","广东11选5","山东11选5","北京PK10","",""};
	private String[]            lottery_ids     = {"1","3","7","22","9","10","27","",""};
	private String[]            lottery_type    = {"1","1","1","3","3","3","10","",""};
	private int[]               lottery_resid   = {R.drawable.lottery_cq_ssc,R.drawable.lottery_tj_ssc,R.drawable.lottery_xj_ssc,R.drawable.lottery_sh_11x5,R.drawable.lottery_gd_11x5,R.drawable.lottery_sd_11x5,R.drawable.lottery_bj_pk10,0,0};

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
		loading    			=   (RelativeLayout)findViewById(R.id.loading);
		choice_collect 		= 	(ImageView)findViewById(R.id.choice_collect);
		toolbar_title		=	(TextView)findViewById(R.id.toolbar_title);
		noCollect			=	(RelativeLayout)findViewById(R.id.no_collect);
		dataload_button		=	(Button)findViewById(R.id.dataload_button);
		adapter = new CollectListAdapter(CollectActivity.this, newsBeanList);
		lottery_titleList = setLotteryTitleData();
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
						requestSelectLottery("22");
						break;
					case TYPE5:
						requestSelectLottery("9");
						break;
					case TYPE6:
						requestSelectLottery("10");
						break;
					case TYPE7:
						requestSelectLottery("27");
						break;
					case TYPE8:
						requestSelectLottery("23");
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

	//设置标题信息
	public List<LotteryTitle> setLotteryTitleData() {
		LotteryTitle lotteryTitle;
		for (int i = 0; i < lottery_title.length; i++) {
			lotteryTitle = new LotteryTitle(lottery_ids[i],lottery_title[i],lottery_type[i],lottery_resid[i], false);
			lottery_titleList.add(lotteryTitle);
		}
		return lottery_titleList;
	}

	public final void requestCollection(){
		enable(false);
		OKHttpManager okHttpManager = new OKHttpManager();
		okHttpManager.setToken(UserMessage.getInstance().getToken());
		Map<String,String> map = new HashMap<>();
		map.put("user_id",UserMessage.getInstance().getUser_id());
		map.put("os_type","1");
		okHttpManager.post(Constants.API+Constants.FIND_FAVORITE, map, new OnNetRequestCallback() {
			@Override
			public void onFailed(String reason) {
				try {
					JSONObject jsonObject = new JSONObject(reason);
					String success = jsonObject.getString("success");
					checkDataState(false);
					enable(true);
					if (success.equals("-1")) {
						Util.ShowMessageDialog(CollectActivity.this);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onSuccess(String response) {
				JSONObject jsonObject;
				CollectBean newsBean;
				try {
					jsonObject = new JSONObject(response);
					String success  = jsonObject.getString("success");
					if(success.equals("1")) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						ArrayList<CollectBean> list = new ArrayList<>();
						for (int i = 0; i < jsonArray.length(); i++) {
							jsonObject = jsonArray.getJSONObject(i);
							String plan_id 		= 	jsonObject.getString("plan_id");
							String play_id 		= 	jsonObject.getString("play_id");
							String lottery_id   = 	jsonObject.getString("lottery_id");
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
						enable(true);
					}
				} catch (JSONException e) {
					checkDataState(false);
					enable(false);
					e.printStackTrace();
				}
			}
		},true);
	}

	public void requestSelectLottery(String lotteryid){
		enable(false);
		OKHttpManager okHttpManager = new OKHttpManager();
		okHttpManager.setToken(UserMessage.getInstance().getToken());
		Map<String,String> map = new HashMap<>();
		map.put("user_id",UserMessage.getInstance().getUser_id());
		map.put("lottery_id",lotteryid);
		map.put("os_type","1");
		okHttpManager.post(Constants.API + Constants.FIND_FAVORITE, map, new OnNetRequestCallback() {
			@Override
			public void onFailed(String reason) {
				try {
					JSONObject jsonObject = new JSONObject(reason);
					String success = jsonObject.getString("success");
					checkDataState(false);
					enable(true);
					if (success.equals("-1")) {
						Util.ShowMessageDialog(CollectActivity.this);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void onSuccess(String response) {
				JSONObject jsonObject;
				CollectBean newsBean;
				try {
					jsonObject = new JSONObject(response);
					String success  = jsonObject.getString("success");
					if(success.equals("1")) {
						JSONArray jsonArray = jsonObject.getJSONArray("data");
						ArrayList<CollectBean> list = new ArrayList<>();
						for (int i = 0; i < jsonArray.length(); i++) {
							jsonObject = jsonArray.getJSONObject(i);
							String plan_id = jsonObject.getString("plan_id");
							String play_id = jsonObject.getString("play_id");
							String lottery_id = jsonObject.getString("lottery_id");
							String log_id = jsonObject.getString("log_id");
							String plan_name = jsonObject.getString("plan_name");
							String s_id = jsonObject.getString("s_id");
							String schemem_name = jsonObject.getString("scheme_name");
							String lottery_name = jsonObject.getString("lottery_name");
							String cls_name = jsonObject.getString("cls_name");
							newsBean = new CollectBean(lottery_name, cls_name, plan_id, play_id, log_id, plan_name, s_id, schemem_name, lottery_id, true);
							list.add(newsBean);
						}
						newsBeanList.clear();
						newsBeanList.addAll(list);
						adapter.notifyDataSetChanged();
						checkDataState(true);
						enable(true);
						checkCollectState();
					}
				} catch (JSONException e) {
					checkDataState(false);
					e.printStackTrace();
				}
			}
		},true);
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
		titleGridView = (MyGridView)view.findViewById(R.id.lottery_title_gridview);
		titleGrildAdapter = new TitleGrildAdapter(this,lottery_titleList);
		titleGridView.setAdapter(titleGrildAdapter);
		titleGrildAdapter.notifyDataSetChanged();
		titleGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				if(position < lottery_title.length-2){
					requestSelectLottery(lottery_titleList.get(position).getLottery_id());
					Message msg = new Message();
					msg.what = 1;
					recomdHandler.sendMessageDelayed(msg,500);
					requstType = position+1;
				}
			}
		});
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

	private void enable(boolean enable) {
		if (enable) {
			loading.setVisibility(View.GONE);
		} else {
			loading.setVisibility(View.VISIBLE);
		}
	}

	public class CollectListAdapter extends BaseAdapter {
		private List<CollectBean> 	mList;
		private LayoutInflater mInflater;
		private Context context;
		private LayoutInflater 		inflater;
		private ViewHolder 			viewholder;
		private boolean             mBusy;

		// 构造方法
		public CollectListAdapter(Context context, List<CollectBean> data) {
			mList = data;
			this.context = context;
			mInflater = LayoutInflater.from(context);
		}

		public boolean ismBusy() {
			return mBusy;
		}

		public void setmBusy(boolean mBusy) {
			this.mBusy = mBusy;
		}

		//添加收藏
		public void requestAddURL(String user_id, String plan_id ,String s_id) {
			OKHttpManager okHttpManager = new OKHttpManager();
			okHttpManager.setToken(UserMessage.getInstance().getToken());
			Map<String,String> map = new HashMap<>();
			map.put("user_id",user_id);
			map.put("plan_id",plan_id);
			map.put("s_id",s_id);
			map.put("os_type","1");
			okHttpManager.post(Constants.API + Constants.PLAN_FAVORITE, map, new OnNetRequestCallback() {
				@Override
				public void onFailed(String reason) {
					ToastUtil.getShortToastByString(context,"收藏失败");
				}

				@Override
				public void onSuccess(String response) {
					ToastUtil.getShortToastByString(context,"收藏成功");
				}
			},true);
		}

		//删除收藏
		public void requestDeleteURL(String user_id,String log_id){
			OKHttpManager okHttpManager = new OKHttpManager();
			okHttpManager.setToken(UserMessage.getInstance().getToken());
			Map<String,String> map = new HashMap<>();
			map.put("user_id",user_id);
			map.put("log_id",log_id);
			map.put("os_type","1");
			okHttpManager.post(Constants.API+Constants.DELETE_PLAN, map, new OnNetRequestCallback() {
				@Override
				public void onFailed(String reason) {
					ToastUtil.getShortToastByString(context,"取消收藏失败");
				}

				@Override
				public void onSuccess(String response) {
					ToastUtil.getShortToastByString(context,"取消收藏成功");
				}
			},true);
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent)
		{
			if (convertView == null) {
				viewholder = new ViewHolder();
				convertView 			= mInflater.inflate(R.layout.program_collect_list_item, null,false);
				viewholder.plan_name 	= (TextView) convertView.findViewById(R.id.program_collect_plan_name);
				viewholder.scheme_name 	= (TextView) convertView.findViewById(R.id.program_collect_scheme_name);
				viewholder.DelectStar 	= (CheckBox)convertView.findViewById(R.id.program_collect_list_item_star);
				viewholder.program_collect_layout 	= (RelativeLayout) convertView.findViewById(R.id.program_collect_layout);
				viewholder.DelectStar.setTag(position);
				viewholder.program_collect_layout.setTag(position);
				convertView.setTag(viewholder);
			} else {
				viewholder = (ViewHolder) convertView.getTag();
			}
			if(!mBusy) {
				viewholder.scheme_name.setText(mList.get(position).getScheme_name()+mList.get(position).getPlan_name().substring(0,2));
				if(mList.get(position).getLottery_id().equals("1")){
					viewholder.plan_name.setText("(重庆时时彩"+mList.get(position).getPlan_name()+")");
				}else if(mList.get(position).getLottery_id().equals("3")){
					viewholder.plan_name.setText("(天津时时彩"+mList.get(position).getPlan_name()+")");
				}else if(mList.get(position).getLottery_id().equals("7")){
					viewholder.plan_name.setText("(新疆时时彩"+mList.get(position).getPlan_name()+")");
				}else if(mList.get(position).getLottery_id().equals("27")){
					viewholder.plan_name.setText("(北京PK10"+mList.get(position).getPlan_name()+")");
				}else if(mList.get(position).getLottery_id().equals("9")){
					viewholder.plan_name.setText("(广东11选5"+mList.get(position).getPlan_name()+")");
				}else if(mList.get(position).getLottery_id().equals("22")){
					viewholder.plan_name.setText("(上海11选5"+mList.get(position).getPlan_name()+")");
				}else if(mList.get(position).getLottery_id().equals("10")){
					viewholder.plan_name.setText("(山东11选5"+mList.get(position).getPlan_name()+")");
				}else if(mList.get(position).getLottery_id().equals("23")){
					viewholder.plan_name.setText("(江苏快3"+mList.get(position).getPlan_name()+")");
				}
				if(mList.get(position).getIsCollected()){
					viewholder.DelectStar.setChecked(true);
				}else{
					viewholder.DelectStar.setChecked(false);
				}
		/*	viewholder.program_collect_layout.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mList.get(position).getIsCollected()) {
						mList.get(position).setCollected(false);
						viewholder.DelectStar.setChecked(false);
						requestDeleteURL(UserMessage.getInstance().getUser_id(),mList.get(position).getLog_id());
					}else{
						viewholder.DelectStar.setChecked(true);
						mList.get(position).setCollected(true);
						requestAddURL(UserMessage.getInstance().getUser_id(), mList.get(position).getLottery_id(),mList.get(position).getPlan_id(),mList.get(position).getS_id());
					}
				}
			});*/
				viewholder.DelectStar.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						if (mList.get(position).getIsCollected()) {
							mList.get(position).setCollected(false);
							ShowMessageDialog(position);

						}else{
							mList.get(position).setCollected(true);
						}
					}
				});
			}
			return convertView;
		}

		public void ShowMessageDialog(final int position){
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle("系统提示")//设置对话框标题
					.setMessage("确定要取消收藏吗?")//设置显示的内容
					.setPositiveButton("确定",new DialogInterface.OnClickListener() {//添加确定按钮
						@Override
						public void onClick(DialogInterface dialog, int which) {//确定按钮的响应事件
							requestDeleteURL(UserMessage.getInstance().getUser_id(),mList.get(position).getLog_id());
							requestCollection();
						}
					}).setNegativeButton("取消",new DialogInterface.OnClickListener() {//添加返回按钮
				@Override
				public void onClick(DialogInterface dialog, int which) {//响应事件
					mList.get(position).setCollected(true);
					CollectListAdapter.super.notifyDataSetChanged();
				}
			});
			//在按键响应事件中显示此对话框;//在按键响应事件中显示此对话框
			AlertDialog alertDialog = builder.create();
			alertDialog.setCancelable(false);
			alertDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
				@Override
				public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event)
				{
					if (keyCode == KeyEvent.KEYCODE_SEARCH) {
						return true;
					}
					else {
						return false; //默认返回 false
					}
				}
			});
			alertDialog.show();
		}


		public class ViewHolder {
			public TextView plan_name,scheme_name;
			public CheckBox DelectStar;
			public RelativeLayout program_collect_layout;
		}

	}
}
