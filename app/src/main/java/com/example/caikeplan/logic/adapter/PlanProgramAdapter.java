package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PlanMessage;

import java.util.List;

/**
 * Created by dell on 2017/5/19.
 */

public class PlanProgramAdapter extends BaseAdapter{

    private Context             context;
    private List<PlanMessage>   mList;
    private LayoutInflater      mInflater;
    private boolean             mBusy= false;

    public PlanProgramAdapter(Context context, List<PlanMessage> data) {
        mList 		    = 	data;
        mInflater 	    = 	LayoutInflater.from(context);
        this.context    =   context;
    }

    public boolean ismBusy() {
        return mBusy;
    }

    public void setmBusy(boolean mBusy) {
        this.mBusy = mBusy;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        final PlanProgramViewHolder viewholder;
        if (convertView == null) {
            viewholder 					        = 	new PlanProgramViewHolder();
            convertView 				        = 	mInflater.inflate(R.layout.item_program, parent,false);
            viewholder.ranges			        =	(TextView) convertView.findViewById(R.id.program_ranges);
            viewholder.program_plans 	        = 	(TextView) convertView.findViewById(R.id.program_plans);
            viewholder.program_nums 	        = 	(TextView) convertView.findViewById(R.id.program_nums);
            viewholder.issue 			        = 	(TextView) convertView.findViewById(R.id.program_issue);
         //   viewholder.nums 			        = 	(TextView) convertView.findViewById(R.id.nums);
            viewholder.hit 				        = 	(TextView) convertView.findViewById(R.id.program_results);
            viewholder.program_results_image    =   (ImageView) convertView.findViewById(R.id.program_results_image);
         //   viewholder.plans_numLayout          =   (ScrollView)convertView.findViewById(R.id.plans_numLayout);
            viewholder.programLayout            =   (RelativeLayout)convertView.findViewById(R.id.programLayout);
            viewholder.openNum                  =   (RelativeLayout)convertView.findViewById(R.id.openNum);
            viewholder.nums_results             =   (RelativeLayout)convertView.findViewById(R.id.nums_results);
            convertView.setTag(viewholder);
        }
        else {
            viewholder  = 	(PlanProgramViewHolder) convertView.getTag();
        }
        if(!mBusy) {
           /* if (position == 0) {
                viewholder.plans_numLayout.setVisibility(View.VISIBLE);
                viewholder.nums.setText(mList.get(0).getPlan_num());
            } else {
                viewholder.plans_numLayout.setVisibility(View.GONE);
            }*/
            if (position % 2 == 0) {//设置每个子项的背景颜色
                viewholder.openNum.setBackgroundResource(R.drawable.shape_while_item);
                viewholder.ranges.setBackgroundResource(R.drawable.shape_while_item);
                viewholder.program_plans.setBackgroundResource(R.drawable.shape_while_item);
                viewholder.nums_results.setBackgroundResource(R.drawable.shape_while_item);
            } else {
                viewholder.openNum.setBackgroundResource(R.drawable.shape_shadow);
                viewholder.ranges.setBackgroundResource(R.drawable.shape_shadow);
                viewholder.program_plans.setBackgroundResource(R.drawable.shape_shadow);
                viewholder.nums_results.setBackgroundResource(R.drawable.shape_shadow);
            }
            if (position == 0) {
                viewholder.program_plans.setTextColor(context.getResources().getColor(R.color.blueBack));
                viewholder.program_plans.setText(mList.get(position).getNums_type() + "(点击复制)");
                viewholder.hit.setTextColor(context.getResources().getColor(R.color.blueBack));
                viewholder.program_nums.setTextColor(context.getResources().getColor(R.color.blueBack));
                viewholder.issue.setTextColor(context.getResources().getColor(R.color.blueBack));
                viewholder.ranges.setTextColor(context.getResources().getColor(R.color.blueBack));
            } else {
                viewholder.program_plans.setTextColor(context.getResources().getColor(R.color.redText));
                viewholder.program_plans.setText(mList.get(position).getNums_type() + "(点击查看)");
                viewholder.program_nums.setTextColor(context.getResources().getColor(R.color.balckText));
                viewholder.issue.setTextColor(context.getResources().getColor(R.color.balckText));
                viewholder.ranges.setTextColor(context.getResources().getColor(R.color.balckText));
            }
            //按照准确率匹配图片
            if (mList.get(position).getHit().equals("未中")) {
                viewholder.program_results_image.setImageResource(R.drawable.icon_wrong);
                viewholder.hit.setText("");
            } else if (mList.get(position).getHit().equals("中")) {
                viewholder.program_results_image.setImageResource(R.drawable.icon_right);
                viewholder.hit.setText("");
            } else {
                viewholder.program_results_image.setImageResource(0);
                viewholder.hit.setBackgroundResource(0);
                viewholder.hit.setText("进行中");
            }
            viewholder.program_nums.setText(mList.get(position).getNums());
            viewholder.issue.setText(mList.get(position).getIssue());
            viewholder.program_plans.setText(mList.get(position).getNums_type() + "(点击查看)");
            viewholder.ranges.setText(mList.get(position).getRanges());
        }
        return convertView;
    }

    class PlanProgramViewHolder {
        public TextView         program_nums, issue, program_plans,ranges,hit;
        public ImageView        program_results_image;
     //   public ScrollView       plans_numLayout;
        public RelativeLayout   programLayout;
        public RelativeLayout   openNum;
        public RelativeLayout   nums_results;
    }
}
