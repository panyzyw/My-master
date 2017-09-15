package com.yongyida.robot.voice.master.recognizer;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.os.Bundle;
import android.os.Environment;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.utils.MyLog;

public class MySpeechRecognizer {
	private static MySpeechRecognizer mInstance;
	
	public static MySpeechRecognizer getInstance(){		
		if(mInstance == null){
			mInstance = new MySpeechRecognizer();
		}		
		return mInstance;
	}
	
	public static MySpeechRecognizer getInstance(MyRecognizerListener recognizerListener){		
		if(mInstance == null){
			mInstance = new MySpeechRecognizer();
		}
		mInstance.setMyRecognizerListener(recognizerListener);
		return mInstance;
	}
	
	private MySpeechRecognizer(){
		initSpeechRecognizer();
	}
		
	private String mSRtext;
	private SpeechRecognizer mIat; 
	private MyRecognizerListener myRecognizerListener;
	
	void initSpeechRecognizer(){
		setListenParam();
	}
	
	public void setMyRecognizerListener(MyRecognizerListener recognizerListener){
		myRecognizerListener = recognizerListener;
	}
	
	/**
	 * 初始化监听器。
	 */
	private  InitListener mInitListener = new InitListener() {
		public void onInit(int code) {
			if (code != ErrorCode.SUCCESS) {
				MyLog.e("MySpeechRecognizer","InitListener err:" + code);
			}
		}
	};
	
	/**
	 * 不带ui的听写监听器。
	 */
	private RecognizerListener mRecognizerListener = new RecognizerListener() {
		public void onBeginOfSpeech() {
			MyLog.i("MySpeechRecognizer","start speech recognizer");
			mSRtext = "";
			if(myRecognizerListener != null)
				myRecognizerListener.onBeginOfSpeech();
		}

		public void onError(SpeechError error) {
			MyLog.e("MySpeechRecognizer","err "+ error.getPlainDescription(true));
			mSRtext = "";
			if(myRecognizerListener != null)
				myRecognizerListener.onError(error);
		}

		public void onEndOfSpeech() {
			MyLog.i("MySpeechRecognizer","end speech recognizer");	
			if(myRecognizerListener != null)
				myRecognizerListener.onEndOfSpeech();
		}

		public void onResult(RecognizerResult results, boolean isLast) {	
			if(results == null) return ;
			String text = parseSpeechRecognizerResult(results.getResultString());			
			mSRtext += text;
			if(isLast) {
				MyLog.i("MySpeechRecognizer","mSRtext = " + mSRtext);		
				if(myRecognizerListener != null)
					myRecognizerListener.onResult(mSRtext);
				mSRtext="";
			}
		}
		public void onVolumeChanged(int volume, byte[] data) {
			if(myRecognizerListener != null)
				myRecognizerListener.onVolumeChanged(volume, data);
		}
		public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {}
	};
    
	/**
	 * 参数设置
	 * @param param
	 * @return 
	 */
	public void setListenParam(){
		// 使用SpeechRecognizer对象，可根据回调消息自定义界面；
		mIat = SpeechRecognizer.createRecognizer(MasterApplication.mAppContext, mInitListener);
		// 清空参数
		mIat.setParameter(SpeechConstant.PARAMS, null);
		String lag = "mandarin";
		// 设置引擎
		mIat.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
		// 设置返回结果格式
		mIat.setParameter(SpeechConstant.RESULT_TYPE, "json");

		if (lag.equals("en_us")) {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "en_us");
		}else {
			// 设置语言
			mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
			// 设置语言区域
			mIat.setParameter(SpeechConstant.ACCENT,lag);
		}
		// 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
		mIat.setParameter(SpeechConstant.VAD_BOS, "4000");
		// 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
		mIat.setParameter(SpeechConstant.VAD_EOS, "1000");
		// 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
		mIat.setParameter(SpeechConstant.ASR_PTT,  "1");	
		// 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
		// 注：AUDIO_FORMAT参数语记需要更新版本才能生效
		mIat.setParameter(SpeechConstant.AUDIO_FORMAT,"wav");
		mIat.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory()+"/msc/iat.wav");
	}
	
	private String parseSpeechRecognizerResult(String json){

		StringBuffer ret = new StringBuffer();
		try {
			JSONTokener tokener = new JSONTokener(json);
			JSONObject joResult = new JSONObject(tokener);

			JSONArray words = joResult.getJSONArray("ws");
			for (int i = 0; i < words.length(); i++) {
				// 转写结果词，默认使用第一个结果
				JSONArray items = words.getJSONObject(i).getJSONArray("cw");
				JSONObject obj = items.getJSONObject(0);
				ret.append(obj.getString("w"));
//				如果需要多候选结果，解析数组其他字段
//				for(int j = 0; j < items.length(); j++)
//				{
//					JSONObject obj = items.getJSONObject(j);
//					ret.append(obj.getString("w"));
//				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return ret.toString();	
	}
	
	public static class MyRecognizerListener{
		public void onBeginOfSpeech() {}
		public void onError(SpeechError error) {}
		public void onEndOfSpeech() {}
		public void onResult(String results) {}
		public void onVolumeChanged(int volume, byte[] data) {}
	}
	
	//开始听写
	public int startSR(){
		MyLog.i("MySpeechRecognizer", "startSR()");
		return mIat.startListening(mRecognizerListener);
	}

	//是否在听写
	public boolean isSR(){
		return mIat.isListening();
	}
	
	//停止听写
	public void stopSR(){
		MyLog.i("MySpeechRecognizer", "stopSR()");
		mIat.stopListening();	
	}
	
	//取消听写
	public void cancelSR(){
		MyLog.i("MySpeechRecognizer", "cancelSR()");
		mIat.cancel();	
	}
	
	
}
