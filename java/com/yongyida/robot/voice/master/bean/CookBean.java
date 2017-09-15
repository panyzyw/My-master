package com.yongyida.robot.voice.master.bean;

import java.util.ArrayList;


public class CookBean extends BaseBean{
	public Data data;
	public Semantic semantic;
	public class Data{
		public ArrayList<Cook> result;
	}
	
	public class Cook{
		public String imgUrl;
		public String ingredient;
		public String source;
		public String url;
	}
	
	public class Semantic{
		public Slots slots;
	}
	
	public class Slots{
		public String dishName;
		public String ingredient;

		public String cookstyle;
		public String person;
		public String material;
		public String season;
		public String taste;
		public String effect;	
	}
	
}
