package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PlanBaseMessage;

import java.util.List;

/**
 * Created by dell on 2017/5/19.
 */

public class HotAdapter extends BaseAdapter {
    private Context context;
    private List<PlanBaseMessage> list;
    private GridHotViewHolder viewholder;
    private LayoutInflater mInflater;
    public HotAdapter(Context context, List<PlanBaseMessage> list){
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
            viewholder                  =   new GridHotViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_hot,parent,false);
            viewholder.itemhot         =   (TextView)convertView.findViewById(R.id.item_hot);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(GridHotViewHolder) convertView.getTag();
        }
        viewholder.itemhot.setText(list.get(position).getScheme_name()+list.get(position).getPlan_name().substring(0,2));
        return convertView;
    }

    public class GridHotViewHolder{
        private TextView    itemhot;
    }
}
