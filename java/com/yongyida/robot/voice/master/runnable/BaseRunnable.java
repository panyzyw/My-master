package com.yongyida.robot.voice.master.runnable;

import java.util.ArrayList;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.bean.BaseBean;
import com.yongyida.robot.voice.master.reciver.MyBroadcastReceiver;
import com.yongyida.robot.voice.master.recognizer.MySpeechRecognizer;
import com.yongyida.robot.voice.master.utils.MyLog;

public class BaseRunnable implements Runnable{
	public static int INTERRUPT_STATE_PAUSE = 0; //出现中断后: 暂停
	public static int INTERRUPT_STATE_RESET= 1;   //出现中断后: 重置
	public static int INTERRUPT_STATE_STOP= 2;    //出现中断后: 关闭	
	protected int mInterrupt_state;
	
	public static int PRIORITY_CALL = 1000;           //来电
	public static int PRIORITY_Notification = 900;  //提醒
	public static int PRIORITY_SMS = 800;               //短信	
	public static int PRIORITY_OTHER = 700;           //其他(聊天,天气,音乐,跳舞,故事,笑话等)
	public static int PRIORITY_SENSE = 600;            //红外感应 
	protected int mPriority;	
	
	protected boolean mIsEndSendRecycle = true;

	protected String mResult;
	
	private static ArrayList<MyBroadcastReceiver> mReceiverList;
	
	public BaseRunnable(){
		if(mReceiverList  == null){
			MyLog.i("BaseRunnable", "mReceiverList  create");
			mReceiverList = new ArrayList<MyBroadcastReceiver>();
		}
	}
	
	public static void registerBroadcast(MyBroadcastReceiver receiver){
		mReceiverList.add(receiver);
	}
	
	public static void releaseResource(){ 
		MyLog.i("BaseRunnable", "releaseResource enter");
		
		//注销注册过的所有广播
		if(mReceiverList  != null){	
			for(MyBroadcastReceiver receiver: mReceiverList){
				if(receiver == null)
					continue;
				MyLog.i("BaseRunnable","release " + receiver.getInfo());			
				MasterApplication.mAppContext.unregisterReceiver(receiver);			
			}
			mReceiverList.clear();
		}

		//取消录音
		if(MySpeechRecognizer.getInstance().isSR()){
			MyLog.i("BaseRunnable", "cancel SpeechRecognizer");
			MySpeechRecognizer.getInstance().cancelSR();
		}
		
		//停止跳舞
		DanceRunnable.stopDance();
		
		MyLog.i("BaseRunnable", "releaseResource end");		
	}
		
	public boolean isResultOk(BaseBean baseBean){
		if(baseBean == null || baseBean.rc != 0 || baseBean.service == null){
			return false;
		}
		return true;
	}

	public int getmInterrupt_state() {
		return mInterrupt_state;
	}

	public void setmInterrupt_state(int mInterrupt_state) {
		this.mInterrupt_state = mInterrupt_state;
	}

	public int getmPriority() {
		return mPriority;
	}

	public void setmPriority(int mPriority) {
		this.mPriority = mPriority;
	}

	public boolean ismIsEndSendRecycle() {
		return mIsEndSendRecycle;
	}

	public void setmIsEndSendRecycle(boolean mIsEndSendRecycle) {
		this.mIsEndSendRecycle = mIsEndSendRecycle;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}
	
}
