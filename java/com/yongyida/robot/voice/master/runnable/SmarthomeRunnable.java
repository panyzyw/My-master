package com.yongyida.robot.voice.master.runnable;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.PowerManager;

import com.google.gson.Gson;
import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.SmarthomeBean;
import com.yongyida.robot.voice.master.bean.SmarthomeBean.SmarthomeProtocolBean;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.reciver.MyBroadcastReceiver;
import com.yongyida.robot.voice.master.utils.BeanUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.utils.MyRegularUtils;
import com.yongyida.robot.voice.master.utils.Utils;
import com.yongyida.robot.voice.master.utils.VideoMonitoring;
import com.yongyida.robot.voice.master.utils.runnableutils.SmarthomeUtils;
import com.yongyida.robot.voice.master.voice.BaseVoice;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.TTS.MySynthesizerListener;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class SmarthomeRunnable extends BaseRunnable {
    private SmarthomeBean mSmarthomeBean;
	public SmarthomeRunnable(String result) {
		mIsEndSendRecycle = false;
		mPriority = PRIORITY_OTHER;
		mInterrupt_state = INTERRUPT_STATE_STOP;

		mResult = result;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		MyLog.i("SmarthomeRunnable", "run()");
		SmarthomeBean smarthomeBean = BeanUtils.parseSmarthomeJson(mResult,
				SmarthomeBean.class);
        mSmarthomeBean = smarthomeBean;
		if (smarthomeBean == null) {
			MyLog.e("SmarthomeRunnable", "smarthomeBean == null");

			mIsEndSendRecycle = true;
			voiceTTS(Constant.comman_unkonw);
			return;
		}

		//点亮屏幕
		lightScreen();
		
		registeSmarthomeReceiver();

		doSmarthome(smarthomeBean);
	}

	private void lightScreen() {
		 //获取电源管理器对象
        PowerManager pm=(PowerManager) MasterApplication.mAppContext.getSystemService(Context.POWER_SERVICE);
         //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
        PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_DIM_WAKE_LOCK,"bright");
        //点亮屏幕
        wl.acquire();
        //释放
        wl.release();
	}

	private void registeSmarthomeReceiver() {
		MyLog.i("SmarthomeRunnable", "registeSmarthomeReceiver");
		MyBroadcastReceiver receiver = new MyBroadcastReceiver(
				"SmarthomeReceiver") {

			@Override
			public void onReceive(Context context, Intent intent) {

				if (intent == null) {
					return;
				}

				if (VideoMonitoring.isVideoing(context)) { // 如果正在视频监控就不执行下面的语音合成
					MyLog.e("SmarthomeRunnable",
							"registeSmarthomeReceiver ,but is videoing or monitoring");
					return;
				}

				String action = intent.getAction();

				if (action.equals("com.yydrobot.QUERY_BACK")) { // 通过广播 返回查询的温度
					MyLog.i("SmarthomeRunnable",
							"action == com.yydrobot.QUERY_BACK");
					doQueryTemperatureBack(intent);
					return;
				}

				if (intent.getAction().equals("com.yydrobot.RESULT_OK")) {
					MyLog.i("SmarthomeRunnable",
							"action == com.yydrobot.QUERY_BACK");
					doResultOk(intent);
					return;
				}

				if (intent.getAction().equals("com.yydrobot.RESULT_NO_REMOTER")) {
					MyLog.i("SmarthomeRunnable",
							"action == com.yydrobot.RESULT_NO_REMOTER");
					doResultNoRemoter();
					return;
				}

				if (intent.getAction().equals("com.yydrobot.RESULT_ERROR")) {
					MyLog.i("SmarthomeRunnable",
							"action == com.yydrobot.RESULT_ERROR");
					doResultError();
					return;
				}

				if (intent.getAction().equals("com.yydrobot.GET_MIN_MAX_TEMP")) {
					MyLog.i("SmarthomeRunnable",
							"action == com.yydrobot.GET_MIN_MAX_TEMP");
					doGetMinMaxTemp(intent);
					return;
				}

				super.onReceive(context, intent);
			}
		};

		// 实例化过滤器并设置要过滤的广播
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction("com.yydrobot.QUERY_BACK");
		intentFilter.addAction("com.yydrobot.RESULT_OK");
		intentFilter.addAction("com.yydrobot.RESULT_NO_REMOTER");
		intentFilter.addAction("com.yydrobot.RESULT_ERROR");
		intentFilter.addAction("com.yydrobot.GET_MIN_MAX_TEMP");

		registerBroadcast(receiver);

		// 注册到自己的广播队列(统一管理)
		MasterApplication.mAppContext.registerReceiver(receiver, intentFilter);

	}

	void doQueryTemperatureBack(Intent intent) {
		int t = intent.getIntExtra("result", -200);
		MyLog.i("SmarthomeRunnable", "query air-condition temperature = " + t);

		if (t == -200) // 错误
			voiceTTS(Constant.query_false);
		else
			voiceTTS(Constant.temperatrue_is + t + Constant.temperatrue);
	}

	void doResultOk(Intent intent) {
		Context context = MasterApplication.mAppContext;
		String response = context.getSharedPreferences("response",
				android.content.Context.MODE_PRIVATE).getString("response", "");
		MyLog.i("SmarthomeRunnable", "response :" + response);
		voiceTTS(response);
	}

	void doResultNoRemoter() {
		voiceTTS(Constant.not_adaptive);
	}

	void doResultError() {
		voiceTTS(Constant.smarthome_err_frequent_operation);
	}

	void doGetMinMaxTemp(Intent intent) {
		String minMax = intent.getStringExtra("getMinMaxTemp");
		if (minMax == null)
			minMax = "17,30"; // 默认最低17度,默认最高30度
		MasterApplication.mAppContext
				.getSharedPreferences("smarthome", Context.MODE_PRIVATE).edit()
				.putString("AIR_CONDITIONER", minMax).commit();
	}

	private void doSmarthome(SmarthomeBean sb) {

		String text = sb.text;
		MyLog.i("text = ", text);
		String[] regexs = { ".*(不|别).*" };
		if (MyRegularUtils.myMatch(regexs, text)) {
			voiceTTS("我都迷糊了,到底要不要执行命令");
			return;
		}

		SmarthomeProtocolBean spb = new SmarthomeProtocolBean();
		spb.device = sb.semantic.slots.device;
		spb.action = sb.semantic.slots.action;

		String jsonResult = "";
		if (sb.semantic.slots.device.equals("AIR_CONDITIONER")) { // 空调
			if (sb.semantic.slots.num != null) {
				String num = SmarthomeUtils.StringtoNum(sb.semantic.slots.num);
				if (SmarthomeUtils.isOkNum(num)) { // num 值没问题才 连接上去
					spb.action += ":" + num;
				}
			}
		}

		if (sb.semantic.slots.device.equals("TV")) { // 电视
			String action = sb.semantic.slots.action;
			if (action.equals("turnup") || action.equals("turndown")
					|| action.equals("volumeup") || action.equals("volumedown")
					|| action.equals("channel") || action.equals("up")
					|| action.equals("down") || action.equals("left")
					|| action.equals("right")) {
				String num = "1";
				if (sb.semantic.slots.num != null)
					num = SmarthomeUtils.TVStringtoNum(sb.semantic.slots.num);
				spb.action += ":" + num;
			}

		}
		
		if (sb.semantic.slots.device.equals("STB")) { // 机顶盒
			// ...
		}

		// 不支持电视音量 max min volumeset 这三个action的命令, 用相应的语音提示
		if (spb.action.equals("max") || spb.action.equals("min") || spb.action.equals("volumeset")) {
			String contentTip = SmarthomeUtils.tvHardLimitOperation(spb.action);
			voiceTTS(contentTip);
			return;
		}

		String contentTip = SmarthomeUtils.voiceTip(MasterApplication.mAppContext, spb.device, spb.action);

		jsonResult = new Gson().toJson(spb);// 构造json
		MyLog.i("SmarthomeRunnable", "jsonResult = " + jsonResult);
		Intent in = new Intent(Constant.SMARTHOME_ACTION_TO_SYSTEM);
		in.putExtra("result", jsonResult);
		MasterApplication.mAppContext.sendBroadcast(in); // 发送广播到红外模块处理
		MyLog.i("SmarthomeRunnable", "send smarthome broadcast , StrExtra = " + jsonResult.toString());

		MasterApplication.mAppContext.getSharedPreferences("response",android.content.Context.MODE_PRIVATE).edit().putString("response", contentTip).commit();
		return;
	}

	public void voiceTTS(String text) {

		if (VideoMonitoring.isVideoing(MasterApplication.mAppContext)) { // 如果正在视频监控就不执行下面的语音合成
			MyLog.e("SmarthomeRunnable",
					"voiceTTS ,but is videoing or monitoring");
			return;
		}

		MyLog.i("SmarthomeRunnable", "voiceTTS");
		TTS tts = new TTS(text, BaseVoice.TTS, mInterrupt_state, mPriority,
				mIsEndSendRecycle, new MySynthesizerListener());
		VoiceQueue.getInstance().add(tts);
        Utils.collectInfoToServer(mSmarthomeBean,text);
	}
}
