package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PlanBaseMessage;
import com.example.caikeplan.logic.message.PlayTitleMessage;

import java.util.List;

/**
 * Created by dell on 2017/6/5.
 */

public class PlayTitleAdapter extends BaseAdapter {
    private Context context;
    private List<PlayTitleMessage> list;
    private PlatTitleViewHolder viewholder;
    private LayoutInflater mInflater;
    public PlayTitleAdapter(Context context, List<PlayTitleMessage> list){
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
            viewholder                  =   new PlatTitleViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_data_play,parent,false);
            viewholder.item_plan         =   (TextView)convertView.findViewById(R.id.item_plan);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(PlatTitleViewHolder) convertView.getTag();
        }
        if(!list.get(position).isFocus()){
            viewholder.item_plan.setTextColor(context.getResources().getColor(R.color.blackText));
            viewholder.item_plan.setBackgroundResource(0);
        }else{
            viewholder.item_plan.setTextColor(context.getResources().getColor(R.color.playBlue));
            viewholder.item_plan.setBackgroundResource(R.drawable.play_check);
        }
        viewholder.item_plan.setText(list.get(position).getPlay_title());
        return convertView;
    }

    public class PlatTitleViewHolder{
        private TextView    item_plan;
    }
}
