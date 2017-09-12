package com.example.caikeplan.logic.message;

/**
 * Created by Decml on 2017/3/18.
 */

public class LotteryTitle {

    private String      lottery_title;
    private String      lottery_id;
    private int         res_id;
    private boolean     isFocus=false;
    private String      type;

    public LotteryTitle(String lottery_id,String lottery_title,String type,int res_id,boolean isFocus){
        this.lottery_id     =   lottery_id;
        this.lottery_title  =   lottery_title;
        this.isFocus        =   isFocus;
        this.res_id         =   res_id;
        this.type           =   type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getRes_id() {
        return res_id;
    }

    public void setRes_id(int res_id) {
        this.res_id = res_id;
    }

    public String getLottery_id() {
        return lottery_id;
    }

    public void setLottery_id(String lottery_id) {
        this.lottery_id = lottery_id;
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
