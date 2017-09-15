package com.yongyida.robot.voice.master.runnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import org.apache.http.entity.StringEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.os.RemoteException;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.DanceBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.dance.DanceServer;
import com.yongyida.robot.voice.master.dance.DanceServer.RemoteBinder;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.MySharedPreferencesUtils;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.utils.VideoMonitoring;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.MP.MyOnCompletionListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnStartListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnStopListener;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;



public class DanceRunnable extends BaseRunnable{
	
	public final static String SpKey = "dance";
	
//	public static Motor motor;
	private static Timer timer;
	//添加识别歌曲代码
	private DanceBean danceBean;
	private static String mRequestURl;
	private static String mResourceURl;	
	private static String mMusicType; //Musics , Storys , ... 
	private static Map<String, String> mHashmap;
	int motorStatus;	//跳舞返回值
	private Context context;
	
	private String httpBackMusicUri;
	
	private static DanceServer motor;
	ServiceConnection conn = new ServiceConnection() {
		
		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			// TODO Auto-generated method stub
			MyLog.i("DanceRunnable", "onServiceDisconnected(ComponentName arg0)");
		}
		
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			// TODO Auto-generated method stub
			MyLog.i("DanceRunnable", "onServiceConnected(ComponentName arg0, IBinder arg1)");
			RemoteBinder remoteBinder = (RemoteBinder)arg1;
			motor = remoteBinder.getbindser();
            
            if(isNeedDance){
				isNeedDance = false ;

				touchDoubleshoulders() ;
			}
		}
	};
	
	public DanceRunnable(Context context,String result){
		MyLog.i("DanceRunnable", "DanceRunnable(Context context,String result)");
		this.context = context;
		
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
		Intent intent =  new Intent();
		intent.setAction(Constant.ACTION_DODANCE);	//bind service
		intent.setPackage(context.getPackageName());	//5.1以后绑定服务需要包名;包名为服务所在的APP
		context.startService(intent);
		if(context.bindService(intent, conn, Context.BIND_AUTO_CREATE))
		{
			MyLog.i("DanceRunnable", "Bind Motor Service Successful");
		}
	}
	
	public DanceRunnable(String result){
		MyLog.i("DanceRunnable", "DanceRunnable(String result)");
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}
	
    private boolean isNeedDance = false ;
    
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("DanceRunnable", "run()");
		
		//摸双肩 启动跳舞
		if(mResult.equals("-1")){
			MyLog.i("DanceRunnable", "touch double shoulders");
//			doDance(null,null);
        
            if(motor != null){
                
                touchDoubleshoulders() ;

				isNeedDance = false ;
            }else{
                
                 isNeedDance = true ;
            }

			return ;
		}
		
		danceBean = BeanUtils.parseDanceJson(mResult, DanceBean.class);
		if(  danceBean == null ){
			MyLog.e("DanceRunnable", "danceBean == null");
			
			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
			return ;
		}
		//添加网络歌曲识别
		initRequest(danceBean);	//获取歌曲网址
		requestDanceServerAndPlay();
		//doDance(danceBean,null);		
	}

	/**向后退*/
	private void touchDoubleshoulders(){

		String[] name = {"1","11","13","15","16","18","19","33","42","49"} ;
		int index = new Random().nextInt(name.length) ;
		voiceMp("touch_shoulder_"+name[index]+".mp3" ,null,null,null) ;

		motor.touchDoubleshoulders() ;
	}

	
	private void doDance(DanceBean danceBean , String url) {
		if (VideoMonitoring.isVideoing(MasterApplication.mAppContext)) { // 如果正在视频监控就不执行跳舞
			MyLog.e("DanceRunnable","is videoing or monitoring");
			return;
		}
		String mUrl = url;
		//motor  = (Motor) MasterApplication.mAppContext.getSystemService("motor");
		if(mUrl == null ||mUrl.equals("")|| mHashmap.get("name") == null || mHashmap.get("name").equals("")|| !(httpBackMusicUri.equalsIgnoreCase(mHashmap.get("name")+".mp3")))	//没有找到歌曲名，或者http网络没有匹配歌曲
		{
			int index = MySharedPreferencesUtils.getInt(SpKey, 0, null); //获取这次要播放的跳舞音乐名(0 - 19 .mp3)
			mUrl = index + ".mp3";	//如果没有识别到歌曲就顺序播放十首歌曲
			MyLog.i("DanceRunnable", "dance music is :" + index + ".mp3");
			index++;
			if(index > 19){ 
				MySharedPreferencesUtils.putInt(SpKey, 0, null);
			}else
			{
				MySharedPreferencesUtils.putInt(SpKey, index, null);	//二十首歌循环播放
			}
		}
		prepareDance(mUrl);
	}

	private void prepareDance(String uri) {		

		MyOnStartListener onStartListener = new MyOnStartListener(){
			@Override
			public void onStart() {
				// TODO Auto-generated method stub
				MyLog.i("DanceRunnable", "onStart");
//				try {
//					motor.robot_start_dance(MySharedPreferencesUtils.getInt(SpKey, 0, null));	//开始执行跳舞
//				} catch (RemoteException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//					final ArrayList<Intent> intents = new ArrayList<Intent>();
//					intents.add(new Intent("com.yongyida.action.lockscreen.ACTION_LAUGH"));
//					intents.add(new Intent("com.yongyida.action.lockscreen.ACTION_JIAYOU"));
//					intents.add(new Intent("com.yongyida.action.lockscreen.ACTION_KU"));
//					intents.add(new Intent("com.yongyida.action.lockscreen.ACTION_LEARN"));
//					intents.add(new Intent("com.yongyida.action.lockscreen.ACTION_SPEAK"));
//					intents.add(new Intent("com.yongyida.action.lockscreen.ACTION_MENG"));
//					intents.add(new Intent("com.yongyida.action.lockscreen.ACTION_QINQIN"));
//					
//					timer = new Timer();					
//					
//					timer.schedule(new TimerTask() {
//						@Override
//						public void run() {
//							Intent intent = intents.get(new Random().nextInt(7));
//							MyLog.i("DanceService_emoji","danceing : send emotion = " + intent.getAction());
//							MasterApplication.mAppContext.sendBroadcast(intent);						
//						}
//					}, 0, 2900);


				if(motor != null){

					try {
						motor.robot_start_dance(MySharedPreferencesUtils.getInt(SpKey, 0, null));	//开始执行跳舞
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}


				final String[] emojis = {"laugh","jiayou","ku","learn","speak","meng","qinqin"};
				final Intent intent = new Intent("com.yydrobot.emotion.CHAT") ;

				timer = new Timer();
				timer.schedule(new TimerTask() {
					@Override
					public void run() {

						String emoji = emojis[new Random().nextInt(7)];
						intent.putExtra("emoji",emoji) ;
						MyLog.i("DanceService_emoji","danceing : send emotion = " + emoji);
						MasterApplication.mAppContext.sendBroadcast(intent);
					}
				}, 0, 2900);
				
			}
		};
		
		MyOnStopListener onStopListener = new MyOnStopListener(){
			public void onStop() {
				MyLog.i("DanceRunnable", "onStop");
				stopDance();
			};
		};
		
		MyOnCompletionListener completionListener = new MyOnCompletionListener(){

			@Override
			public void onCompletion(MediaPlayer mp) {
				// TODO Auto-generated method stub
				MyLog.i("DanceRunnable", "onCompletion");
				stopDance();
			}
		};
		if(getDanceStation())	//获取跳舞服务状态
		{
			voiceMp(uri,completionListener,onStartListener,onStopListener);	
		}
	}
	
	public boolean getDanceStation() {
		MyLog.i("DanceRunnable", "getDanceStation()");
		try {
			motorStatus = motor.get_motor_station();	//获取跳舞服务状态
			if (motorStatus == 0) {//底层调用失败
				MyLog.e("DanceRunnable", "getDanceStation fail , system start dance fail");
				voiceTTS(MasterApplication.mAppContext.getString(R.string.tq004_dance_dianyadi),null); //读电压低
				return false;
			} else if (motorStatus == 2) {//底层电压低
				MyLog.i("DanceRunnable", "getDanceStation fail , power low");
				voiceTTS(MasterApplication.mAppContext.getString(R.string.tq004_dance_dianyadi),null);//读电压低
				return false;
			} else if (motorStatus == 1) {//成功
				MyLog.i("DanceRunnable", "getDanceStation sucess");
				return true;
			}
		} catch (Throwable e) {
			MyLog.e("DanceRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		return false;
	}

	public static void stopDance() {
		MyLog.i("DanceRunnable", "stopDance()");
		try {					
			if(motor != null)
				motor.robot_stop_dance();
			if(timer != null){
				timer.cancel();
				timer = null;
			}

		} catch (Throwable e) {
			MyLog.e("DanceRunnable", MyExceptionUtils.getStringThrowable(e));
		}
	}
	
	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("DanceRunnable", "voiceTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(danceBean,text);
	}
	
	public void voiceMp(String uri,MyOnCompletionListener onCompletionListener,MyOnStartListener onStartListener,MyOnStopListener onStopListener) {
		MyLog.i("DanceRunnable", "voiceMp");
		if(onStartListener == null){
			onStartListener = new MyOnStartListener();
		}
		if(onStopListener == null){
			onStopListener = new MyOnStopListener();
		}		
		if(onCompletionListener == null){
			onCompletionListener = new MyOnCompletionListener();
		}
		
		MP mp = new MP(uri,BaseVoice.MP_ASSETS_RESOURCE,mInterrupt_state,mPriority,mIsEndSendRecycle,null,onCompletionListener,onStartListener,onStopListener);
		VoiceQueue.getInstance().add(mp);
	}	
	//http申请播放歌曲
	private void initRequest(DanceBean danceBean) {
		MyLog.i("DanceRunnable", "server : " + danceBean.service);
			mRequestURl = Domain.getMusicRequestURl();			
			mHashmap = getMusicParam(danceBean);
			
			mResourceURl = Domain.getMusicResourceURl();
			mMusicType = "Musics";	
		MyLog.i("DanceRunnable", "requestURl : " + mRequestURl);
		MyLog.i("DanceRunnable", "param : " + mHashmap.toString());
		MyLog.i("DanceRunnable", "mResourceURl : " + mResourceURl);
	}
	
	private Map<String, String> getMusicParam(DanceBean mb){
		MyLog.i("DanceRunnable", "getMusicParam");
		
		Map<String, String> hashmap = new HashMap<String, String>();
		hashmap.put("name", "");
		hashmap.put("singer", "");
		hashmap.put("type", "");
		hashmap.put("area", "");
		hashmap.put("language", "");
		try{
			if (mb.semantic != null && mb.semantic.slots != null) {

				if (!TextUtils.isEmpty(mb.semantic.slots.song)) {
					MyLog.i("DanceRunnable", "name:" + mb.semantic.slots.song);
					hashmap.put("name", mb.semantic.slots.song);
				}
				if (!TextUtils.isEmpty(mb.semantic.slots.artist)) {
					MyLog.i("DanceRunnable", "singer:" + mb.semantic.slots.artist);
					hashmap.put("singer", mb.semantic.slots.artist);
				}
				if (!TextUtils.isEmpty(mb.semantic.slots.category)) {
					MyLog.i("DanceRunnable", "type:" + mb.semantic.slots.category);
					hashmap.put("type", mb.semantic.slots.category);
				}
			} else if (!TextUtils.isEmpty(mb.service) && mb.service.equals("music")) {
				MyLog.i("DanceRunnable", "choose randomly");				
			}
			
		}catch(Exception e){
			MyLog.e("DanceRunnable",MyExceptionUtils.getStringThrowable(e));
			hashmap.put("name", "");
			hashmap.put("singer", "");
			hashmap.put("type", "");
			hashmap.put("area", "");
			hashmap.put("language", "");
		}		
		return hashmap;
	}
	
	private void requestDanceServerAndPlay() {
		try {
			MyLog.i("DanceRunnable", "requestDanceServerAndPlay()");
			RequestParams requestparams = new RequestParams();
			requestparams.setBodyEntity(new StringEntity(new JSONObject(mHashmap).toString(), "UTF-8"));
			requestparams.setContentType("text/plain");
			
			HttpUtils httputils = new HttpUtils();
			httputils.send(HttpMethod.POST, mRequestURl, requestparams, new RequestCallBack<String>() {

				@Override
				public void onFailure(HttpException arg0, String arg1) {
					// TODO Auto-generated method stub
					MyLog.i("DanceRunnable", "httputils---jonFailure()");
					doDance(danceBean , null);
				}

				@Override
				public void onSuccess(ResponseInfo<String> arg0) {	//http通信正常
					// TODO Auto-generated method stub
					MyLog.i("DanceRunnable", "httputils---onSuccess()");
					try {
						JSONObject jsonobject0 = new JSONObject(new JSONTokener(arg0.result));
						JSONArray jsonArray = new JSONArray(jsonobject0.optString(mMusicType));
						MyLog.i("DanceRunnable", "jsonArray.length:" + jsonArray.length());
						if (jsonArray.length() > 0) {
							httpBackMusicUri = jsonArray.getJSONObject(new Random().nextInt(jsonArray.length())).getString("uri");							
							String url = mResourceURl+ httpBackMusicUri;
							MyLog.i("DanceRunnable", "url = " + url);
							doDance(danceBean , url);
						} else {
							MyLog.i("DanceRunnable", "doDance(danceBean , null)");
							doDance(danceBean , null);
						}
					} catch (JSONException e) {
						MyLog.e("DanceRunnable", MyExceptionUtils.getStringThrowable(e));
					}
				}
			});
		} catch (Throwable e) {
			MyLog.e("DanceRunnable", MyExceptionUtils.getStringThrowable(e));
		}
	}
}
