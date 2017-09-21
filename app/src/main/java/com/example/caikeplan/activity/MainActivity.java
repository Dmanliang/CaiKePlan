package com.example.caikeplan.activity;

import com.example.base.Constants;
import com.example.base.DownLoadService;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.TabItem;
import com.example.getJson.HttpTask;
import com.example.util.Util;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import android.Manifest;
import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.widget.TabWidget;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Action1;

public class MainActivity extends TabActivity {

	private List<TabItem> 		tabItems;
	private TabWidget 			mTabWidget;
	private LayoutInflater 		mLayouInflater;
	private Class 				fragmentArray[] 	= {ProgramActivity.class,RankActivity.class, DataActivity.class ,PersonalActivity.class};
	private int 				mImageViewArray[] 	= { R.drawable.main_tab_item_program,R.drawable.main_tab_item_lottery,R.drawable.main_tab_item_data,
													R.drawable.main_tab_item_user};
	private String 				mTextviewArray[] 	= {"方案","排行","数据分析", "个人"};
	private String 				tabname[] 			= {"方案","排行","数据分析", "个人"};
	private Intent 				intent[] 			= new Intent[4];
	private int 				i 					= 0;
	private TabHost 			tabhost;
	private TabSpec 			tabSpec;


	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
	/*	getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
		setContentView(R.layout.activity_main);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
			getWindow().getDecorView().setFitsSystemWindows(true);
			getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
			//getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
		}
		tabhost 		= 	getTabHost();
		mTabWidget 		= 	getTabWidget();            //获取TabWidget
		mLayouInflater 	= 	getLayoutInflater();
		initView();
	}


	private void initView() {
		initTabItem();
		for (i = 0; i < fragmentArray.length; i++) {
			View 		view      	= 	mLayouInflater.inflate(R.layout.item_tab, null, false);
			ImageView 	imageView 	= 	(ImageView) view.findViewById(R.id.tab_item_image);
			TextView 	textView  	= 	(TextView) view.findViewById(R.id.tab_item_text);
			textView.setText(tabItems.get(i).getTitle());
			Drawable 	image 		= 	getResources().getDrawable(tabItems.get(i).getIcon());
			imageView.setBackgroundResource(tabItems.get(i).getIcon());
			//设置tabspec
			tabSpec = tabhost.newTabSpec(tabItems.get(i).getTitle());
			//把TabItem view 添加到 TabSpec
			tabSpec.setIndicator(view);
			//添加选择后的Intent
			tabSpec.setContent(tabItems.get(i).getIntent());
			//将TabSpec添加到TabHost
			tabhost.addTab(tabSpec);
			tabhost.getTabWidget().setDividerDrawable(null);
		}
		tabhost.setCurrentTab(0);//设置当前的选项卡,这里为Tab1
	}

	public void initTabItem() {
		tabItems = new ArrayList<>();
		for (int i = 0; i < fragmentArray.length; i++) {
			intent[i] 		= 	new Intent(MainActivity.this, fragmentArray[i]);
			TabItem tabItem = 	new TabItem(mTextviewArray[i], mImageViewArray[i], intent[i]);
			tabItems.add(tabItem);
		}
	}

	@Override
	protected void onResume() {
		MobclickAgent.onResume(this);
		super.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		MobclickAgent.onPause(this);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			MobclickAgent.onKillProcess( this );
			int pid = android.os.Process.myPid();
			android.os.Process.killProcess(pid);
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
