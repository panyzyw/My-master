package com.yongyida.robot.voice.master.bean;


public class SinologyBean extends BaseBean{
	public Semantic semantic;
	
	public class Semantic{
		public Slots slots;
	}
	
	public class Slots{
		public String author;
		public String dynasty;
		public String title;
		public String motion;
	}
}
