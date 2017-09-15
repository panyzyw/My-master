package com.yongyida.robot.voice.master.bean;


public class MovieInfoBean extends BaseBean{
	
	public Semantic semantic;

	public class Semantic{
		public Slots slots;
	}
	
	public class Slots{
		public String area;
		public String classification;
		public String movie;
		public String query_movie;
		public String time;
		
		//还有参数没写,语义里面暂时没有
	}
}
