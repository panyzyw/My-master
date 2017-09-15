package com.yongyida.robot.voice.master.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

/**
 * 打印、保存日志的单例类
 * @author zx
 *
 */
public class Tip {
	public final static boolean isDebug = true;
	public static String TAG = "zx321";
	private static String mDefaultLogName = "log_name.txt";
	private static Tip mInstance;
	
	private boolean mIsDebug;
	private String mDebugFileName;
	private String mTag;	
	private String packageName="";
	
	/**
	 * 构造方法
	 * @param context
	 * @param isDebug
	 */
	private Tip(Context context,boolean isDebug) {
		mIsDebug = isDebug;
		mTag = TAG;		
		String path = getSDPath(); 
		if(path.equals("")){
			Log.e(TAG, "sd not exsit or do not have permision");
		}else{
			mDebugFileName = path + "/" + getPackageName(context);
			if(isFileOver2M(mDebugFileName))
				deleteLogFile();
			
			//隔开行
			writeLogFile("--------------------\n");
		}
	}	

	/**
	 * 构造方法
	 * @param context
	 * @param isDebug
	 * @param tag
	 */
	private Tip(Context context,boolean isDebug, String tag) {
		this(context,isDebug);
		mTag = tag;		
	}

	/**
	 * 获取单实例
	 * @param context
	 * @param isDebug
	 * @return
	 */
	public static Tip getInstance(Context context,boolean isDebug) {
		if (mInstance == null) {
			mInstance = new Tip(context,isDebug);
		}
		return mInstance;
	}
	
	/**
	 * 获取单实例
	 * @param context
	 * @param isDebug
	 * @param tag
	 * @return
	 */
	public static Tip getInstance(Context context,boolean isDebug, String tag) {
		if (mInstance == null) {
			mInstance = new Tip(context,isDebug , tag);
		}
		return mInstance;
	}
	
	/**
	 * 判断文件大小是否超过2m
	 * @param filePath
	 * @return
	 */
	boolean isFileOver2M(String filePath){		
		File f = new File(filePath); 
		//大于2m
		if( f.exists() && f.length() > 2097152) 
			return true;		
		return false;
	}

	/**
	 *  打印 err等级 日志并写入文件
	 * @param msg
	 */
	public void log_e(String msg) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + msg;
		Log.e(mTag,strLog);
		writeLogFile(strLog);
	}
	
	/**
	 * 打印 err等级 日志并写入文件
	 * @param msg
	 * @param methodName 调用的所在方法名
	 */
	public void log_e(String msg,String methodName) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + methodName + " :" + msg;
		Log.e(mTag,strLog);
		writeLogFile(strLog);
	}

	/**
	 * 打印 err等级 日志并写入文件
	 * @param msg
	 * @param methodName 调用的所在方法名
	 * @param className 调用的所在类名
	 */
	public void log_e(String msg,String methodName,String className) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + className + "." + methodName + " :" + msg;
		Log.e(mTag,strLog);
		writeLogFile(strLog);
	}
	
	/**
	 * 打印 info 等级 日志并写入文件
	 * @param msg
	 */
	public void log_i(String msg) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + msg;
		Log.i(mTag,strLog);
		writeLogFile(strLog);
	}

	/**
	 * 打印 info 等级 日志并写入文件
	 * @param msg
	 * @param methodName  调用的所在方法名
	 */
	public void log_i(String msg,String methodName) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + methodName + " :" + msg;
		Log.i(mTag,strLog);
		writeLogFile(strLog);
	}

	/**
	 * 打印 info 等级 日志并写入文件
	 * @param msg
	 * @param methodName 调用的所在方法名
	 * @param className 调用的所在类名
	 */
	public void log_i(String msg,String methodName,String className) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + className + "." + methodName + " :" + msg;
		Log.i(mTag,strLog);
		writeLogFile(strLog);
	}
	
	/**
	 * 打印 debug 等级 日志并写入文件
	 * @param msg
	 */
	public void log_d(String msg) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + msg;
		Log.d(mTag,strLog);
		writeLogFile(strLog);
	}

	/**
	 * 打印 debug 等级 日志并写入文件
	 * @param msg
	 * @param methodName  调用的所在方法名
	 */
	public void log_d(String msg,String methodName) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + methodName + " :" + msg;
		Log.d(mTag,strLog);
		writeLogFile(strLog);
	}

	/**
	 * 打印 debug 等级 日志并写入文件
	 * @param msg
	 * @param methodName 调用的所在方法名
	 * @param className 调用的所在类名
	 */
	public void log_d(String msg,String methodName,String className) {
		if (mIsDebug == false) return;
		String strLog = getLocalSysDateTime() + "," + packageName + ":" + className + "." + methodName + " :" + msg;
		Log.d(mTag,strLog);
		writeLogFile(strLog);
	}
	
	/**
	 * 获取sd卡路径
	 * @return
	 */
	public static String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
		// 判断sd卡是否存在
		if (sdCardExist) {
			// 获取根目录
			sdDir = Environment.getExternalStorageDirectory();
			return sdDir.toString();
		}
		Log.i(TAG, "sd not exsit or do not have permision");
		return "";
	}

	/**
	 * 删除指定path文件
	 * @param file 
	 */
	public void deleteFile(File file) {
		if (file.exists()) { // 判断文件是否存在
			if (file.isFile()) { // 判断是否是文件
				file.delete();// delete()方法 你应该知道 是删除的意思;
			}
		}
	}

	/**
	 * 删除日志文件
	 */
	public void deleteLogFile() {
		File f = new File(mDebugFileName);
		deleteFile(f);
	}

	// 写入logfile
	public void writeLogFile(String msg) {
		if( !getSDPath().equals("") ){
			appendFile( mDebugFileName , msg );
		}
	}
	
    /**
     * 追加信息写到文件尾		    
     * @param fileName
     * @param content   追加的内容
     */
    public  void appendFile(String fileName, String content) {
        try {
            //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(fileName, true);
            writer.write(content+"\n");
            writer.close();
        } catch (IOException e) {
        	Log.e(TAG, "write exception");
            e.printStackTrace();
        }
    }
    
    /**
     * 获取context所在程序包名
     * @param context
     * @return
     */
    String getPackageName(Context context){
    	String fileName = mDefaultLogName;
    	try {		
    		PackageInfo info = context.getPackageManager().getPackageInfo( context.getPackageName(), 0);
			packageName = info.packageName;
			fileName = packageName+".txt";
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
    }
    
    /**
     * 获取本地时间
     * @return
     */
    String getLocalSysDateTime(){
    	Date d = new Date(System.currentTimeMillis());    	
    	return d.toLocaleString();
    }
}
