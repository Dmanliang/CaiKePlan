package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/8/22.
 */

public class NoticeMessage {

    private String url;
    private String notice_id;
    private String send_id;
    private String send_name;
    private String name;
    private String created_at;
    private String update_at;
    private boolean isRead = false;

    public NoticeMessage(String notice_id,String send_id,String send_name,String name,String url,String created_at,String update_at){
        this.name =  name;
        this.notice_id = notice_id;
        this.send_id   = send_id;
        this.send_name = send_name;
        this.name   = name;
        this.created_at = created_at;
        this.update_at = update_at;
        this.url     =  url;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public String getNotice_id() {
        return notice_id;
    }

    public void setNotice_id(String notice_id) {
        this.notice_id = notice_id;
    }

    public String getSend_id() {
        return send_id;
    }

    public void setSend_id(String send_id) {
        this.send_id = send_id;
    }

    public String getSend_name() {
        return send_name;
    }

    public void setSend_name(String send_name) {
        this.send_name = send_name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
