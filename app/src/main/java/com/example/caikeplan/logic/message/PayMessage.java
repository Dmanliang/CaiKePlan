package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/9/26.
 */

public class PayMessage {

    private int pay_icon;
    private String pay_describe;
    private String pay_name;
    private String pay_payChannel;
    public PayMessage(int pay_icon,String pay_describe,String pay_name,String pay_payChannel){
        this.pay_describe = pay_describe;
        this.pay_icon  = pay_icon;
        this.pay_name  = pay_name;
        this.pay_payChannel =   pay_payChannel;
    }

    public String getPay_payChannel() {
        return pay_payChannel;
    }

    public void setPay_payChannel(String pay_payChannel) {
        this.pay_payChannel = pay_payChannel;
    }

    public int getPay_icon() {
        return pay_icon;
    }

    public void setPay_icon(int pay_icon) {
        this.pay_icon = pay_icon;
    }

    public String getPay_describe() {
        return pay_describe;
    }

    public void setPay_describe(String pay_describe) {
        this.pay_describe = pay_describe;
    }

    public String getPay_name() {
        return pay_name;
    }

    public void setPay_name(String pay_name) {
        this.pay_name = pay_name;
    }
}
