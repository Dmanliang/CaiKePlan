package com.example.mainlist;

import com.example.caikeplan.logic.message.SendMessage;

//获取玩法计划数据
public class MainListBean
{
	private String cls_name;
	private String play_cls;
	private String play_id;
	private String plan_name;
	private String issue_count;
	private String num_count;
	private String plan_id;
	private String log_id;
	private String isCollect;
	private String lottery_id;
	private String play_name;

	public String getCls_name() {
		return cls_name;
	}

	public void setCls_name(String cls_name) {
		this.cls_name = cls_name;
	}

	public String getPlay_name() {
		return play_name;
	}

	public void setPlay_name(String play_name) {
		this.play_name = play_name;
	}

	public String getLog_id() {
		return log_id;
	}

	public void setLog_id(String log_id) {
		this.log_id = log_id;
	}

	public String getIsCollect() {
		return isCollect;
	}

	public void setIsCollect(String isCollect) {
		this.isCollect = isCollect;
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

	public String getUser_id()
	{
		return SendMessage.getInstance().getUser_id();
	}

	public String getMac()
	{
		return SendMessage.getInstance().getMac();
	}

	public String getPlan_name() {
		return plan_name;
	}

	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}

	public String getIssue_count() {
		return issue_count;
	}

	public void setIssue_count(String issue_count) {
		this.issue_count = issue_count;
	}

	public String getNum_count() {
		return num_count;
	}

	public void setNum_count(String num_count) {
		this.num_count = num_count;
	}

	public String getPlay_id() {
		return play_id;
	}

	public void setPlay_id(String play_id) {
		this.play_id = play_id;
	}

	public String getPlay_cls() {
		return play_cls;
	}

	public void setPlay_cls(String play_cls) {
		this.play_cls = play_cls;
	}

}