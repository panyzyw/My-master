package com.yongyida.robot.voice.master.voice;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.util.ResourceUtil;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.FaceDetect;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.runnable.BaseRunnable;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.zccl.ruiqianqi.brain.system.ISynthesizerCallback;
import com.zccl.ruiqianqi.brain.system.SystemPresenter;

/**
 * 讯飞语音合成类
 * @author zx
 *
 */
public class TTS extends BaseVoice{

	/**
	 * 发音人
	 * 云端 aisxa
	 * 语记 ""
	 * 本地 xiaofeng xiaoyan nannan jiajia
	 */
	public static String speeker = "jiajia";
	/** 设置合成语速 */
	public static String SPEED = "50";
	/** 设置合成音调 */
	public static String PITCH = "50";
	/** 设置合成音量 */
	public static String VOLUME = "100";
	/** 设置播放器音频流类型 */
	public static String STREAM_TYPE = "3";
	/** 设置播放合成音频打断音乐播放，默认为true */
	public static String KEY_REQUEST_FOCUS = "true";
	/** 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限 */
	public static String AUDIO_FORMAT = "wav";
	public String mVoiceText;
	private SpeechSynthesizer mTts;// 语音合成器
	private SynthesizerListener mSynListener;// 合成监听器
    private ISynthesizerCallback ISynthesizerCallback;

	public TTS(String voiceText ,int voicer,int interrupt_state ,int priority ,boolean isEndSendRecycle,final MySynthesizerListener mySynthesizerListener){
		super(interrupt_state,voicer,priority,isEndSendRecycle);
		mVoiceText = voiceText;
		init();
		// 合成监听器
		mSynListener = new SynthesizerListener() {
			// 会话结束回调接口，没有错误时，error为null
			public void onCompleted(SpeechError error) {
				Log.d("tempTAG", "TTS => onCompleted");
				sendLedBroad(false);
				mySynthesizerListener.onCompleted(error);
				if(error != null){
					MyLog.e("TTS","error message : " + error.getMessage());
					MyLog.e("TTS", MyExceptionUtils.getStringThrowable(error.fillInStackTrace()));
				}
				MyLog.i("TTS", "tts onCompleted");
				MyLog.i("TTS", "complete : queue.size() = " + VoiceQueue.getInstance().getSize());
				VoiceQueue.getInstance().popAndRun();
			}
			public void onBufferProgress(int percent, int beginPos, int endPos,String info) {
				mySynthesizerListener.onBufferProgress(percent, beginPos, endPos, info);
			}
			public void onSpeakBegin() {
                //默认正在说话时会闪灯（挚康的不要闪）
                if(MasterApplication.isNeedSpekLed()){
                    sendLedBroad(true);
                }
				mySynthesizerListener.onSpeakBegin();
			}
			public void onSpeakPaused() {
				mySynthesizerListener.onSpeakPaused();
			}
			public void onSpeakProgress(int percent, int beginPos, int endPos) {
				mySynthesizerListener.onSpeakProgress(percent, beginPos, endPos);
			}
			public void onSpeakResumed() {
				mySynthesizerListener.onSpeakResumed();
			}
			public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
				mySynthesizerListener.onEvent(arg0, arg1, arg2, arg3);
			}
		};

        ISynthesizerCallback = new ISynthesizerCallback() {
            @Override
            public void OnComplete(SpeechError error) {
                Log.d("tempTAG", "TTS => onCompleted");
                sendLedBroad(false);
                mySynthesizerListener.onCompleted(error);
                if(error != null){
                    MyLog.e("TTS","error message : " + error.getMessage());
                    MyLog.e("TTS", MyExceptionUtils.getStringThrowable(error.fillInStackTrace()));
                }
                MyLog.i("TTS", "tts onCompleted");
                MyLog.i("TTS", "complete : queue.size() = " + VoiceQueue.getInstance().getSize());
                VoiceQueue.getInstance().popAndRun();
            }

            @Override
            public void OnBegin() {
                //默认正在说话时会闪灯（挚康的不要闪）
                //更新：挚康和荣事达开始说话时都不闪灯了。
			  /*  if(MasterApplication.isNeedSpekLed()){
					sendLedBroad(true);
				}*/
                mySynthesizerListener.onSpeakBegin();
            }

            @Override
            public void OnPause() {
                mySynthesizerListener.onSpeakPaused();
            }

            @Override
            public void OnResume() {
                mySynthesizerListener.onSpeakResumed();
            }
        };
	}

	private void init(){

		// 1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
		mTts = SpeechSynthesizer.createSynthesizer(MasterApplication.mAppContext, null);

		/*
		// 2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
		// 设置发音人（更多在线发音人，用户可参见 附录12.2
		mTts.setParameter(SpeechConstant.VOICE_NAME, Constant.xiaoai);
		// 设置发音人
		mTts.setParameter(SpeechConstant.SPEED, "50");
		//设置合成音调
		mTts.setParameter(SpeechConstant.PITCH, "50");
		// 设置语速
		mTts.setParameter(SpeechConstant.VOLUME, "100");
		*/
		mTts.setParameter(SpeechConstant.PARAMS, null);
		if (speeker.equals(Constant.xiaoai)) {
			mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD);
			mTts.setParameter(SpeechConstant.VOICE_NAME, speeker);

			mTts.setParameter(SpeechConstant.SPEED, SPEED);
			mTts.setParameter(SpeechConstant.PITCH, PITCH);
			mTts.setParameter(SpeechConstant.VOLUME, VOLUME);

		} else {

			if(speeker.equals("")) {
				mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
				mTts.setParameter(SpeechConstant.VOICE_NAME, "");
			}else {
				mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_LOCAL);
				//设置发音人资源路径
				mTts.setParameter(ResourceUtil.TTS_RES_PATH, getResourcePath());
				mTts.setParameter(SpeechConstant.VOICE_NAME, speeker);

				mTts.setParameter(SpeechConstant.SPEED, SPEED);
				mTts.setParameter(SpeechConstant.PITCH, PITCH);
				mTts.setParameter(SpeechConstant.VOLUME, VOLUME);
			}
		}

		mTts.setParameter(SpeechConstant.STREAM_TYPE, STREAM_TYPE);
		mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, KEY_REQUEST_FOCUS);
		mTts.setParameter(SpeechConstant.AUDIO_FORMAT, AUDIO_FORMAT);
		//mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, path);
	}

	/**
	 * 获取发音人资源路径
	 */
	private String getResourcePath(){
		StringBuffer tempBuffer = new StringBuffer();
		//合成通用资源
		tempBuffer.append(ResourceUtil.generateResourcePath(MasterApplication.mAppContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/common.jet"));
		tempBuffer.append(";");
		//发音人资源
		tempBuffer.append(ResourceUtil.generateResourcePath(MasterApplication.mAppContext, ResourceUtil.RESOURCE_TYPE.assets, "tts/"+speeker+".jet"));
		return tempBuffer.toString();
	}
	
	public void setVoiceNamer(String voiceNamer){
		mTts.setParameter(SpeechConstant.VOICE_NAME, voiceNamer);
	}
	
	public void setMySynthesizerListener(final MySynthesizerListener mySynthesizerListener){
		mSynListener = new SynthesizerListener() {
			// 会话结束回调接口，没有错误时，error为null
			public void onCompleted(SpeechError error) {
				if(mySynthesizerListener != null)
					mySynthesizerListener.onCompleted(error);
				MyLog.e("TTS", "tts onCompleted");
				VoiceQueue.getInstance().popAndRun();
			}
			public void onBufferProgress(int percent, int beginPos, int endPos,String info) {
				if(mySynthesizerListener != null)
					mySynthesizerListener.onBufferProgress(percent, beginPos, endPos, info);
			}
			public void onSpeakBegin() {
				if(mySynthesizerListener != null)
					mySynthesizerListener.onSpeakBegin();
			}
			public void onSpeakPaused() {
				if(mySynthesizerListener != null)
					mySynthesizerListener.onSpeakPaused();
			}
			public void onSpeakProgress(int percent, int beginPos, int endPos) {
				if(mySynthesizerListener != null)
					mySynthesizerListener.onSpeakProgress(percent, beginPos, endPos);
			}
			public void onSpeakResumed() {
				if(mySynthesizerListener != null)
					mySynthesizerListener.onSpeakResumed();
			}
			public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
				if(mySynthesizerListener != null)
					mySynthesizerListener.onEvent(arg0, arg1, arg2, arg3);
			}
		};
	}
	
	public void start(){
		MyLog.i("TTS", "startSpeaking");		
		//mTts.startSpeaking(mVoiceText, mSynListener);
        if(MasterApplication.isFirstFaceDetec){
            MasterApplication.isFirstFaceDetec = false;
            SystemPresenter.getInstance(MasterApplication.mAppContext).startTTS(FaceDetect.getInstance().getWho() + "，" + mVoiceText,null, ISynthesizerCallback);
        }else {
            SystemPresenter.getInstance(MasterApplication.mAppContext).startTTS(mVoiceText,null, ISynthesizerCallback);
        }

	}
	
	public void reStart(){
		if( getInterrupt_state() == BaseRunnable.INTERRUPT_STATE_PAUSE  ){
			MyLog.i("TTS", "resumeSpeaking");
			MyLog.i("TTS", "mTts == " + mTts.toString());
			//mTts.resumeSpeaking();
            SystemPresenter.getInstance(MasterApplication.mAppContext).resumeTTS();
		}else if( getInterrupt_state() == BaseRunnable.INTERRUPT_STATE_RESET ){
			MyLog.i("TTS", "startSpeaking");
			//mTts.startSpeaking(mVoiceText, mSynListener);
            SystemPresenter.getInstance(MasterApplication.mAppContext).startTTS(mVoiceText,null, ISynthesizerCallback);
		}
	}
	
	public void pause(){
		MyLog.i("TTS", "mRunningVoice pause");
		MyLog.i("TTS", "mTts == " + mTts.toString());
		//mTts.pauseSpeaking();
        SystemPresenter.getInstance(MasterApplication.mAppContext).pauseTTS();
		setIsInterrupted(true);
	}
	
	public void stop(){
		MyLog.i("TTS", "mRunningVoice stop");
		//mTts.stopSpeaking();
        SystemPresenter.getInstance(MasterApplication.mAppContext).stopTTS(MasterApplication.mPackageName);
	}

	private void sendLedBroad(boolean onOff) {
		Intent intentSpeakOpen = new Intent("com.yongyida.robot.change.BREATH_LED");
		intentSpeakOpen.putExtra("on_off", onOff);
		intentSpeakOpen.putExtra("place", 3);
		intentSpeakOpen.putExtra("colour", 3);
		intentSpeakOpen.putExtra("frequency", 2);
		intentSpeakOpen.putExtra("Permanent", "speak");
		intentSpeakOpen.putExtra("priority", 6);
		intentSpeakOpen.putExtra("package", MasterApplication.mAppContext.getPackageName());
		MasterApplication.mAppContext.sendBroadcast(intentSpeakOpen);
	}
	
	public boolean isRunning(){
		//return mTts.isSpeaking();
		return SystemPresenter.getInstance(MasterApplication.mAppContext).isSpeaking();
	}
	
	public static class MySynthesizerListener{
		public void onCompleted(SpeechError error) {}
		public void onBufferProgress(int percent, int beginPos, int endPos,String info) {}
		public void onSpeakBegin() {}
		public void onSpeakPaused() {}
		public void onSpeakProgress(int percent, int beginPos, int endPos) {}
		public void onSpeakResumed() {}
		public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {}
	}
	
}
