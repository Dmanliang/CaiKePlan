package com.example.caikeplan.logic.message;

/**
 * Created by dell on 2017/8/17.
 */

public class PersonMessage {

    private String user_id;
    private String username;
    private String due_time;
    private String power_add;

    public PersonMessage(String user_id,String username,String due_time,String power_add){
        this.user_id    = user_id;
        this.username   = username;
        this.due_time   = due_time;
        this.power_add  = power_add;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDue_time() {
        return due_time;
    }

    public void setDue_time(String due_time) {
        this.due_time = due_time;
    }

    public String getPower_add() {
        return power_add;
    }

    public void setPower_add(String power_add) {
        this.power_add = power_add;
    }
}
