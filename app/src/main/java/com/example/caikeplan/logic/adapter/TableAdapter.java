package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.TableMessage;

import java.util.List;

/**
 * Created by dell on 2017/6/5.
 */

public class TableAdapter extends BaseAdapter {

    private Context context;
    private List<TableMessage> list;
    private TableViewHolder viewholder;
    private LayoutInflater mInflater;
    public TableAdapter(Context context, List<TableMessage> list){
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
            viewholder                  =   new TableViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_table,parent,false);
            viewholder.appear_count     =   (TextView)convertView.findViewById(R.id.appear_count);
            viewholder.dan_code         =   (TextView)convertView.findViewById(R.id.dan_code);
            viewholder.omit             =   (TextView)convertView.findViewById(R.id.omit);
            viewholder.cool_hot         =   (TextView)convertView.findViewById(R.id.cool_hot);
            viewholder.table_item_layout=   (LinearLayout)convertView.findViewById(R.id.table_item_layout);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(TableViewHolder) convertView.getTag();
        }
        if(position%2==0){//设置每个子项的背景颜色
            viewholder.table_item_layout.setBackgroundColor(ContextCompat.getColor(convertView.getContext(),R.color.whileText));
        }else{
            viewholder.table_item_layout.setBackgroundColor(ContextCompat.getColor(convertView.getContext(),R.color.blueback));
        }
        if(list.get(position).getCoolhot().equals("冷")){
            viewholder.cool_hot.setTextColor(context.getResources().getColor(R.color.coolGreen));
        }else  if(list.get(position).getCoolhot().equals("温")){
            viewholder.cool_hot.setTextColor(context.getResources().getColor(R.color.warmYellow));
        }else{
            viewholder.cool_hot.setTextColor(context.getResources().getColor(R.color.hotRed));
        }
        viewholder.appear_count.setText(list.get(position).getCount());
        viewholder.dan_code.setText(list.get(position).getDanma());
        viewholder.omit.setText(list.get(position).getOmit());
        viewholder.cool_hot.setText(list.get(position).getCoolhot());
        return convertView;
    }

    public class TableViewHolder{
        private TextView    appear_count,dan_code,omit,cool_hot;
        private LinearLayout table_item_layout;
    }
}
