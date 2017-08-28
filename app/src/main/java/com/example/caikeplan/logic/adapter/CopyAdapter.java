package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PlanBaseMessage;

import java.util.List;

/**
 * Created by dell on 2017/5/22.
 */

public class CopyAdapter extends BaseAdapter {
    private Context context;
    private List<PlanBaseMessage> list;
    private CopyGridViewHolder viewholder;
    private LayoutInflater mInflater;

    public CopyAdapter(Context context, List<PlanBaseMessage> list){
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
            viewholder              =   new CopyGridViewHolder();
            convertView             =   mInflater.inflate(R.layout.item_copy,parent,false);
            viewholder.item_copy    =   (TextView)convertView.findViewById(R.id.item_copy);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(CopyGridViewHolder) convertView.getTag();
        }
        viewholder.item_copy.setText(list.get(position).getScheme_name());
        return convertView;
    }

    public class CopyGridViewHolder{
        private TextView    item_copy;
    }
}
