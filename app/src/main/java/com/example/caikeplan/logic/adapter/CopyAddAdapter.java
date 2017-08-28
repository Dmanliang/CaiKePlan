package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PlanBaseMessage;

import java.util.List;

/**
 * Created by dell on 2017/5/22.
 */

public class CopyAddAdapter extends BaseAdapter {

    private Context context;
    private List<PlanBaseMessage>   list;
    private AddGridViewHolder       viewholder;
    private LayoutInflater          mInflater;
    private CloseClickListener      closeClickListener;
    public CopyAddAdapter(Context context, List<PlanBaseMessage> list){
        this.context = context;
        this.list    = list;
        mInflater    = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        viewholder = null;
        if(convertView == null){
            viewholder                  =   new AddGridViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_addcopy,parent,false);
            viewholder.item_add_copy    =   (TextView)convertView.findViewById(R.id.item_add_copy);
            viewholder.item_close       =   (ImageView)convertView.findViewById(R.id.item_close);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(AddGridViewHolder) convertView.getTag();
        }
        viewholder.item_add_copy.setText(list.get(position).getScheme_name());
        viewholder.item_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeClickListener.CloseClick(viewholder,position);
            }
        });
        return convertView;
    }

    public void setColseClick(CloseClickListener closeClickListener){
        this.closeClickListener =   closeClickListener;
    }

    public interface CloseClickListener{
        public void CloseClick(AddGridViewHolder viewHolder,int position);
    }

    public class AddGridViewHolder{
        private TextView    item_add_copy;
        private ImageView   item_close;
    }
}
