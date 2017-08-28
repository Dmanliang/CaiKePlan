package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.IssueTitleMessage;
import com.example.caikeplan.logic.message.PlanBaseMessage;

import java.util.List;

/**
 * Created by dell on 2017/6/5.
 */

public class IssueTitleAdapter extends BaseAdapter {
    private Context context;
    private List<IssueTitleMessage> list;
    private IssueTitleViewHolder viewholder;
    private LayoutInflater mInflater;
    public IssueTitleAdapter(Context context, List<IssueTitleMessage> list){
        this.context = context;
        this.list    = list;
        mInflater    =   LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder = null;
        if(convertView == null){
            viewholder                  =   new IssueTitleViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_issues,parent,false);
            viewholder.item_issues      =   (TextView)convertView.findViewById(R.id.issues_title);
            viewholder.issue_check      =   (ImageView)convertView.findViewById(R.id.issue_check);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(IssueTitleViewHolder) convertView.getTag();
        }
        if(!list.get(position).isFocus()){
            viewholder.item_issues.setTextColor(context.getResources().getColor(R.color.blackText));
            viewholder.issue_check.setImageResource(0);
        }else{
            viewholder.item_issues.setTextColor(context.getResources().getColor(R.color.playBlue2));
            viewholder.issue_check.setImageResource(R.drawable.icon_check);
        }
        viewholder.item_issues.setText(list.get(position).getIssueTitle());
        return convertView;
    }

    public class IssueTitleViewHolder{
        private TextView    item_issues;
        private ImageView   issue_check;
    }
}
