package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.LotteryTitle;
import com.example.caikeplan.logic.message.PlanBaseMessage;

import java.util.List;

/**
 * Created by dell on 2017/8/31.
 */

public class TitleGrildAdapter extends BaseAdapter {

    private Context context;
    private List<LotteryTitle> list;
    private GridTitleViewHolder viewholder;
    private LayoutInflater mInflater;

    public TitleGrildAdapter(Context context, List<LotteryTitle> list){
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
            viewholder                  =   new GridTitleViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_lottery_choice,parent,false);
            viewholder.lottery_text     =   (TextView)convertView.findViewById(R.id.lottery_text);
            viewholder.lottery_icon     =   (ImageView)convertView.findViewById(R.id.lottery_icon);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(GridTitleViewHolder) convertView.getTag();
        }
        viewholder.lottery_text.setText(list.get(position).getLottery_title());
        viewholder.lottery_icon.setBackgroundResource(list.get(position).getRes_id());
        return convertView;
    }

    public class GridTitleViewHolder{
        public TextView    lottery_text;
        public ImageView   lottery_icon;
    }
}
