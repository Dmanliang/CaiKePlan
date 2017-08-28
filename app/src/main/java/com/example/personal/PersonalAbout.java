package com.example.personal;

import com.example.caikeplan.R;
import com.example.caikeplan.activity.BaseActivity;
import com.example.caikeplan.activity.PersonalConnect;
import com.example.caikeplan.activity.PersonalDisclaimer;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

public class PersonalAbout extends BaseActivity implements OnClickListener{

	private LinearLayout	program_back;
	private RelativeLayout	state,connectus;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setView(R.layout.personal_aboutus);
		initView();
	}

	public void initView(){
		program_back = (LinearLayout)findViewById(R.id.program_back);
		state= (RelativeLayout)findViewById(R.id.state);
		connectus= (RelativeLayout)findViewById(R.id.connectus);
		program_back.setVisibility(View.VISIBLE);
		program_back.setOnClickListener(this);
		state.setOnClickListener(this);
		connectus.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()){
			case R.id.program_back:
				finish();
				break;
			case R.id.connectus:
				startActivity(new Intent(PersonalAbout.this, PersonalConnect.class));
				break;
			case R.id.state:
				startActivity(new Intent(PersonalAbout.this, PersonalDisclaimer.class));
				break;
		}
	}
}
