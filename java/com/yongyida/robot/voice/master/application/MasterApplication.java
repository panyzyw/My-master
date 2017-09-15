package com.yongyida.robot.voice.master.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.SystemProperties;
import com.amap.api.location.AMapLocation;
import com.amap.api.location.LocationManagerProxy;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.yongyida.robot.voice.master.Container.ServiceMap;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master.utils.MyExceptionUtils;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.version.VersionControl;
import com.yongyida.robot.voice.master.voice.TTS;
import com.yongyida.robot.voice.master.voice.VoiceQueue;
import com.yongyida.robot.voice.master1.R;
import com.zccl.ruiqianqi.brain.system.SystemPresenter;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MasterApplication extends Application{
	public static Context mAppContext;
	public static String mPackageName;
	public static ExecutorService mFixedThreadPool;
	public static LocationManagerProxy aMapLocManager;
	public static AMapLocation mAMapLocation = null;
    public static String USER_DI = null;
    public static String rid = null;
    public static boolean isFirstFaceDetec = false;


	public void onCreate() {
		super.onCreate();
        SystemPresenter.getInstance(getApplicationContext()).bindSystemService();
		if (TTS.speeker.equals(Constant.xiaoai)) {
			//讯飞id
			SpeechUtility.createUtility(this, "appid=" + getString(R.string.appid));
		} else {
			if(TTS.speeker.equals("")) {
				//讯飞id
				SpeechUtility.createUtility(this, "appid=" + getString(R.string.appid));
			}else {
				StringBuffer param = new StringBuffer();
				param.append(SpeechConstant.APPID + "=" + getString(R.string.appid));
				param.append(",");
				// 设置使用v5+
				param.append(SpeechConstant.ENGINE_MODE+"="+SpeechConstant.MODE_MSC);
				SpeechUtility.createUtility(this, param.toString());
			}
		}
		mAppContext = getApplicationContext();
		initPackageName();
		CrashHandler crashHandler = CrashHandler.getInstance();    
        crashHandler.init(this);
        VoiceQueue.getInstance();
        initMapLocManager();
		Constant.initConstant(this);
		//根据系统版本设置Constant 里面的一些与版本有关的常量 
		VersionControl.initVersion(); //这个必须放在 Constant.initConstant(this); 后面执行
		loadModule("module.properties");
		mFixedThreadPool = Executors.newFixedThreadPool(4);

	}
	
    private void initMapLocManager() {
    	aMapLocManager = LocationManagerProxy.getInstance(MasterApplication.mAppContext);
	}

	public void loadModule(String fileName){
        try {
            InputStreamReader inputReader = new InputStreamReader( getResources().getAssets().open(fileName) ); 
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line="";
            while((line = bufReader.readLine()) != null){
            	if(line.trim().equals("") || line.charAt(0) == '#' )
            		continue;            	
            	registerModule(line); 
            }
        } catch (Exception e) { 
            e.printStackTrace();
        }		
    }
    
    /**
     * 
     * @param s
     */
    public void registerModule(String s){    	
    	try {
    		String[] arr = s.split("=");
			ServiceMap.getInstance().register(arr[0], Class.forName(arr[1]));
			MyLog.i("MasterApplication", "registerModule : " + arr[0]);
		} catch (ClassNotFoundException e) {
			MyLog.e("MasterApplication", MyExceptionUtils.getStringThrowable(e));
		}
    }

    private void initPackageName(){
    	try {		
    		PackageInfo info = getPackageManager().getPackageInfo( getPackageName(), 0);
    		mPackageName = info.packageName;		
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mPackageName = "master";
		}
    }
	
	//正在说话时要不要闪灯
    public static boolean isNeedSpekLed(){
        boolean isNeed = SystemProperties.getBoolean("persist.yongyida.speak_led",true);
        return isNeed;
    }
    
}
