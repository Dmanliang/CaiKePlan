package com.example.next.list;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.getJson.HttpTask;
import com.example.next.list.listview.ListView;
import com.example.next.list.listview.PullToRefreshLayout;

import android.app.Activity;
import android.content.ClipboardManager;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public class ProgramMainNext extends Activity implements OnClickListener {

	private ListView 				mListview;
	private List<NextListBean> 		newsBeanList;
	private String 					URL;
	private HorizontalScrollView 	scrollView;
	private RadioGroup 				radio_group;
	private int 					S_id;
	private int 					pos 		= 0;
	private int						position	= 0;
	private NextListAdapter 		adapter;
	private TextView 				accuracy, NoNum, ZhouQi, EndNum;
	private TextView 				title_accuray,title_plan,title;
	private PopupWindow 			windowItem;
	private TextView 				textArea;
	private View 					popupView,ItemView;
	private int  					countYes = 0,countNo = 0,countCyecles = 0;
	private String 					update_time;
	private SimpleDateFormat 		format  = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private String 					bestS_id,bestRate;
	private String[] 				state 	= {"以筠", "新竹", "绮露", "白竹", "飞兰", "曼雁", "雁露", "凝冬", "含灵", "初阳" };

	public void setURL(String plan_id, int s_id , int page_index, int page_size) {
		this.URL = "http://120.77.242.46:9550/plan/find_plan_result_list?" + "plan_id="
				+ plan_id + "&s_id=" + s_id +"&page_index="+page_index+"&page_size="+page_size;
	}

	public String getURL() {
		return this.URL;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.program_next);
		initView();
	}

	private void initView(){
		mListview 				= 	(ListView) findViewById(R.id.program_next_result_listview);
		scrollView 				= 	(HorizontalScrollView) findViewById(R.id.scrollview_next);
		radio_group				= 	(RadioGroup) findViewById(R.id.radioGroup_next);
		title_accuray			=	(TextView)findViewById(R.id.title_accuracy);
		title_plan				=	(TextView)findViewById(R.id.title_plan);
		Button back_btn 		= 	(Button)findViewById(R.id.program_next_back_btn);
		title 					= 	(TextView) findViewById(R.id.program_tilte_next);
		accuracy 				= 	(TextView) findViewById(R.id.program_next_accuracy);
		NoNum 					= 	(TextView) findViewById(R.id.program_next_No_num);
		ZhouQi 					= 	(TextView) findViewById(R.id.program_next_zhouqi_num);
		EndNum 					= 	(TextView) findViewById(R.id.program_next_end_program_num);
		Button copy_btn 		= 	(Button)findViewById(R.id.collect_copy11);
		title.setText(SendMessage.getInstance().getLotteryName() + SendMessage.getInstance().getNextPageName());
		Display d = getWindowManager().getDefaultDisplay();
		DisplayMetrics dm = new DisplayMetrics();
		d.getMetrics(dm);
		final int screenHalf = d.getWidth() / 2;
		//请求数据
		setURL(SendMessage.getInstance().getProgram_Plan_id(), 1, 1 , 29);
		requestData();
		//选项按钮监听
		radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				reSetData();
				position=radio_group.indexOfChild(radio_group.findViewById(checkedId));
				int scrollX = scrollView.getScrollX();
				RadioButton rb = (RadioButton) findViewById(checkedId);
				int left = rb.getRight();
				int leftScreen = left - scrollX;
				scrollView.smoothScrollBy((leftScreen - screenHalf), 0);
				S_id = radio_group.indexOfChild(radio_group.findViewById(checkedId))+1;
				setURL(SendMessage.getInstance().getProgram_Plan_id(), S_id, 1, 29);
				requestData();
			}
		});

		back_btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

		copy_btn.setOnClickListener( new OnClickListener() {
			public void onClick(View v) {
				showSelectPic(v);
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(textArea.getText());
				Toast.makeText(ProgramMainNext.this, "复制成功！", Toast.LENGTH_LONG).show();
			}
		});

		mListview.getListView().setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
									int arg2, long arg3) {
				TextView plan_ranges 	= (TextView)arg1.findViewById(R.id.program_next_result_list_ranges);
				TextView plan_nums		= (TextView)arg1.findViewById(R.id.program_next_result_list_plan_id);
				TextView issue 			= (TextView)arg1.findViewById(R.id.program_next_result_list_result_issue);
				TextView num 			= (TextView)arg1.findViewById(R.id.program_next_result_list_nums);
				showItem(arg1,""+plan_ranges.getText(),""+plan_nums.getText());
			}
		});
		mListview.setLoadDataListener(new PullToRefreshLayout.LoadDataListener() {
			@Override
			//下拉刷新调用
			public void onRefresh() {
				requestData();
			}

			@Override
			//下拉加载更多调用
			public void onLoadMore() {
			}
		});

	}

	public void reSetData(){
		countCyecles=0;
		countNo=0;
		countYes=0;
	}

	public void setData(int index){
		accuracy.setText(SendMessage.getInstance().getAccuracy(index)+"%");
		NoNum.setText("" + (int)SendMessage.getInstance().getCountNo(index));
		ZhouQi.setText("" + SendMessage.getInstance().getCountCycles(index));
		EndNum.setText(state[position]);
		title_plan.setText(state[Integer.parseInt(bestS_id)-1]);
		title_accuray.setText(bestRate+"%");
	}

	private void requestData() {
		newsBeanList = new ArrayList<>();
		HttpTask httpTask = new HttpTask();
		httpTask.execute(getURL());
		reSetData();
		httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
			@Override
			public void taskSuccessful(String json) {
				try {
					JSONObject jsonObject = new JSONObject(json);
					JSONArray data = jsonObject.getJSONArray("data");
					bestRate = jsonObject.getString("mrate");
					bestS_id = jsonObject.getString("ms_id");
					for (int i = 0; i < data.length(); i++) {
						jsonObject = data.getJSONObject(i);
						NextListBean newsBean = new NextListBean();
						newsBean.nums = jsonObject.getString("nums");
						newsBean.issue = jsonObject.getString("issue");
						newsBean.plan_num = jsonObject.getString("plan_num");
						newsBean.hit = jsonObject.getString("hit");
						newsBean.ranges = jsonObject.getString("ranges");
						//计算周期
						countCyecles++;
						//计算准确率
						if (newsBean.hit.equals("0")) {
							countNo 	+= 1;
						} else if (newsBean.hit.equals("1")) {
							countYes 	+= 1;
						}
						if (i == data.length() - 1) {
							SendMessage.getInstance().setCountYes(countYes);
							SendMessage.getInstance().setCountNo(pos, countNo);
							SendMessage.getInstance().setCountCycles(pos, countCyecles);
							SendMessage.getInstance().setAccuracy(pos, Double.parseDouble(accuracy(SendMessage.getInstance().getCountYes(), (SendMessage.getInstance().getCountYes() + SendMessage.getInstance().getCountNo(pos)), 1)));
						}
						newsBeanList.add(newsBean);
					}
					setListView(newsBeanList);
					mListview.onLoadComplete(true, false);
				}catch (Exception e){
					e.printStackTrace();
				}
			}

			@Override
			public void taskFailed() {
				mListview.onLoadComplete(true, true);
			}

		});
	}

	public void setListView(List<NextListBean> newsBeanList){
		adapter = new NextListAdapter(ProgramMainNext.this, newsBeanList,pos);
		mListview.getListView().setAdapter(adapter);
		setData(pos);
		adapter.notifyDataSetChanged();
	}

	//计算准确率
	public static String accuracy(double num, double total, int scale){
		DecimalFormat df = (DecimalFormat) NumberFormat.getInstance();
		//可以设置精确几位小数
		df.setMaximumFractionDigits(scale);
		//模式 例如四舍五入
		df.setRoundingMode(RoundingMode.HALF_UP);
		double accuracy_num = num / total * 100;
		return df.format(accuracy_num);
	}

	public void showSelectPic(View view) {
		popupView = ProgramMainNext.this.getLayoutInflater().inflate(R.layout.program_collect_copy_text, null);
		final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.MATCH_PARENT,600, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		popupWindow.setAnimationStyle(R.style.popup_window_anim);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));
		//获取本地时间
		try{
			update_time=format.format(time());
			textArea = (TextView) popupView.findViewById(R.id.program_collect_copy_textArea);
			textArea.setText("");
			textArea.append(SendMessage.getInstance().getCopyhead());
			textArea.append(getResources().getString(R.string.line));
			textArea.append(getResources().getString(R.string.lottery_title));
			for (int k = 0; k < 30; k++) {
				textArea.append(SendMessage.getInstance().getSHUZU(k));
			}
			textArea.append(getResources().getString(R.string.line));
			textArea.append(SendMessage.getInstance().getCopyEnd()+"\n");
			textArea.append(getResources().getString(R.string.update));
			textArea.append(update_time);
		}catch (Exception e){
			e.printStackTrace();
		}
		popupWindow.setFocusable(false);
		popupWindow.dismiss();
	}

	public void showItem(View view,String ranges,String num) {
		ItemView   = ProgramMainNext.this.getLayoutInflater().inflate(R.layout.program_next_detailed_dialog, null);
		windowItem = new PopupWindow(ItemView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, true);
		windowItem.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
		windowItem.setAnimationStyle(R.style.popup_window_anim);
		windowItem.setOutsideTouchable(true);
		windowItem.update();
		if (windowItem.isShowing()) {
			windowItem.dismiss();
		} else {
			windowItem.showAtLocation(ItemView, Gravity.CENTER, 0, 0);
			windowItem.setFocusable(true);
			windowItem.getContentView().setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					windowItem.setFocusable(false);
					windowItem.dismiss();
					return false;
				}
			});
		}
		TextView 	copy_ranges  = 	(TextView) ItemView.findViewById(R.id.copy_ranges);
		TextView 	copy_context = 	(TextView) ItemView.findViewById(R.id.copy_context);
		TextView 	copy_plays	 = 	(TextView) ItemView.findViewById(R.id.copy_plays);
		Button 	 	copy_close   = 	(Button)   ItemView.findViewById(R.id.copy_close);
		Button   	copy_button  = 	(Button)   ItemView.findViewById(R.id.copy_button);
		String 		textRanges	 =	ranges;
		String 		textPlays 	 =	SendMessage.getInstance().getNextPageName().substring(0,4);
		String 		textNum 	 =	num;
		copy_ranges.setText	("期号: "+	textRanges);
		copy_plays.setText	("玩法: "+	textPlays);
		copy_context.setText("内容: "+	textNum);
		final String copyContext="详细信息:\n"+"期号: "+textRanges+"\n玩法:"+textPlays+"\n内容:"+textNum;
		copy_button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cm.setText(copyContext);
				Toast.makeText(ProgramMainNext.this,"复制成功",Toast.LENGTH_SHORT).show();
			}
		});

		copy_close.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				windowItem.setFocusable(false);
				windowItem.dismiss();
			}
		});
	}

	public Date time() { //获取本地时间
		Date date = new Date();
		return date;
	}

	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.program_next_back_btn) {
			finish();
		}
	}
}
