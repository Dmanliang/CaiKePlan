package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/6/5.
 */

public class PlayTitleMessage {

    private String      play_title;
    private boolean     isFocus=false;
    private String      loc;

    public PlayTitleMessage(String play_title,String loc,boolean isFocus){
        this.play_title = play_title;
        this.loc        = loc;
        this.isFocus    = isFocus;
    }

    public String getLoc() {
        return loc;
    }

    public void setLoc(String loc) {
        this.loc = loc;
    }

    public String getPlay_title() {
        return play_title;
    }

    public void setPlay_title(String play_title) {
        this.play_title = play_title;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }
}
