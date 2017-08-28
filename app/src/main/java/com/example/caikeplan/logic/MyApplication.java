package com.example.caikeplan.logic;


import android.app.Application;
import android.content.Context;

public class MyApplication extends Application{
	public static Context CONTEXT;
	@Override
	public void onCreate() {
		super.onCreate();
		setContext(this);
	}
	
	private static void setContext(Context mContext) {
		CONTEXT = mContext;
	}
	
	public static Context getContext() {
		return CONTEXT;
	}
}
