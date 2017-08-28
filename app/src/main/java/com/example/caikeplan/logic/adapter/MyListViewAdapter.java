package com.example.caikeplan.logic.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.CaiZhongMessage;

import java.util.List;

/**
 * Created by dell on 2017/3/3.
 */

public class MyListViewAdapter extends BaseAdapter{

    private List<CaiZhongMessage> datas;
    private Context context;
    private LayoutInflater inflater;
    private ListViewOnListener listViewOnListener;
    public  CaiZhongViewHolder holder;
    //彩球播放开奖动画
    private String[] strs=new String[]{"1", "2", "3", "4","5","6","7","8","9","0"};
    private int      curStr1,curStr2,curStr3,curStr4,curStr5;
    private TextView textball1,textball2,textball3,textball4,textball5;
    //小球开奖线程
    private Handler ballHandler;
    private Runnable ballRunnable=new Runnable() {
        @Override
        public void run() {
            Message message=new Message();
            ballHandler.sendMessage(message);
        }
    };

    public MyListViewAdapter(Context context, List<CaiZhongMessage> datas) {
        this.context=context;
        this.datas=datas;
        inflater=LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final int pos=position;
        if(convertView==null)
        {
            holder=new CaiZhongViewHolder();
            convertView=inflater.inflate(R.layout.item_caizhong_open,parent,false);
            initHolder(holder,convertView);
           // setBallRun(holder);
            convertView.setTag(holder);
        }else{
            holder=(CaiZhongViewHolder)convertView.getTag();
        }
        setBallData(holder,position);
        holder.home_title.setText(datas.get(position).getHome_title());
        holder.home_date_end.setText(datas.get(position).getHome_date_end());
        holder.home_date.setText(datas.get(position).getHome_date());
        holder.home_timer.setText(datas.get(position).getHome_timer());
        holder.item_arrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listViewOnListener!=null){
                    listViewOnListener.onItemClick(pos,holder);
                }
            }
        });
        holder.relativeArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listViewOnListener!=null){
                    listViewOnListener.onItemClick(pos,holder);
                }
            }
        });
       /* ballHandler=new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                DataNext(holder);
                ballHandler.postDelayed(ballRunnable,400);
            }
        };*/
        return convertView;
    }

    //初始化各选项控件
    public void initHolder(CaiZhongViewHolder holder,View itemView)
    {
        holder.home_title=(TextView)itemView.findViewById(R.id.home_title);
        holder.home_date_end=(TextView)itemView.findViewById(R.id.home_date_end);
        holder.ball_1=(TextView)itemView.findViewById(R.id.ball_1);
        holder.ball_2=(TextView)itemView.findViewById(R.id.ball_2);
        holder.ball_3=(TextView)itemView.findViewById(R.id.ball_3);
        holder.ball_4=(TextView)itemView.findViewById(R.id.ball_4);
        holder.ball_5=(TextView)itemView.findViewById(R.id.ball_5);
        holder.home_date=(TextView)itemView.findViewById(R.id.home_date);
        holder.home_timer=(TextView)itemView.findViewById(R.id.home_timer);
        holder.item_arrow=(ImageButton)itemView.findViewById(R.id.item_arrow);
        holder.relativeArrow=(RelativeLayout)itemView.findViewById(R.id.relativeArrow);
        holder.home_data_open=(ImageView)itemView.findViewById(R.id.home_data_open);
    }

   /* //设置小球控件
    public void setBallRun(CaiZhongViewHolder holder){
        holder.ball_1.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textball1=setTextBall();
                return textball1;
            }
        });
        holder.ball_2.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textball2=setTextBall();
                return textball2;
            }
        });
        holder.ball_3.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textball3=setTextBall();
                return textball3;
            }
        });
        holder.ball_4.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textball4=setTextBall();
                return textball4;
            }
        });
        holder.ball_5.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textball5=setTextBall();
                return textball5;
            }
        });

    }*/

    public void setBallData(CaiZhongViewHolder holder,int position){
        holder.ball_1.setText(datas.get(position).getBall1());
        holder.ball_2.setText(datas.get(position).getBall2());
        holder.ball_3.setText(datas.get(position).getBall3());
        holder.ball_4.setText(datas.get(position).getBall4());
        holder.ball_5.setText(datas.get(position).getBall5());
    }

    /*public TextView setTextBall(){
        TextView textball=new TextView(context);
        textball.setTextColor(Color.WHITE);
        textball.setTextSize(24);
        textball.setSingleLine();
        textball.setEllipsize(TextUtils.TruncateAt.END);
        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.gravity = Gravity.CENTER;
        textball.setLayoutParams(lp);
        return textball;
    }

    public void DataNext(CaiZhongViewHolder holder){
        holder.ball_1.setText(strs[curStr1++%strs.length]);
        holder.ball_2.setText(strs[curStr2++%strs.length]);
        holder.ball_3.setText(strs[curStr3++%strs.length]);
        holder.ball_4.setText(strs[curStr4++%strs.length]);
        holder.ball_5.setText(strs[curStr5++%strs.length]);
    }*/

    public void setOnItemListener(ListViewOnListener listViewOnListener){
        this.listViewOnListener=listViewOnListener;
    }

    public class CaiZhongViewHolder{
        public TextView     home_title;
        public TextView     home_date_end;
        public TextView     ball_1,ball_2,ball_3,ball_4,ball_5;
        public TextView     home_date;
        public TextView     home_timer;
        public ImageView    home_data_open;;
        public ImageButton  item_arrow;
        public RelativeLayout relativeArrow;
    }

    public interface ListViewOnListener{
        void onItemClick(int position, CaiZhongViewHolder holder);
    }


}
