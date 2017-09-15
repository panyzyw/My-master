package com.yongyida.robot.voice.master.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.BaseBean;
import com.yongyida.robot.voice.master.constant.MyData;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * 通用工具类
 * @author Administrator
 *
 */
public class Utils {
	private static Context context = MasterApplication.mAppContext;
    public static final String APP_NAME = "YYDRobotMaster";
	/**
	 * 发送广播
	 * @param action
	 */
	public static void sendBroadcast(String action){
		Intent intent = new Intent(action);	
		MasterApplication.mAppContext.sendBroadcast(intent);
	}
	
	/**
	 * 发送广播
	 * @param action
	 * @param from 广播的发送者
	 * @param for_   发送该广播的目的
	 */
	public static void sendBroadcast(String action, String from, String for_){
		Intent intent = new Intent(action);
		if(TextUtils.isEmpty(from)){
			from = "";
		}
		if(TextUtils.isEmpty(for_)){
			for_ = "";
		}
		intent.putExtra(MyData.FROM, from);
		intent.putExtra(MyData.FOR, for_);		
		MasterApplication.mAppContext.sendBroadcast(intent);
	}

	public static void sendBroadcast(String action, String result) {
		Intent intent = new Intent(action);
		if (TextUtils.isEmpty(result)) {
			result = "";
		}
		intent.putExtra(MyData.FROM, result);
		MasterApplication.mAppContext.sendBroadcast(intent);
	}

	/**
	 * 发送广播
	 * @param intent
	 */
	public static void sendBroadcast(Intent intent){
		MasterApplication.mAppContext.sendBroadcast(intent);
	}
	
	
	/**
	 * 启动activity
	 * @param cls
	 */
	public static void startActivity(Class<?> cls){
		MasterApplication.mAppContext.sendBroadcast(new Intent(MasterApplication.mAppContext,cls));
	}

    public static void collectInfoToServer(BaseBean bean,String answer){
        String info = "";
        if(bean != null ){
            try {
                JSONObject infoJsonObject = new JSONObject();
                infoJsonObject.put("semantic","");
                infoJsonObject.put("service",bean.service);
                infoJsonObject.put("operation",bean.operation);
                infoJsonObject.put("text",bean.text);
                infoJsonObject.put("answer",answer);
                info = infoJsonObject.toString();
                Log.i("collectInfoToServer:", info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            Log.i("collectInfoToServer:", "bean=null");
        }
        Intent intent = new Intent("com.yongyida.robot.COLLECT");
        intent.putExtra("collect_result",info);
        intent.putExtra("collect_from",APP_NAME);
        context.sendBroadcast(intent);
        Log.i("collectInfoToServer:", "com.yongyida.robot.COLLECT");
    }

	
}
