package com.example.collect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.base.Constants;
import com.example.caikeplan.R;
import com.example.caikeplan.activity.PlanProgram;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.caikeplan.logic.message.UserMessage;
import com.example.getJson.HttpTask;
import com.example.util.OKHttpManager;
import com.example.util.OnNetRequestCallback;

public class CollectListAdapter extends BaseAdapter{
	private List<CollectBean> 	mList;
	private LayoutInflater 		mInflater;
	private Context 			context;
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
						requestDeleteURL(UserMessage.getInstance().getUser_id(),mList.get(position).getLog_id());
					}else{
						mList.get(position).setCollected(true);
						requestAddURL(UserMessage.getInstance().getUser_id(),mList.get(position).getPlan_id(),mList.get(position).getS_id());
					}
				}
			});
		}
		return convertView;
	}

	class ViewHolder {
		public TextView plan_name,scheme_name;
		public CheckBox DelectStar;
		public RelativeLayout program_collect_layout;
	}

}
