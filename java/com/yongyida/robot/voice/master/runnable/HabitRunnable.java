package com.yongyida.robot.voice.master.runnable;

import java.util.HashMap;
import java.util.Random;

import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.bean.HabitBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.MP.MyOnCompletionListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnPreparedListener;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class HabitRunnable extends BaseRunnable{
	public HabitRunnable(String result){
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;
	
		mResult = result;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("HabitRunnable", "run()");
		HabitBean habitBean = BeanUtils.parseHabitJson(mResult, HabitBean.class);
		
		if(  habitBean == null ){
			MyLog.e("HabitRunnable", "habitBean == null");		
			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw,new MySynthesizerListener());
			return ;
		}
		
		doHabit(habitBean);		
	}
	
	private void doHabit(HabitBean habitBean) {
		HashMap<String,String> params = getParams(habitBean);
		requestHabitList(params);
	}
	
	HashMap<String,String> getParams(HabitBean habitBean){
		HashMap<String,String> params = new HashMap<String, String>();
		params.put("name", "");
		try{
			if(habitBean.semantic.slots.name != null){
				params.put("name", habitBean.semantic.slots.name);
			}
		}catch(Exception e){
			MyLog.e("HabitRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		
		MyLog.i("HabitRunnable", "params : " + params.toString());
		return params;	
	}
	
	void requestHabitList(HashMap<String,String> params){
		final String name = params.get("name");

		try {
			HttpUtils httputils = new HttpUtils();
			httputils.send(HttpMethod.GET, Domain.getHabitResourceListURl(), new RequestCallBack<String>() {

				@Override
				public void onStart() {}
				@Override
				public void onLoading(long total, long current,boolean isUploading) {}
				@Override
				public void onSuccess(ResponseInfo<String> responseInfo) {						
						MyLog.i("HabitRunnable", "requestJoke onSuccess , result = " + responseInfo.result);
						String[] habitNameArr = responseInfo.result.split("\n");				
						String habitName = getHabitByName(habitNameArr,name);
						String url;
						if(!TextUtils.isEmpty(habitName)){ //播放指定名字的
							url = Domain.getHabitResourceURl() + habitName;
						}else{ // 随机播放
							int nRandom = Math.abs(new Random().nextInt())% habitNameArr.length;							
							url = Domain.getHabitResourceURl() + habitNameArr[nRandom];							
						}
						MyLog.i("HabitRunnable", "url = " + url);
						voiceMp(url);
					}				
				
				@Override
				public void onFailure(HttpException error, String msg) {
					MyLog.e("HabitRunnable", " failure : access server for habitlist , msg = " + msg);
					voiceTTS("服务器查询失败",null);
				}
			
			});
		} catch (Throwable e) {
			MyLog.e("HabitRunnable", MyExceptionUtils.getStringThrowable(e));
		}
		
	}
	
	String getHabitByName(String[] habits,String name){
		for(String s : habits){
			if(s.contains(name)){
				return s;
			}
		}
		return "";
	}

	public void voiceTTS(String text,MySynthesizerListener mySynthesizerListener) {
		MyLog.i("HabitRunnable", "voiceTTS");
		if( mySynthesizerListener == null )
			mySynthesizerListener = new MySynthesizerListener();
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,mIsEndSendRecycle, mySynthesizerListener);
		VoiceQueue.getInstance().add(tts);
	}
	
	void voiceTTS(String text){
		voiceTTS(text,null);
	}
	
	public void voiceMp(String uri) {
		MP mp = new MP(uri,BaseVoice.MP_INTENT_RESOURCE,mInterrupt_state,mPriority,mIsEndSendRecycle,new MyOnPreparedListener(),new MyOnCompletionListener());
		VoiceQueue.getInstance().add(mp);
	}
}
