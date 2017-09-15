package com.yongyida.robot.voice.master.dance;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.yongyida.robot.motorcontrol.MotorController;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yyd.robot.aidl.YYDDanceCallBack;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

public class DanceServer extends Service{
	private String tag = "doDance";
	
	private MotorController mMotorController;	//串口服务类
	private Timer Play1Timer;	//动作执行定时器
	private Timer timer;	//执行基本动作定时器
	private Timer mTaskTimer;	//执行任务的定时器
	private boolean isRun;	//是否开始跳舞
	private int batteryLevel;	//电池电量
	
	private boolean mHeadUpSigns;	//下巴动作是否在上的标识
	private boolean mHeadLeftSigns;	//下巴动作是否在左的标识
	
	private RemoteBinder remoteBinder;
	
	/**
	 * 
	 * 本地Binder，供本APP调用的binder
	 * 
	 * **/
	public class RemoteBinder extends Binder
	{
        public DanceServer getbindser() {
            return DanceServer.this;
        }
	}

	/**
	 * 
	 * 
	 * 获取马达串口服务的ServiceConnection
	 * 
	 * **/
	private ServiceConnection motorServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mMotorController=MotorController.Stub.asInterface(service);
			try{
				mMotorController.setDrvType(0);
				MyLog.d(tag, "get mMotorController successful");
            }catch (RemoteException e){
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName componentName) {

        }
    };
    /***
     * 
     * 
     * 远程Binder，供其他AP调用。功能保留
     * 
     * 
     * ***/
    YYDDanceCallBack.Stub originBinder = new YYDDanceCallBack.Stub() {

		@Override
		public void robot_head_down(int arg0) throws RemoteException {
			// TODO Auto-generated method stub
			MyLog.d(tag, "robot_head_down() "+arg0);
			if(arg0 == 0)
			{
				mMotorController.setSpeed(90); // 设置速度位最大
				//mMotorController.headDown(500);
			}else
			{
				mMotorController.setSpeed(90); // 设置速度位最大
				//mMotorController.headDown(arg0);
			}
		}

		@Override
		public void robot_head_left(int arg0) throws RemoteException {
			// TODO Auto-generated method stub
			MyLog.d(tag, "robot_head_left() "+arg0);
			if(arg0 == 0)
			{
				mMotorController.setSpeed(90); // 设置速度位最大
				//mMotorController.headLeft(1000);
			}else
			{
				mMotorController.setSpeed(90); // 设置速度位最大
				//mMotorController.headDown(arg0);
			}
		}

		@Override
		public void robot_head_right(int arg0) throws RemoteException {
			// TODO Auto-generated method stub
			MyLog.d(tag, "robot_head_right() "+arg0);
			if(arg0 == 0)
			{
				mMotorController.setSpeed(90); // 设置速度位最大
				//mMotorController.headRight(1000);
			}else
			{
				mMotorController.setSpeed(90); // 设置速度位最大
				//mMotorController.headDown(arg0);
			}
		}

		@Override
		public void robot_head_up(int arg0) throws RemoteException {
			// TODO Auto-generated method stub
			MyLog.d(tag, "robot_head_up() "+arg0);
			if(arg0 == 0)
			{
				mMotorController.setSpeed(40); // 设置速度位最大
				//mMotorController.headUp(500);
			}else
			{
				mMotorController.setSpeed(90); // 设置速度位最大
				//mMotorController.headDown(arg0);
			}
		}
	};
	
	/**
	 * 
	 * robot_head_down  本地函数
	 * 
	 * 
	 * ***/
	public void robot_head_down(int arg0) throws RemoteException {
		// TODO Auto-generated method stub
		MyLog.d(tag, "robot_head_down() "+arg0);
		if(arg0 == 0)
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headDown(500);
		}else
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headDown(arg0);
		}
	}
	/**
	 * 
	 * robot_head_left  本地函数
	 * 
	 * 
	 * ***/
	public void robot_head_left(int arg0) throws RemoteException {
		// TODO Auto-generated method stub
		MyLog.d(tag, "robot_head_left() "+arg0);
		if(arg0 == 0)
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headLeft(1000);
		}else
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headDown(arg0);
		}
	}
	/**
	 * 
	 * robot_head_right  本地函数
	 * 
	 * 
	 * ***/
	public void robot_head_right(int arg0) throws RemoteException {
		// TODO Auto-generated method stub
		MyLog.d(tag, "robot_head_right() "+arg0);
		if(arg0 == 0)
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headRight(1000);
		}else
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headDown(arg0);
		}
	}

	/**
	 * 
	 * robot_head_up  本地函数
	 * 
	 * 
	 * ***/
	public void robot_head_up(int arg0) throws RemoteException {
		// TODO Auto-generated method stub
		MyLog.d(tag, "robot_head_up() "+arg0);
		if(arg0 == 0)
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headUp(500);
		}else
		{
			mMotorController.setSpeed(100); // 设置速度位最大
			//mMotorController.headDown(500);
		}
	}	
	
	public int get_motor_station()
	{
		MyLog.d(tag, "get_motor_station()");
		if(batteryLevel != 0&& batteryLevel<30)
		{
			return 2;
		}else if(mMotorController == null)
		{
			return 0;
		}
			return 1;
	}
	
	/**
	 * 
	 * robot_stop_dance  本地函数
	 * 
	 * 
	 * ***/
	public void robot_stop_dance() throws RemoteException {	//结束跳舞
		// TODO Auto-generated method stub
		MyLog.d(tag, "robot_stop_dance()");
		if (isRun) {	
			isRun = false;
		}
	}
	/**
	 * 
	 * robot_start_dance  本地函数
	 * 
	 * 
	 * ***/
	public void robot_start_dance(int songindex) throws RemoteException {	//开始跳舞
		// TODO Auto-generated method stub
		MyLog.d(tag, "robot_start_dance() "+songindex);
		switch(songindex)
		{
		case 0:
			setDance0();
			break;
		case 1:
			setDance1();
			break;
		case 2:
			setDance2();
			break;
		case 3:
			setDance0();
			break;
		case 4:
			setDance1();
			break;
		case 5:
			setDance2();
			break;
		case 6:
			setDance1();
			break;
		case 7:
			setDance2();
			break;
		case 8:
			setDance0();
			break;
		case 9:
			setDance2();
			break;
		default:
			setDance1();
			break;
		}
		dancePlay();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		MyLog.d(tag,"----onDestroy()-----");
		if(mMotorController != null)
		{
			unbindService(motorServiceConnection);
		}
		super.onDestroy();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		MyLog.d(tag,"----onStartCommand()----");
		if(mMotorController == null)
		{
			Intent it = new Intent();
			it.setAction("com.yongyida.robot.MotorService");
			it.setPackage("com.yongyida.robot.motorcontrol");
	        bindService(it,motorServiceConnection,Context.BIND_AUTO_CREATE);
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	   @Override
	    public IBinder onBind(Intent intent) {
		   MyLog.d(tag,"------onBind()-------");
		   if(intent.getAction().equals(Constant.ACTION_DODANCE))
		   {
			   return remoteBinder;
		   }else if(intent.getAction().equals(Constant.ACTION_ORIGIN_DODANCE))
		   {
		        return originBinder;
		   }
		   return null;
	    }
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		//绑定MotorService
		Intent intent = new Intent();
        intent.setAction(Constant.ACTION_MOTORSERVICE);
        intent.setPackage(Constant.ACTION_MOTORPAKAGE);
        if(bindService(intent,motorServiceConnection,Context.BIND_AUTO_CREATE))
        {
        	MyLog.d(tag,"bind motorService successful");
        }
		
        //动态注册电池电量广播接受器
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_BATTERY_CHANGED);
        filter.addAction("TouchSensor");
		registerReceiver(danceReceiver, filter);
		
		remoteBinder = new RemoteBinder();
        MyLog.d(tag," onCreate()");
	}
	
	private BroadcastReceiver danceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(intent != null)
			{
				if(intent.getAction().equals(Intent.ACTION_BATTERY_CHANGED))	//获取电池电量广播
				{               
			    	batteryLevel = intent.getIntExtra("level", 0);  
			    	MyLog.d(tag,"batteryLevel: "+batteryLevel);
				}
				else if(intent.getAction().equals("TouchSensor"))	//Y20只有摸双肩功能
				{
					String touch = intent.getExtras().getString("android.intent.extra.Touch");
					MyLog.d(tag," touch: "+touch);
					if(touch.equals("dance"))
					{
//						sety20dance();
//						y20danceplay();
					}
				}
			}
		}

	};
	
	/****
	 * 
	 * 
	 * 马达串口通信封装函数
	 * 
	 * 
	 * ****/
	private void danceLeftRecle() // 左转圈 2s
	{
		try {
			mMotorController.setSpeed(90); // 设置速度位最大
			mMotorController.left(2000);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void danceRightRecle() // 右转圈 2s
	{
		try {
			mMotorController.setSpeed(90); // 设置速度位最大
			mMotorController.right(2000);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void danceHeadUpAndDownRecle() // 头部循环运动 time:2s
	{
		try {
			mMotorController.setSpeed(40); // 头部速度设置为20%
			//mMotorController.headDown(200);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (timer == null) {
			timer = new Timer();
		}
		
		final TimerTask mTaskHeadDown = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mMotorController.setSpeed(40); // 设置速度位最大
					//mMotorController.headDown(200);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
				timer.purge();
				timer.cancel();
				timer = null;
			}
		};
		final TimerTask mTaskHeadUp = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mMotorController.setSpeed(40); // 设置速度为20
					//mMotorController.headUp(300);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
				timer.purge();
				timer.schedule(mTaskHeadDown, 700, 5000);
			}
		};
		timer.schedule(mTaskHeadUp, 700, 5000);
	}
	
	private void danceHeadLeftAndRightRecle() // 头部循环运动 time:2s
	{
		try {
			mMotorController.setSpeed(90); // 头部速度设置为30%
			//mMotorController.headLeft(620);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (timer == null) {
			timer = new Timer();
		}
		
		final TimerTask mTaskHeadLeft = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mMotorController.setSpeed(90); // 设置速度位最大
					//mMotorController.headLeft(500);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
				timer.purge();
				timer.cancel();
				timer = null;
			}
		};
		
		
		final TimerTask mTaskHeadRight = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				try {
					mMotorController.setSpeed(90); // 设置速度位最大
					//mMotorController.headRight(880);
				} catch (RemoteException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				this.cancel();
				timer.purge();
				timer.schedule(mTaskHeadLeft, 800, 5000);
			}
		};
		timer.schedule(mTaskHeadRight, 650, 5000);
	}

	private void danceRunForward() // 向前运动 time:2s
	{
		try {
			mMotorController.setSpeed(50); // 设置速度位最大
			//mMotorController.forward(2000);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void danceRunBackWard() // 向后运动2s
	{
		try {
			mMotorController.setSpeed(50); // 设置速度位最大
			//mMotorController.back(2000);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	//头部上下循环运动
	class danceHeadUpAndDownRecleTask extends TimerTask
	{
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			danceHeadUpAndDownRecle();	//头部循环两次
		}
		
	}
	
	//头部左右循环运动
	class danceHeadLeftAndRightRecleTask extends TimerTask
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			danceHeadLeftAndRightRecle();	//头部循环两次
		}
	}
	
	class danceRightRecleTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			danceRightRecle();
		}
		
	}
	
	class danceLeftRecleTask extends TimerTask
	{

		@Override
		public void run() {
			// TODO Auto-generated method stub
			danceLeftRecle();
		}
		
	}
	
	class danceForwardTask extends TimerTask
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			danceRunForward();
		}
		
	}
	
	class danceBackwardTask extends TimerTask
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			danceRunBackWard();
		}
		
	}
	
	List mList = new LinkedList<TimerTask>();	//时间片任务
	
	/***
	 * 
	 * Y20跳舞动作的实现
	 * 
	 * ****/
	private void y20danceleft() // 向左运动
	{
		try {
			mMotorController.setSpeed(90); // 设置速度位最大
			//mMotorController.reverseLeft(1000);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void y20danceright() // 向右运动
	{
		try {
			mMotorController.setSpeed(90); // 设置速度位最大
			//mMotorController.reverseRight(1000);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	class y20danceleft extends TimerTask
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			y20danceleft();
		}
		
	}
	
	class y20danceright extends TimerTask
	{
		@Override
		public void run() {
			// TODO Auto-generated method stub
			y20danceright();
		}
		
	}
	
	private void sety20dance()
	{
		isRun = true;	//设置开始跳舞
		mList.add(new y20danceleft());
		mList.add(new y20danceright());
		mList.add(new y20danceleft());
		mList.add(new y20danceright());	
	}

	private void y20danceplay()
	{
		if(mTaskTimer == null)
		{
			mTaskTimer = new Timer();
		}
		TimerTask taskTimerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(isRun)
				{
					if(Play1Timer != null)
					{
						Play1Timer.cancel();
						Play1Timer.purge();
						Play1Timer = null;
					}
					Play1Timer =  new Timer();
					if(!mList.isEmpty())
					{
						TimerTask mTimerTask = (TimerTask) mList.remove(0);
						Play1Timer.schedule(mTimerTask, 0, 5000);
					}
					else	//所有动作执行完毕
					{
						if(Play1Timer != null)
						{
							Play1Timer.cancel();
							Play1Timer.purge();
							Play1Timer = null;
						}
						
						this.cancel();
						if(mTaskTimer != null)
						{
							mTaskTimer.cancel();
							mTaskTimer.purge();
							mTaskTimer =  null;
						}
					}
				}
				else	//主动停止跳舞功能
				{
					if(Play1Timer != null)
					{
						Play1Timer.cancel();
						Play1Timer.purge();
						Play1Timer = null;
					}
					
					this.cancel();
					if(mTaskTimer != null)
					{
						mTaskTimer.cancel();
						mTaskTimer.purge();
						mTaskTimer =  null;
					}
					
					if(!mList.isEmpty())
					{
						mList.clear();	//清空之前的舞蹈动作
					}
				}
			}
		};
		mTaskTimer.schedule(taskTimerTask, 0, 1200);	//Y20 跳舞间隔
	}
	
	/***
	 * 
	 * 设置Y50pro的舞动动作
	 * 
	 * ***/
	private void setDance0()	//设置舞蹈动作  时间片为2s 中间停留0.5s
	{
		isRun = true;	//设置开始跳舞
		mList.add(new danceHeadLeftAndRightRecleTask());
		mList.add(new danceHeadUpAndDownRecleTask());
		mList.add(new danceForwardTask());
		mList.add(new danceBackwardTask());
		mList.add(new danceBackwardTask());
		mList.add(new danceForwardTask());
//		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceLeftRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceLeftRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceLeftRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
		}
		for(int i = 0;i<20;i++)
		{
			mList.add(new danceLeftRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
		}
	}
	
	private void setDance1()	//设置舞蹈动作  时间片为2s 中间停留0.5s
	{
		isRun = true;	//设置开始跳舞
		
		mList.add(new danceForwardTask());
		mList.add(new danceBackwardTask());
		mList.add(new danceHeadLeftAndRightRecleTask());
		mList.add(new danceHeadUpAndDownRecleTask());
		mList.add(new danceHeadLeftAndRightRecleTask());
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceLeftRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceLeftRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
		}
		for(int i = 0;i<20;i++)
		{
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
		}
	}
	
	
	private void setDance2()	//设置舞蹈动作  时间片为2s 中间停留0.5s
	{
		isRun = true;	//设置开始跳舞
		mList.add(new danceForwardTask());
		mList.add(new danceBackwardTask());
		mList.add(new danceForwardTask());
		mList.add(new danceHeadLeftAndRightRecleTask());
		mList.add(new danceHeadUpAndDownRecleTask());
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceLeftRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceLeftRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
		}
		for(int i = 0;i<4;i++)
		{
			mList.add(new danceRightRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
		}
		for(int i = 0;i<20;i++)
		{
			mList.add(new danceLeftRecleTask());
			mList.add(new danceLeftRecleTask());
			mList.add(new danceHeadUpAndDownRecleTask());
			mList.add(new danceHeadLeftAndRightRecleTask());
			mList.add(new danceRightRecleTask());
			mList.add(new danceRightRecleTask());
		}
	}
	
	
	private void dancePlay()	//播放舞蹈动作
	{
		if(mTaskTimer == null)
		{
			mTaskTimer = new Timer();
		}
		TimerTask taskTimerTask = new TimerTask() {
			
			@Override
			public void run() {
				// TODO Auto-generated method stub
				if(isRun)
				{
					if(Play1Timer != null)
					{
						Play1Timer.cancel();
						Play1Timer.purge();
						Play1Timer = null;
					}
					Play1Timer =  new Timer();
					if(!mList.isEmpty())
					{
						TimerTask mTimerTask = (TimerTask) mList.remove(0);
						Play1Timer.schedule(mTimerTask, 0, 5000);
					}
					else	//所有动作执行完毕
					{
						if(Play1Timer != null)
						{
							Play1Timer.cancel();
							Play1Timer.purge();
							Play1Timer = null;
						}
						
						this.cancel();
						if(mTaskTimer != null)
						{
							mTaskTimer.cancel();
							mTaskTimer.purge();
							mTaskTimer =  null;
						}
					}
				}
				else	//主动停止跳舞功能
				{
					if(Play1Timer != null)
					{
						Play1Timer.cancel();
						Play1Timer.purge();
						Play1Timer = null;
					}
					
					this.cancel();
					if(mTaskTimer != null)
					{
						mTaskTimer.cancel();
						mTaskTimer.purge();
						mTaskTimer =  null;
					}
					
					if(!mList.isEmpty())
					{
						mList.clear();	//清空之前的舞蹈动作
					}
				}
			}
		};
		mTaskTimer.schedule(taskTimerTask, 0, 2500);
	}
	
    
    private ArrayList<Dance> mDances ;

	abstract class Dance{
        int time ;

        Dance(int time){
            this.time = time ;
        }

        void startDance(){

            try {
                Thread.sleep(time+500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    class BackTurnLeftDance extends Dance{

        BackTurnLeftDance(int time) {
            super(time);
        }

        void startDance(){

            try {

                mMotorController.setSpeed(80);
                mMotorController.backTurnLeft(time) ;

                super.startDance() ;

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }

    class BackTurnRightDance extends Dance{

        BackTurnRightDance(int time) {
            super(time);
        }

        void startDance(){

            try {
                mMotorController.setSpeed(80);

                mMotorController.backTurnRight(time) ;

                super.startDance() ;

            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
    }
	
	/**摸双肩*/
	public void touchDoubleshoulders() {
		// TODO Auto-generated method stub
		
		if(mDances == null){

            mDances = new ArrayList() ;

            mDances.add(new BackTurnLeftDance(400)) ;
            mDances.add(new BackTurnRightDance(800)) ;
            mDances.add(new BackTurnLeftDance(800)) ;
            mDances.add(new BackTurnRightDance(400)) ;
        }

		isRun = true;	//设置开始跳舞

		final int size = mDances.size() ;
		for (int i =0 ; i < size ; i++){

			Dance dance = mDances.get(i) ;

			if(!isRun){

				return;
			}

			dance.startDance();
		}
		
		
	}
	
}
