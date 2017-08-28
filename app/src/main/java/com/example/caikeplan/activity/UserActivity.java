
package com.example.caikeplan.activity;

import com.example.base.Constants;
import com.example.base.DownLoadService;
import com.example.caikeplan.R;
import com.example.caikeplan.logic.ToastUtil;
import com.example.caikeplan.logic.message.SendMessage;
import com.example.personal.PersonalAbout;
import com.example.personal.PersonalCopy;
import com.example.util.Util;
import com.meiqia.core.callback.OnInitCallback;
import com.meiqia.meiqiasdk.util.MQConfig;
import com.meiqia.meiqiasdk.util.MQIntentBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.functions.Action1;

public class UserActivity extends BaseActivity implements OnClickListener{

	private RelativeLayout		caculator_layout,shrink_layout,aboutus_layout,feedback_layout,contentset_layout,share_layout,update_layout;
	private View   				updateView;
	private static final int    EMPTY   = 0xFFFFFFFF;
	private static final int    SUCCESS = 0xFFFFEEEE;
	private DownloadManager 	downloadManager;
	private long                id;
	private TimerTask 			mTimerTask;
	private String              downloadUrl,content;
	private RelativeLayout		rootRelative;
	private boolean 			isUpdate = false;
	private TextView			toolbar_title;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.personal_main);
		initView();
	}

	private void checkClientVersion() {
		mHandler.post(mRunnableCheckVersion);
	}

	public void initView(){
		rootRelative		=	(RelativeLayout)findViewById(R.id.rootRelative);
		caculator_layout 	= 	(RelativeLayout)findViewById(R.id.caculator_layout);
		shrink_layout 		= 	(RelativeLayout)findViewById(R.id.shrink_layout);
		aboutus_layout 		= 	(RelativeLayout)findViewById(R.id.aboutus_layout);
		feedback_layout 	= 	(RelativeLayout)findViewById(R.id.feedback_layout);
		contentset_layout 	= 	(RelativeLayout)findViewById(R.id.contentset_layout);
		share_layout 		= 	(RelativeLayout)findViewById(R.id.share_layout);
		update_layout 		= 	(RelativeLayout)findViewById(R.id.update_layout);
		toolbar_title		=	(TextView)findViewById(R.id.toolbar_title);
		toolbar_title.setText("个人中心");
		rootRelative.setOnClickListener(this);
		caculator_layout.setOnClickListener(this);
		shrink_layout.setOnClickListener(this);
		aboutus_layout.setOnClickListener(this);
		feedback_layout.setOnClickListener(this);
		contentset_layout.setOnClickListener(this);
		share_layout.setOnClickListener(this);
		update_layout.setOnClickListener(this);

		MQConfig.init(UserActivity.this, "a54876992351e243a40ed52de0abb4e1", new OnInitCallback() {
			public void onSuccess(String clientId) {
			}

			public void onFailure(int code, String message) {
				ToastUtil.getShortToastByString(UserActivity.this,"与客服连接失败");
			}
		});
		//设置美洽
		MQConfig.ui.titleBackgroundResId=R.color.blueMQ;
		MQConfig.ui.titleTextColorResId=R.color.whileText;
		MQConfig.isVoiceSwitchOpen=true;
		MQConfig.isSoundSwitchOpen=true;
		MQConfig.isLoadMessagesFromNativeOpen=true;
	}

	public void showUpdateItem() {
		updateView = UserActivity.this.getLayoutInflater().inflate(R.layout.download_activity, null);
		final PopupWindow popupWindow = new PopupWindow(updateView, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, true);
		popupWindow.setTouchable(true);
		popupWindow.setOutsideTouchable(true);
		//popupWindow.setAnimationStyle(R.style.popup_window_anim);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
		if (popupWindow.isShowing()) {
			popupWindow.dismiss();
		} else {
			popupWindow.showAtLocation(rootRelative, Gravity.CENTER, 0, 0);
			popupWindow.setFocusable(true);
			popupWindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					return false;
				}
			});
		}
		TextView updateText	   =	(TextView)updateView.findViewById(R.id.update_finish);
		Button   updateButton  = 	(Button)updateView.findViewById(R.id.btn_download);
		updateText.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
		updateButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if(!isUpdate){
					isUpdate=true;
					RxPermissions.getInstance(UserActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe(new Action1<Boolean>() {
						@Override
						public void call(Boolean aBoolean) {
							if (aBoolean) {
								Intent service = new Intent(UserActivity.this, DownLoadService.class);
								service.putExtra("downloadurl", "http://120.77.242.46:8088/"+downloadUrl);
								ToastUtil.getShortToastByString(UserActivity.this, "正在下载中");
								startService(service);
							} else {
								ToastUtil.getShortToastByString(UserActivity.this, "SD卡下载权限被拒绝");
							}
						}
					});
					//downloadAPK("http://120.77.242.46:8088/"+downloadUrl);
					popupWindow.dismiss();
				}else{
					ToastUtil.getShortToastByString(UserActivity.this,"已更新啦!");
				}
			}
		});
		updateText.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				popupWindow.dismiss();
			}
		});


	}

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what != EMPTY) {
				String           localVersion   = Util.getVersion(UserActivity.this);
				Bundle           bundles        = msg.getData();
				String           serverVersion  = bundles.getString("version");
				downloadUrl           			= bundles.getString("url");
				content							= bundles.getString("content");
				if (localVersion.compareTo(serverVersion) >= 0) {
					ToastUtil.getShortToastByString(UserActivity.this, "当前最新版本");
				} else {
					showUpdateItem();
					ToastUtil.getShortToastByString(UserActivity.this, "有新版本");
				}
			}
		}
	};

	private void downloadAPK(String downloadUrl) {
		downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
		request.setTitle(getString(R.string.app_name));
		request.setMimeType("application/vnd.android.package-archive");
		request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).mkdir();
		request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, getString(R.string.app_name) + ".apk");
		id = downloadManager.enqueue(request);
		final DownloadManager.Query query = new DownloadManager.Query();
		Timer timer = new Timer();
		mTimerTask = new TimerTask() {
			@Override
			public void run() {
				Cursor cursor = downloadManager.query(query.setFilterById(id));
				if (cursor != null && cursor.moveToFirst()) {
					if (cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
							== DownloadManager.STATUS_SUCCESSFUL) {
						mTimerTask.cancel();
						File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
								, getString(R.string.app_name) + ".apk");
						Intent intent = new Intent(Intent.ACTION_VIEW);
						// 由于没有在Activity环境下启动Activity,设置下面的标签
						intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						if(Build.VERSION.SDK_INT>=24) { //判读版本是否在7.0以上
							//参数1 上下文, 参数2 Provider主机地址 和配置文件中保持一致   参数3  共享的文件
							Uri apkUri = FileProvider.getUriForFile(UserActivity.this, "com.example.caikeplan.fileprovider", file);
							//添加这一句表示对目标应用临时授权该Uri所代表的文件
							intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
							intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
						}else{
							intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
						}
						startActivity(intent);
					}
					cursor.close();
				}
			}
		};
		timer.schedule(mTimerTask, 0, 500);
	}

	private OkHttpClient mClient               = new OkHttpClient();
	private Runnable     mRunnableCheckVersion = new Runnable() {
		@Override
		public void run() {
			Request request = new Request.Builder()
					.url(Constants.API+Constants.UPDATE)
					.build();
			mClient.newCall(request).enqueue(new Callback() {
				@Override
				public void onFailure(Call call, IOException e) {
					mHandler.sendEmptyMessage(EMPTY);
				}

				@Override
				public void onResponse(Call call, Response response) throws IOException {
					try {
						JSONObject 	root        = new JSONObject(response.body().string());
						JSONArray   data       	= root.getJSONArray("data");
						String     	versions 	= data.getJSONObject(0).getString("version_num");
						String     	urls       	= data.getJSONObject(0).getString("download_url");
						String 		content		= data.getJSONObject(0).getString("content");
						Message 	message     = mHandler.obtainMessage();
						Bundle      bundle   	= new Bundle();
						bundle.putString("version",versions);
						bundle.putString("url",urls);
						bundle.putString("content",content);
						message.what = SUCCESS;
						message.setData(bundle);
						mHandler.sendMessage(message);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			});
		}
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.caculator_layout:
				ToastUtil.getShortToastByString(this, "开发中");
				break;
			case R.id.shrink_layout:
				ToastUtil.getShortToastByString(this, "开发中");
				break;
			case R.id.aboutus_layout:
				UserActivity.this.startActivity(new Intent(UserActivity.this, PersonalAbout.class));
				break;
			case R.id.feedback_layout:
				Intent intent = new MQIntentBuilder(UserActivity.this).build();
				startActivity(intent);
				break;
			case R.id.contentset_layout:
				startActivity(new Intent(UserActivity.this, PersonalCopy.class));
				break;
			case R.id.share_layout:
				Intent intent1 = new Intent(Intent.ACTION_SEND);
				intent1.putExtra(Intent.EXTRA_TEXT, "file:///C:/Users/dell/Desktop/caike/caike/index.html");
				intent1.setType("text/plain");
				startActivity(Intent.createChooser(intent1, "分享"));
				break;
			case R.id.update_layout:
				checkClientVersion();
				break;
		}
	}
}