package com.yongyida.robot.voice.master.bean;


public class ChatBean extends BaseBean{
	public Answer answer;
	public Semantic semantic;
	public class Answer{
		public String type;
		public String text;
	}
	public class Semantic{
		public Slots slots;
	}
	
	public class Slots{
		public String isname;
		public String name;
		public String action;
		public String answer;
		
		//针对patch
		public String what;
	}
	
	public class CmccChatBean{
		public Semantic semantic;	
		public class Semantic{
			public Slots slots;
		}
		public class Slots{
			public String n;
			public String answer1;
			public String answer2;
			public String answer3;
			public String answer4;
			public String answer5;
			public String answer6;
			public String answer7;
			public String answer8;
			public String answer9;
			public String answer10;
			public String answer11;
			public String answer12;
			public String answer13;
			public String answer14;
			public String answer15;
			public String answer16;
			public String answer17;
			public String answer18;
			public String answer19;
			public String answer20;
		}
	}
}
