package com.yongyida.robot.voice.master.constant;

public class MyIntent {
	
	public static final String QUEUE_COMPLETE = "queue_complete"; //
	
	//诗词
	public static final String  POETRYLEARN_RECYCLE = "poetrylearn_recycle";
	public static final String  POETRYLEARN_RECOGNITION = "poetrylearn_recognition";
	//算术
	public static final String  ARITHMETIC_QUESTION_COMPLETE = "arithmetic_question_complete";
	public static final String  ARITHMETIC_ANSWER_COMPLETE = "arithmetic_answer_complete";
	//天气
	public static final String  WEATHER_LOCATECITY_COMPLETE = "weather_locatecity_complete";
	//---
	
	public static final String BOOT_ACTION_START = "android.intent.action.BOOT_COMPLETED"; 
	/*音乐*/
	public static final String INTENT_MUSIC = "com.yydrobot.MUSIC";
	/*新闻*/
	public static final String INTENT_NEWS = "com.yydrobot.NEWS";
	/*股票*/
	public static final String INTENT_STOCK = "com.yydrobot.STOCK";
	/*天气*/
	public static final String INTENT_WEATHER = "com.yydrobot.WEATHER";
	/*笑话*/
	public static final String INTENT_JOKE = "com.yydrobot.JOKE";
	/*故事*/
	public static final String INTENT_STORY = "com.yydrobot.STORY";
	/*聊天*/
	public static final String INTENT_CHAT = "com.yydrobot.CHAT";
	/*百科*/
	public static final String INTENT_ENCYCLOPEDIAS = "com.yydrobot.ENCYCLOPEDIAS";
	/*国学*/
	public static final String INTENT_POETRY = "com.yydrobot.POETRY";
	/*诗词学习*/
	public static final String INTENT_POETRY_STUDY = "com.yydrobot.POETRY_STUDY";
	/*提醒*/
	public static final String INTENT_REMIND = "com.yydrobot.REMIND";
	/*旅游*/
	public static final String INTENT_TRIP = "com.yydrobot.TRIP";
	/*美容*/
	public static final String INTENT_COSMETOLOGY = "com.yydrobot.COSMETOLOGY";
	/*游戏*/
	public static final String INTENT_GAME = "com.yydrobot.GAME";
	/*电子书*/
	public static final String INTENT_EBOOK = "com.yydrobot.EBOOK";
	/*算术*/
	public static final String INTENT_ARITHMETIC = "com.yydrobot.ARITHMETIC";
	/*习惯*/
	public static final String INTENT_HABIT = "com.yydrobot.HABIT";
	/*打电话*/
	public static final String INTENT_CALL = "com.yydrobot.CALL";
	/*保存用户交流语言*/
	public static final String INTENT_USER_VOICE = "com.yydrobot.SAVE_USER_VOICE";
	/*国学*/
	public static final String INTENT_SINOLOGY = "com.yydrobot.SINOLOGY";
	/*诗词*/
	public static final String INTENT_STUDY = "com.yydrobot.STUDY";
	/*影视资讯*/
	public static final String INTENT_VIDEO = "com.yydrobot.VIDEO";
	/*提醒*/
	public static final String INTENT_SCHEDULE = "com.yydrobot.SCHEDULE";
	/*拍照*/
	public static final String INTENT_CAMERA = "com.yydrobot.CAMERA";
	/*广场舞*/
	public static final String INTENT_SQUAREDANCE = "com.yydrobot.SQUAREDANCE";
	/*机器人提问*/
	public static final String INTENT_QUSETION = "com.yydrobot.QUSETION";
	/*取消*/
	public static final String INTENT_CANCEL = "com.yydrobot.CANCEL";
	/*摄影*/
	public static final String INTENT_RECORD = "com.yydrobot.SHOOT";
	/*游戏*/
	public static final String INTENT_YYDCHAT = "com.yydrobot.YYDCHAT";
	/*地图*/
	public static final String INTENT_MAP = "com.yydrobot.MAP";
	/*跳舞*/
	public static final String INTENT_DANCE = "com.yydrobot.DANCE";
	/*戏曲*/
	public static final String INTENT_OPERA = "com.yydrobot.OPERA";
	/*健康资讯*/
	public static final String INTENT_HEALTH = "com.yydrobot.HEALTH";
	/*影视资讯*/
	public static final String INTENT_MOVIEINFO = "com.yydrobot.MOVIEINFO";
	/*智能家居*/
	public static final String INTENT_SMARTHOME = "com.yydrobot.SMARTHOME";
	
	public static final String INTENT_SMS = "com.yydrobot.SMS";
	
	public static final String INTENT_COOKBOOK = "com.yydrobot.COOKBOOK";
	
	public static final String INTENT_STOP = "com.yydrobot.STOP";
	
	public static final String INTENT_RECYCLE = "com.yydrobot.RECYCLE";
	
	public static final String INTENT_START = "com.yydrobot.START";
	
	public static final String INTENT_ACHT_ALL = "com.yydrobot.ACHT_ALL";

	public static final String INTENT_INSIDEGOING_CALL = "com.yydrobot.INSIDEGOING_CALL";

	public static final String INTENT_VOICE_MUSIC_END = "com.yydrobot.VOICE_MUSIC_END";

	public static final String INTENT_MUSIC_NO_FOUND = "com.yydrobot.MUSIC_NO_FOUND";
	
	public static final String INTENT_MOVE = "com.yydrobot.CONTROLL";
	
	//播放提醒内容时停止其它语音
    public static final String STOPOTHER="com.yydrobot.STOPOTHER";
	
	//视频
	public static final String INTENT_ENTERVIDEO = "com.yydrobot.ENTERVIDEO";
	public static final String INTENT_EXITVIDEO = "com.yydrobot.EXITVIDEO";
	
	//监控
	public static final String ENTERMONITOR = "com.yydrobot.ENTERMONITOR";
	public static final String EXITMONITOR = "com.yydrobot.EXITMONITOR";
	
	public static final String INTENT_FORWARD = "{\"cmd\":\"/robot/push\",\"command\":\"{\"cmd\":\"move\",\"type\":\"forward\"}\"}";
	public static final String INTENT_BACK = "{\"cmd\":\"/robot/push\",\"command\":{\"cmd\":\"move\",\"type\":\"back\"}}";
	public static final String INTENT_TURN_LEFT = "{\"cmd\":\"/robot/push\",\"command\":\"{\"cmd\":\"move\",\"type\":\"turn_left\"}\"}";
	public static final String INTENT_TURN_RIGHT = "{\"cmd\":\"/robot/push\",\"command\":\"{\"cmd\":\"move\",\"type\":\"turn_right\"}\"}";
	public static final String INTENT_HEAD_UP = "{\"cmd\":\"/robot/push\",\"command\":\"{\"cmd\":\"move\",\"type\":\"head_up\"}\"}";
	public static final String INTENT_HEAD_DOWN = "{\"cmd\":\"/robot/push\",\"command\":\"{\"cmd\":\"move\",\"type\":\"head_down\"}\"}";
	public static final String INTENT_HEAD_LEFT = "{\"cmd\":\"/robot/push\",\"command\":\"{\"cmd\":\"move\",\"type\":\"head_left\"}\"}";
	public static final String INTENT_HEAD_RIGHT = "{\"cmd\":\"/robot/push\",\"command\":\"{\"cmd\":\"move\",\"type\":\"head_right\"}\"}";
	

}
