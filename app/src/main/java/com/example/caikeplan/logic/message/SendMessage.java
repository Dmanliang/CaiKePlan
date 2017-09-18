package com.example.caikeplan.logic.message;

import com.example.util.Util;
import com.youth.xframe.cache.XCache;

import java.util.ArrayList;
import java.util.List;

public class SendMessage {

	private String 				issue_count;												// 几码几期的期号
	private String 				num_count; 													// 几码几期的码数
	private String 				url; 														//中转的url
	private String 				bestURL  = "http://120.77.242.46:9550"; 					//服务器地址
	private double 				countYes, countNo; 											//记录对错的次数
	private int    				countCycles;												//记录周期的个数
	private double[]  			allNos   = new double[10];									//记录每个计划对应错误次数数组
	private int[]	  			allCycles= new int[10];										//记录每个计划对应周期个数数组
	private double[]  			accuracy = new double[10];									//计算正确率
	private double    			bestAccuray;
	private int 	  			maxPlan;
	private String 				user_id  	= "1";
	private String 				mac		 	= "1001";
	private String 				plan_id;
	private String 				play_id;
	private String 				play_cls 	="1";
	private String 				next_plan_num,next_issue,next_num,next_hit;
	private List<String>   		test 		= new ArrayList<>();
	private List<String> 		test2		= new ArrayList<>();
	private String				head 		=	"聚财盆";
	private String 				mid 		= 	"聚财盆";
	private String 				end			=	"聚财盆    仅供参考";							//复制的头中尾
	private String				NextPageName;													//方案分页的标题名字
	private String				LotteryName	=	"重庆时时彩";									//全局的彩种名字
	private String				program_plan_id;
	private int 				lottery_id;
	private int 				swich	  	= 0;												//开奖页倒计时的开关
	private String 				ranges;
	private String 				temphead,tempmid,tempend;
	private String              divider=" ";
	private String 				tempdivider=" ";

    public SendMessage() {
		for(int i=0;i<accuracy.length;i++){
			accuracy[i]=0;
			allNos[i]=0;
			allCycles[i]=0;
		}
	}

	private static SendMessage instance = new SendMessage();

	public static SendMessage getInstance() {
		return instance;
	}

	public void setMac(String mac) {
		this.mac = mac;
	}

	public void setUser_id() {
		this.user_id = Util.getPhoneId();
	}

	public void setOffTimer(int swich)//开奖页倒计时的开关的set方法
	{
		this.swich = this.swich+swich;
	}

	public int getOffTimer()//开奖页倒计时的开关的get方法
	{
		return swich;
	}

	public void setProgram_Plan_id(String id)//方案页跳转时带的计划参数set方法
	{
		this.program_plan_id = id;
	}

	public String getProgram_Plan_id()//方案页跳转时带的计划参数get方法
	{
		return program_plan_id;
	}

	public void setLotteryName(String name)//全局彩种名字set方法
	{
		this.LotteryName = name;
	}

	public String getLotteryName()//全局彩种名字get方法
	{
		return LotteryName;
	}

	public void setNextPageName(String name)//计划分页的标题set方法
	{
		this.NextPageName=name;
	}

	public String getNextPageName()//计划分页的标题
	{
		return NextPageName;
	}

	public void setCopyhead(String head)//获取复制的头部set方法
	{
		temphead = head;
		if(head.isEmpty()==true) {
			this.head="聚财盆"+"("+getLotteryName()+")";
		}
		else if(head.isEmpty()==false) {
			this.head = head;
		}
	}

	public String getCopyhead(){//获取复制的头部
		return head+"("+getLotteryName()+")";
	}

	public void setCopymid(String mid){//获取复制的中部set方法
		if(mid==null) {
			this.mid="聚财盆";
		}
		else if(mid!=null) {
			this.mid = mid;
		}
	}

	public void setTempMid(){
		tempmid = SendMessage.getInstance().getCopymid();
	}

	public void setTempdivider(){
		tempdivider = SendMessage.getInstance().getDivider();
	}

	public String getTempdivider() {
		return tempdivider;
	}

	public void setTempdivider(String tempdivider) {
		this.tempdivider = tempdivider;
	}

	public String getCopyEnd() {
		return end+"\n";
	}

	public void setCopyEnd(String end) {
		tempend = end;
		if(end.isEmpty()==true){
			this.end="聚财盆    仅供参考";
		}
		else if(end.isEmpty()==false){
			this.end = end;
		}
	}

	public String getCopymid(){//获取复制的中部
		return mid;
	}

	public void setIssueCount(String issue_count) {
		this.issue_count = issue_count;
	}

	public void setSHUZU(String ranges,String nums_type,String plan_num,String num,String hit,int pos,boolean isDanshi) {
		if(isDanshi){
			if(pos == 0){
				test.add("\n"+ranges+"    "+SendMessage.getInstance().getCopymid()+":"+nums_type+"    "+num+"    "+hit+"\n"+plan_num);
			}else{
				test.add("\n"+ranges+"    "+SendMessage.getInstance().getCopymid()+":"+nums_type+"    "+num+"    "+hit);
			}
		}else{
			test.add("\n"+ranges+"    "+SendMessage.getInstance().getCopymid()+":"+plan_num+"    "+num+"    "+hit);
		}
	}

	public void updateMid(){
		for(int i=0;i<test.size();i++){
			test.set(i,test.get(i).replace(tempmid,SendMessage.getInstance().getCopymid()));
		}
		//test.set(0,test.get(0).replace(tempdivider,SendMessage.getInstance().getDivider()));
	}

	public String getSHUZU(int pos) {
		return test.get(pos);
	}

	public void setCopyPlan(String ranges,String nums_type,String plan_num,String num,String hit,int pos,boolean isDanshi){
		if(isDanshi){
			if(pos%5==0){
				test2.add("\n"+ranges+"    "+SendMessage.getInstance().getCopymid()+":"+nums_type+"    "+num+"    "+hit+"\n"+plan_num);
			}else{
				test2.add("\n"+ranges+"    "+SendMessage.getInstance().getCopymid()+":"+nums_type+"    "+num+"    "+hit);
			}
		}else{
			test2.add("\n"+ranges+"    "+SendMessage.getInstance().getCopymid()+":"+plan_num+"    "+num+"    "+hit);
		}
	}

	public void setLotteryTitle(String title){
		test2.add("\n"+title);
	}

	public List<String> getTest() {
		return test;
	}

	public void setTest(List<String> test) {
		this.test = test;
	}

	public List<String> getTest2() {
		return test2;
	}

	public void setTest2(List<String> test2) {
		this.test2 = test2;
	}

	public String getCopyPlan(int pos){
		return test2.get(pos);
	}

	public String getmIssueCount() {
		return issue_count;
	}

	public void setNumCount(String num_count) {
		this.num_count = num_count;
	}

	public String getNumCount() {
		return num_count;
	}

	public void setURL(String url) {
		this.url = url;
	}

	public String getURL() {
		return url;
	}

	public String getBestURL() {
		return bestURL;
	}

	public void setCountYes(int count) {
		this.countYes = count;
	}

	public double getCountYes() {
		return countYes;
	}

	public void setCountNo(int pos,int count) {
		this.countNo = count;
		allNos[pos]=count;
	}

	public double getCountNo(int pos) {
		return allNos[pos];
	}

	public void setAccuracy(int position,double accuracy)
	{
		this.accuracy[position] = accuracy;
	}
	public double getAccuracy(int index)
	{
		return accuracy[index];
	}

	public void selectOrder(double[] numbers){
		int maxindex=0;
		double max = numbers[0];
		for (int i = 1; i < numbers.length; i++) {
			if (max < numbers[i]) {
				max = numbers[i];
				maxindex=i;
			}
		}
		setMaxPlan(maxindex);
		setBestAccuray(max);
	}

	public String getUser_id()
	{
		return user_id;
	}

	public String getMac()
	{
		return mac;
	}

	public void setLotteryId(int lottery_id) {
		this.lottery_id = lottery_id;
	}

	public int getLotteryId() {
		return lottery_id;
	}

	public void setPlay_id(String play_id) {
		this.play_id = play_id;
	}

	public String getPlay_id() {
		return play_id;
	}

	public void setPlan_id(String plan_id) {
		this.plan_id = plan_id;
	}

	public String getPlan_id() {
		return plan_id;
	}

	public void setNextPlanNum(String plan_num) {
		this.next_plan_num = plan_num;
	}

	public String getNextPlanNum() {
		return next_plan_num;
	}

	public void setNextIssue(String issue) {
		this.next_issue = issue;
	}

	public String getNextIssue() {
		return next_issue;
	}

	public void setNextNum(String num) {
		this.next_num = num;
	}

	public String getNextNum() {
		return next_num;
	}

	public void setNextHit(String hit) {
		this.next_hit = hit;
	}

	public String getNextHit() {
		return next_num;
	}

	public int getCountCycles(int pos) {
		return allCycles[pos];
	}

	public void setCountCycles(int pos,int countCycles) {
		this.allCycles[pos] = countCycles;
	}

	public int getMaxPlan() {
		return maxPlan;
	}

	public void setMaxPlan(int maxPlan) {
		this.maxPlan = maxPlan;
	}

	public double getBestAccuray() {
		return bestAccuray;
	}

	public void setBestAccuray(double bestAccuray) {
		this.bestAccuray = bestAccuray;
	}

	public String getPlay_cls() {
		return play_cls;
	}

	public void setPlay_cls(String play_cls) {
		this.play_cls = play_cls;
	}

	public String getRanges() {
		return ranges;
	}

	public void setRanges(String ranges) {
		this.ranges = ranges;
	}

    public String getDivider() {
        return divider;
    }

    public void setDivider(String divider) {
		if(divider != null){
			this.divider = divider;
		}
    }

}