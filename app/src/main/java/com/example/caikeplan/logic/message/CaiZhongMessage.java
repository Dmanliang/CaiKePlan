package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/3/3.
 */

public class CaiZhongMessage {

    private String  home_title;                      //彩种标题
    private String  home_date_end;                   //已开奖时间
    private String  expect_time;                     //本期预计开奖时间
    private String  ball1,ball2,ball3,ball4,ball5;   //五个彩种球
    private String  home_date;                       //下期开奖时间
    private String  home_timer;                      //开奖倒计时
    private Class   clas;                            //跳转页面
    private String  lottery_id;                      //彩种id

    public CaiZhongMessage(String lottery_id, String home_title, String home_date_end, String ball1, String ball2, String ball3, String ball4, String ball5, String home_date, String home_timer, Class clas){
        this.lottery_id =  lottery_id;
        this.home_title =home_title;
        this.home_date_end=home_date_end;
        this.ball1=ball1;
        this.ball2=ball2;
        this.ball3=ball3;
        this.ball4=ball4;
        this.ball5=ball5;
        this.home_date=home_date;
        this.home_timer=home_timer;
        this.clas=clas;
    }

    public String getLottery_id() {
        return lottery_id;
    }

    public void setLottery_id(String lottery_id) {
        this.lottery_id = lottery_id;
    }

    public String getHome_title() {
        return home_title;
    }

    public void setHome_title(String home_title) {
        this.home_title = home_title;
    }

    public String getHome_date_end() {
        return home_date_end;
    }

    public void setHome_date_end(String home_date_end) {
        this.home_date_end = home_date_end;
    }

    public String getBall1() {
        return ball1;
    }

    public void setBall1(String ball1) {
        this.ball1 = ball1;
    }

    public String getBall2() {
        return ball2;
    }

    public void setBall2(String ball2) {
        this.ball2 = ball2;
    }

    public String getBall3() {
        return ball3;
    }

    public void setBall3(String ball3) {
        this.ball3 = ball3;
    }

    public String getBall4() {
        return ball4;
    }

    public void setBall4(String ball4) {
        this.ball4 = ball4;
    }

    public String getBall5() {
        return ball5;
    }

    public void setBall5(String ball5) {
        this.ball5 = ball5;
    }

    public String getHome_date() {
        return home_date;
    }

    public void setHome_date(String home_date) {
        this.home_date = home_date;
    }

    public String getHome_timer() {
        return home_timer;
    }

    public void setHome_timer(String home_timer) {
        this.home_timer = home_timer;
    }

    public Class getClas() {
        return clas;
    }

    public void setClas(Class clas) {
        this.clas = clas;
    }

    public String getExpect_time() {
        return expect_time;
    }

    public void setExpect_time(String expect_time) {
        this.expect_time = expect_time;
    }

}
