package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PlanMessage;
import com.example.caikeplan.logic.message.SendMessage;

import java.util.List;

/**
 * Created by dell on 2017/5/26.
 */

public class CopyEditAdpater extends BaseAdapter {

    private Context context;
    private List<PlanMessage> mList;
    private LayoutInflater mInflater;
    private String issue;
    private String plans;
    private String nums;
    private String ranges;
    private String hit;
    private CoypEditViewHolder viewholder;

    // 构造方法
    public CopyEditAdpater(Context context, List<PlanMessage> data) {
        mList 		    = 	data;
        mInflater 	    = 	LayoutInflater.from(context);
        this.context    =   context;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            //加载子布局
            viewholder 					= 	new CoypEditViewHolder();
            convertView 				= 	mInflater.inflate(R.layout.item_copy_edit, parent,false);
            viewholder.copy_issue       =   (EditText)convertView.findViewById(R.id.copy_issue);
            viewholder.copy_plans       =   (EditText)convertView.findViewById(R.id.copy_plans);
            viewholder.copy_nums        =   (EditText)convertView.findViewById(R.id.copy_nums);
            viewholder.copy_results     =   (EditText)convertView.findViewById(R.id.copy_results);
            viewholder.copy_ranges      =   (EditText)convertView.findViewById(R.id.copy_ranges);
            convertView.setTag(viewholder);
        }
        else {
            viewholder 					= 	(CoypEditViewHolder) convertView.getTag();
        }
        hit     =	mList.get(position).getHit().replaceAll("-1", "进行中").replaceAll("1", "中").replaceAll("0", "未中");
        issue   =   mList.get(position).getIssue().substring(8,mList.get(position).getIssue().length())+"期";
        plans   =   mList.get(position).getPlan_num();
        nums    =   mList.get(position).getNums();
        ranges  =   mList.get(position).getRanges();
  //      SendMessage.getInstance().setSHUZU(ranges,plans,issue+" "+nums,"  "+hit,position);
        viewholder.copy_issue.setText(issue);
        viewholder.copy_plans.setText(plans);
        viewholder.copy_nums.setText(nums);
        viewholder.copy_ranges.setText(ranges);
        viewholder.copy_results.setText(hit);
        setFocusListener(position);
        return convertView;
    }

    public void setFocusListener(final int i){
        viewholder.copy_issue.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus) {
                    mList.get(i).setIssue(viewholder.copy_issue.getText().toString());
                } else {
                    mList.get(i).setIssue(viewholder.copy_issue.getText().toString());
                }
            }
        });
    }

    public class CoypEditViewHolder {
       public EditText copy_ranges,copy_plans,copy_nums,copy_issue,copy_results;
    }
}
