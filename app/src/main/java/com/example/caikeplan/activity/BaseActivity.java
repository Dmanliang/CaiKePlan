package com.example.caikeplan.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.caikeplan.R;
import com.example.util.Util;
import com.readystatesoftware.systembartint.SystemBarTintManager;

/**
 * Created by dell on 2017/6/6.
 */

public class BaseActivity extends AppCompatActivity{

    private RelativeLayout  toolbar_container;
    private ImageView       back;
    private TextView        back_text;
    private LinearLayout    program_back;
    private TextView        play_select;
    private TextView        toolbar_title;
    private ImageView       title_arrow;
    private TextView        toolbar_set;
    private TextView        toolbar_save;
    private ImageView       btn_copy_plan;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        initToolBarView();
    }

    public void initToolBarView(){
        toolbar_container   =   (RelativeLayout)findViewById(R.id.toolbar_container);
        program_back        =   (LinearLayout)findViewById(R.id.program_back);
        back                =   (ImageView)findViewById(R.id.back);
        back_text           =   (TextView)findViewById(R.id.back_text);
        play_select         =   (TextView)findViewById(R.id.play_select);
        toolbar_title       =   (TextView)findViewById(R.id.toolbar_title);
        title_arrow         =   (ImageView)findViewById(R.id.title_arrow);
        toolbar_set         =   (TextView)findViewById(R.id.toolbar_set);
        toolbar_save        =   (TextView)findViewById(R.id.toolbar_save);
        btn_copy_plan       =   (ImageView)findViewById(R.id.btn_copy_plan);
    }

    public void setView(int layoutID) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().getDecorView().setFitsSystemWindows(true);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            /*getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
            //  getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        setContentView(layoutID);

    }
}
