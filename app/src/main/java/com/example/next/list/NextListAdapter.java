package com.example.next.list;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.caikeplan.R;
import com.example.caikeplan.R.drawable;
import com.example.caikeplan.logic.message.SendMessage;

public class NextListAdapter extends BaseAdapter{

	private List<NextListBean> 	mList;
	private LayoutInflater 		mInflater;
	private String 				num ;
	private String 				issue;
	private String 				plan_num;
	private String 				hit;
	private int					pos;
	// 构造方法
	public NextListAdapter(Context context, List<NextListBean> data, int pos) {
		mList 		= 	data;
		mInflater 	= 	LayoutInflater.from(context);
		this.pos	=	pos;
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

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// viewholder方式
		ViewHolder viewholder;
		if (convertView == null) {
			//加载子布局
			viewholder 					= 	new ViewHolder();
			convertView 				= 	mInflater.inflate(R.layout.program_next_result_list_item, null);
			viewholder.relativeResult	=	(RelativeLayout)convertView.findViewById(R.id.relativeResult);
			// 对viewholder元素进行初始化
			viewholder.nums 			= 	(TextView) convertView.findViewById(R.id.program_next_result_list_nums);
			viewholder.issue 			= 	(TextView) convertView.findViewById(R.id.program_next_result_list_result_issue);
			viewholder.plan_num 		= 	(TextView) convertView.findViewById(R.id.program_next_result_list_plan_id);
			viewholder.hit 				= 	(ImageView) convertView.findViewById(R.id.program_next_result_list_result_hit_image);
			viewholder.ranges			=	(TextView)convertView.findViewById(R.id.program_next_result_list_ranges);
			convertView.setTag(viewholder);
		}
		else {
			viewholder 					= 	(ViewHolder) convertView.getTag();
		}
		if(position%2==0){//设置每个子项的背景颜色
			viewholder.relativeResult.setBackgroundColor(ContextCompat.getColor(convertView.getContext(),R.color.whileItem));
		}else{
			viewholder.relativeResult.setBackgroundColor(ContextCompat.getColor(convertView.getContext(),R.color.whileText));
		}
		if(this.pos == 3){
			if(mList.get(position).plan_num.equals("0")){
				plan_num	=	"小";
			}else if(mList.get(position).plan_num.equals("1")){
				plan_num	=	"大";
			}
		}else if(this.pos == 4){
			if(mList.get(position).plan_num.equals("1")){
				plan_num	=	"单";
			}else if(mList.get(position).plan_num.equals("2")){
				plan_num	=	"双";
			}
		}else if(this.pos == 5){
			plan_num	=	orderPlanNum(mList.get(position).plan_num,this.pos);
		}else{
			plan_num	=	orderPlanNum(mList.get(position).plan_num,this.pos);
		}
		num 		= 	mList.get(position).nums.replaceAll(",","");
		issue 		= 	mList.get(position).issue.substring(mList.get(position).issue.length() - 3,mList.get(position).issue.length()) + "期";
		hit 		=	mList.get(position).hit.replaceAll("-1", "未开奖").replaceAll("1", "对").replaceAll("0", "错");
		viewholder.ranges.setText(mList.get(position).ranges);
		viewholder.nums.setText(num);
		viewholder.issue.setText(issue);
		viewholder.plan_num.setText(plan_num);
		//按照准确率匹配图片
		if(mList.get(position).hit.equals("0"))
		{
			viewholder.hit.setImageResource(drawable.next_list_no);
		}
		else if(mList.get(position).hit.equals("-1"))
		{
			viewholder.hit.setImageDrawable(null);
		}
		else if(mList.get(position).hit.equals("1"))
		{
			viewholder.hit.setImageResource(drawable.next_list_yes);
		}
		return convertView;
	}


	public String orderPlanNum(String plan_n,int pos){
		String[]   planNums = plan_n.split(",");
		Integer[]  nums 	= new Integer[planNums.length];
		for(int i=0;i<planNums.length;i++){
			nums[i] = Integer.valueOf(planNums[i]);
		}
		int temp;
		for(int i=0;i<nums.length-1;i++){
			for(int j=0;j<nums.length-i-1;j++){
				if(nums[j+1]<nums[j]){
					temp = nums[j];
					nums[j]=nums[j+1];
					nums[j+1]=temp;
				}
			}
		}
		plan_n ="";
		if(pos == 5){
			for(int i=0;i<nums.length;i++){
				if(i !=nums.length-1){
					plan_n += Integer.toString(nums[i])+",";
				}else{
					plan_n += Integer.toString(nums[i]);
				}

			}
		}else{
			for(int i=0;i<nums.length;i++){
				plan_n += Integer.toString(nums[i]);
			}
		}

		return plan_n;
	}

	class ViewHolder {
		public TextView nums, issue, plan_num, s_id,ranges;
		public RelativeLayout relativeResult;
		public ImageView hit;
	}

}
