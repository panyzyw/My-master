package com.yongyida.robot.voice.master.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.yongyida.robot.voice.master.Container.ServiceMap;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.constant.MyData;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;

public class VoiceSubService extends Service {
	public IBinder onBind(Intent intent) {
		return null;
	}

	//private Queue<BaseMoudle> moduleQueue;

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		MyLog.i("VoiceSubService", "onCreate()");
		// moduleQueue = new LinkedList<BaseMoudle>();
	}

	public int onStartCommand(Intent intent, int flags, int startId) {
		MyLog.i("VoiceSubService", "onStartCommand()");
		
		if(intent == null){
			MyLog.i("VoiceSubService", "intent == null");
			//myStopSelf();	服务器一直开启,避免开启耗时
			return super.onStartCommand(intent, flags, startId);
		}
		
//		BaseMoudle bm = getMoudle(intent);	
//		if( bm == null ){
//			MyLog.i("VoiceSubService", "bm == null");
//			myStopSelf();
//		}else{	
//			//moduleQueue.add(bm);
//			bm.run();			
//		}
		
		//放入线程池,执行
		BaseRunnable br = getRunnable(intent);
		if(br != null){
			MasterApplication.mFixedThreadPool.execute(br);
		}else{				
			MyLog.e("VoiceSubService", "br == null");
		}
		
		return super.onStartCommand(intent, flags, startId);
	}

	// private BaseMoudle getMoudle(Intent intent){
	//
	// String result = intent.getStringExtra("result");
	// if(result == null){
	// MyLog.i("VoiceSubService","result == null");
	// return null;
	// }
	//
	// String action = intent.getAction();
	// if(action == null){
	// MyLog.i("VoiceSubService","action == null");
	// return null;
	// }
	//
	// //BaseBean bb = BeanUtils.parseBaseBeanJson(result, BaseBean.class);
	// Class<?> cls = ServiceMap.getInstance().getServerClass(action);
	// if(cls == null){
	// MyLog.i("VoiceSubService","cls == null");
	// return null;
	// }
	//
	// BaseMoudle bm;
	// try {
	// Log.i(MyData.TAG, "result = " + result);
	// bm = (BaseMoudle)
	// cls.getConstructor(Context.class,String.class,String.class).newInstance(this,action,result);
	// } catch (Exception e) {
	// e.printStackTrace();
	// myLog.log("bm = (BaseMoudle) cls.newInstance() : throw exception");
	// return null;
	// }
	// return bm;
	// }

	private BaseRunnable getRunnable(Intent intent) {

		String result = intent.getStringExtra("result");
		if (result == null) {
			MyLog.i("VoiceSubService", "result == null");
			return null;
		}

		String action = intent.getAction();
		if (action == null) {
			MyLog.i("VoiceSubService", "action == null");
			return null;
		}

		// BaseBean bb = BeanUtils.parseBaseBeanJson(result, BaseBean.class);
		Class<?> cls = ServiceMap.getInstance().getServerClass(action);
		if (cls == null) {
			MyLog.i("VoiceSubService", "cls == null");
			return null;
		}

		BaseRunnable br;
		if(cls.toString().equals("class com.yongyida.robot.voice.master.runnable.DanceRunnable"))	//跳舞的Runnable,拿到Context
		{
			MyLog.i("VoiceSubService", "Class: "+cls.toString());
			try {
				br = (BaseRunnable) cls.getConstructor(Context.class,String.class).newInstance(getApplicationContext(),result);
				MyLog.i("VoiceSubService", "cls.getConstructor()");
			} catch (Exception e) {
				MyLog.e("VoiceSubService", MyExceptionUtils.getStringThrowable(e));
				return null;
			}
		}
		else
		{
			try {
				br = (BaseRunnable) cls.getConstructor(String.class).newInstance(result);
			} catch (Exception e) {
				MyLog.e("VoiceSubService",MyExceptionUtils.getStringThrowable(e));
				return null;
			}
		}
		return br;
	}

	public void myStopSelf() {
		Intent intent = new Intent(this, VoiceSubService.class);
		stopService(intent);
	}

	public void onDestroy() {
		// TODO Auto-generated method stub
		//Log.i(MyData.TAG, "VoiceSubService onDestroy");
		// releaseResource();
		super.onDestroy();
	}

	public void releaseResource() {
		//Log.i(MyData.TAG, "VoiceSubService releaseResource");
//		if (moduleQueue == null)
//			return;
//		BaseMoudle bm;
//		while ((bm = moduleQueue.poll()) != null) {
//			bm.releaseResource();
//		}
//		moduleQueue = null;
	}
}
