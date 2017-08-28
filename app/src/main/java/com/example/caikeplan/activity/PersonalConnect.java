package com.example.caikeplan.activity;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.caikeplan.R;

public class PersonalConnect extends BaseActivity implements OnClickListener{

	private LinearLayout program_back;
	private TextView  toolbar_title;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.personal_connect);
		initViews();
	}

	public void initViews(){
		program_back = (LinearLayout)findViewById(R.id.program_back);
		toolbar_title = (TextView) findViewById(R.id.toolbar_title);
		program_back.setVisibility(View.VISIBLE);
		toolbar_title.setText("联系我们");
		program_back.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.program_back:
				finish();
				break;
		}
	}


}
