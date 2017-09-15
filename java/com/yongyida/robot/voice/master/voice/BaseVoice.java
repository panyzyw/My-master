package com.yongyida.robot.voice.master.voice;

public class BaseVoice {	
	private boolean mIsInterrupted; //是否中断过
	private int mInterrupt_state;
	private int mPriority;	
	private boolean mIsEndSendRecycle; // 结束时是否发送recycle广播	
	
	public static final int TTS = 0;
	public static final int MP = 1;	
	public static final int MP_INTENT_RESOURCE = 2;
	public static final int MP_ASSETS_RESOURCE = 3;
	private int mVoicer; // TTS , MP ,  MP_INTENT_RESOURCE , MP_ASSETS_RESOURCE
	
	public BaseVoice(int interrupt_state,int voicer,int priority,boolean isEndSendRecycle){
		mVoicer = voicer;
		mInterrupt_state =interrupt_state;
		mPriority = priority;
		mIsEndSendRecycle = isEndSendRecycle;
		mIsInterrupted = false;
	}
	public void start(){	};
	public void reStart(){	};	
	public void pause(){}
	public void stop(){}
	public boolean isRunning(){return false;}
	public void reset(){} 
	
	public int getPriority(){
		return mPriority;
	}
	public void setPriority(int priority){
		mPriority = priority;
	}

	public int getInterrupt_state(){
		return mInterrupt_state;
	}
	public void setInterrupt_state(int interrupt_state){
		mInterrupt_state = interrupt_state;
	}	
	
	public boolean getIsInterrupted(){
		return mIsInterrupted;
	}
	public void setIsInterrupted(boolean isInterrupted){
		mIsInterrupted = isInterrupted;
	}	
	
	public boolean getIsEndSendRecycle(){
		return mIsEndSendRecycle;
	}
	public void setIsEndSendRecycle(boolean isEndSendRecycle){
		mIsEndSendRecycle = isEndSendRecycle;
	}	
	
	public int getVoicer(){
		return mVoicer;
	}
	public void setVoicer(int voicer){
		mVoicer = voicer;
	}
}
