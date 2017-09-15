package com.yongyida.robot.voice.master.bean;



public class MusicBean  extends BaseBean{
	public Semantic semantic;

	public class Semantic {
		public Slots slots;
	}

	public class Slots {
		//music
		public String artist;
		public String song;
		public String category;
		
		//story
		public String department;
		public String name;
	
		//opera
		
		//squaredance
		
	}
}
