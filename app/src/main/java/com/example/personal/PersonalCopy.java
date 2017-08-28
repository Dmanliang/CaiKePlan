package com.example.personal;

import com.example.caikeplan.R;
import com.example.caikeplan.activity.BaseActivity;
import com.example.caikeplan.logic.message.SendMessage;
import com.youth.xframe.cache.XCache;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PersonalCopy extends BaseActivity implements OnClickListener{

	private RelativeLayout 		personalcopy_toolbar;
	private RelativeLayout      toolbar_container;
	private TextView 			back_text;
	private LinearLayout 		program_back;
	private TextView            play_select;
	private TextView            toolbar_title;
	private ImageView           title_arrow;
	private TextView            toolbar_set;
	private TextView            toolbar_save;
	private ImageView           btn_copy_plan;
	private EditText 			head,mid,end;
    private RadioGroup 			radiogroup;
    private RadioButton         comma,blank,vertical;
	private ImageView			content_help;
	private PopupWindow			helpwindow;
	private Button				content_setting_close;
	private XCache				xCache;
	private String 				shead,smid,send;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.personal_copy);
		initView();
	}

	public void initView(){
		xCache				=	XCache.get(this);
		radiogroup			=	(RadioGroup)findViewById(R.id.radiogroup);
		comma				=	(RadioButton)findViewById(R.id.comma);
		blank				=	(RadioButton)findViewById(R.id.blank);
		vertical			=	(RadioButton)findViewById(R.id.vertical);
		head 				= 	(EditText) findViewById(R.id.personal_copy_head);
		mid 				= 	(EditText) findViewById(R.id.personal_copy_mid);
		end 				= 	(EditText) findViewById(R.id.personal_copy_end);
		personalcopy_toolbar=   (RelativeLayout)findViewById(R.id.personalcopy_toolbar);
		toolbar_container   =   (RelativeLayout)findViewById(R.id.toolbar_container);
		program_back        =   (LinearLayout)findViewById(R.id.program_back);
		back_text           =   (TextView)findViewById(R.id.back_text);
		play_select         =   (TextView)findViewById(R.id.play_select);
		toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
		title_arrow         =   (ImageView)findViewById(R.id.title_arrow);
		toolbar_set         =   (TextView)findViewById(R.id.toolbar_set);
		toolbar_save        =   (TextView)findViewById(R.id.toolbar_save);
		btn_copy_plan       =   (ImageView)findViewById(R.id.btn_copy_plan);
		content_help		=	(ImageView)findViewById(R.id.content_help);
		toolbar_title.setText("复制计划");
		if(xCache.getAsString("head")!=null){
			head.setText(xCache.getAsString("head"));
		}
		if(xCache.getAsString("mid")!=null){
			mid.setText(xCache.getAsString("mid"));
		}
		if(xCache.getAsString("end")!=null){
			end.setText(xCache.getAsString("end"));
		}
		SendMessage.getInstance().setDivider(" ");
		xCache.put("divider"," ");
		program_back.setVisibility(View.VISIBLE);
		toolbar_save.setVisibility(View.VISIBLE);
		program_back.setVisibility(View.VISIBLE);
		program_back.setOnClickListener(this);
		toolbar_save.setOnClickListener(this);
		content_help.setOnClickListener(this);
		radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
				switch(checkedId){
					case R.id.comma:
						SendMessage.getInstance().setDivider(",");
						xCache.put("divider",",");
						break;
					case R.id.blank:
						SendMessage.getInstance().setDivider(" ");
						xCache.put("divider"," ");
						break;
					case R.id.vertical:
						SendMessage.getInstance().setDivider("|");
						xCache.put("divider","|");
						break;
				}
			}
		});
	}

	//显示选项复制弹框
	public void showItem(View view) {
		view = PersonalCopy.this.getLayoutInflater().inflate(R.layout.content_setting, null);
		helpwindow = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT, true);
		helpwindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#80000000")));
		helpwindow.setOutsideTouchable(true);
		helpwindow.update();
		if (helpwindow.isShowing()) {
			helpwindow.dismiss();
		} else {
			helpwindow.showAtLocation(view, Gravity.CENTER, 0, 0);
			helpwindow.setFocusable(true);
			helpwindow.getContentView().setOnTouchListener(new View.OnTouchListener() {
				@Override
				public boolean onTouch(View view, MotionEvent motionEvent) {
					helpwindow.setFocusable(false);
					helpwindow.dismiss();
					return false;
				}
			});
		}
		content_setting_close = (Button)view.findViewById(R.id.content_setting_close);
		content_setting_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				helpwindow.setFocusable(false);
				helpwindow.dismiss();
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.program_back:
				finish();
				break;
			case R.id.toolbar_save:
				shead	=	head.getText().toString();
				smid	=	mid.getText().toString();
				send	=	end.getText().toString();
				if(!shead.equals("") && !smid.equals("") && !send.equals("")){
					xCache.put("head",shead);
					xCache.put("mid",smid);
					xCache.put("end",send);
					SendMessage.getInstance().setCopyhead(shead);
					SendMessage.getInstance().setCopymid(smid);
					SendMessage.getInstance().setCopyEnd(send);
					Toast.makeText(PersonalCopy.this, "保存成功！", Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(this, "配置不能为空,请重新输入!", Toast.LENGTH_SHORT).show();
				}
				finish();
				break;
			case R.id.content_help:
				showItem(v);
				break;
		}
	}
}
