package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.NoticeMessage;
import com.example.caikeplan.logic.message.PlanBaseMessage;

import java.util.List;

/**
 * Created by dell on 2017/8/22.
 */

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<NoticeMessage> list;
    private MessageViewHolder viewholder;
    private LayoutInflater mInflater;
    private boolean                 mBusy;

    public MessageAdapter(Context context, List<NoticeMessage> list){
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
            viewholder                  =   new MessageViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_message,parent,false);
            viewholder.message_content  =   (TextView)convertView.findViewById(R.id.message_content);
            viewholder.message_time     =   (TextView)convertView.findViewById(R.id.message_time);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(MessageViewHolder) convertView.getTag();
        }
        if(!mBusy){
            setData(position);
        }
        return convertView;
    }

    public void setData(int pos){
        if(list.get(pos).isRead()){
            viewholder.message_content.setTextColor(context.getResources().getColor(R.color.whileDivider));
            viewholder.message_time.setTextColor(context.getResources().getColor(R.color.whileDivider));
        }else{
            viewholder.message_content.setTextColor(context.getResources().getColor(R.color.blackText));
            viewholder.message_time.setTextColor(context.getResources().getColor(R.color.blackText));
        }
        viewholder.message_content.setText(list.get(pos).getName());
        viewholder.message_time.setText(list.get(pos).getCreated_at());
    }

    public class MessageViewHolder{
        private TextView    message_content,message_time;
    }
}
