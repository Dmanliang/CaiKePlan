package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.PersonMessage;
import java.util.List;

/**
 * Created by dell on 2017/8/17.
 */

public class ManagerAdpater extends BaseAdapter {

    private Context             context;
    private List<PersonMessage> list;
    private ManagerViewHolder  viewholder;
    private LayoutInflater mInflater;
    private boolean             mBusy;
    public ManagerAdpater(Context context, List<PersonMessage> list){
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
        return list.size()-1;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position+1);
    }

    @Override
    public long getItemId(int position) {
        return position+1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        viewholder = null;
        if(convertView == null){
            viewholder                  =   new ManagerViewHolder();
            convertView                 =   mInflater.inflate(R.layout.item_manager,parent,false);
            viewholder.person_name      =   (TextView)convertView.findViewById(R.id.person_name);
            viewholder.person_endtime   =   (TextView)convertView.findViewById(R.id.person_endtime);
            convertView.setTag(viewholder);
        }else{
            viewholder 					= 	(ManagerViewHolder) convertView.getTag();
        }
        if(!mBusy) {
            viewholder.person_name.setText(list.get(position+1).getUsername());
            viewholder.person_endtime.setText(list.get(position+1).getDue_time());
        }
        return convertView;
    }

    public class ManagerViewHolder{
        private TextView        person_name;
        private TextView        person_endtime;
    }
}
