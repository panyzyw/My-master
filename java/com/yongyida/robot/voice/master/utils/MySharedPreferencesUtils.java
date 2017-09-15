package com.yongyida.robot.voice.master.utils;

import android.content.Context;
import android.text.TextUtils;

import com.yongyida.robot.voice.master.application.MasterApplication;

public class MySharedPreferencesUtils {

	private static String defaultSPName = "SharedPreferences";
	
	public static void putInt(String key,int value,String SharedPreferencesName){
		if(TextUtils.isEmpty(SharedPreferencesName)){
			SharedPreferencesName = defaultSPName;
		}
		MasterApplication.mAppContext.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE).edit().putInt(key, value).commit();
	}

	public static int getInt(String key,int defaultValue,String SharedPreferencesName){
		if(TextUtils.isEmpty(SharedPreferencesName)){
			SharedPreferencesName = defaultSPName;
		}
		return MasterApplication.mAppContext.getSharedPreferences(SharedPreferencesName, Context.MODE_PRIVATE).getInt(key, defaultValue);
	}
}
