package com.example.mainlist;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.SendMessage;


public class MainListAdapter extends BaseAdapter {
    private List<MainListBean>  mList;
    private LayoutInflater      mInflater;
    private LayoutInflater      inflater;
    private Context             context;
    private ViewHolder          viewholder = null;

    // 构造方法
    public MainListAdapter(Context context, List<MainListBean> data) {
        this.context = context;
        mList = data;
        mInflater = LayoutInflater.from(context);
    }

    //添加收藏链接
    public String setAddURL(String user_id, String mac, int lottery_id, String play_id, String plan_id) {
        String url;
        url = SendMessage.getInstance().getBestURL() + "/plan/add_plan_favorite?" + "user_id=" + user_id + "&mac=" + mac + "&lottery_id=" + lottery_id
                + "&play_id=" + play_id + "&plan_id=" + plan_id;
        return url;
    }

    //删除收藏链接
    public String setDeleteURL(String user_id, String mac, String log_id) {
        String url;
        url = SendMessage.getInstance().getBestURL() + "/plan/del_plan_favorite?" + "user_id=" + user_id + "&mac=" + mac + "&log_id=" + log_id;
        return url;
    }

/*    //添加收藏
    public void addCollect(String user_id, String mac, int lottery_id, String play_id, String plan_id) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        // 第一个参数为xml文件中view的id，第二个参数为此view的父组件，可以为null，android会自动寻找它是否拥有父组件
        View view = inflater.inflate(R.layout.program_main_webview, null);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl(setAddURL(user_id, mac, lottery_id, play_id, plan_id));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return false;
            }
        });
    }*/

 /*   //删除收藏
    public void DeleteCollect(String user_id, String mac, String log_id) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.program_main_webview, null);
        WebView webView = (WebView) view.findViewById(R.id.webView);
        webView.loadUrl(setDeleteURL(user_id, mac, log_id));
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                view.loadUrl(url);
                return false;
            }
        });
    }
*/
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // viewholder方式
        if (convertView == null) {
            viewholder = new ViewHolder();
            convertView             = mInflater.inflate(R.layout.program_main_list_item, null);
            // 对viewholder元素进行初始化
            viewholder.name         = (TextView) convertView.findViewById(R.id.program_main_list_item_name);
            viewholder.plan_id      = (TextView) convertView.findViewById(R.id.program_main_list_item_id);
            viewholder.collect_star = (CheckBox) convertView.findViewById(R.id.program_main_list_collect);
            convertView.setTag(viewholder);
        } else {
            viewholder = (ViewHolder) convertView.getTag();
        }
        viewholder.name.setText(mList.get(position).getPlan_name().replace("胆", ""));
        viewholder.plan_id.setText(mList.get(position).getPlan_id());
        if(mList.get(position).getIsCollect().equals("1")){
            viewholder.collect_star.setChecked(true);
        }else{
            viewholder.collect_star.setChecked(false);
        }
        viewholder.collect_star.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (mList.get(position).getIsCollect().equals("1")) {
                    mList.get(position).setIsCollect("0");
                    //DeleteCollect(SendMessage.getInstance().getUser_id(), SendMessage.getInstance().getMac(),mList.get(position).getLog_id());
                    Toast.makeText(context.getApplicationContext(), "取消成功！", Toast.LENGTH_LONG).show();
                } else if(mList.get(position).getIsCollect().equals("0")){
                    mList.get(position).setIsCollect("1");
                    Toast.makeText(context.getApplicationContext(), "收藏成功！", Toast.LENGTH_LONG).show();
                    SendMessage.getInstance().setProgram_Plan_id(viewholder.plan_id.getText().toString());
                 //   addCollect(SendMessage.getInstance().getUser_id(), SendMessage.getInstance().getMac(), SendMessage.getInstance().getLotteryId(), mList.get(position).getPlay_id(), mList.get(position).getPlan_id());
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        public TextView name, issue_count, num_count, plan_id;
        public CheckBox collect_star;
        public Button btn;
    }

}
