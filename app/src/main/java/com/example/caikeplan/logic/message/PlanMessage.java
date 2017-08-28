package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/5/19.
 */

public class PlanMessage {

    private String plan_num;         //计划号码
    private String nums;             //开奖号码
    private String play_id;          //玩法
    private String lottery_id;       //彩种编号
    private String ranges;           //计划周期
    private String plan_id;          //彩种编号-玩法-计划号码数量-周期
    private String hit;              //是否命中
    private String issue;            //开奖期数
    private String nums_type;        //码数

    public PlanMessage(String plan_num, String nums, String play_id, String lottery_id, String ranges, String plan_id, String hit, String issue,String nums_type){
        this.issue      =   issue;
        this.plan_num   =   plan_num;
        this.hit        =   hit;
        this.nums       =   nums;
        this.play_id    =   play_id;
        this.ranges     =   ranges;
        this.lottery_id =   lottery_id;
        this.plan_id    =   plan_id;
        this.nums_type  =   nums_type;
    }

    public String getNums_type() {
        return nums_type;
    }

    public void setNums_type(String nums_type) {
        this.nums_type = nums_type;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }

    public String getPlan_num() {
        return plan_num;
    }

    public void setPlan_num(String plan_num) {
        this.plan_num = plan_num;
    }

    public String getHit() {
        return hit;
    }

    public void setHit(String hit) {
        this.hit = hit;
    }

    public String getNums() {
        return nums;
    }

    public void setNums(String nums) {
        this.nums = nums;
    }

    public String getRanges() {
        return ranges;
    }

    public void setRanges(String ranges) {
        this.ranges = ranges;
    }

    public String getPlay_id() {
        return play_id;
    }

    public void setPlay_id(String play_id) {
        this.play_id = play_id;
    }

    public String getLottery_id() {
        return lottery_id;
    }

    public void setLottery_id(String lottery_id) {
        this.lottery_id = lottery_id;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }
}
