package com.example.caikeplan.activity;

import com.example.base.Constants;
import com.example.base.DownLoadService;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.TabItem;
import com.example.getJson.HttpTask;
import com.example.util.Util;
import com.tbruyelle.rxpermissions.RxPermissions;

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
	private static final int    EMPTY   = 0xFFFFFFFF;
	private static final int    SUCCESS = 0xFFFFEEEE;
	private String              downloadUrl,content,update_mode;
	private boolean 			isUpdate = false;

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
		requestUpdate();
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

	//更新弹窗
	private void showChioceUpdateDialog(){
		final AlertDialog.Builder normalDialog =
				new AlertDialog.Builder(MainActivity.this);
		normalDialog.setIcon(R.drawable.logo);
		normalDialog.setTitle("聚彩盆新版本");
		normalDialog.setMessage("有新版本是否更新？");
		normalDialog.setCancelable(false);
		normalDialog.setPositiveButton("确定",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!isUpdate) {
							isUpdate = true;
							RxPermissions.getInstance(MainActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
								@Override
								public void call(Boolean aBoolean) {
									if (aBoolean) {
										Intent service = new Intent(MainActivity.this, DownLoadService.class);
										service.putExtra("downloadurl",downloadUrl);
										ToastUtil.getShortToastByString(MainActivity.this, "正在下载中");
										startService(service);
									} else {
										ToastUtil.getShortToastByString(MainActivity.this, "SD卡下载权限被拒绝");
									}
								}
							});
						}
					}
				});
		normalDialog.setNegativeButton("关闭",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		normalDialog.show();
	}

	//更新强制弹窗
	private void showNoChioceUpdateDialog(){
		final AlertDialog.Builder normalDialog =
				new AlertDialog.Builder(MainActivity.this);
		normalDialog.setIcon(R.drawable.logo);
		normalDialog.setTitle("聚彩盆新版本");
		normalDialog.setMessage("有新版本是否更新？");
		normalDialog.setCancelable(false);
		normalDialog.setPositiveButton("更新",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						if (!isUpdate) {
							isUpdate = true;
							RxPermissions.getInstance(MainActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
								@Override
								public void call(Boolean aBoolean) {
									if (aBoolean) {
										Intent service = new Intent(MainActivity.this, DownLoadService.class);
										service.putExtra("downloadurl",downloadUrl);
										ToastUtil.getShortToastByString(MainActivity.this, "正在下载中");
										startService(service);
									} else {
										ToastUtil.getShortToastByString(MainActivity.this, "SD卡下载权限被拒绝");
									}
								}
							});
						}
					}
				});
		normalDialog.show();
	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what != EMPTY) {
				String           localVersion   = Util.getVersion(MainActivity.this);
				Bundle           bundles        = msg.getData();
				String           serverVersion  = bundles.getString("version");
				downloadUrl           			= bundles.getString("url");
				content							= bundles.getString("content");
				update_mode                     = bundles.getString("update_mode");
				if (localVersion.compareTo(serverVersion) < 0 && update_mode.equals("1")) {
					ToastUtil.getShortToastByString(MainActivity.this, "有新版本");
					showChioceUpdateDialog();
				}else if(localVersion.compareTo(serverVersion) < 0 && update_mode.equals("2")){
					ToastUtil.getShortToastByString(MainActivity.this, "有新版本");
					showNoChioceUpdateDialog();
				}
			}
		}
	};

	//请求版本数据
	public void requestUpdate(){
		HttpTask httpTask = new HttpTask();
		httpTask.execute(Constants.API+Constants.UPDATE);
		httpTask.setTaskHandler(new HttpTask.HttpTaskHandler() {
			@Override
			public void taskSuccessful(String json) {
				try {
					JSONObject  root        = new JSONObject(json);
					JSONArray   data       	= root.getJSONArray("data");
					String     	versions 	= data.getJSONObject(0).getString("version_num");
					String     	urls       	= data.getJSONObject(0).getString("download_url");
					String 		content		= data.getJSONObject(0).getString("content");
					String      update_mode = data.getJSONObject(0).getString("update_mode");
					Message 	message     = mHandler.obtainMessage();
					Bundle      bundle   	= new Bundle();
					bundle.putString("version",versions);
					bundle.putString("url",urls);
					bundle.putString("content",content);
					bundle.putString("update_mode",update_mode);
					message.what = SUCCESS;
					message.setData(bundle);
					mHandler.sendMessage(message);
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}

			@Override
			public void taskFailed() {

			}
		});
	}

}
