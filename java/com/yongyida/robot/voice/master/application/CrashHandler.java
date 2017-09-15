package com.yongyida.robot.voice.master.application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;
import java.text.SimpleDateFormat;
import java.util.Date;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Environment;
import android.util.Log;

import com.yongyida.robot.voice.master.service.VoiceSubService;
import com.yongyida.robot.voice.master.utils.MyLog;
import com.yongyida.robot.voice.master.voice.VoiceQueue;

public class CrashHandler implements UncaughtExceptionHandler {  
    public static final String TAG = "CrashHandler";  
    private static CrashHandler INSTANCE = new CrashHandler();  
    private Context mContext;  
    private Thread.UncaughtExceptionHandler mDefaultHandler;  
  
    String mPackagename="";
    
    private CrashHandler() {      	
    }  
  
    public static CrashHandler getInstance() {  
        return INSTANCE;  
    }  
  
    public void init(Context ctx) {  
        mContext = ctx;  
        
       mPackagename = getPackageName(mContext);
        
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();  
        Thread.setDefaultUncaughtExceptionHandler(this);  
    }  
  
    @Override  
    public void uncaughtException(Thread thread, Throwable ex) {
    	if(ex == null || mDefaultHandler==null) return ;
    	
    	Log.e("CrashHandler","--- enter : uncaughtException ---");   
        StringWriter sw = new StringWriter();  
        PrintWriter pw = new PrintWriter(sw, true);  
        ex.printStackTrace(pw);  
        MyLog.e("CrashHandler", sw.getBuffer().toString());
    	   	
        String path;
        if( (path = getSDPath()) != null){
        	writeFile(path+"/" + "zxCrashHandler_" + mPackagename , 
        			getTime() + "\n\n" +sw.getBuffer().toString());
        }
    	
    	try{
			if( !VoiceQueue.getInstance().isEmpty() ){
				VoiceQueue.getInstance().clearQueue();
			}
	    	
	    	if(mContext != null){
				Intent inte = new Intent(mContext,VoiceSubService.class);
				mContext.stopService(inte);
	    	}
    	}catch(Exception e){  
    		e.printStackTrace();
    	}
    	
         android.os.Process.killProcess(android.os.Process.myPid());  
         System.exit(10);  
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
		Log.i("CrashHandler", "sd not exsit or do not have permision");
		return null;
	}
	
    /**
     * 		    
     * @param fileName
     * @param content   
     */
    public  void writeFile(String fileName, String content) {
        try {            
            FileWriter writer = new FileWriter(fileName, false);
            writer.write(content);
            writer.close();
        } catch (IOException e) {
        	Log.e("CrashHandler", "write exception");
            e.printStackTrace();
        }
    }
    
    /**
     * 获取context所在程序包名
     * @param context
     * @return
     */
    String getPackageName(Context context){
    	String fileName = "zxCrashHandler";
    	try {		
    		PackageInfo info = context.getPackageManager().getPackageInfo( context.getPackageName(), 0);
			String packageName = info.packageName;
			fileName = packageName+".txt";
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return fileName;
    }

	public static String dateFormat(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.format(date);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String getTime() {
		return dateFormat(new Date(System.currentTimeMillis()));
	}
}
