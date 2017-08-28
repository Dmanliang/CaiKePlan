package com.example.caikeplan.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.base.Constants;
import com.example.caikeplan.R;

import org.w3c.dom.Text;

/**
 * Created by dell on 2017/8/25.
 */

public class ToolsActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout  calculat_layout;
    private LinearLayout    program_back;
    private TextView        toolbar_title;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setView(R.layout.tools_activity);
        initView();
    }

    public void initView(){
        calculat_layout = (RelativeLayout)findViewById(R.id.calculat_layout);
        program_back      = (LinearLayout)findViewById(R.id.program_back);
        toolbar_title     = (TextView)findViewById(R.id.toolbar_title);
        calculat_layout.setOnClickListener(this);
        program_back.setOnClickListener(this);
        program_back.setVisibility(View.VISIBLE);
        toolbar_title.setText("工具大全");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.calculat_layout:
                Intent intent = new Intent(ToolsActivity.this,BannerLinkActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("url", Constants.API+Constants.CALCULATOR);
                bundle.putString("title","计算器");
                intent.putExtras(bundle);
                startActivity(intent);
                break;
            case R.id.program_back:
                finish();
                break;
        }
    }
}
