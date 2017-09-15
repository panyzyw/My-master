package com.yongyida.robot.voice.master.utils;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

public class MyLog2 {
	private static Uri uri = Uri.parse("content://com.yongyidarobot.voice/voice_log"); 

	public Context m_context;
	public String m_module_name;
	public String m_class_name;
	public String m_fun_name;
	public String m_info;
	public String m_tag;
	
	public MyLog2(Context context){
		m_context = context;
		m_module_name = "";
		m_class_name = "";
		m_fun_name = "";
		m_tag = "";
	}
	
	public MyLog2(Context context,String tag){
		m_context = context;
		m_module_name = "";
		m_class_name = "";
		m_fun_name = "";
		m_tag = tag;
	}
	
	public MyLog2(Context context,String tag,String module){
		m_context = context;
		m_module_name = module;
		m_class_name = "";
		m_fun_name = "";
		m_tag = tag;	
	}
	
	public MyLog2(Context context,String tag,String module,String class_name){
		m_context = context;
		m_module_name = module;
		m_class_name = class_name;
		m_fun_name = "";
		m_tag = tag;					
	}
	
	public MyLog2(Context context,String tag,String module,String class_name,String fun){
		m_context = context;
		m_module_name = module;
		m_class_name = class_name;
		m_fun_name = fun;
		m_tag = tag;				
	}
	
	public void log(String info){
		log_(m_context,m_module_name,m_class_name,m_fun_name,info,m_tag);
	}

	public void log(String tag,String info){
		log_(m_context,m_module_name,m_class_name,m_fun_name,info,tag);
	}	

	public void log(String module,String tag,String info){
		log_(m_context,module,m_class_name,m_fun_name,info,tag);
	}
	
	public void log(String module,String class_name,String tag,String info){
		log_(m_context,module,class_name,m_fun_name,info,tag);
	}
	
	public void log(String module,String class_name,String fun,String tag,String info){
		log_(m_context,module,class_name,fun,info,tag);
	}
	
	public void setModule(String module){
		m_module_name = module;
	}
	
	public void setClass(String class_name){
		m_class_name = class_name;
	}
	
	public void setFun(String fun){
		m_fun_name = fun;
	}
	
	public void setTag(String tag){
		m_tag = tag;
	}
	
	public void cleanAll(){
		cleanMCF();
		m_tag = "";
	}
	
	public void cleanMCF(){
		m_module_name = "";
		cleanCF();
	}
	
	public void cleanCF(){
		m_class_name = "";
		m_fun_name = "";
	}

	public void log_(Context context,String module_name,String class_name,String fun_name,String info,String tag){
		s_log_(context,module_name,class_name,fun_name,info,tag);
	}
	
	
	public static void sLog(Context context,String info) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues contentValues = new ContentValues();
		contentValues.put("datetime",DateUtils.timestamp2String(System.currentTimeMillis()));
		contentValues.put("package_name", context.getPackageName());
		contentValues.put("module_name", "");
		contentValues.put("class_name", "");
		contentValues.put("fun_name", "");
		contentValues.put("info", info);
		contentValues.put("tag", "");
		resolver.insert(uri, contentValues);
	}
	
	public static void sLog(Context context,String tag,String info) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues contentValues = new ContentValues();
		contentValues.put("datetime",DateUtils.timestamp2String(System.currentTimeMillis()));
		contentValues.put("package_name", context.getPackageName());
		contentValues.put("module_name", "");
		contentValues.put("class_name", "");
		contentValues.put("fun_name", "");
		contentValues.put("info", info);
		contentValues.put("tag", tag);
		resolver.insert(uri, contentValues);
	}
	
	public static void sLog(Context context,String module,String tag,String info) {
		ContentResolver resolver = context.getContentResolver();
		ContentValues contentValues = new ContentValues();
		contentValues.put("datetime",DateUtils.timestamp2String(System.currentTimeMillis()));
		contentValues.put("package_name", context.getPackageName());
		contentValues.put("module_name", module);
		contentValues.put("class_name", "");
		contentValues.put("fun_name", "");
		contentValues.put("info", info);
		contentValues.put("tag", tag);
		resolver.insert(uri, contentValues);
	}
	
	private static void s_log_(Context context,String module_name,String class_name,String fun_name,String info,String tag){
		ContentResolver resolver = context.getContentResolver();
		ContentValues contentValues = new ContentValues();
		contentValues.put("datetime",DateUtils.timestamp2String(System.currentTimeMillis()));
		contentValues.put("package_name", context.getPackageName());
		contentValues.put("module_name", module_name);
		contentValues.put("class_name", class_name);
		contentValues.put("fun_name", fun_name);
		contentValues.put("info", info);
		contentValues.put("tag", tag);
		
		resolver.insert(uri, contentValues);
	}
}
