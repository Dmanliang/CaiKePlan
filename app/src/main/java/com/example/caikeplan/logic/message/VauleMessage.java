package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/9/19.
 */

public class VauleMessage {

    private String  old_value;
    private String  new_value;
    private String  recharge_time;
    private String  mouth;
    private String     money;

    public VauleMessage(String recharge_time,String old_value,String new_value,String mouth,String money){
        this.recharge_time  =   recharge_time;
        this.old_value      =   old_value;
        this.new_value      =   new_value;
        this.mouth          =   mouth;
        this.money          =   money;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getMouth() {
        return mouth;
    }

    public void setMouth(String mouth) {
        this.mouth = mouth;
    }

    public String getOld_value() {
        return old_value;
    }

    public void setOld_value(String old_value) {
        this.old_value = old_value;
    }

    public String getNew_value() {
        return new_value;
    }

    public void setNew_value(String new_value) {
        this.new_value = new_value;
    }

    public String getRecharge_time() {
        return recharge_time;
    }

    public void setRecharge_time(String recharge_time) {
        this.recharge_time = recharge_time;
    }
}
