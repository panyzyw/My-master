package com.yongyida.robot.voice.master.bean;


public class SmarthomeBean  extends BaseBean{
	public Semantic semantic;
	
	public class Semantic{
		public Slots slots;		
	}
	
	public class Slots{
		public String device;
		public String action;
		public String num;
	}
	
	public static class SmarthomeProtocolBean {
		public String device;
		public String action;
	}
}
