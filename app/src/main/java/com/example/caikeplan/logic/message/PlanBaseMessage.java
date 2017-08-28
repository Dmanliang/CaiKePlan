package com.example.caikeplan.logic.message;

import java.io.Serializable;

/**
 * Created by dell on 2017/5/24.
 */

public class PlanBaseMessage implements Serializable {

    private String  lottery_id;      //彩票编号
    private String  s_id;            //计划编号
    private String  scheme_name;     //计划名称
    private String  plan_id;         //计划id
    private String  plan_name;       //推荐计划名
    private int     index;           //标记缓存位置
    private String  cls_name;        //类名
    private String  count;           //数量
    private String  lottery_name;    //彩种名称
    private String  id;              //序号

    public PlanBaseMessage(String lottery_name,String lottery_id, String s_id, String scheme_name, String plan_id, String plan_name, String cls_name, String count){
        this.lottery_name = lottery_name;
        this.lottery_id =   lottery_id;
        this.s_id       =   s_id;
        this.scheme_name=   scheme_name;
        this.plan_id    =   plan_id;
        this.plan_name  =   plan_name;
        this.cls_name   =   cls_name;
        this.count      =   count;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLottery_name() {
        return lottery_name;
    }

    public void setLottery_name(String lottery_name) {
        this.lottery_name = lottery_name;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getCls_name() {
        return cls_name;
    }

    public void setCls_name(String cls_name) {
        this.cls_name = cls_name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getLottery_id() {
        return lottery_id;
    }

    public void setLottery_id(String lottery_id) {
        this.lottery_id = lottery_id;
    }

    public String getS_id() {
        return s_id;
    }

    public void setS_id(String s_id) {
        this.s_id = s_id;
    }

    public String getScheme_name() {
        return scheme_name;
    }

    public void setScheme_name(String scheme_name) {
        this.scheme_name = scheme_name;
    }

    public String getPlan_id() {
        return plan_id;
    }

    public void setPlan_id(String plan_id) {
        this.plan_id = plan_id;
    }

    public String getPlan_name() {
        return plan_name;
    }

    public void setPlan_name(String plan_name) {
        this.plan_name = plan_name;
    }


}
