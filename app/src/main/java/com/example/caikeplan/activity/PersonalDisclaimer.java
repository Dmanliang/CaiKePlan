package com.example.caikeplan.activity;

import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.caikeplan.logic.ToastUtil;

public class PersonalDisclaimer extends BaseActivity implements OnClickListener{

	private TextView    	toolbar_title;
	private TextView    	back_text;
	private LinearLayout	program_back;
	private RelativeLayout	qqlayout;
	private TextView		qqtext;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.personal_mianzeshengming);
		initViews();
	}

	public void initViews(){
		toolbar_title 	= (TextView)findViewById(R.id.toolbar_title);
		qqtext 			= (TextView)findViewById(R.id.qqtext);
		back_text 		= (TextView)findViewById(R.id.back_text);
		program_back 	= (LinearLayout)findViewById(R.id.program_back);
		qqlayout		= (RelativeLayout)findViewById(R.id.qqlayout);
		program_back.setVisibility(View.VISIBLE);
		back_text.setVisibility(View.VISIBLE);
		toolbar_title.setText("免责声明");
		program_back.setOnClickListener(this);
		qqlayout.setOnClickListener(this);
		toolbar_title.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.program_back:
				finish();
				break;
			case R.id.toolbar_title:
				finish();
				break;
			case R.id.qqlayout:
				ClipboardManager cms = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
				cms.setText(qqtext.getText().toString());
				ToastUtil.getShortToastByString(PersonalDisclaimer.this,"QQ复制成功");
				break;
		}
	}
}
