package com.yongyida.robot.voice.master.constant;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master1.R;

public class MyData {
	//public  static final String TAG = "zx123";
	public  static final String FROM = "from";
	public  static final String FOR = "for";
    public final static String[] REPEAT_ANSWER = new String[]{"repeat_answer_1.mp3","repeat_answer_2.mp3","repeat_answer_3.mp3","repeat_answer_4.mp3","repeat_answer_5.mp3","repeat_answer_6.mp3","repeat_answer_7.mp3","repeat_answer_8.mp3"};
	public static final String VOICE_CONTENT_PROVIDER_AUTHORITY = "com.yongyidarobot.voice";
    public static final String[] REPEAT_ANSWER2 = new String[]{
            MasterApplication.mAppContext.getString(R.string.repeat_1),
            MasterApplication.mAppContext.getString(R.string.repeat_2),
            MasterApplication.mAppContext.getString(R.string.repeat_3),
            MasterApplication.mAppContext.getString(R.string.repeat_4),
            MasterApplication.mAppContext.getString(R.string.repeat_5),
            MasterApplication.mAppContext.getString(R.string.repeat_6),
            MasterApplication.mAppContext.getString(R.string.repeat_7),
            MasterApplication.mAppContext.getString(R.string.repeat_8),
            MasterApplication.mAppContext.getString(R.string.repeat_9),
            MasterApplication.mAppContext.getString(R.string.repeat_10),
            MasterApplication.mAppContext.getString(R.string.repeat_11),
            MasterApplication.mAppContext.getString(R.string.repeat_12),
            MasterApplication.mAppContext.getString(R.string.repeat_13),
            MasterApplication.mAppContext.getString(R.string.repeat_14),
            MasterApplication.mAppContext.getString(R.string.repeat_15),
            MasterApplication.mAppContext.getString(R.string.repeat_16),
            MasterApplication.mAppContext.getString(R.string.repeat_17),
            MasterApplication.mAppContext.getString(R.string.repeat_18),
            MasterApplication.mAppContext.getString(R.string.repeat_19),
            MasterApplication.mAppContext.getString(R.string.repeat_20),
            MasterApplication.mAppContext.getString(R.string.repeat_21),
    };

    public static final String[] NOT_SINOLOGY_ANSWER = new String[]{
            MasterApplication.mAppContext.getString(R.string.sinology_1),
            MasterApplication.mAppContext.getString(R.string.sinology_2),
            MasterApplication.mAppContext.getString(R.string.sinology_3),
            MasterApplication.mAppContext.getString(R.string.sinology_4),
            MasterApplication.mAppContext.getString(R.string.sinology_5),
            MasterApplication.mAppContext.getString(R.string.sinology_6)
    };

    public static final String[] NOT_COOK_ANSWER = new String[]{
            MasterApplication.mAppContext.getString(R.string.cook_1),
            MasterApplication.mAppContext.getString(R.string.cook_2),
            MasterApplication.mAppContext.getString(R.string.cook_3),
            MasterApplication.mAppContext.getString(R.string.cook_4)
    };
}
