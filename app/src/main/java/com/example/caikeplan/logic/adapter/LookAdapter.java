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
 * Created by dell on 2017/5/18.
 */

public class LookAdapter extends BaseAdapter{

    private Context context;
    private List<PlanBaseMessage> list;
    private GridLookViewHolder viewholder;
    private LayoutInflater mInflater;
    public LookAdapter(Context context, List<PlanBaseMessage> list){
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
            viewholder                  =   new GridLookViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_lookthrough,parent,false);
            viewholder.itemLook         =   (TextView)convertView.findViewById(R.id.item_look);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(GridLookViewHolder) convertView.getTag();
        }
        viewholder.itemLook.setText(list.get(position).getScheme_name()+list.get(position).getPlan_name().substring(0,2));
        return convertView;
    }

    public class GridLookViewHolder{
        private TextView    itemLook;
    }

}
