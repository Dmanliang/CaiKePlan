package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/6/5.
 */

public class TableMessage {

    private String count;   //出现次数
    private String danma;   //胆码
    private String omit;    //当前遗漏
    private String coolhot; //冷热
    private int    miss;    //遗漏次数
    private float  ratio;   //比例


    public TableMessage(String count, String danma, String omit, String coolhot){
        this.count      =   count;
        this.danma      =   danma;
        this.omit       =   omit;
        this.coolhot    =   coolhot;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getDanma() {
        return danma;
    }

    public void setDanma(String danma) {
        this.danma = danma;
    }

    public String getOmit() {
        return omit;
    }

    public void setOmit(String omit) {
        this.omit = omit;
    }

    public String getCoolhot() {
        return coolhot;
    }

    public void setCoolhot(String coolhot) {
        this.coolhot = coolhot;
    }

    public int getMiss() {
        return miss;
    }

    public void setMiss(int miss) {
        this.miss = miss;
    }

    public float getRatio() {
        return ratio;
    }

    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

}
