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

import java.util.List;

/**
 * Created by Decml on 2017/3/18.
 */

public class LotteryTitleAdapter extends BaseAdapter {

    private Context             context;
    private List<LotteryTitle>  ltData;
    private LayoutInflater      mInflater;
    private TitleHolderView     viewholder;
    public LotteryTitleAdapter(Context context,List<LotteryTitle> ltData){
        this.context    =   context;
        this.ltData     =   ltData;
        mInflater       =   LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return ltData.size();
    }

    @Override
    public Object getItem(int i) {
        return ltData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        viewholder = null;
        if(view == null){
            viewholder                  =   new TitleHolderView();
            view                        =   mInflater.inflate(R.layout.item_lottery_listview,viewGroup,false);
            viewholder.lottery_title    =   (TextView)view.findViewById(R.id.lottery_title);
            viewholder.title_check      =   (ImageView)view.findViewById(R.id.title_checks);
            view.setTag(viewholder);
        }else{
            viewholder 					= 	(TitleHolderView) view.getTag();
        }
        viewholder.lottery_title.setText(ltData.get(i).getLottery_title());
        viewholder.lottery_title.setTextColor(context.getResources().getColor(R.color.blackText));

        if(!ltData.get(i).isFocus()){
            viewholder.lottery_title.setTextColor(context.getResources().getColor(R.color.blackText));
            viewholder.title_check.setBackgroundResource(0);
        }else{
            viewholder.lottery_title.setTextColor(context.getResources().getColor(R.color.redText));
            viewholder.title_check.setBackgroundResource(R.drawable.icon_check);
        }

        return view;
    }

    public class TitleHolderView {
        public  TextView     lottery_title;
        private ImageView    title_check;
    }
}
