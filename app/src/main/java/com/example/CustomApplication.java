package com.example;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.example.base.CrashHandler;

/**
 * Created by dell on 2017/3/15.
 */

public class CustomApplication extends Application{
    private static Context sContext;
    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >23) {
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            builder.detectFileUriExposure();
        }
        sContext = this;
        CrashHandler mCrashHandler = new CrashHandler();
        mCrashHandler.init(getApplicationContext());

    }

    public static Context getAppContext() {
        return sContext;
    }
}
