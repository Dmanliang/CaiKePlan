package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PlanBaseMessage;

import java.util.List;

/**
 * Created by dell on 2017/8/14.
 */

public class RankAdapter extends BaseAdapter {

    private Context                 context;
    private List<PlanBaseMessage>   list;
    private RankViewHolder          viewholder;
    private LayoutInflater          mInflater;
    private boolean                 mBusy;

    public RankAdapter(Context context, List<PlanBaseMessage> list){
        this.context = context;
        this.list    = list;
        mInflater    =   LayoutInflater.from(context);
    }

    public boolean ismBusy() {
        return mBusy;
    }

    public void setmBusy(boolean mBusy) {
        this.mBusy = mBusy;
    }

    @Override
    public int getCount() {
        return list.size()-3;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position+3);
    }

    @Override
    public long getItemId(int position) {
        return position+3;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder = null;
        if(convertView == null){
            viewholder                  =   new RankViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_rank,parent,false);
            viewholder.item_name        =   (TextView)convertView.findViewById(R.id.item_plan);
            viewholder.item_id          =   (TextView)convertView.findViewById(R.id.item_num);
            viewholder.item_hotcount    =   (TextView)convertView.findViewById(R.id.item_hot_count);
            viewholder.item_rank_layout =   (RelativeLayout) convertView.findViewById(R.id.item_rank_layout);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(RankViewHolder) convertView.getTag();
        }
        if(!mBusy) {
            if (position % 2 == 0) {//设置每个子项的背景颜色
                viewholder.item_rank_layout.setBackgroundResource(R.color.pinkback);
            } else {
                viewholder.item_rank_layout.setBackgroundResource(R.color.whileText);
            }
            viewholder.item_name.setText(list.get(position+3).getScheme_name()+list.get(position+3).getPlan_name().substring(0,2));
            viewholder.item_id.setText(list.get(position+3).getId());
            viewholder.item_hotcount.setText(list.get(position+3).getCount());
        }
        return convertView;
    }

    public class RankViewHolder{
        private TextView        item_name;
        private TextView        item_id;
        private TextView        item_hotcount;
        private RelativeLayout  item_rank_layout;
    }
}
