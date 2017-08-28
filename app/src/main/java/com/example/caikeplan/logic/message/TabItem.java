package com.example.caikeplan.logic.message;

import android.content.Intent;

/**
 * Created by dell on 2017/3/6.
 */

public class TabItem {

    private String title;
    private int icon;



    private Intent intent;
    public TabItem(String title,int icon,Intent intent){
        this.title=title;
        this.icon=icon;
        this.intent=intent;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public Intent getIntent() {
        return intent;
    }

    public void setIntent(Intent intent) {
        this.intent = intent;
    }

}
