package com.yongyida.robot.voice.master.bean;

public class RobotAgeBean {
	
	private String rid;
	private long oldTime;
	private long newTime;	
	private int year;
	private int month;
	private int day;
	private int ret;
	
	public int getRet() {
		return ret;
	}
	public void setRet(int ret) {
		this.ret = ret;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getDay() {
		return day;
	}
	public void setDay(int day) {
		this.day = day;
	}
	public String getRid() {
		return rid;
	}
	public void setRid(String rid) {
		this.rid = rid;
	}
	public long getOldTime() {
		return oldTime;
	}
	public void setOldTime(long oldTime) {
		this.oldTime = oldTime;
	}
	public long getNewTime() {
		return newTime;
	}
	public void setNewTime(long newTime) {
		this.newTime = newTime;
	}

}
