package com.example.NextPage;

import java.text.ParseException;
import java.text.SimpleDateFormat;
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

import org.w3c.dom.Text;

public class NextPageAdapter extends BaseAdapter {

	public static final int     TYPE_COUNT = 3;
	public static final int     TYPE_ITEM1 = 1;
	public static final int     TYPE_ITEM2 = 2;
	public static final int     TYPE_ITEM3 = 3;
	private Context context;
	private List<NextPageBean> 	mList;
	private LayoutInflater 		mInflater;
	private int 				current	=	-1;
	SimpleDateFormat 			format1 =  	new SimpleDateFormat("yyyyMMddHHmmss");
	SimpleDateFormat 			format2 =  	new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private ViewHolder 			viewholder = null;
	private PKViewHolder		pkViewHolder = null;
	private K3ViewHolder		k3ViewHolder = null;
	private int                 currentType;
	private View                itemview1,itemview2,itemview3;
	private int[]               nums={R.drawable.type2_ball_1,R.drawable.type2_ball_2,R.drawable.type2_ball_3,R.drawable.type2_ball_4,
								R.drawable.type2_ball_5,R.drawable.type2_ball_6, R.drawable.type2_ball_7,R.drawable.type2_ball_8,R.drawable.type2_ball_9,R.drawable.type2_ball_10};
	private int[]               k3nums={R.drawable.type3_ball_1,R.drawable.type3_ball_2,R.drawable.type3_ball_3,R.drawable.type3_ball_4,R.drawable.type3_ball_5,R.drawable.type3_ball_6};
	// 构造方法
	public NextPageAdapter(Context context, List<NextPageBean> data) {
		mList = data;
		mInflater = LayoutInflater.from(context);
		this.context = context;
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
	public int getItemViewType(int position) {
		if("1".equals(mList.get(position).getType())){
			return TYPE_ITEM1;
		}else if("2".equals(mList.get(position).getType())){
			return TYPE_ITEM2;
		}else if("3".equals(mList.get(position).getType())){
			return TYPE_ITEM3;
		}else{
			return 100;
		}
	}

	@Override
	public int getViewTypeCount() {
		return TYPE_COUNT;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		currentType = getItemViewType(position);
		switch (currentType){
			case TYPE_ITEM1:
				if (convertView == null) {
					viewholder 	= new ViewHolder();
					itemview1 	= mInflater.inflate(R.layout.lottery_list_item,parent,false);
					setView1(itemview1);
					itemview1.setTag(viewholder);
					convertView = itemview1;
				} else {
					viewholder	= 	(ViewHolder) convertView.getTag();
				}
				setData1(position);
				break;
			case TYPE_ITEM2:
				if (convertView == null) {
					pkViewHolder 	= new PKViewHolder();
					itemview2 		= mInflater.inflate(R.layout.lottery_listpk10_item,parent,false);
					setView2(itemview2);
					itemview2.setTag(pkViewHolder);
					convertView 	= itemview2;
				} else {
					pkViewHolder	= (PKViewHolder) convertView.getTag();
				}
				setData2(position);
				break;
			case TYPE_ITEM3:
				if (convertView == null) {
					k3ViewHolder 	= new K3ViewHolder();
					itemview3 		= mInflater.inflate(R.layout.lottery_listk3_item,parent,false);
					setView3(itemview3);
					itemview3.setTag(k3ViewHolder);
					convertView 	= itemview3;
				} else {
					k3ViewHolder	= (K3ViewHolder) convertView.getTag();
				}
				setData3(position);
				break;
		}
		return convertView;
	}

	public void setView1(View view){
		viewholder.relativeItem		=	(RelativeLayout)view.findViewById(R.id.relativeItem);
		viewholder.relativeHide		=	(RelativeLayout)view.findViewById(R.id.relativeHide);
		viewholder.relativeDrop		=	(RelativeLayout)view.findViewById(R.id.relativeDrop);
		viewholder.up_arrow			=	(ImageView)view.findViewById(R.id.up_arrow);
		viewholder.num1 			= 	(TextView)view.findViewById(R.id.lottery_list_item_ball_1);
		viewholder.num2 			= 	(TextView)view.findViewById(R.id.lottery_list_item_ball_2);
		viewholder.num3 			= 	(TextView)view.findViewById(R.id.lottery_list_item_ball_3);
		viewholder.num4 			= 	(TextView)view.findViewById(R.id.lottery_list_item_ball_4);
		viewholder.num5 			= 	(TextView)view.findViewById(R.id.lottery_list_item_ball_5);
		viewholder.issue 			= 	(TextView)view.findViewById(R.id.lty_cq_nextpage_issue);
		viewholder.expect_time 		= 	(TextView)view.findViewById(R.id.lty_cq_nextpage_time);
		viewholder.startThree		=	(TextView)view.findViewById(R.id.startThree);
		viewholder.middleThree		=	(TextView)view.findViewById(R.id.middleThree);
		viewholder.endThree			=	(TextView)view.findViewById(R.id.endThree);
		viewholder.bigSingle		=	(TextView)view.findViewById(R.id.bigSingle);
	}

	public void setData1(final int position){
		viewholder.issue.setText("第"+mList.get(position).getIssue()+"期");
		if(current==position){
			viewholder.up_arrow.setBackgroundResource(R.drawable.drop_up);
			viewholder.relativeHide.setVisibility(View.VISIBLE);
		}else{
			viewholder.up_arrow.setBackgroundResource(R.drawable.drop_arrow);
			viewholder.relativeHide.setVisibility(View.GONE);
		}
		viewholder.relativeDrop.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int tag	=	position;
				if(tag	==	current){
					current	=	-1;
				}else {
					current = 	tag;
				}
				notifyDataSetChanged();
			}
		});
		try {
			viewholder.expect_time.setText(format2.format(format1.parse(mList.get(position).getExpect_time())));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		viewholder.num1.setText(mList.get(position).getNums().get(0));
		viewholder.num2.setText(mList.get(position).getNums().get(1));
		viewholder.num3.setText(mList.get(position).getNums().get(2));
		viewholder.num4.setText(mList.get(position).getNums().get(3));
		viewholder.num5.setText(mList.get(position).getNums().get(4));
		viewholder.startThree.setText(mList.get(position).getStartthree());
		viewholder.middleThree.setText(mList.get(position).getMiddlethree());
		viewholder.endThree.setText(mList.get(position).getEndthree());
		viewholder.bigSingle.setText(mList.get(position).getSingledouble());
	}

	public void setView2(View view){
		pkViewHolder.num1 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball1);
		pkViewHolder.num2 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball2);
		pkViewHolder.num3 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball3);
		pkViewHolder.num4 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball4);
		pkViewHolder.num5 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball5);
		pkViewHolder.num6 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball6);
		pkViewHolder.num7 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball7);
		pkViewHolder.num8 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball8);
		pkViewHolder.num9 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball9);
		pkViewHolder.num10 			= 	(ImageView)view.findViewById(R.id.lottery_list_type2_ball10);
		pkViewHolder.issue 			= 	(TextView)view.findViewById(R.id.lty_cq_nextpage_issue_pk10);
		pkViewHolder.expect_time 	= 	(TextView)view.findViewById(R.id.lty_cq_nextpage_time_pk10);
	}

	public void setData2(int position){
		pkViewHolder.issue.setText("第"+mList.get(position).getIssue()+"期");
		try {
			pkViewHolder.expect_time.setText(format2.format(format1.parse(mList.get(position).getExpect_time())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(mList.get(position).getNums().size()!=0){
			pkViewHolder.num1.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(0)) - 1]));
			pkViewHolder.num2.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(1)) - 1]));
			pkViewHolder.num3.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(2)) - 1]));
			pkViewHolder.num4.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(3)) - 1]));
			pkViewHolder.num5.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(4)) - 1]));
			pkViewHolder.num6.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(5)) - 1]));
			pkViewHolder.num7.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(6)) - 1]));
			pkViewHolder.num8.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(7)) - 1]));
			pkViewHolder.num9.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(8)) - 1]));
			pkViewHolder.num10.setBackground(ContextCompat.getDrawable(context, nums[Integer.parseInt(mList.get(position).getNums().get(9)) - 1]));
		}
	}

	public void setView3(View view){
		k3ViewHolder.expect_time 	= 	(TextView)view.findViewById(R.id.lty_cq_nextpage_time_k3);
		k3ViewHolder.issue 	     	= 	(TextView)view.findViewById(R.id.lty_cq_nextpage_issue_pk10);
		k3ViewHolder.num1 			= 	(ImageView)view.findViewById(R.id.lottery_list_type3_ball1);
		k3ViewHolder.num2 			= 	(ImageView)view.findViewById(R.id.lottery_list_type3_ball2);
		k3ViewHolder.num3 			= 	(ImageView)view.findViewById(R.id.lottery_list_type3_ball3);
	}

	public void setData3(int position){
		k3ViewHolder.issue.setText("第"+mList.get(position).getIssue()+"期");
		try {
			k3ViewHolder.expect_time.setText(format2.format(format1.parse(mList.get(position).getExpect_time())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		if(mList.get(position).getNums().size()!=0){
			k3ViewHolder.num1.setBackground(ContextCompat.getDrawable(context, k3nums[Integer.parseInt(mList.get(position).getNums().get(0)) - 1]));
			k3ViewHolder.num2.setBackground(ContextCompat.getDrawable(context, k3nums[Integer.parseInt(mList.get(position).getNums().get(1)) - 1]));
			k3ViewHolder.num3.setBackground(ContextCompat.getDrawable(context, k3nums[Integer.parseInt(mList.get(position).getNums().get(2)) - 1]));
		}
	}

	class ViewHolder {
		public TextView 		issue,expect_time,num1,num2,num3,num4,num5,startThree,middleThree,endThree,bigSingle;
		public ImageView 		up_arrow;
		public RelativeLayout 	relativeItem,relativeHide,relativeDrop;
	}

	class PKViewHolder{
		public TextView 		issue,expect_time;
		public ImageView		num1,num2,num3,num4,num5,num6,num7,num8,num9,num10;
	}

	class K3ViewHolder{
		public TextView 		issue,expect_time;
		public ImageView		num1,num2,num3;
	}

}
