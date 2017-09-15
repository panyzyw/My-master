package com.yongyida.robot.voice.master.reciver;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.yongyida.robot.voice.master.activity.BaiduActivity;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyData;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.runnable.TelephoneRunnable;
import com.yongyida.robot.voice.master.service.VoiceSubService;
import com.yongyida.robot.voice.master.utils.HttpUtil;
import com.yongyida.robot.voice.master.utils.JsonParser;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.TelephoneUtil;
import com.yongyida.robot.voice.master.version.VersionControl;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class ServiceReceiver extends BroadcastReceiver {
    private static final String TAG = "ServiceReceiverTAG";

    public void onReceive(Context context, Intent intent) {

        if (!TelephoneUtil.phoneStateRinging) {
            if (intent == null) {
                return;
            }

            String action = intent.getAction();

            if (isExsitAction(action)) {
                MyLog.i("ServiceReceiver", "receiver action:" + action + ",from:" + intent.getStringExtra(MyData.FROM) + ",for:" + intent.getStringExtra(MyData.FOR));
                if (action.equals(MyIntent.INTENT_STOP) || action.equals(MyIntent.STOPOTHER)) {
                    if (!VoiceQueue.getInstance().isEmpty()) {
						String from = intent.getStringExtra(MyData.FROM);
						String forUse = intent.getStringExtra(MyData.FOR);
						if(!TextUtils.isEmpty(from) && !TextUtils.isEmpty(forUse) && from.contains("TelephoneRunnable") && forUse.contains("phone")){

						}else {
							VoiceQueue.getInstance().clearQueue();
						}
                    }

                    BaseRunnable.releaseResource();

//				Intent inte = new Intent(context,VoiceSubService.class);
//				context.stopService(inte);
                } else if (action.equals(MyIntent.INTENT_RECYCLE)) {

                } else { //启动服务的广播
                    String result = intent.getStringExtra("result");
                    if (TextUtils.isEmpty(result)) {
                        MyLog.i("ServiceReceiver", "result == null");
                    } else
                        startService(context, result, action);
                }
            }
        }
    }

	private void startService(Context context, String result, String action) {

		if (action.equals(MyIntent.INTENT_DANCE) || action.equals(MyIntent.INTENT_SQUAREDANCE)) {
//			if (result.contains("跳")) {
//				 final JSONObject json;
//				try {
//					json = new JSONObject(result);
//					 new Thread(new Runnable() {
//
//						@Override
//						public void run() {
//
//							try {
//								if(MasterApplication.USER_DI==null||MasterApplication.USER_DI.equals("")){
//									MasterApplication.USER_DI =	getSystemPlatform();
//
//								}
//								HttpUtil.setKey(json.getString("text"));
//								HttpUtil.SetUrlType(1);
//								String json = HttpUtil.HttpGet();
//								String RSVP_result = JsonParser.praseRSVPJsonResult(json);
//								if(RSVP_result!=null&&RSVP_result.contains("豆豆")){
//									RSVP_result = RSVP_result.replaceAll("豆豆", "我");
//								}
//								Log.d("jlog", "RSVP:"+RSVP_result);
//								voiceTTS(RSVP_result,null);
//							} catch (JSONException e) {
//								// TODO Auto-generated catch block
//								e.printStackTrace();
//							}
//						}
//					}).start();
//				} catch (JSONException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
//
//
//
//
//
//				/*voiceTTS(Constant.comman_unkonw, null); // 当接受到语音跳舞时，提示这个我还不会
//
//				String text = action.equals(MyIntent.INTENT_DANCE) ? "跳舞"
//						: "广场舞";
//
//				Intent intentBaidu = new Intent(MasterApplication.mAppContext,
//						BaiduActivity.class);
//				intentBaidu.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				intentBaidu.putExtra("text", text);
//				MasterApplication.mAppContext.startActivity(intentBaidu);*/
//			} else
				if (result.contains("-1")) // 摸摸双肩退出
			{
                Intent intent = new Intent(context, VoiceSubService.class);
				intent.setAction(action);
				intent.putExtra("result", result);
				context.startService(intent);
				return;
			} else // 听广场舞是要听的
			{
				Intent intent = new Intent(context, VoiceSubService.class);
				intent.setAction(action);
				intent.putExtra("result", result);
				context.startService(intent);
			}
		} else {
			Intent intent = new Intent(context, VoiceSubService.class);
			intent.setAction(action);
			intent.putExtra("result", result);
			context.startService(intent);
		}
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
    public boolean isExsitAction(String action) {
        if (action.equals(MyIntent.INTENT_STOP) || action.equals("com.yydrobot.TEST") ||
                action.equals(MyIntent.INTENT_MUSIC) || action.equals(MyIntent.INTENT_NEWS) || action.equals(MyIntent.INTENT_STOCK) ||
                action.equals(MyIntent.INTENT_WEATHER) || action.equals(MyIntent.INTENT_JOKE) || action.equals(MyIntent.INTENT_STORY) ||
                action.equals(MyIntent.INTENT_CHAT) || action.equals(MyIntent.INTENT_ENCYCLOPEDIAS) || action.equals(MyIntent.INTENT_POETRY) ||
                action.equals(MyIntent.INTENT_POETRY_STUDY) || action.equals(MyIntent.INTENT_REMIND) || action.equals(MyIntent.INTENT_TRIP) ||
                action.equals(MyIntent.INTENT_COSMETOLOGY) || action.equals(MyIntent.INTENT_GAME) || action.equals(MyIntent.INTENT_EBOOK) ||
                action.equals(MyIntent.INTENT_ARITHMETIC) || action.equals(MyIntent.INTENT_HABIT) || action.equals(MyIntent.INTENT_CALL) ||
                action.equals(MyIntent.INTENT_SINOLOGY) || action.equals(MyIntent.INTENT_STUDY) ||
                action.equals(MyIntent.INTENT_VIDEO) || action.equals(MyIntent.INTENT_SCHEDULE) || action.equals(MyIntent.INTENT_CAMERA) ||
                action.equals(MyIntent.INTENT_SQUAREDANCE) || action.equals(MyIntent.INTENT_QUSETION) || action.equals(MyIntent.INTENT_CANCEL) ||
                action.equals(MyIntent.INTENT_RECORD) || action.equals(MyIntent.INTENT_YYDCHAT) || action.equals(MyIntent.INTENT_MAP) ||
                action.equals(MyIntent.INTENT_DANCE) || action.equals(MyIntent.INTENT_OPERA) || action.equals(MyIntent.INTENT_HEALTH) ||
                action.equals(MyIntent.INTENT_COOKBOOK) || action.equals(MyIntent.INTENT_ACHT_ALL) || action.equals(MyIntent.INTENT_RECYCLE) ||
                action.equals(MyIntent.INTENT_SMARTHOME) || action.equals(MyIntent.INTENT_MOVIEINFO) || action.equals(MyIntent.STOPOTHER)
                )
            return true;
        return false;
    }

    public void voiceTTS(String text, TTS.MySynthesizerListener mySynthesizerListener) {
        MyLog.i("StockRunnable", "voiceTTS");
        if (mySynthesizerListener == null)
            mySynthesizerListener = new TTS.MySynthesizerListener();
        TTS tts = new TTS(text, BaseVoice.TTS, 2, 700, true, mySynthesizerListener);
        VoiceQueue.getInstance().add(tts);
    }
}
