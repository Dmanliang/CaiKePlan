package com.example.caikeplan.logic.message;

/**
 * Created by Decml on 2017/3/18.
 */

public class LotteryTitle {

    private String      lottery_title;
    private boolean     isFocus=false;

    public LotteryTitle(String lottery_title ,boolean isFocus){
        this.lottery_title  =   lottery_title;
        this.isFocus        =   isFocus;
    }

    public boolean isFocus() {
        return isFocus;
    }

    public void setFocus(boolean focus) {
        isFocus = focus;
    }

    public String getLottery_title() {
        return lottery_title;
    }

    public void setLottery_title(String lottery_title) {
        this.lottery_title = lottery_title;
    }

}
