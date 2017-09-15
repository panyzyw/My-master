package com.yongyida.robot.voice.master.version;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import android.os.Build;
import android.util.Log;

import com.yongyida.robot.voice.master.application.MasterApplication;
import com.yongyida.robot.voice.master.constant.Constant;
import com.yongyida.robot.voice.master1.R;

public class VersionControl {

	//存在的版本
	public final static int VERSION_DEV = 0;//y50b大陆:
	public final static int VERSION_DEV_HK = 1;//y50b香港:
	public final static int VERSION_ST = 2;//y50b ST大陆:
	public final static int VERSION_ST_HK = 3;//y50b ST香港:
	public final static int VERSION_CMCC = 4;//y50b CMCC大陆:
	public final static int VERSION_CMCC_HK = 5;//y50b CMCC香港:
	public final static int VERSION_DEMO = 6;//y50b 演示版
	public final static int VERSION_DEMO_HK = 7;//
	
    public static int SYSTEM_VERSIONS;
    public final static int SYSTEM_VERSIONS_Y20 = 0;
    public final static int SYSTEM_VERSIONS_Y50 = 1;
    public final static int SYSTEM_VERSIONS_Y50BPRO = 2;
    public final static int SYSTEM_VERSIONS_ERROR = 4;
	
	public static String[] versions = {"VERSION_DEV","VERSION_DEV_HK",
										    "VERSION_ST","VERSION_ST_HK",
										    "VERSION_CMCC","VERSION_CMCC_HK",
										    "VERSION_DEMO","VERSION_DEMO_HK"};
	
	//默认设置
	public static int mVersion = VERSION_DEV; //当前的版本
	
	/**
	 * 初始化配置与版本相关的内容,避免每次切换版本反复修改一些内容
	 */
	public static void initVersion(){
		//获取机器人版本(现有版本:正式版 三土 移动 演示, 每个版本又分大陆(中) 香港(英文))
		//mVersion = getVersion();
		mVersion = VERSION_DEV;
		
		showVersionInfo(mVersion);
		
		switch (mVersion) {
		
		case VERSION_DEV:
			setVersionDev();
			break;
		case VERSION_DEV_HK:
			setVersionDevHk();
			break;
		case VERSION_ST:
			setVersionSt();
			break;
		case VERSION_ST_HK:
			setVersionStHk();
			break;
		case VERSION_CMCC:
			setVersionCmcc();
			break;
		case VERSION_CMCC_HK:
			setVersionCmccHk();
			break;
		case VERSION_DEMO:
			setVersionDemo();
			break;
		case VERSION_DEMO_HK:
			setVersionDemoHk();
			break;
				
		}
		
	}
	
	private static void showVersionInfo(int versionCode) {
		Log.i("VersionControl", "versionCode = " + versionCode );		
		if(versionCode < versions.length){
			Log.i("VersionControl", "versionName = " + versions[versionCode] );
		}else{
			Log.i("VersionControl", "versionName = " );
		}
	}

	/**
	 * 获取机器人的版本
	 * @return
	 */
	private static int getVersion() {
		//...		
		String s = Build.DISPLAY;
		
		if( s.contains("_ST_") ){//三土版本
			return VERSION_ST;
		}
		
		if(s.contains("_CMCC_")){ // 中移动
			return VERSION_CMCC;
		}
		
		if(s.contains("_MBN")){ //nuance
			return VERSION_DEV;  //暂时没处理
		}
		
		if(s.contains("_DEMO")){//演示版
			return VERSION_DEMO;
		}
		
		//其他都认为是正式发布版
		return VERSION_DEV;
	}
	
	private static void setVersionDev() {
		Constant.robot_default_name = MasterApplication.mAppContext.getString(R.string.robot_default_name_DEV);
	}

	private static void setVersionDevHk() {		
		setVersionDev();
	}

	private static void setVersionSt() {
		Constant.robot_default_name = MasterApplication.mAppContext.getString(R.string.robot_default_name_ST);
	}

	private static void setVersionStHk() {	
		setVersionSt();
	}

	private static void setVersionCmcc() {	
		Constant.robot_default_name = MasterApplication.mAppContext.getString(R.string.robot_default_name_CMCC);
	}

	private static void setVersionCmccHk() {		
		setVersionCmcc();
	}
	
	private static void setVersionDemo() {
		setVersionDev();
	}
	
	private static void setVersionDemoHk() {
		setVersionDev();
	}
	
	private static String getSystemString(String key) {
        Class<?> clazz;
        try {
            clazz = Class.forName("android.os.SystemProperties");
            Method method = clazz.getDeclaredMethod("get", String.class);
            return (String) method.invoke(clazz.newInstance(), key);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return "Y50B-566";
    }
	
}
