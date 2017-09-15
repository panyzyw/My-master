package com.yongyida.robot.voice.master.constant;

import android.content.Context;

import com.yongyida.robot.voice.master1.R;

public class Constant {
	
//	<!-- 中国移动 -->
	public static String answer_who_mother1;
	public static  String answer_who_mother2;
	public static  String[] answer_who_mothers;
	
	public static  String answer_who_father1;
	public static  String answer_who_father2;
	public static  String[] answer_who_fathers;
	
	public static  String your_father_mother_name;
	
	public static  String answer_home1;
	public static  String answer_home2;
	public static  String[] answer_homes;
	
	public static  String answer_boss;
	
	public static  String answer_maker1;
	public static  String answer_maker2;
	public static  String[] answer_makers;
//    <!-- 中国移动 -->
	
	
	//comman
	public static String comman_unkonw;
	
	
	public final static String SMARTHOME_ACTION = "com.yydrobot.SMARTHOME";
	public final static String SMARTHOME_ACTION_TO_SYSTEM = "com.yydrobot.VOICESMARTHOME";
	public final static int DEVICE_AIR_CONDITIONER = 0;
	public final static int DEVICE_TV = 1;
	public final static int ACTION_OPEN = 100;
	public final static int ACTION_STOP = 101;
	public final static String VOICE_TIP = "VOICE_TIP";
	public final static int VOICE_RECV = 10;
	public final static int VOICE_CONTENT = 11;

	//--->joke
	public static String joke;
	public static String joke_type_sport;
	public static String joke_type_cool;
	public static String joke_have_not_the_type_joke;
	public static String joke_have_not_the_joke;
	//<---joke
	
	//--->cook
	public static  String COOK_RECEIVER_ACTION = "com.yydrobot.COOKBOOK"; 
	public static  String QUERY_COOK_SERVER_URL = "http://server.yydrobot.com/menu/query";//服务器
	
	public static  String COOK_SOURCE ;
	public static String cook_web;
	public static String cook_not_find_the_cook;
	public static String cook_not_find;
	public static String cook_access_server_faile;
	
	public static String cook_commend_love_eat;
	public static String cook_child1;
	public static String cook_child2;
	
	public static String cook_de_step_list;
	public static String cook_step_content;
	public static String cook_no;
	public static String cook_step;
	public static String cook_temporary_not_have;
	//<---cook
	
	// ---> smarthome
	public static String not_find_device;
	public static String know;
	public static String not_adaptive = "";
	public static String smarthome_err_frequent_operation = "";
	
	//电视
	public static String openTV;
	public static String closeTV;
	public static String openMenu;
	public static String openSignal;
	public static String setTVMute;
	public static String up;
	public static String down;
	public static String left;
	public static String right;
	public static String turnup;
	public static String turndown;
	public static String openHomepage;
	public static String back;
	public static String quit;
	public static String ok;
	public static String changeNextChannel;
	public static String changePrevChannel;
	public static String TVSetChannel;
	public static String channel;
	public static String channelUnknow;
	public static String turnupVolum;
	public static String turndownVolum;
	public static String volumeMaxOperationTip;
	public static String volumeMinOperationTip;
	public static String volumeSetOperationTip;
	//空调
	public static String query;
	public static String query_false;
	public static String temperatrue_is;
	
	public static String openAircondition;
	public static String closeAircondition;
	public static String cool;
	public static String hot;
	public static String auto;
	public static String dry;
	public static String blowwind;	
	public static String windAuto;
	public static String strongWind;
	public static String midWind;
	public static String lowWind;
	public static String turnUpWindSpeed;
	public static String turnDownWindSpeed;
	public static String verticalWind;
	public static String horizontalWind;
	public static String openWind;
	public static String stopWind;
	public static String queryTemperatrue;
	public static String setTemperatrue;
	public static String temperatrue;
	public static String temperatrueUnkonw;
	public static String turnupTemperatrue;
	public static String turndownTemperatrue;

	public static String openSTB;
	public static String closeSTB;
	
	public static String negative;
	public static String one;
	public static String two;
	public static String two_;
	public static String three;
	public static String four;
	public static String five;
	public static String six;
	public static String seven;
	public static String eight;
	public static String nine;
	public static String ten;
	public static String ten_;
	public static String eleven;
	public static String twenty;
	public static String thirty;
	public static String forty;
	public static String negative_ten;
	public static String negative_ten_;
	public static String negative_twenty;
	public static String negative_thirty;
	public static String negative_forty;
	// <---- smarthome
	
	//--->sense
	public static String hellos1;
	public static String hellos2;
	public static String hellos3;
	
	public static String robotHellos1;
	public static String robotHellos2;
	public static String robotHellos3;	
	
	public static String open_sense;
	public static String close_sense;

	public static String sense_lable;
	public static String sense_open;
	public static String sense_close;
	//<---sense
	
	//--->sms
	public static final int SRC_COMPLETED  = 1;  // 合成语音 询问是否播放结束
	public static final int BODY_COMPLETED = 2; // 合成语音 播放内容结束
	public static final int UNDERSTAND_COMPLETED = 3; // 合成语音 播放内容结束
	public static final int DESTORY = 4; // 合成语音 播放内容结束
	public static final int RECOGNITION_COMPLETED = 5; // 合成语音 播放内容结束	
	public static String SERVICE = "readsms"; 
	
	public static String ringoff;
	public static String call;
	public static String ring;
	
	public static String recvfrom;
	public static String de_sms;
	public static String de_sms_ask;
	public static String sms_content_is;
	public static String readsms_yes1;
	public static String readsms_yes2;
	public static String readsms_yes3;
	public static String readsms_yes4;
	public static String readsms_yes5;
	public static String readsms_yes6;
	public static String readsms_yes7;
	public static String readsms_yes8;
	public static String readsms_yes9;
	public static String readsms_yes10;
	public static String readsms_yes11;
	 public static String readsms_yes12;

	public static String readsms_no1;
	public static String readsms_no2;
	public static String readsms_no3;
	public static String readsms_no4;
	
	//<---- sms
	
	//---> 聊天
    public static String action_settings;
    public static String robot_name_xy;
	public static String robot_default_name;
	public static String robot_no_name1;
	public static String robot_no_name2;
	public static String answer_name1;
	public static String answer_name2;
	public static String answer_name3;
	
	public static String set_name_answer1;
	public static String set_name_answer2;
	public static String set_name_answer3;
	public static String set_name_answer4;

	public static String is_name;
	public static String not_is_name;
	public static String CONTENT_ROBOT_NAME_URI;
	
	public static String chat_shutup1;
	public static String chat_shutup2;
	
	public static String chat_localism_close;
	public static String chat_localism_open;
	public static String chat_can_not_know;
	public static String chat_what;
	public static String chat_my_name;
	
	//
	
	public static String hunans1;
	public static String hunans2;
	public static String hunans3;
	public static String hunans4;
    
	public static String taiwans1;
	public static String  taiwans2;
	public static String taiwans3;
	public static String taiwans4;

	public static String dongbeis1;
	public static String dongbeis2;
	public static String dongbeis3;
	public static String dongbeis4;
	public static String dongbeis5;
	public static String dongbeis6;
	public static String dongbeis7;
	public static String dongbeis8;
	public static String dongbeis9;
	public static String dongbeis10;
	public static String dongbeis11;
	public static String dongbeis12;
	public static String dongbeis13;
	public static String dongbeis14;
	public static String dongbeis15;
	public static String dongbeis16;
	public static String dongbeis17;

	public static String sichuans1;
	public static String sichuans2;
	public static String sichuans3;
	public static String sichuans4;
	public static String sichuans5;
	public static String sichuans6;
	public static String sichuans7;
    
	public static String guangdongs1;
	public static String guangdongs2;
	public static String guangdongs3;
	public static String  guangdongs4;
	public static String guangdongs5;
	public static String  guangdongs6;
	public static String guangdongs7;
    
	public static String henans1;
	public static String henans2;
 
    public static String laughs1;
    public static String laughs2;
    public static String laughs3;
 
    public static String angrys1;
    public static String angrys2;
    public static String angrys3;
    public static String angrys4;
    public static String angrys5;
    public static String angrys6;
    public static String angrys7;
    public static String angrys8;  
    
    public static String crys1; 
    public static String crys2;
    public static String crys3;
    public static String crys4;
    public static String crys5;
    public static String crys6;
    public static String crys7;
    public static String crys8;
    public static String crys9;
    
    
    public static String hungrys1;
    public static String hungrys2;
    public static String hungrys3;
    public static String hungrys4;
    public static String hungrys5;

    public static String mengs1;
    public static String mengs2;
    public static String mengs3;
    public static String mengs4;
    public static String mengs5;
    public static String mengs6;
  
    public static String afraids1;
    public static String afraids2;
    public static String afraids3;
    public static String afraids4;
    public static String afraids5;
    public static String afraids6;

    public static String jiayous1;
    public static String jiayous2;
    public static String jiayous3;
    public static String jiayous4;
    public static String jiayous5;
        
    public static String learns1;
    public static String learns2;
    
    public static String sleeps1;
    public static String sleeps2;
    public static String sleeps3;
    public static String sleeps4;
    
    public static String qinqins1;
    public static String qinqins2;
        
    public static String quites1;
    public static String quites2;
    public static String quites3;
    
    public static String yuns1;
	public static String yuns2;
    
	public static String kus1;
    public static String kus2;
    public static String kus3;
    public static String kus4;
	
    
	public static String mEmotions1;

	public static String nEmotions1;

	//
	//发音人
	public static final String xiaoai = "aisxa"; // 童声
	public static final String nannan = "aisnn"; // 童声
	public static final String xiaomei = "xiaomei"; // 粤语
	public static final String aisxlin = "aisxlin"; // 台湾
	public static final String aisxqian = "aisxqian"; // 东北
	public static final String aisxrong = "aisxrong"; // 四川
	public static final String aisxkun = "aisxkun"; // 河南
	public static final String aisxqiang = "aisxqiang";// 湖南
	
	//使用各地方言的关键字
	public static  String[] hunans;
	public static  String[] taiwans ;
	public static  String[] dongbeis;
	public static  String[] sichuans ;
	public static  String[] guangdongs;
	public static  String[] henans;
	
	//表情的关键词
	public static String[] laughs;
	public static String[] angrys;
	public static String[] crys;
	public static String[] hungrys;
	public static String[] mengs;
	
	public static String[] afraids;
	public static String[] jiayous;
	public static String[] kus;
	public static String[] learns;
	public static String[] normals;
	public static String[] qinqins;
	public static String[] sleeps;
	public static String[] quites;
	public static String[] speaks;
	public static String[] yuns;
	
	
	//表情的正负
	public static String[] mEmotions;  // 中间
	public static String[] nEmotions;   //否定
	//表情
	public final static String ACTION_AFRAID = "com.yongyida.action.lockscreen.ACTION_AFRAID";//害怕 0
    public final static String ACTION_LAUGH = "com.yongyida.action.lockscreen.ACTION_LAUGH";//大笑 1
    public final static String ACTION_ANGRY = "com.yongyida.action.lockscreen.ACTION_ANGRY";//愤怒 2
    public final static String ACTION_CRY = "com.yongyida.action.lockscreen.ACTION_CRY";//哭泣 3
    public final static String ACTION_HUNGRY = "com.yongyida.action.lockscreen.ACTION_HUNGRY";//4
    public final static String ACTION_JIAYOU = "com.yongyida.action.lockscreen.ACTION_JIAYOU";//加油 5
    public final static String ACTION_KU = "com.yongyida.action.lockscreen.ACTION_KU";//6
    public final static String ACTION_LEARN = "com.yongyida.action.lockscreen.ACTION_LEARN";//学习 7
    public final static String ACTION_MENG = "com.yongyida.action.lockscreen.ACTION_MENG";//萌萌哒 8
    public final static String ACTION_NORMAL = "com.yongyida.action.lockscreen.ACTION_NORMAL";//安静状态 9
    public final static String ACTION_QINQIN = "com.yongyida.action.lockscreen.ACTION_QINQIN";//10
    public final static String ACTION_SLEEP = "com.yongyida.action.lockscreen.ACTION_SLEEP";//11
    public final static String ACTION_QUITE = "com.yongyida.action.lockscreen.ACTION_QUITE";//安静 12
    public final static String ACTION_SPEAK = "com.yongyida.action.lockscreen.ACTION_SPEAK";//说话 13
    public final static String ACTION_YUN = "com.yongyida.action.lockscreen.ACTION_YUN";//14
	//<--- 聊天
	
    
    //doDance
    public final static String ACTION_DODANCE = "com.yongyida.robot.voice.master.dance.remote";//开始跳舞的动作 remote
    public final static String ACTION_ORIGIN_DODANCE = "com.yongyida.robot.voice.master.dance.origin";//开始跳舞的动作 origin
    
    //motor
    public final static String ACTION_MOTORSERVICE = "com.yongyida.robot.MotorService";	//马达串口服务
    public final static String ACTION_MOTORPAKAGE = "com.yongyida.robot.motorcontrol";//马达串口服务包
    
	//---> helper
	
	public static String today_weather;
	public static String tomorrow_weather;
	public static String listen_news;
	public static String children_love_cooks;
	public static String player_joke;
	public static String player_music;
	public static String player_health;
	public static String player_chaild_music;
	public static String player_story;
	public static String helper_joke_recommend;
	public static String helper_health_recommend;
	public static String helper_story_recommend;
	public static String helper_today_weather_recommend;
	public static String helper_tomorrow_weather_recommend;
	public static String helper_news_recommend;
	public static String helper_music_recommend;
	public static String helper_cook_recommend;
	
	//<---helper
	
	// ---> music
	public static String cannot_find_music_server;
	// <--- music

	public static void initConstant(Context context){
		if( instance == null ){
			instance = new Constant(context);
		}
	}	
	private static Constant instance;
	private Constant(Context context) {
		
	    //中国移动
	    
		answer_who_mother1 = context.getString(R.string.answer_who_mother1);	
		answer_who_mother2 = context.getString(R.string.answer_who_mother2);	
		answer_who_mothers = new String[]{answer_who_mother1,answer_who_mother2};
		
		answer_who_father1 = context.getString(R.string.answer_who_father1);	
		answer_who_father2 = context.getString(R.string.answer_who_father2);	
		answer_who_fathers = new String[]{answer_who_father1,answer_who_father2};
		
		your_father_mother_name = context.getString(R.string.answer_who_father_mother);
		
		answer_home1 = context.getString(R.string.answer_home1);	
		answer_home2 = context.getString(R.string.answer_home2);	
		answer_homes = new String[]{answer_home1,answer_home2};
		
		answer_boss = context.getString(R.string.answer_boss);	
		
		answer_maker1 = context.getString(R.string.answer_maker1);	
		answer_maker2 = context.getString(R.string.answer_maker2);	
		answer_makers = new String[]{answer_maker1,answer_maker2};
	    //中国移动
		
		
		//comman
		comman_unkonw = context.getString(R.string.comman_unkonw);
		
		
		not_find_device = context.getString(R.string.not_find_device);
		know = context.getString(R.string.know);
		smarthome_err_frequent_operation = context.getString(R.string.smarthome_err_frequent_operation);
		
		//笑话
		joke = context.getString(R.string.joke);
		joke_type_sport = context.getString(R.string.joke_type_sport);
		joke_type_cool = context.getString(R.string.joke_type_cool);
		joke_have_not_the_type_joke = context.getString(R.string.joke_have_not_the_type_joke);
		joke_have_not_the_joke = context.getString(R.string.joke_have_not_the_joke);
		
		//菜谱
		COOK_SOURCE = context.getString(R.string.COOK_SOURCE);
		cook_web = context.getString(R.string.cook_web);
		cook_not_find_the_cook = context.getString(R.string.cook_not_find_the_cook);
		cook_not_find = context.getString(R.string.cook_not_find);
		cook_access_server_faile = context.getString(R.string.cook_access_server_faile);
		
		cook_commend_love_eat = context.getString(R.string.cook_commend_love_eat);
		cook_child1 = context.getString(R.string.cook_child1);
		cook_child2 = context.getString(R.string.cook_child2); 
		
		cook_de_step_list = context.getString(R.string.cook_de_step_list);
		cook_step_content = context.getString(R.string.cook_step_content);
		cook_no = context.getString(R.string.cook_no);
		cook_step = context.getString(R.string.cook_step);
		cook_temporary_not_have = context.getString(R.string.cook_temporary_not_have);
		
		//电视
		openTV = context.getString(R.string.openTV);
		closeTV = context.getString(R.string.closeTV);
		openMenu = context.getString(R.string.openMenu);
		openSignal = context.getString(R.string.openSignal);
		setTVMute = context.getString(R.string.setTVMute);
		up = context.getString(R.string.tvUp);
		down = context.getString(R.string.tvDown);
		left = context.getString(R.string.left);
		right = context.getString(R.string.right);
		turnup = context.getString(R.string.turnup);
		turndown = context.getString(R.string.turndown);
		openHomepage = context.getString(R.string.openHomepage);
		back = context.getString(R.string.back);
		quit = context.getString(R.string.quit);
		ok = context.getString(R.string.tvOk);
		changeNextChannel = context.getString(R.string.changeNextChannel);
		changePrevChannel = context.getString(R.string.changePrevChannel);
		TVSetChannel = context.getString(R.string.TVSetChannel);
		channel = context.getString(R.string.channel);
		channelUnknow = context.getString(R.string.channelUnknow);
		turnupVolum = context.getString(R.string.turnupVolum);
		turndownVolum = context.getString(R.string.turndownVolum);

		volumeMaxOperationTip = context.getString(R.string.volumeMaxOperationTip);
		volumeMinOperationTip = context.getString(R.string.volumeMinOperationTip);
		volumeSetOperationTip = context.getString(R.string.volumeSetOperationTip);
		
		//空调
		query = context.getString(R.string.query);
		query_false = context.getString(R.string.query_false);
		String temperatrue_is = context.getString(R.string.temperatrue_is);
		
		openAircondition = context.getString(R.string.openAircondition);
		closeAircondition = context.getString(R.string.closeAircondition);
		cool = context.getString(R.string.cool);
		hot = context.getString(R.string.hot);
		auto = context.getString(R.string.auto);
		dry = context.getString(R.string.dry);
		blowwind = context.getString(R.string.blowwind);
		windAuto = context.getString(R.string.windAuto);
		strongWind = context.getString(R.string.strongWind);
		midWind = context.getString(R.string.midWind);
		lowWind = context.getString(R.string.lowWind);
		turnUpWindSpeed = context.getString(R.string.turnUpWindSpeed);
		turnDownWindSpeed = context.getString(R.string.turnDownWindSpeed);
		verticalWind = context.getString(R.string.verticalWind);
		horizontalWind = context.getString(R.string.horizontalWind);
		openWind = context.getString(R.string.openWind);
		stopWind = context.getString(R.string.stopWind);
		queryTemperatrue = context.getString(R.string.queryTemperatrue);
		setTemperatrue = context.getString(R.string.setTemperatrue);
		temperatrue = context.getString(R.string.temperatrue);
		temperatrueUnkonw = context.getString(R.string.temperatrueUnkonw);
		turnupTemperatrue = context.getString(R.string.turnupTemperatrue);
		turndownTemperatrue = context.getString(R.string.turndownTemperatrue);

		openSTB = context.getString(R.string.openSTB);
		closeSTB = context.getString(R.string.closeSTB);
		
		negative = context.getString(R.string.negative);
		one = context.getString(R.string.one);
		two = context.getString(R.string.two);
		two_ = context.getString(R.string.two_);
		three = context.getString(R.string.three);
		four = context.getString(R.string.four);
		five = context.getString(R.string.five);
		six = context.getString(R.string.six);
		seven = context.getString(R.string.seven);
		eight = context.getString(R.string.eight);
		nine = context.getString(R.string.nine);
		ten = context.getString(R.string.ten);
		ten_ = context.getString(R.string.ten_);
		eleven = context.getString(R.string.eleven);
		twenty = context.getString(R.string.twenty);
		thirty = context.getString(R.string.thirty);
		forty = context.getString(R.string.forty);
		negative_ten = context.getString(R.string.negative_ten);
		negative_ten_ = context.getString(R.string.negative_ten_);
		negative_twenty = context.getString(R.string.negative_twenty);
		negative_thirty = context.getString(R.string.negative_thirty);
		negative_forty = context.getString(R.string.negative_forty);
		
		not_adaptive = context.getString(R.string.not_adaptive);
		
		//红外感应
		hellos1 = context.getString(R.string.hellos1);
		hellos2 = context.getString(R.string.hellos2);
		hellos3 = context.getString(R.string.hellos3);
		
		robotHellos1 = context.getString(R.string.robotHellos1);
		robotHellos2 = context.getString(R.string.robotHellos2);
		robotHellos3 = context.getString(R.string.robotHellos3);	
		
		open_sense = context.getString(R.string.open_sense);
		close_sense = context.getString(R.string.close_sense);
		
		sense_lable = context.getString(R.string.sense_lable);
		sense_open = context.getString(R.string.sense_open);
		sense_close = context.getString(R.string.sense_close);
		
		//短信
		ringoff = context.getString(R.string.ringoff);
		call = context.getString(R.string.call);
		ring = context.getString(R.string.ring);
		
		recvfrom = context.getString(R.string.recvfrom);
		de_sms = context.getString(R.string.de_sms);
		de_sms_ask = context.getString(R.string.de_sms_ask);
		sms_content_is = context.getString(R.string.sms_content_is);
		readsms_yes1 = context.getString(R.string.readsms_yes1);
		readsms_yes2 = context.getString(R.string.readsms_yes2);
		readsms_yes3 = context.getString(R.string.readsms_yes3);
		readsms_yes4 = context.getString(R.string.readsms_yes4);
		readsms_yes5 = context.getString(R.string.readsms_yes5);
		readsms_yes6 = context.getString(R.string.readsms_yes6);
		readsms_yes7 = context.getString(R.string.readsms_yes7);
		readsms_yes8 = context.getString(R.string.readsms_yes8);
		readsms_yes9 = context.getString(R.string.readsms_yes9);
		readsms_yes10 = context.getString(R.string.readsms_yes10);
		readsms_yes11 = context.getString(R.string.readsms_yes11);
		 readsms_yes12 = context.getString(R.string.readsms_yes12);

		readsms_no1 = context.getString(R.string.readsms_no1);
		readsms_no2 = context.getString(R.string.readsms_no2);
		readsms_no3 = context.getString(R.string.readsms_no3);
		readsms_no4 = context.getString(R.string.readsms_no4);
		
		//使用各地方言的关键字
		//湖南
		hunans1 = context.getString(R.string.hunans1);
	    hunans2 = context.getString(R.string.hunans2);
	    hunans3 = context.getString(R.string.hunans3);
	    hunans4 = context.getString(R.string.hunans4);	
		hunans = new String[]{hunans1,hunans2,hunans3,hunans4};
		//台湾
	    taiwans1 = context.getString(R.string.taiwans1);
	    taiwans2 = context.getString(R.string.taiwans2);
	    taiwans3 = context.getString(R.string.taiwans3);
	    taiwans4 = context.getString(R.string.taiwans4);
	    taiwans = new String[]{taiwans1,taiwans2,taiwans3,taiwans4};
	    //东北
	    dongbeis1 = context.getString(R.string.dongbeis1);
	    dongbeis2 = context.getString(R.string.dongbeis2);
	    dongbeis3 = context.getString(R.string.dongbeis3);
	    dongbeis4 = context.getString(R.string.dongbeis4);
	    dongbeis5 = context.getString(R.string.dongbeis5);
	    dongbeis6 = context.getString(R.string.dongbeis6);
	    dongbeis7 = context.getString(R.string.dongbeis7);
	    dongbeis8 = context.getString(R.string.dongbeis8);
	    dongbeis9 = context.getString(R.string.dongbeis9);
	    dongbeis10 = context.getString(R.string.dongbeis10);
	    dongbeis11 = context.getString(R.string.dongbeis11);
	    dongbeis12 = context.getString(R.string.dongbeis12);
	    dongbeis13 = context.getString(R.string.dongbeis13);
	    dongbeis14 = context.getString(R.string.dongbeis14);
	    dongbeis15 = context.getString(R.string.dongbeis15);
	    dongbeis16 = context.getString(R.string.dongbeis16);
	    dongbeis17 = context.getString(R.string.dongbeis17);
	    dongbeis = new String[]{dongbeis1,dongbeis2,dongbeis3,dongbeis4,dongbeis5,dongbeis6,dongbeis7,dongbeis8,dongbeis9,
	    		dongbeis10,dongbeis11,dongbeis12,dongbeis13,dongbeis14,dongbeis15,dongbeis16,dongbeis17};
	    //四川
	    sichuans1 = context.getString(R.string.sichuans1);
	    sichuans2 = context.getString(R.string.sichuans2);
	    sichuans3 = context.getString(R.string.sichuans3);
	    sichuans4 = context.getString(R.string.sichuans4);
	    sichuans5 = context.getString(R.string.sichuans5);
	    sichuans6 = context.getString(R.string.sichuans6);
	    sichuans7 = context.getString(R.string.sichuans7);	    
	    sichuans = new String[]{sichuans1,sichuans2,sichuans3,sichuans4,sichuans5,sichuans6,sichuans7};
	    //广东
	    guangdongs1 = context.getString(R.string.guangdongs1);
	    guangdongs2 = context.getString(R.string.guangdongs2);
	    guangdongs3 = context.getString(R.string.guangdongs3);
	    guangdongs4 = context.getString(R.string.guangdongs4);
	    guangdongs5 = context.getString(R.string.guangdongs5);
	    guangdongs6 = context.getString(R.string.guangdongs6);
	    guangdongs7 = context.getString(R.string.guangdongs7);	    
	    guangdongs = new String[]{guangdongs1,guangdongs2,guangdongs3,guangdongs4,guangdongs5,guangdongs6,guangdongs7};
	    //河南
	    henans1 = context.getString(R.string.henans1);
	    henans2 = context.getString(R.string.henans2);
	    henans = new String[]{henans1,henans2};
	    //表情关键词
	    //微笑
	    laughs1 = context.getString(R.string.laughs1);
	    laughs2 = context.getString(R.string.laughs2);
	    laughs3 = context.getString(R.string.laughs3);

	    laughs = new String[]{laughs1,laughs2,laughs3};
	    //愤怒
	    angrys1 = context.getString(R.string.angrys1);
	    angrys2 = context.getString(R.string.angrys2);
	    angrys3 = context.getString(R.string.angrys3);
	    angrys4 = context.getString(R.string.angrys4);    
	    angrys5 = context.getString(R.string.angrys5);    
	    angrys6 = context.getString(R.string.angrys6);    
	    angrys7 = context.getString(R.string.angrys7);    
	    angrys8 = context.getString(R.string.angrys8);    
	    
	    angrys = new String[]{angrys1,angrys2,angrys3,angrys4};
	    //哭
	    crys1 = context.getString(R.string.crys1);
	    crys2 = context.getString(R.string.crys2);
	    crys3 = context.getString(R.string.crys3);    
	    crys4 = context.getString(R.string.crys4); 
	    crys5 = context.getString(R.string.crys5); 
	    crys6 = context.getString(R.string.crys6); 
	    crys7 = context.getString(R.string.crys7); 
	    crys8 = context.getString(R.string.crys8); 
	    crys9 = context.getString(R.string.crys9); 
	    
	    crys = new String[]{crys1,crys2,crys3,crys4,crys5,crys6,crys7,crys8,crys9};
	    
	    //饿
	    hungrys1 = context.getString(R.string.hungrys1);
	    hungrys2 = context.getString(R.string.hungrys2);
	    hungrys3 = context.getString(R.string.hungrys3);
	    hungrys4 = context.getString(R.string.hungrys4);
	    hungrys5 = context.getString(R.string.hungrys5);	 
	    
	    hungrys = new String[]{hungrys1,hungrys2,hungrys3,hungrys4,hungrys5};
	    
	    //卖萌
	    mengs1 = context.getString(R.string.mengs1);
	    mengs2 = context.getString(R.string.mengs2);
	    mengs3 = context.getString(R.string.mengs3);
	    mengs4 = context.getString(R.string.mengs4);	    
	    mengs5 = context.getString(R.string.mengs5);	
	    mengs6 = context.getString(R.string.mengs6);	

	    mengs = new String[]{mengs1,mengs2,mengs3,mengs4,mengs5,mengs6};
	    
	    //害怕
	    afraids1 = context.getString(R.string.afraids1);
	    afraids2 = context.getString(R.string.afraids2);
	    afraids3 = context.getString(R.string.afraids3);
	    afraids4 = context.getString(R.string.afraids4);
	    afraids5 = context.getString(R.string.afraids5);
	    afraids6 = context.getString(R.string.afraids6);
	    
	    afraids = new String[]{afraids1,afraids2,afraids3,afraids4,afraids5,afraids6};
	    
	    //加油
	    jiayous1 = context.getString(R.string.jiayous1);
	    jiayous2 = context.getString(R.string.jiayous2);
	    jiayous3 = context.getString(R.string.jiayous3);
	    jiayous4 = context.getString(R.string.jiayous4);
	    jiayous5 = context.getString(R.string.jiayous5);
	    
	    jiayous = new String[]{jiayous1,jiayous2,jiayous3,jiayous4,jiayous5};
	    
	    //读书
	    learns1 = context.getString(R.string.learns1);
	    learns2 = context.getString(R.string.learns2);
	    
	    learns = new String[]{learns1,learns2};
	    
	    //睡觉
	    sleeps1 = context.getString(R.string.sleeps1);
	    sleeps2 = context.getString(R.string.sleeps2);
	    sleeps3 = context.getString(R.string.sleeps3);
	    sleeps4 = context.getString(R.string.sleeps4);
	    
	    sleeps = new String[]{sleeps1,sleeps2,sleeps3,sleeps4};
	    //亲亲
	    qinqins1 = context.getString(R.string.qinqins1);
	    qinqins2 = context.getString(R.string.qinqins2);
	       
	    qinqins = new String[]{qinqins1,qinqins2};
	    //安静
	    quites1 = context.getString(R.string.quites1);
	    quites2 = context.getString(R.string.quites2);
	    quites3 = context.getString(R.string.quites3);
	    
	    quites = new String[]{quites1,quites2,quites3};
	    //晕
	    yuns1 = context.getString(R.string.yuns1);
		yuns2 = context.getString(R.string.yuns2);
	    
		yuns = new String[]{yuns1,yuns2};
		//酷
		kus1 = context.getString(R.string.kus1);
	    kus2 = context.getString(R.string.kus2);
	    kus3 = context.getString(R.string.kus3);
	    kus4 = context.getString(R.string.kus4);
	    
	    kus = new String[]{kus1,kus2,kus3,kus4};
	    
	    //中间
	    mEmotions1 = context.getString(R.string.mEmotions1);
	    mEmotions = new String[]{mEmotions1};
	    //否定
	    nEmotions1 = context.getString(R.string.nEmotions1);				
	    nEmotions = new String[]{nEmotions1};
	
	    //	    
	    action_settings = context.getString(R.string.action_settings);
	    robot_name_xy = context.getString(R.string.robot_name_xy);
		robot_default_name = context.getString(R.string.robot_default_name);
		robot_no_name1 = context.getString(R.string.robot_no_name1);
		robot_no_name2 = context.getString(R.string.robot_no_name2);
		answer_name1 = context.getString(R.string.answer_name1);
		answer_name2 = context.getString(R.string.answer_name2);
		answer_name3 = context.getString(R.string.answer_name3);
		
		set_name_answer1 = context.getString(R.string.set_name_answer1);
		set_name_answer2 = context.getString(R.string.set_name_answer2);
		set_name_answer3 = context.getString(R.string.set_name_answer3);
		set_name_answer4 = context.getString(R.string.set_name_answer4);

		is_name = context.getString(R.string.is_name);
		not_is_name = context.getString(R.string.not_is_name);
		
		CONTENT_ROBOT_NAME_URI = context.getString(R.string.CONTENT_ROBOT_NAME_URI);
		
		chat_shutup1 = context.getString(R.string.chat_shutup1);
		chat_shutup2 = context.getString(R.string.chat_shutup2);
		
		chat_localism_close = context.getString(R.string.chat_localism_close);
		chat_localism_open = context.getString(R.string.chat_localism_open);
		chat_can_not_know = context.getString(R.string.chat_can_not_know);
		chat_what = context.getString(R.string.chat_what);
		chat_my_name = context.getString(R.string.chat_my_name);
		
		//helper
		today_weather = context.getString(R.string.today_weather);
		tomorrow_weather = context.getString(R.string.tomorrow_weather);
		listen_news = context.getString(R.string.listen_news);
		children_love_cooks = context.getString(R.string.children_love_cooks);
		player_joke = context.getString(R.string.player_joke);
		player_music = context.getString(R.string.player_music);
		player_health = context.getString(R.string.player_health);
		player_chaild_music = context.getString(R.string.player_chaild_music);
		player_story = context.getString(R.string.player_story);
		
		helper_joke_recommend = context.getString(R.string.helper_joke_recommend);
		helper_health_recommend = context.getString(R.string.helper_health_recommend);
		helper_story_recommend = context.getString(R.string.helper_story_recommend);
		helper_today_weather_recommend = context.getString(R.string.helper_today_weather_recommend);
		helper_tomorrow_weather_recommend = context.getString(R.string.helper_tomorrow_weather_recommend);
		helper_news_recommend = context.getString(R.string.helper_news_recommend);
		helper_music_recommend = context.getString(R.string.helper_music_recommend);
		helper_cook_recommend = context.getString(R.string.helper_cook_recommend);
		
		//music
		cannot_find_music_server  = context.getString(R.string.cannot_find_music_server);
	}
}
