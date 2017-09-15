package com.yongyida.robot.voice.master.bean;

public class DanceBean extends BaseBean{
	public Semantic semantic;

	public class Semantic {
		public Slots slots;
	}
	//添加music信息  
	public class Slots {
		//music
		public String artist;
		public String song;
		public String category;	
	}
}
