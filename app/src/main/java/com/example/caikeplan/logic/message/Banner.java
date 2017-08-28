package com.example.caikeplan.logic.message;

import com.google.gson.annotations.SerializedName;

/**
 * Created by DELL on 2017/3/30.
 */

public class Banner {
    @SerializedName("img_url")
    private String imgUrl;
    private String link;
    private String title;

    public Banner(String imgUrl,String link,String title){
        this.imgUrl =   imgUrl;
        this.link   =   link;
        this.title  =   title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }
}
