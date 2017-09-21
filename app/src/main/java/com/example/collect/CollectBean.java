package com.example.collect;

public class CollectBean {

	private String 	plan_id;
	private String 	lottery_id;
	private String 	play_id;
	private String 	log_id;
	private String 	plan_name;
	private String 	s_id;
	private String  scheme_name;
	private String  lottery_name;
	private String  cls_name;
	private boolean isCollected;

	public CollectBean(String lottery_name,String cls_name,String plan_id,String play_id,String log_id,String plan_name,String s_id,String scheme_name,String lottery_id,boolean isCollected){
		this.lottery_name =lottery_name;
		this.cls_name =	cls_name;
		this.plan_id = plan_id;
		this.play_id = play_id;
		this.log_id  = log_id;
		this.plan_name = plan_name;
		this.scheme_name = scheme_name;
		this.s_id = s_id;
		this.lottery_id = lottery_id;
		this.isCollected = isCollected;
	}

	public String getLottery_name() {
		return lottery_name;
	}

	public void setLottery_name(String lottery_name) {
		this.lottery_name = lottery_name;
	}

	public String getCls_name() {
		return cls_name;
	}

	public void setCls_name(String cls_name) {
		this.cls_name = cls_name;
	}

	public String getScheme_name() {
		return scheme_name;
	}

	public void setScheme_name(String scheme_name) {
		this.scheme_name = scheme_name;
	}

	public String getS_id() {
		return s_id;
	}

	public void setS_id(String s_id) {
		this.s_id = s_id;
	}

	public String 	getLog_id() {
		return log_id;
	}
	public void 	setLog_id(String log_id) {
		this.log_id = log_id;
	}
	public String 	getPlan_id() {
		return plan_id;
	}
	public void 	setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}
	public String getLottery_id() {
		return lottery_id;
	}
	public void setLottery_id(String lottery_id) {
		this.lottery_id = lottery_id;
	}
	public String 	getPlay_id() {
		return play_id;
	}
	public void 	setPlay_id(String play_id) {
		this.play_id = play_id;
	}
	public boolean 	getIsCollected() {
		return isCollected;
	}
	public void 	setCollected(boolean collected) {
		isCollected = collected;
	}


	public void setPlan_name(String plan_name) {
		this.plan_name = plan_name;
	}

	public String getPlan_name() {
		return plan_name;
	}

	public boolean isCollected() {
		return isCollected;
	}


}