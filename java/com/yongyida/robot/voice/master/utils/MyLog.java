package com.yongyida.robot.voice.master.utils;

import android.util.Log;

public class MyLog {
	public static void i(String tag,String msg){
		Log.i(tag,msg);
		//写入文件,异步  文件名 从application 获取
		//...
	}
	
	public static void d(String tag,String msg){
		Log.d(tag,msg);
		//写入文件,异步
		//...
	}

	public static void e(String tag,String msg){
		Log.e(tag,msg);
		//写入文件,异步
		//...  内容前加上error:\n
	}
	
	/**
	 * 
	 * @param tag
	 * @param msg
	 * @param isPrtTimestamp 用于判断是不是要打印当前时间戳,一般用于测试 程序执行的耗时
	 */
	public static void i(String tag,String msg,boolean isPrtTimestamp){
		i( tag, msg);
		
		if(isPrtTimestamp)
			Log.i(tag,getCurrentTime());
	}
	
	public static void d(String tag,String msg,boolean isPrtTimestamp){
		d( tag, msg);
		
		if(isPrtTimestamp)
			Log.d(tag,getCurrentTime());
	}

	public static void e(String tag,String msg,boolean isPrtTimestamp){
		e( tag, msg);
		
		if(isPrtTimestamp)
			Log.e(tag,getCurrentTime());
	}
	
	
	private static String getCurrentTime(){
		return System.currentTimeMillis() + "";
	}
	
}
