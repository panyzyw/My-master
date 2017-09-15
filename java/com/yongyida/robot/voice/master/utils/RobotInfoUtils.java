package com.yongyida.robot.voice.master.utils;

import java.util.Random;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.version.VersionControl;
/**
 * 该工具类是 处理 机器人 自定义信息 的 类
 * @author Administrator
 *
 */
public class RobotInfoUtils {
	public static final String[] set_name_answer= { "好的,我叫" , "记住了,我叫" , "记住了,以后我就叫" ,"记住了,以后我的名字是" };
	public static final String[] answer_name= { "我叫" , "我的名字是" , "你可以叫我" };
	/**
	 * 从系统获取机器人的名字
	 * @param context
	 * @return
	 */
	public static String getRobotName(){
		Context context = MasterApplication.mAppContext;
		//从系统获取用户设置的名字
		Uri uri = Uri.parse(Constant.CONTENT_ROBOT_NAME_URI);
		ContentResolver resolver = context.getContentResolver();
		
		String name = null;
		Cursor cursor = resolver.query(uri, null, null, null, null); 
		if(cursor != null){
			if(cursor.moveToFirst()){
				 name = cursor.getString(cursor.getColumnIndex("name")); //获取机器人的名字
			}		
			cursor.close();
		}else
			Tip.getInstance(context, Tip.isDebug).log_i("cursor == null", "getRobotName", "TempUtils");
				
		if( TextUtils.isEmpty(name) )
			return Constant.robot_default_name;
		return name;		
	}
	
	/**
	 * 查询名字时的回答
	 * @param context
	 * @return
	 */
	public static String getQueryNameAnswer(){
		Context context = MasterApplication.mAppContext;

		try{
			String name = getRobotName();
			
			if(VersionControl.mVersion == VersionControl.VERSION_CMCC ||
					VersionControl.mVersion == VersionControl.VERSION_CMCC_HK){
				
				if(name.equals(Constant.robot_name_xy)){
					return Constant.robot_no_name2;
				}
				
				if(name.equals(".")){				
					return Constant.robot_no_name1;
				}
				
				if(name.equals("#")){
					return Constant.robot_no_name2;
				}
			}
	
			int n = getRandUnsignedInt()%answer_name.length;
			String robotName = answer_name[n] + name;
			MyLog.i("RobotInfoUtils", "robotName = " + robotName);
			return robotName;
		}catch(Exception e){
			MyLog.e("RobotInfoUtils", MyExceptionUtils.getStringThrowable(e));
		}
			
		return Constant.robot_default_name;
	}
	
	/**
	 * 修改机器人名字,并返回聊天回答
	 * @param context
	 * @param name
	 * @return
	 */
	public static String updateRobotName(String name){
		Context context = MasterApplication.mAppContext;
		try{
			int n = getRandUnsignedInt()%set_name_answer.length;
			
			//修改名字
			Uri uri = Uri.parse(Constant.CONTENT_ROBOT_NAME_URI);
			ContentResolver resolver = context.getContentResolver();
			
		    ContentValues values = new ContentValues();
		    values.put("name", name);
		    resolver.update(uri, values, null, null);
			Tip.getInstance(context, Tip.isDebug).log_d("set name :" + name, "updateRobotName", "RobotInfoUtils");
		    return set_name_answer[n] + name;
		}catch(Exception e){
			e.printStackTrace();
			Tip.getInstance(context, Tip.isDebug).log_e("update name throw exception", "updateRobotName", "RobotInfoUtils");
		}
		return Constant.chat_my_name + Constant.robot_default_name;
	}
	
	/**
	 * 获取随机数
	 * @return
	 */
	public static int getRandUnsignedInt(){
		Random r =  new Random();
		return Math.abs(r.nextInt());
	}
}
