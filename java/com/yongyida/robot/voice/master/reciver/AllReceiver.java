package com.yongyida.robot.voice.master.reciver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

import org.json.JSONObject;

import java.lang.reflect.Method;

/**
 * 监听所有来自讯飞的返回
 *
 * @author Administrator
 */
public class AllReceiver extends BroadcastReceiver {
public  JSONObject object = null;
    @Override
    public void onReceive( Context context, Intent intent) {
        // TODO Auto-generated method stub
        if (intent == null) {
            return;
        }

        if (intent.getAction().equals("com.yongyida.robot.PARSE_RESULT")) {
            String result = intent.getStringExtra("result");
            if (result != null) {
                MyLog.e("AllReceiver", "result = " + result);
               
              // JSONObject object = null;
                /*try {
                	 MasterApplication.rid = intent.getStringExtra("rid");
                    object = new JSONObject(result);
                    final int rc = Integer.parseInt(object.getString("rc"));
                    if(MasterApplication.USER_DI==null||MasterApplication.USER_DI.equals("")){
						MasterApplication.USER_DI =	getSystemPlatform();
						
					}
                   
                    //	String prase_result = HttpUtil.HttpGet(result); 
                    //	ArrayList<ParticipleBean> lists = JsonParser.praseJsonResult(prase_result);
*//*                        Intent intentBaidu = new Intent(MasterApplication.mAppContext, BaiduActivity.class);
                        intentBaidu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intentBaidu.putExtra("text", object.getString("text"));
                        MasterApplication.mAppContext.startActivity(intentBaidu);*//*
                   //获取薄言解析的结果，并语音播报 	
                    	new Thread(new Runnable() {
							
							@Override
							public void run() {
								
								try {
									if(rc!=0){
										//	HttpUtil.setKey(object.getString("text"));									
										HttpUtil.SetUrlType(2);
										String json = HttpUtil.submitPostData(RobotInfoUtils.getRobotName(), MasterApplication.USER_DI, object.getString("text"), "utf-8",rc);
										//String json = HttpUtil.doHttpPost(RobotInfoUtils.getRobotName(), MasterApplication.USER_DI, object.getString("text"));
										//String RSVP_result = JsonParser.praseRSVPJsonResult(json);
										String RSVP_result = JsonParser.praseServiceJsonResult(json);
										Log.d("jlog", json);
										if(RSVP_result!=null&&RSVP_result.contains("豆豆")){
											RSVP_result = RSVP_result.replaceAll("豆豆", "我");
										}else if(RSVP_result!=null&&RSVP_result.contains("小狗蛋")&&RSVP_result.contains("换一首")){
										//	{"ret":"0","answer":"小狗蛋没有找到你要听的，要不我给你换一首吧。"}
											RSVP_result = "我没有找到你要听的";
										}
										
										if(RSVP_result == null||RSVP_result.equals("")){
											Utils.sendBroadcast(MyIntent.INTENT_RECYCLE, "master", "");
											return;
										}*//*else if(RSVP_result.equals("")){
											RSVP_result = context.getString(R.string.comman_unkonw);
										}*//*
										
										Log.d("jlog", "RSVP:"+RSVP_result);
										voiceTTS(RSVP_result);																		
									}else if(rc == 0){
				                    	HttpUtil.SetUrlType(3);
				                    	String json = HttpUtil.submitPostData(RobotInfoUtils.getRobotName(), MasterApplication.rid, object.toString(), "utf-8",rc);
				                    	Log.d("jlog", json);
				                    }
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}).start();
                    
                } catch (JSONException e) {
                    e.printStackTrace();
                }*/
            } else {
                MyLog.i("AllReceiver", "result == null");
            }
        }
    }
    public void voiceTTS(String text) {
		voiceTTS(text, null);
	}
    
	public void voiceTTS(String text, String voicerName) {
		MyLog.i("ChatRunnable", "voiceTTS");
		TTS tts = new TTS(text, BaseVoice.TTS, 2, 700,
				true, new MySynthesizerListener());
		if (voicerName == null) {
			voicerName = Constant.xiaoai;
		}
		VoiceQueue.getInstance().add(tts);
	}
	
	private String getSystemPlatform() {
		String key = "gsm.serial";
		        Class<?> clazz;
		        try {
		            clazz = Class.forName("android.os.SystemProperties");
		            Method method = clazz.getDeclaredMethod("get", String.class);
		           String userid = (String) method.invoke(clazz.newInstance(), key);
		           if(userid!=null&&!userid.equals("")){
		        	   if(userid.contains("-")){
		        		   return userid.substring(0, userid.indexOf("-"));
		        	   }
		           }else{
		        	   return "";
		           }
		        } catch (Exception e) {
		            e.printStackTrace();
		        } 
		        return "";
		    }
}
