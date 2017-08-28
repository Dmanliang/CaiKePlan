package com.example.NextPage;

import java.util.ArrayList;
import java.util.List;

public class NextPageBean
{
	private String issue;
	private String issue_date;
	private String expect_time;
	private List<String> nums = new ArrayList<>();
	private String singledouble;
	private String startthree;
	private String middlethree;
	private String endthree;
	private String    type;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<String> getNums() {
		return nums;
	}

	public void setNums(List<String> nums) {
		this.nums = nums;
	}

	public String getSingledouble() {
		return singledouble;
	}

	public void setSingledouble(String singledouble) {
		this.singledouble = singledouble;
	}

	public String getStartthree() {
		return startthree;
	}

	public void setStartthree(String startthree) {
		this.startthree = startthree;
	}

	public String getMiddlethree() {
		return middlethree;
	}

	public void setMiddlethree(String middlethree) {
		this.middlethree = middlethree;
	}

	public String getEndthree() {
		return endthree;
	}

	public void setEndthree(String endthree) {
		this.endthree = endthree;
	}

	public String getIssue() {
		return issue;
	}

	public void setIssue(String issue) {
		this.issue = issue;
	}

	public String getIssue_date() {
		return issue_date;
	}

	public void setIssue_date(String issue_date) {
		this.issue_date = issue_date;
	}

	public String getExpect_time() {
		return expect_time;
	}

	public void setExpect_time(String expect_time) {
		this.expect_time = expect_time;
	}
}