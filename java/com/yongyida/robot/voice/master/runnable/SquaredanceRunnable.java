package com.yongyida.robot.voice.master.runnable;

import java.util.Random;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.text.TextUtils;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.SquaredanceBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.constant.MyIntent;
import com.yongyida.robot.voice.master.reciver.MyBroadcastReceiver;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.Domain;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.MyRegularUtils;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.utils.VideoMonitoring;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.MP;
import com.yongyida.robot.voice.master.voice.MP.MyOnCompletionListener;
import com.yongyida.robot.voice.master.voice.MP.MyOnPreparedListener;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;

//处理music story opera 
public class SquaredanceRunnable extends BaseRunnable {
	private String TAG = "SquaredanceRunnable";
	private boolean mIsContinuousPlay;
	private boolean isRegisted;
	private SquaredanceRecycleReceiver squaredanceRecycleReceiver;

	private static String mRequestURl;
	private static String mResourceURl;
	private static String mSquaredanceName; 
	
	private SquaredanceBean mBean;
    private SquaredanceBean mSquaredanceBean;
	public SquaredanceRunnable(String result) {
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;

		isRegisted = false;
		mIsContinuousPlay = true;
		mResult = result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i(TAG, "run()");
		SquaredanceBean squaredanceBean = BeanUtils.parseSquaredanceJson(
				mResult, SquaredanceBean.class);
        mSquaredanceBean = squaredanceBean;
		mBean = squaredanceBean;

		if (VideoMonitoring.isVideoing(MasterApplication.mAppContext)) { // 如果正在视频监控就不执行下面唱歌
			MyLog.e(TAG,
					"want to play squaredance ,but is videoing or monitoring");
			return;
		}

		if (squaredanceBean == null) {
			MyLog.e(TAG, "squaredanceBean == null");

			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw);
			return;
		}

		MyLog.i(TAG, "text = " + squaredanceBean.text);
		if (isNotDoMusic(squaredanceBean.text)) {
			voiceTTS("我都晕了,到底要不要听音乐啊,要听就直接点撒");
			return;
		}

		if (isSingleMusic(squaredanceBean.text)) {
			mIsContinuousPlay = false;
		}

		// 获取音乐(音乐 戏曲 故事)请求的参数和链接
		initRequest(squaredanceBean);

		requestServerAndPlay();
	}

	private void initRequest(SquaredanceBean sb) {
		MyLog.i(TAG, "initRequest()");
		mRequestURl = Domain.getSquaredanceRequestURl();
		mResourceURl = Domain.getSquaredanceResourceURl();
		try{
			mSquaredanceName = sb.semantic.slots.name;
			mIsContinuousPlay = false;
		}catch(Exception e){
			mSquaredanceName = "";
		}
		
		MyLog.i(TAG, "requestURl : " + mRequestURl);
	}

	private boolean isNotDoMusic(String text) {
		String[] regexs = { ".*(不|别).*(唱|听|放|播).*" };
		if (MyRegularUtils.myMatch(regexs, text)) {
			MyLog.i(TAG, "isNotDoMusic : true");
			return true;
		}
		MyLog.i(TAG, "isNotDoMusic : false");
		return false;
	}

	// 判断是不是只要1首
	public boolean isSingleMusic(String text) {
		String[] regexs = { ".*(一|1|唱|听|放|播|来)(首|个).*" };
		if (MyRegularUtils.myMatch(regexs, text)) {
			MyLog.i(TAG, "isSingleMusic : true");
			return true;
		}

		MyLog.i(TAG, "isSingleMusic : false");
		return false;
	}

	public void requestServerAndPlay() {
		MyLog.i(TAG, "requestServerAndPlay");
		try {
			HttpUtils httputils = new HttpUtils();
			httputils.send(HttpMethod.GET, mRequestURl,
					new RequestCallBack<String>() {

						@Override
						public void onFailure(HttpException arg0, String arg1) {
							MyLog.e(TAG, "get squaredance from yyd failed:" + arg1);
							voiceTTS(Constant.cannot_find_music_server);
						}

						@Override
						public void onSuccess(ResponseInfo<String> arg0) {
							MyLog.i(TAG, "result = " + arg0.result);
							String url;
							try {
								String[] listFromYYD = arg0.result.split("\n");
								
								if(mIsContinuousPlay){
									//注册接收音乐播放结束的广播
									registSquaredanceRecycleReceiver();
								}
								
								if (!TextUtils.isEmpty(mSquaredanceName)) {														
									for (int i = 0; i < listFromYYD.length; i++) {										
										if (listFromYYD[i].contains(mSquaredanceName)) {
											url = mResourceURl + listFromYYD[i];
											MyLog.i(TAG, "url = " + url);
											voiceMp(url);
											return ;
										}
									}
								} 
								
								url = mResourceURl + listFromYYD[new Random().nextInt(listFromYYD.length)];
								MyLog.i(TAG, "url = " + url);
								voiceMp(url);
								
							} catch (Exception e) {
								MyLog.e(TAG,MyExceptionUtils.getStringThrowable(e));
								voiceTTS(MasterApplication.mAppContext.getString(R.string.tq007_yinyuemeizhaodao));
							}
						}
					});
		} catch (Throwable e) {
			MyLog.e(TAG, MyExceptionUtils.getStringThrowable(e));
			voiceTTS(MasterApplication.mAppContext
					.getString(R.string.tq007_yinyuemeizhaodao));
		}
	}

	public void voiceTTS(String text) {
		TTS tts = new TTS(text, BaseVoice.TTS, INTERRUPT_STATE_STOP, mPriority,
				true, new MySynthesizerListener());
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mSquaredanceBean,text);
	}

	public void voiceMp(String uri) {
		MP mp = new MP(uri, BaseVoice.MP_INTENT_RESOURCE, mInterrupt_state,
				mPriority, mIsEndSendRecycle, new MyOnPreparedListener(),
				new MyOnCompletionListener());
		VoiceQueue.getInstance().add(mp);
	}

	public void registSquaredanceRecycleReceiver() {
		if (isRegisted)
			return;
		MyLog.i(TAG, "registMusicRecycleReceiver");
		squaredanceRecycleReceiver = new SquaredanceRecycleReceiver("SquaredanceRecycleReceiver");
		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter(MyIntent.QUEUE_COMPLETE);
		registerBroadcast(squaredanceRecycleReceiver);

		// 注册到自己的广播队列(统一管理)
		MasterApplication.mAppContext.registerReceiver(squaredanceRecycleReceiver,intentFilter);
		isRegisted = true;
	}

	public class SquaredanceRecycleReceiver extends MyBroadcastReceiver {

		public SquaredanceRecycleReceiver(String info) {
			super(info);
		}

		public void onReceive(Context context, Intent intent) {
			if (intent == null) {
				return;
			}
			MyLog.i(TAG, "SquaredanceRecycleReceiver onReceive");
			requestServerAndPlay();
		}
	}
}
