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

public class PlanAdapter extends BaseAdapter{

    private Context         context;
    private List<PlanBaseMessage>    list;
    private GridViewHolder  viewholder;
    private LayoutInflater  mInflater;
    public PlanAdapter(Context context, List<PlanBaseMessage> list){
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
            viewholder                  =   new GridViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_plan,parent,false);
            viewholder.itemPlan         =   (TextView)convertView.findViewById(R.id.item_plan);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(GridViewHolder) convertView.getTag();
        }
        viewholder.itemPlan.setText(list.get(position).getScheme_name()+list.get(position).getPlan_name().substring(0,2));
        return convertView;
    }

    public class GridViewHolder{
        private TextView    itemPlan;
    }

}
